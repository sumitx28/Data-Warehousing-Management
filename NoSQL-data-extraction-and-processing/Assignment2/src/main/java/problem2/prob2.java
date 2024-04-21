package problem2;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;

class Prob2 {

    public static void main(String[] args) {

        String connectionString = "mongodb+srv://sumitpatelcanada:sumit28012001@cluster0.vw5qhug.mongodb.net/?retryWrites=true&w=majority";
        String dbName = "ReuterDb";
        String collectionName = "newsCollection";

        MongoClientURI uri = new MongoClientURI(connectionString);
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase(dbName);

        MongoCollection<Document> collection = database.getCollection(collectionName);

        List<String> newsTitles = getNewsTitles(collection);

        List<String> positiveWords = readWordsFromFile("src/main/java/problem2/positive-words.txt");
        List<String> negativeWords = readWordsFromFile("src/main/java/problem2/negative-words.txt");

        List<Map<String, Object>> results = new ArrayList<>();

        for (String newsTitle : newsTitles) {
            Map<String, Integer> bow = getWordFrequencies(newsTitle);

            int score = 0;
            List<String> matchedWords = new ArrayList<>();

            for (Map.Entry<String, Integer> entry : bow.entrySet()) {
                String word = entry.getKey();
                int frequency = entry.getValue();

                if (positiveWords.contains(word)) {
                    score += frequency;
                    matchedWords.add(word);
                } else if (negativeWords.contains(word)) {
                    score -= frequency;
                    matchedWords.add(word);
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("NewsTitle", newsTitle);
            result.put("MatchedWords", matchedWords);
            result.put("Score", score);
            result.put("Polarity", getPolarity(score));

            results.add(result);
        }

        insertResultsIntoTable(results);

        mongoClient.close();
    }

    private static void insertResultsIntoTable(List<Map<String, Object>> results) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/NewsDb";
        String username = "root";
        String password = "Sumit2801";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            String insertQuery = "INSERT INTO NewsAnalysis (NewsTitle, MatchedWords, Score, Polarity) VALUES (?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                for (Map<String, Object> result : results) {
                    Object matchedWordsObject = result.get("MatchedWords");
                    List<String> matchedWords;
                    if (matchedWordsObject instanceof List<?>) {
                        matchedWords = ((List<?>) matchedWordsObject)
                                .stream()
                                .map(Object::toString)
                                .collect(Collectors.toList());
                    } else {
                        matchedWords = List.of(matchedWordsObject.toString());
                    }

                    preparedStatement.setString(1, (String) result.get("NewsTitle"));
                    preparedStatement.setString(2, String.join(", ", matchedWords));
                    preparedStatement.setInt(3, (int) result.get("Score"));
                    preparedStatement.setString(4, (String) result.get("Polarity"));

                    preparedStatement.executeUpdate();
                }
            }

            System.out.println("Records inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getPolarity(int score) {
        if(score < 0) {
            return "Negative";
        }
        else if(score > 0) {
            return "Positive";
        }
        else{
            return "Neutral";
        }
    }

    private static Map<String, Integer> getWordFrequencies(String input) {
        Map<String, Integer> wordFrequencies = new HashMap<>();
        String[] words = input.split("\\s+");

        for (String word : words) {
            word = word.replaceAll("[^a-zA-Z]", "").toLowerCase();

            if (!word.isEmpty()) {
                wordFrequencies.put(word, wordFrequencies.getOrDefault(word, 0) + 1);
            }
        }

        return wordFrequencies;
    }

    private static List<String> getNewsTitles(MongoCollection<Document> collection) {
        List<String> newsTitles = new ArrayList<>();

        FindIterable<Document> iterable = collection.find();
        for (Document document : iterable) {
            String title = document.getString("title");
            if (title != null && !title.isEmpty()) {
                newsTitles.add(title);
            }
        }

        return newsTitles;
    }

    private static List<String> readWordsFromFile(String fileName) {
        List<String> words = new ArrayList<>();
        try {
            words = Files.readAllLines(Paths.get(new File(fileName).getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }
}
