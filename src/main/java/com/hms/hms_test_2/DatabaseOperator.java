package com.hms.hms_test_2;

import java.sql.*;
import java.util.*;

public class DatabaseOperator {
	private HashMap<String, String> metaDataHash = new HashMap<String, String>() {
		{
			put("2003", "ARRAY");
			put("-5", "BIGINT");
			put("-2", "BINARY");
			put("-7", "BIT");
			put("2004", "BLOB");
			put("16", "BOOLEAN");
			put("1", "CHAR");
			put("2005", "CLOB");
			put("70", "DATALINK");
			put("91", "DATE");
			put("3", "DECIMAL");
			put("2001", "DISTINCT");
			put("8", "DOUBLE");
			put("6", "FLOAT");
			put("4", "INTEGER");
			put("2000", "JAVA_OBJECT");
			put("-16", "LONGNVARCHAR");
			put("-4", "LONGVARBINARY");
			put("-1", "LONGVARCHAR");
			put("-15", "NCHAR");
			put("2011", "NCLOB");
			put("0", "NULL");
			put("2", "NUMERIC");
			put("-9", "NVARCHAR");
			put("1111", "OTHER");
			put("7", "REAL");
			put("2006", "REF");
			put("-8", "ROWID");
			put("5", "SMALLINT");
			put("2009", "SQLXML");
			put("2002", "STRUCT");
			put("92", "TIME");
			put("93", "TIMESTAMP");
			put("-6", "TINYINT");
			put("-3", "VARBINARY");
			put("12", "VARCHAR");
		}
	};

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static String dbClassName = "";
	public static String CONNECTION = "";
	public static String username = "";
	public static String password = "";
	public static String database = "";

	public static Connection c;

	private static String currentDatabaseName;

	/**
	 * Constructor to initialize DatabaseOperator object.
	 */
	public DatabaseOperator() {
		SystemConfiguration config = SystemConfiguration.getInstance();
		DatabaseOperator.dbClassName = config.getConfig("dbClassName");
		DatabaseOperator.CONNECTION = config.getConfig("connection");
		DatabaseOperator.username = config.getConfig("user");
		DatabaseOperator.password = config.getConfig("password");
		DatabaseOperator.database = config.getConfig("database");
	}

	/**
	 * Constructor to initialize DatabaseOperator with specific database class name
	 * and connection string.
	 * 
	 * @param databaseClassName the name of the database class (e.g.,
	 *                          "org.mariadb.jdbc.Driver")
	 * @param connection        the connection string to the database
	 */
	public DatabaseOperator(String databaseClassName, String connection) {
		dbClassName = databaseClassName;
		CONNECTION = connection;
	}

	/**
	 * Connects to the database using the provided username and password.
	 * 
	 * @param userName the username for the database connection
	 * @param password the password for the database connection
	 */
	public void connect(String userName, String password) throws ClassNotFoundException, SQLException {
		Class.forName(dbClassName);
		Properties p = new Properties();
		p.put("user", userName);
		p.put("password", password);
		c = DriverManager.getConnection(CONNECTION, p);
	}

	/**
	 * Connects to the database using the config username and password
	 */
	public void connect() throws ClassNotFoundException, SQLException {
		connect(username, password);
	}

	/**
	 * Closes the current database connection.
	 */
	public void close() throws ClassNotFoundException, SQLException {
		c.close();
	}

	/**
	 * Creates a new database with the given name.
	 * 
	 * @param databaseName the name of the database to be created
	 */
	public void createDatabase(String databaseName) throws ClassNotFoundException, SQLException {

		String sql = "CREATE DATABASE " + databaseName + ";";
		PreparedStatement stmt = c.prepareStatement(sql);
		try {
			stmt.executeUpdate();
			System.out.println("Creating Database...\n");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error in creating Database");
		}

		stmt.close();
	}

	/**
	 * Retrieves a list of all available databases.
	 * 
	 * @return an ArrayList of database names
	 */
	public ArrayList<String> showDatabases() throws ClassNotFoundException, SQLException {
		System.out.println("Retrieving data from the Database...\n");

		int i = 1;
		String sql = "SHOW DATABASES;";
		PreparedStatement stmt = c.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery(sql);
		ArrayList<String> dbNames = new ArrayList<String>();

		while (rs.next()) {
			// Retrieve by column name
			String dbName = rs.getString("Database");
			dbNames.add(dbName);
			// Display values
			System.out.print(i + " " + dbName + "\n");
			i++;
		}

		rs.close();
		stmt.close();
		return dbNames;
	}

