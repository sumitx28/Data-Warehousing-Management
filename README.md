 Data Warehousing Management

This repository contains two Java projects developed for the CSCI 5408 course: Data Management and Warehousing Analytics Projects.

## Project 1: MySQL-Java

- Created a lightweight multi-user database system using Java.
- Features multi-user authentication, Query Processor, and Single Transaction Management.

# Project 2: NoSQL Data Extraction and Processing

This project involves solving 3 problems related to data extraction and processing. It includes algorithms for cleaning news heading data and performing sentiment analysis using a Bag of Words (BoW) model. The cleaned data is pushed to a MongoDB cloud cluster by Atlas.

## Algorithm

### Read and Parse XML File

- The function starts by taking the fileName and MongoCollection<Document> as parameters.
- It creates a File object using the provided file name and gets its absolute path.
- It uses the Jsoup library to parse the XML file using the XML parser. The result is stored in a org.jsoup.nodes.Document object named document.

### Initialize List for MongoDB Documents

- It initializes an empty List<Document> named newsDocuments to store the MongoDB documents that will be created from the XML file.

### Iterate Over "REUTERS" Elements

- The function iterates over each "REUTERS" element in the parsed XML document.

### Extract Title and Body

- For each "REUTERS" element, it extracts the text content of the "TEXT > TITLE" and "TEXT > BODY" elements.
- The extracted text is passed through the cleanText method, which removes non-alphanumeric characters.

### Problem 1B: News Data Processing using Spark

Steps followed: UniqueWordCount.py file can be found in the repository inside the Problem1B folder.

### Problem 2: Sentiment Analysis using BoW Model

- Successfully implemented the task using files of Positive and Negative words from the same author. This algorithm performs sentiment analysis on news titles stored in a MongoDB collection. The sentiment analysis involves scoring each news title based on the presence of positive and negative words. The results are then inserted into a MySQL table for further analysis.

#### Algorithm

1. **Connect to MongoDB:**
   - Established a connection to MongoDB using the provided connection string, database name, and collection name.
   - Fetched the desired collection (newsCollection) from the MongoDB database.

2. **Retrieve News Titles:**
   - Query the MongoDB collection to retrieve the news titles.
   - Stored the news titles in a list (newsTitles).

3. **Read Positive and Negative Words:**
   - Read positive and negative words from files (positive-words.txt and negative-words.txt).
   - Stored positive and negative words in separate lists (positiveWords and negativeWords).

4. **Sentiment Analysis Loop:**
   - Iterated over each news title in the newsTitles list.

5. **Build Bag of Words (BoW):**
   - Built a Bag of Words (BoW) for each news title.
   - Used a map (bow) to store word frequencies in the news title.

6. **Score Calculation:**
   - Compared each word in the BoW to positive and negative words.
   - Calculated a sentiment score based on the frequency of positive and negative words.

7. **Determine Polarity:**
   - Determined the polarity of the news title based on the calculated score.

8. **Store Results:**
   - Stored the news title, matched words, sentiment score, and polarity in a map (result).
   - Added each result map to a list (results).

9. **Insert into MySQL Table:**
   - Established a connection to a MySQL database using the JDBC URL, username, and password.
   - Inserted each result into a table named NewsAnalysis with columns: NewsTitle, MatchedWords, Score, and Polarity.
   - Used a prepared statement to efficiently insert multiple records.

10. **Close Connections:**
    - Closed the MongoDB connection after processing all news titles.
    - Closed the MySQL connection after inserting all results into the table.
   
## Author

- Sumit Savaliya (sumit.savaliya@dal.ca)
