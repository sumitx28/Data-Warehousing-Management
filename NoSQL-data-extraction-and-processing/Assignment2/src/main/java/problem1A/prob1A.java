package problem1A;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertManyOptions;
import org.bson.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class prob1A {

    public static void main(String[] args) {
        String connectionString = "mongodb+srv://sumitpatelcanada:sumit28012001@cluster0.vw5qhug.mongodb.net/?retryWrites=true&w=majority";
        String dbName = "ReuterDb";
        String collectionName = "newsCollection";

        ConnectionString connString = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .build();

        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase(dbName);

        MongoCollection<Document> collection = database.getCollection(collectionName);

        processNewsFile("src/main/java/problem1A/reut2-009.sgm", collection);
        processNewsFile("src/main/java/problem1A/reut2-014.sgm", collection);

        mongoClient.close();
    }

    private static void processNewsFile(String fileName, MongoCollection<Document> collection) {
        try {
            File input = new File(new File(fileName).getAbsolutePath());
            org.jsoup.nodes.Document document = Jsoup.parse(input, "UTF-8", "", Parser.xmlParser());

            List<Document> newsDocuments = new ArrayList<>();

            for (Element reuter : document.select("REUTERS")) {
                String title = cleanText(reuter.select("TEXT > TITLE").text());
                String body = cleanText(reuter.select("TEXT > BODY").text());

                Document newsDocument = new Document()
                        .append("title", title)
                        .append("body", body);

                newsDocuments.add(newsDocument);
            }

            try {
                InsertManyOptions options = new InsertManyOptions().ordered(false);
                collection.insertMany(newsDocuments, options);
                System.out.println("Inserted " + newsDocuments.size() + " documents into MongoDB");
            } catch (Exception e) {
                System.err.println("Error inserting documents into MongoDB: " + e.getMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String cleanText(String text) {
        return text.replaceAll("[^a-zA-Z0-9\\s]", "");
    }


}
