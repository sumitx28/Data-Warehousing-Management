package database;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryMethods {
    private Map<String, Map<String, String>> tables;

    /**
     * Constructor for the QueryMethods class. Initializes the tables map and loads data from the file.
     */
    public QueryMethods() {
        this.tables = new HashMap<>();
        loadTableDataFromFile();
    }

    /**
     * Saves the current database tables and their attributes to a file.
     * The file is specified by the path in the method.
     * This method writes the table names and their attributes to the file in a specific format.
     * If an IOException occurs during the file writing process, it is caught and printed to the standard error.
     */
    private void saveTableDataToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(System.getProperty("user.dir").toString() + "src/main/java/persistentStorage/tabledata.txt"))) {
            for (Map.Entry<String, Map<String, String>> entry : tables.entrySet()) {
                writer.println("Table: " + entry.getKey());
                for (Map.Entry<String, String> row : entry.getValue().entrySet()) {
                    writer.println(row.getKey() + "=" + row.getValue());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads database tables and their attributes from a file into the current database.
     * The file is specified by the path in the method.
     * This method reads the table names and their attributes from the file in a specific format and populates the internal data structure with the data.
     * If an IOException occurs during the file reading process, it is caught and printed to the standard error.
     */
    public void loadTableDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir").toString() + "src/main/java/persistentStorage/tabledata.txt"))) {
            String line;
            String tableName = null;
            Map<String, String> currentTable = new HashMap<>();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Table: ")) {
                    if (tableName != null) {
                        tables.put(tableName, currentTable);
                        currentTable = new HashMap<>();
                    }
                    tableName = line.substring(7);
                } else {
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        currentTable.put(parts[0], parts[1]);
                    }
                }
            }

            if (tableName != null) {
                tables.put(tableName, currentTable);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new database table based on a "CREATE TABLE" query string.
     *
     * @param query The "CREATE TABLE" query in the format "CREATE TABLE table_name (column1 data_type constraints, column2 data_type constraints, ...);".
     * @return True if the table was created successfully, false if there was an issue with the query or the table already exists.
     */
    public boolean createTable(String query) {
        return handleCreateTableQuery(query);
    }

    /**
     * Handles a CREATE TABLE SQL query by extracting the table name and attributes
     * and adding the table definition to the database.
     *
     * @param query The SQL query to process.
     * @return true if the table was successfully created and added to the database, false otherwise.
     */
    private boolean handleCreateTableQuery(String query) {
        // Use regular expressions to extract table name, attributes, etc.
        Pattern pattern = Pattern.compile("CREATE TABLE (\\w+) \\((.+)\\);");
        Matcher matcher = pattern.matcher(query);

        if (matcher.matches()) {
            String tableName = matcher.group(1);
            String attributes = matcher.group(2);
            Map<String, String> tableAttributes = new HashMap<>();
            // Parse and store attributes
            String[] attributeTokens = attributes.split(",");
            for (String attributeToken : attributeTokens) {
                String[] attributeParts = attributeToken.trim().split(" ");
                if (attributeParts.length >= 2) {
                    String attributeName = attributeParts[0];
                    String attributeType = attributeParts[1];
                    tableAttributes.put(attributeName, attributeType);
                }
            }
            if (!tables.containsKey(tableName)) {
                tables.put(tableName, tableAttributes);
                saveTableDataToFile();
                return true;
            }
        }
        return false;
    }

    /**
     * Inserts data into an existing database table based on an INSERT query string.
     *
     * @param query The INSERT query in the format "INSERT INTO table_name (column1, column2, ...) VALUES (value1, value2, ...);".
     * @return True if data was inserted successfully, false if there was an issue with the query or data.
     */
    public boolean insertData(String query) {
        // Example query format: "INSERT INTO users (id, username, email, created_at) VALUES (1, 'user1', 'user1@example.com', '2023-10-24 12:00:00');"
        if (query.startsWith("INSERT INTO ")) {
            // Extract table name
            int tableStart = query.indexOf("INSERT INTO ") + "INSERT INTO ".length();
            int tableEnd = query.indexOf("(", tableStart);
            String tableName = query.substring(tableStart, tableEnd).trim();

            if (tables.containsKey(tableName)) {
                Map<String, String> table = tables.get(tableName);

                // Extract columns and values
                int columnsStart = query.indexOf("(", tableEnd) + 1;
                int columnsEnd = query.indexOf(") VALUES", columnsStart);
                String columnsPart = query.substring(columnsStart, columnsEnd).trim();
                String[] columns = columnsPart.split(", ");

                int valuesStart = query.indexOf("VALUES (") + "VALUES (".length();
                int valuesEnd = query.indexOf(");");
                String valuesPart = query.substring(valuesStart, valuesEnd).trim();
                String[] values = valuesPart.split(", ");

                if (columns.length == values.length) {
                    for (int i = 0; i < columns.length; i++) {
                        String columnName = columns[i].trim();
                        String value = values[i].trim();

                        table.put(columnName, value);
                    }
                    saveTableDataToFile();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Retrieves and formats all data from a table.
     *
     * @param tableName The name of the table.
     * @return A formatted string containing all data from the specified table.
     */
    public String selectAllFromTable(String tableName) {
        if (tables.containsKey(tableName)) {
            Map<String, String> table = tables.get(tableName);

            if (table.isEmpty()) {
                return "Table is empty.";
            }

            StringBuilder result = new StringBuilder();
            result.append("SELECT Result for ").append(tableName).append(":\n");

            // Get the maximum length of keys and values for formatting
            int maxKeyLength = table.keySet().stream().map(String::length).max(Integer::compareTo).orElse(0);
            int maxValueLength = table.values().stream().map(String::length).max(Integer::compareTo).orElse(0);

            // Create a header row
            result.append("+");
            result.append("-".repeat(maxKeyLength + 2));
            result.append("+");
            result.append("-".repeat(maxValueLength + 2));
            result.append("+\n");

            result.append("| Key");
            result.append(" ".repeat(maxKeyLength - 3));
            result.append("| Value");
            result.append(" ".repeat(maxValueLength - 5));
            result.append("|\n");

            // Get keys in alphabetical order
            List<String> sortedKeys = new ArrayList<>(table.keySet());
            Collections.sort(sortedKeys);

            // Add rows for each key-value pair in sorted order
            for (String key : sortedKeys) {
                result.append("+");
                result.append("-".repeat(maxKeyLength + 2));
                result.append("+");
                result.append("-".repeat(maxValueLength + 2));
                result.append("+\n");

                result.append("| ").append(key);
                result.append(" ".repeat(maxKeyLength - key.length()));
                result.append("| ").append(table.get(key));
                result.append(" ".repeat(maxValueLength - table.get(key).length()));
                result.append("|\n");
            }

            result.append("+");
            result.append("-".repeat(maxKeyLength + 2));
            result.append("+");
            result.append("-".repeat(maxValueLength + 2));
            result.append("+\n");

            return result.toString();
        }
        return "Table does not exist.";
    }


    /**
     * Deletes a table and its data.
     *
     * @param tableName The name of the table to be deleted.
     * @return True if the table was successfully deleted, false if the table does not exist.
     */
    public boolean deleteTable(String tableName) {
        if (tables.containsKey(tableName)) {
            tables.remove(tableName);
            saveTableDataToFile();
            return true;
        }
        return false;
    }

    /**
     * Updates data in a table based on specified conditions.
     *
     * @param tableName      The name of the table.
     * @param keyToUpdate    The column to update.
     * @param valueToUpdate  The new value to set.
     * @param conditionKey   The column used as a condition for the update.
     * @param conditionValue The value to match in the condition column.
     * @return A message indicating the result of the update operation.
     */
    public String updateTable(String tableName, String keyToUpdate, String valueToUpdate, String conditionKey, String conditionValue) {
        if (tables.containsKey(tableName)) {
            Map<String, String> table = tables.get(tableName);
            if (table.containsKey(conditionKey) && table.get(conditionKey).equals(conditionValue)) {
                table.put(keyToUpdate, valueToUpdate);
                saveTableDataToFile();
                return "Table updated.";
            } else {
                return "Condition not met or key not found.";
            }
        }
        return "Table does not exist.";
    }
}