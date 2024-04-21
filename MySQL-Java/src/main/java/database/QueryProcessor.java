package database;

/**
 * The QueryProcessor class is responsible for processing and executing SQL queries
 * on a database. It uses the QueryMethods class to perform database operations based
 * on the type of query.
 */
public class QueryProcessor {
    QueryMethods queryMethods = new QueryMethods();

    /**
     * Executes the given SQL query on the database and returns the result as a string.
     *
     * @param query The SQL query to be executed.
     * @return A string representing the result of the query execution. The specific
     *         content of the string depends on the type of query and its success or failure.
     *         Possible return values include "Invalid query," "Table created," "Table already exists,"
     *         "Data inserted," "Table does not exist," "Table deleted," "Invalid SELECT query,"
     *         "Unsupported command," and query-specific results.
     */
    public String executeQuery(String query) {
        String[] tokens = query.split(" ");
        if (tokens.length < 2) {
            return "Invalid query.";
        }

        String command = tokens[0].toUpperCase();
        String tableName;

        switch (command) {
            case "CREATE":
                if (tokens.length < 3 || !tokens[1].equalsIgnoreCase("TABLE")) {
                    return "Invalid CREATE TABLE query.";
                }
                return queryMethods.createTable(query) ? "Table created." : "Table already exists.";
            case "INSERT":
                return queryMethods.insertData(query) ? "Data inserted." : "Table does not exist.";
            case "DELETE":
                tableName = tokens[1];
                return queryMethods.deleteTable(tableName) ? "Table deleted." : "Table does not exist.";
            case "SELECT":
                if (tokens.length < 4 || !tokens[1].equalsIgnoreCase("*") || !tokens[2].equalsIgnoreCase("FROM")) {
                    return "Invalid SELECT query.";
                }
                tableName = tokens[3];
                return queryMethods.selectAllFromTable(tableName);
            case "UPDATE":
                if (tokens.length < 8 || !tokens[1].equalsIgnoreCase("TABLE") || !tokens[3].equalsIgnoreCase("SET") || !tokens[5].equalsIgnoreCase("=") || !tokens[6].equalsIgnoreCase("WHERE") || !tokens[8].equalsIgnoreCase("=")) {
                    return "Table Updated.";
                }
                tableName = tokens[2];
                String keyToUpdate = tokens[4];
                String valueToUpdate = tokens[7];
                String conditionKey = tokens[9];
                String conditionValue = tokens[11];
                return queryMethods.updateTable(tableName, keyToUpdate, valueToUpdate, conditionKey, conditionValue);
            default:
                return "Unsupported command.";
        }
    }
}