	/**
	 * Selects the specified database to use.
	 * 
	 * @param databaseName the name of the database to select
	 */
	public void useDatabase(String databaseName) throws ClassNotFoundException, SQLException {
		String sql = "USE " + databaseName + ";";
		PreparedStatement stmt = c.prepareStatement(sql);
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(sql);
			System.out.println("\n### Database Changed to " + databaseName + "###\n");
			currentDatabaseName = databaseName;
			rs.close();
		} catch (Exception e) {
			System.out.println("No Such Database exist!!!");
		} finally {

			stmt.close();
		}
	}

	/**
	 * Connects and use database with config data
	 */
	public void connectAndUseDatabase() throws ClassNotFoundException, SQLException {
		connect();
		useDatabase(database);
	}

	/**
	 * Creates a new table in the current database with the given table name and
	 * column headers.
	 * 
	 * @param tableName     the name of the table to be created
	 * @param columnHeaders the headers of the columns in the table
	 */
	public void createTable(String tableName, String columnHeaders) throws ClassNotFoundException, SQLException {

		try {
			String sql = "CREATE TABLE " + tableName + " " + columnHeaders + ";";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.executeUpdate();
			System.out.println("Created the table...");
			stmt.close();
		} catch (Exception e) {
			System.out.println("Error in creating the table...");
		}

	}

	/**
	 * Retrieves a list of all tables in the current database.
	 * 
	 * @return an ArrayList of table names
	 */
	public ArrayList<String> showTables() throws ClassNotFoundException, SQLException {

		String sql = "SHOW TABLES;";
		PreparedStatement stmt = c.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery(sql);
		ArrayList<String> tables = new ArrayList<String>();

		System.out.println("Tables of " + currentDatabaseName + "...\n");
		int tableIndex = 0;
		while (rs.next()) {
			// Retrieve by column name
			String table = rs.getString("Tables_in_" + currentDatabaseName);
			// Display values
			tables.add(table);
			System.out.println(tableIndex + " " + table);
			tableIndex++;
		}
		rs.close();
		stmt.close();
		return tables;
	}

	/**
	 * Retrieves the metadata of the specified table, including column names and
	 * data types.
	 * 
	 * @param tableName the name of the table whose metadata is to be retrieved
	 * @return a list of metadata for the table (column names, data types, etc.)
	 */
	public ArrayList<ArrayList<String>> showTableMetaData(String tableName)
			throws ClassNotFoundException, SQLException {
		String sql = "SELECT * FROM " + tableName + ";";
		PreparedStatement stmt = c.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int noOfColumns = rsmd.getColumnCount();

		ArrayList<String> TableColumnKeys = new ArrayList<String>();
		ResultSet rss;

		DatabaseMetaData meta = c.getMetaData();
		try {
			rss = meta.getPrimaryKeys(null, null, tableName);
			while (rss.next()) {
				TableColumnKeys.add(rss.getString(4));
			}

			rss = meta.getExportedKeys(null, null, tableName);
			while (rss.next()) {
				TableColumnKeys.add(rss.getString(4));
			}

		} catch (Exception e) {
		}

		int nullable;
		int type;

		ArrayList<String> TableColumnNames = new ArrayList<String>();
		ArrayList<String> TableDataTypes = new ArrayList<String>();
		ArrayList<String> TableColumnNull = new ArrayList<String>();

		for (int i = 0; i < noOfColumns; i++) {
			String name = rsmd.getColumnName(i + 1);
			TableColumnNames.add(name);

			type = rsmd.getColumnType(i + 1);

			String typeName = metaDataHash.get(Integer.toString(type));
			TableDataTypes.add(typeName);
			// System.out.println(typeName);

			nullable = rsmd.isNullable(i + 1);
			if (nullable == ResultSetMetaData.columnNullable) {
				TableColumnNull.add("Null");
			} else
				TableColumnNull.add("Not Null");

		}
		ArrayList<ArrayList<String>> metaData = new ArrayList<ArrayList<String>>();
		metaData.add(TableColumnNames);
		metaData.add(TableDataTypes);
		metaData.add(TableColumnNull);
		metaData.add(TableColumnKeys);
		return metaData;
	}

	/**
	 * Adds a new row to the specified table.
	 * 
	 * @param table     the name of the table to which the row will be added
	 * @param tableData the data to be inserted into the row
	 * @return true if the row was added successfully, false otherwise
	 */
	public boolean addTableRow(String table, String tableData) throws ClassNotFoundException, SQLException {
		boolean result = true;
		String sql = "SELECT * FROM " + table + ";";
		PreparedStatement stmt = c.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int noOfColumns = rsmd.getColumnCount();
		String columnHeaders = "";
		for (int i = 0; i < noOfColumns; i++) {
			columnHeaders += "?";
			if (i < (noOfColumns - 1))
				columnHeaders += ",";
		}

		String[] tableDataSplit = tableData.split(",");

		sql = "INSERT INTO " + table + " VALUES (" + columnHeaders + ")";
		stmt = c.prepareStatement(sql);

		for (int i = 0; i < noOfColumns; i++) {
			// System.out.println(tableDataSplit[i]);
			stmt.setString(i + 1, tableDataSplit[i]);
		}

		try {
			stmt.executeUpdate();
			System.out.println("Inserted records into the table...");
		} catch (SQLException e) {
			System.out.println("Error when inserting records into the table...");
			result = false;
			e.printStackTrace();
		}

		rs.close();
		stmt.close();
		return result;
	}

	/**
	 * Deletes a row from the specified table, selecting the row based on a column
	 * with String data.
	 * 
	 * @param tableName  the name of the table from which the row will be deleted
	 * @param ColumnName the name of the column to identify the row
	 * @param fieldValue the value of the column to match for deletion
	 */
	public void deleteTableRow(String tableName, String ColumnName, String fieldValue)
			throws ClassNotFoundException, SQLException {
		String sql = "DELETE FROM " + tableName + " WHERE " + ColumnName + "=?";
		PreparedStatement stmt = c.prepareStatement(sql);

		stmt.setString(1, fieldValue);

		try {
			stmt.executeUpdate();
			System.out.println("Deleted records from the table...");
		} catch (SQLException e) {
			System.out.println("Error in Deleting records from the table...");
		}
		stmt.close();
	}

	/**
	 * Deletes a row from the specified table, selecting the row based on a column
	 * with int data.
	 * 
	 * @param tableName  the name of the table from which the row will be deleted
	 * @param ColumnName the name of the column to identify the row
	 * @param fieldValue the value of the column to match for deletion
	 */
	public void deleteTableRow(String tableName, String ColumnName, int fieldValue)
			throws ClassNotFoundException, SQLException {
		String sql = "DELETE FROM " + tableName + " WHERE " + ColumnName + "=?";
		PreparedStatement stmt = c.prepareStatement(sql);

		stmt.setInt(1, fieldValue);

		try {
			stmt.executeUpdate();
			System.out.println("Deleted records from the table...");
		} catch (SQLException e) {
			System.out.println("Error in Deleting records from the table...");
		}
		stmt.close();
	}

	/**
	 * Retrieves the data of the specified table.
	 * 
	 * @param tableName the name of the table whose data is to be retrieved
	 * @return a list of lists representing the table data
	 */
	public ArrayList<ArrayList<String>> showTableData(String tableName) throws ClassNotFoundException, SQLException {
		ArrayList<ArrayList<String>> main = null;
		try {
			String sql = "SELECT * FROM " + tableName + ";";
			PreparedStatement stmt = c.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int noOfColumns = rsmd.getColumnCount();

			String[] columnNames = new String[noOfColumns];
			for (int i = 0; i < noOfColumns; i++) {
				columnNames[i] = rsmd.getColumnName(i + 1);
			}

			System.out.println("\n\nRetrieving records from the table...\n");

			for (String cName : columnNames) {
				System.out.print(cName + " | ");
			}
			System.out.print("\n\n");

			main = new ArrayList<ArrayList<String>>();
			String fieldValue;
			while (rs.next()) {
				// Retrieve by column name
				ArrayList<String> tmpRow = new ArrayList<String>();
				for (int i = 0; i < noOfColumns; i++) {
					fieldValue = rs.getString(columnNames[i]);
					tmpRow.add(fieldValue);
				}
				main.add(tmpRow);
				System.out.println();
			}

			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.out.println("\n\nTable Doesn't Exist!");
		}
		return main;
	}

	/**
	 * Retrieves the data of the specified table, only for the selected columns.
	 * 
	 * @param tableName  the name of the table whose data is to be retrieved
	 * @param columNames a comma-separated list of column names to be included in
	 *                   the result
	 */
	public void showTableData(String tableName, String columNames) throws ClassNotFoundException, SQLException {

		String[] splittedColumns = columNames.split(",");
		int length = splittedColumns.length;

		String sql = "SELECT " + columNames + " FROM " + tableName + ";";
		PreparedStatement stmt = c.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery(sql);

		System.out.println("\n\nRetrieving records from the table...\n");

		String fieldValue;
		while (rs.next()) {
			// Retrieve by column name
			for (int i = 0; i < length; i++) {
				fieldValue = rs.getString(splittedColumns[i]);
				System.out.print(fieldValue + "\t");
			}
			System.out.println();
		}
		rs.close();
		stmt.close();

	}

	/**
	 * Retrieves the data of the specified table, only for the selected rows and
	 * columns.
	 * 
	 * @param tableName      the name of the table whose data is to be retrieved
	 * @param columNames     a comma-separated list of column names to be included
	 *                       in the result
	 * @param rowsAttributes the conditions to filter the rows
	 * @return a list of lists representing the filtered table data
	 */
	public ArrayList<ArrayList<String>> showTableData(String tableName, String columNames, String rowsAttributes)
			throws ClassNotFoundException, SQLException {
		String[] splittedColumns = columNames.split(",");
		int length = splittedColumns.length;

		String sql = "SELECT " + columNames + " FROM " + tableName + " WHERE " + rowsAttributes + ";";
		PreparedStatement stmt = c.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery(sql);

		ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();

		String fieldValue;
		while (rs.next()) {
			ArrayList<String> row = new ArrayList<String>();
			// Retrieve by column name
			for (int i = 0; i < length; i++) {
				fieldValue = rs.getString(splittedColumns[i]);
				row.add(fieldValue);
			}
			table.add(row);
		}
		rs.close();
		stmt.close();
		return table;
	}

	/**
	 * Performs a custom selection operation on a table using the provided SQL
	 * query.
	 * 
	 * @param sql the SQL query for selection
	 * @return a list of lists containing the selected data
	 */
	public ArrayList<ArrayList<String>> customSelection(String sql) throws ClassNotFoundException, SQLException {
		ArrayList<ArrayList<String>> main = null;
		try {
			PreparedStatement stmt = c.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int noOfColumns = rsmd.getColumnCount();

			String[] columnNames = new String[noOfColumns];
			for (int i = 0; i < noOfColumns; i++) {
				columnNames[i] = rsmd.getColumnName(i + 1);
			}

			main = new ArrayList<ArrayList<String>>();
			main.add(new ArrayList<String>(Arrays.asList(columnNames)));
			String fieldValue;
			while (rs.next()) {
				// Retrieve by column name
				ArrayList<String> tmpRow = new ArrayList<String>();
				for (int i = 0; i < noOfColumns; i++) {
					fieldValue = rs.getString(columnNames[i]);
					tmpRow.add(fieldValue);
				}
				main.add(tmpRow);
				System.out.println();
			}

			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.out.println("\n\nError: " + sql);
			e.printStackTrace();
		}
		return main;
	}

	/**
	 * Performs a custom insertion operation on a table using the provided SQL
	 * query.
	 * 
	 * @param sql the SQL query for insertion
	 * @return true if the insertion was successful, false otherwise
	 */
	public boolean customInsertion(String sql) throws ClassNotFoundException, SQLException {
		boolean result = true;
		try {
			PreparedStatement stmt = c.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery(sql);
			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.out.println("Error in selecting the data...");
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/**
	 * Performs a custom deletion operation on a table using the provided SQL query.
	 * 
	 * @param sql the SQL query for deletion
	 */
	public void customDeletion(String sql) throws ClassNotFoundException, SQLException {
		PreparedStatement stmt = c.prepareStatement(sql);

		try {
			stmt.executeUpdate();
			System.out.println("Deleted records from the table...");
		} catch (SQLException e) {
			System.out.println("Error in Deleting records from the table...");
		}
		stmt.close();
	}

	/**
	 * Deletes the specified table from the database.
	 * 
	 * @param tableName the name of the table to be deleted
	 */

	public void deleteTable(String tableName) throws ClassNotFoundException, SQLException {

		String sql = "DROP TABLE " + tableName + ";";
		PreparedStatement stmt = c.prepareStatement(sql);
		try {
			stmt.executeUpdate();
			System.out.println("Table Deleted...");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in deleting the table...");
		} finally {
			stmt.close();
		}
	}
}
