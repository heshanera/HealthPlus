package com.hms.hms_test_2;

import java.sql.*;
import java.util.*;

public class DatabaseOperator
{
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////// methods /////////////////////////////////////////////////////////////////////////////////////////////
	/*
	* 
	* 
	* 
	public DatabaseOperator()										==>	Constructor
        public DatabaseOperator(String dbClassName, String connection)						==>	Constructor
	
	public void connect(String userName, String password)							==>	connect to database
	public void close()											==>	close the connection
	public void createDatabase(String databaseName)								==>	creating a database
	public ArrayList<String> showDatabases()								==>	show the databases
	public void useDatabse(String databaseName)								==>	select a database
	public void createTable(String tableName,String columnHeaders)						==>	create a table
	public ArrayList<String> showTables()									==>	show tables in a database
	public ArrayList<ArrayList<String>> showTableMetaData(String tableName)                                 ==>     show the columns in the selected table 
	public boolean addTableRow(String table,String tableData)						==>	add new row
	public void deleteTableRow(String tableName, String ColumnName, String fieldValue)                      ==>	delete row (selecting a column with String data)
	public void deleteTableRow(String tableName, String ColumnName, int fieldValue)                         ==>	delete row (Selecting a column with int data)
	public ArrayList<ArrayList<String>> showTableData(String tableName)					==>	show table data
	public void showTableData(String tableName, String columNames)						==>	show table data (only selected columns)
	public ArrayList<ArrayList<String>> showTableData(String tableName,String columNames,String rowsAttributes)  ==>    show table data (only selected rows & columns)
	public void deleteDatabase(String DatabaseName)								==>	delete database
	public void deleteTable(String tableName)								==>	delete table
	
	public void customDeletion(String sql) 									==>	delete data from table
	public ArrayList<ArrayList<String>> customSelection(String sql)						==>	select data from table
	public boolean customInsertion(String sql) 								==>	insert data to table
	
	* 
	*/
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////// MetaData ///////////////////////////////////////////////////////////////
	
	
	private HashMap<String, String> metaDataHash = new HashMap<String, String>()
	{{
	    put("2003","ARRAY");
		put("-5","BIGINT");
		put("-2","BINARY");
		put("-7","BIT");
		put("2004","BLOB");
		put("16","BOOLEAN");
		put("1","CHAR");
		put("2005","CLOB");
		put("70","DATALINK");
		put("91","DATE");
		put("3","DECIMAL");
		put("2001","DISTINCT");
		put("8","DOUBLE");
		put("6","FLOAT");
		put("4","INTEGER");
		put("2000","JAVA_OBJECT");
		put("-16","LONGNVARCHAR");
		put("-4","LONGVARBINARY");
		put("-1","LONGVARCHAR");
		put("-15","NCHAR");
		put("2011","NCLOB");
		put("0","NULL");
		put("2","NUMERIC");
		put("-9","NVARCHAR");
		put("1111","OTHER");
		put("7","REAL");
		put("2006","REF");
		put("-8","ROWID");
		put("5","SMALLINT");
		put("2009","SQLXML");
		put("2002","STRUCT");
		put("92","TIME");
		put("93","TIMESTAMP");
		put("-6","TINYINT");
		put("-3","VARBINARY");
		put("12","VARCHAR");
	}};
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
    public static String dbClassName = "org.mariadb.jdbc.Driver";
    public static String CONNECTION = "jdbc:mysql://127.0.0.1/";
    
    public static Connection c;
    
    private static String currentDatabaseName;
    
    public DatabaseOperator()
    { }
    
    public DatabaseOperator(String dbClassName, String connection)
    {
		this.dbClassName = dbClassName;
		this.CONNECTION = connection;
	}
	
	public void connect(String userName, String password) throws ClassNotFoundException,SQLException
	{
		Class.forName(dbClassName);
		
        Properties p = new Properties();
        p.put("user", userName);
        p.put("password", password);
        // Try to connect
        this.c = DriverManager.getConnection(CONNECTION,p);
	}
	
	public void close() throws ClassNotFoundException,SQLException
	{
		c.close();
	}
	
	public void createDatabase(String databaseName) throws ClassNotFoundException,SQLException
	{
		
		
		String sql = "CREATE DATABASE " + databaseName + ";";
		PreparedStatement stmt = c.prepareStatement(sql);
		try
		{
			stmt.executeUpdate();
			System.out.println("Creating Database...\n");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Error in creating Database");
		}

        stmt.close();
	}
	
	
	public ArrayList<String> showDatabases() throws ClassNotFoundException,SQLException
	{
		System.out.println("Retreiving data from the Database...\n");
		
		int i = 1;
		String sql = "SHOW DATABASES;";
		PreparedStatement stmt = c.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery(sql);
		ArrayList<String> dbNames = new ArrayList<String>();
		
		
		while(rs.next())
		{
			//Retrieve by column name
			String dbName  = rs.getString("Database");
			dbNames.add(dbName);
			//Display values
			System.out.print(i+" " + dbName+"\n");i++;
		}
		
		rs.close();
        stmt.close();
        return dbNames;
	}
	
	public void useDatabse(String databaseName) throws ClassNotFoundException,SQLException
	{
		String sql = "USE " + databaseName + ";";
		PreparedStatement stmt = c.prepareStatement(sql);
		ResultSet rs = null;
		try
		{
			rs = stmt.executeQuery(sql);
			System.out.println("\n### Database Changed to " + databaseName+"###\n");
			currentDatabaseName = databaseName;
			rs.close();
		}
		catch(Exception e)
		{
			System.out.println("No Such Database exist!!!");
		}
		finally
		{
			
			stmt.close();
		}
	}
	
	public void createTable(String tableName,String columnHeaders) throws ClassNotFoundException,SQLException
	{
		
		try
		{
			String sql = "CREATE TABLE " + tableName + " " + columnHeaders + ";";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.executeUpdate();
			System.out.println("Created the table...");
			stmt.close();
        }catch(Exception e)
        {
			System.out.println("Error in creating the table...");
		}
        
       
	}
	
	public ArrayList<String> showTables() throws ClassNotFoundException,SQLException
	{
		
		String sql = "SHOW TABLES;";
		PreparedStatement stmt = c.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery(sql);
		ArrayList<String> tables = new ArrayList<String>();
		
		System.out.println("Tables of " + currentDatabaseName + "...\n");
		int tableIndex = 0;
		while(rs.next())
		{
			//Retrieve by column name
			String table  = rs.getString("Tables_in_" + currentDatabaseName);
			//Display values
			tables.add(table);
			System.out.println(tableIndex + " " + table);tableIndex++;
		}
		rs.close();
        stmt.close();
        return tables;
	}
	
	
	public ArrayList<ArrayList<String>> showTableMetaData(String tableName)	 throws ClassNotFoundException,SQLException
	{
		String sql = "SELECT * FROM " + tableName + ";";
		PreparedStatement stmt = c.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int noOfColumns = rsmd.getColumnCount();
		
		ArrayList<String> TableColumnKeys = new ArrayList<String>();
		ResultSet rss;
		
		DatabaseMetaData meta = c.getMetaData();
		try{
			rss = meta.getPrimaryKeys(null, null, tableName);
			while(rss.next())
			{
				//System.out.println("Primery Key :"+rss.getString(4));
				TableColumnKeys.add(rss.getString(4));
			}
		
			rss=meta.getExportedKeys(null, null, tableName);
			while(rss.next())
			{
				//System.out.println("Foreign Key :"+rss.getString(4));
				TableColumnKeys.add(rss.getString(4));
			}
			
		}catch(Exception e){}		
	
		int nullable;
		int type;
		
		ArrayList<String> TableColumnNames = new ArrayList<String>();
		ArrayList<String> TableDataTypes = new ArrayList<String>();
		ArrayList<String> TableColumnNull = new ArrayList<String>();
		
		for(int i = 0; i<noOfColumns; i++)
		{
			String name = rsmd.getColumnName(i+1);
			TableColumnNames.add(name);
			
			type = rsmd.getColumnType(i+1);
			
			String typeName = metaDataHash.get(Integer.toString(type));
			TableDataTypes.add(typeName);
			//System.out.println(typeName);
			
			nullable = rsmd.isNullable(i+1);
			if(nullable == ResultSetMetaData.columnNullable) 
			{
				TableColumnNull.add("Null");
			}
			else TableColumnNull.add("Not Null");
			
		}
		ArrayList<ArrayList<String>> metaData = new ArrayList<ArrayList<String>>();
		metaData.add(TableColumnNames);
		metaData.add(TableDataTypes);
		metaData.add(TableColumnNull);
		metaData.add(TableColumnKeys);
		return metaData;
	}
	
	public boolean addTableRow(String table,String tableData) throws ClassNotFoundException,SQLException
	{
		boolean result = true;
		String sql = "SELECT * FROM " + table + ";";
		PreparedStatement stmt = c.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int noOfColumns = rsmd.getColumnCount();
		String columnHeaders = "";
		for (int i = 0; i < noOfColumns; i++)
		{
			columnHeaders += "?";
			if (i < (noOfColumns-1)) 
				columnHeaders += ",";
		}
		
		String[] tableDataSplit = tableData.split(",");
		//System.out.println(tableData);
                
		sql = "INSERT INTO "+ table +" VALUES ("+ columnHeaders +")";
		stmt = c.prepareStatement(sql);
		
		for(int i = 0; i < noOfColumns; i++)
		{
                        //System.out.println(tableDataSplit[i]);
			stmt.setString(i+1, tableDataSplit[i]); 
		}
		
		try
		{
			stmt.executeUpdate();
			System.out.println("Inserted records into the table...");
		}catch(SQLException e)
		{
			System.out.println("Error when inserting records into the table...");
			result = false;
			e.printStackTrace();
		}
		
        rs.close();
        stmt.close();
        return result;
	}
	
	public void deleteTableRow(String tableName, String ColumnName, String fieldValue) throws ClassNotFoundException,SQLException
	{
		String sql = "DELETE FROM " + tableName + " WHERE " + ColumnName + "=?";
		PreparedStatement stmt = c.prepareStatement(sql);
		
		stmt.setString(1, fieldValue);

		try
		{
			stmt.executeUpdate();
			System.out.println("Deleted records from the table...");	
		}catch(SQLException e)
		{
			System.out.println("Error in Deleting records from the table...");
		}
        stmt.close();
	}
	
	public void deleteTableRow(String tableName, String ColumnName, int fieldValue) throws ClassNotFoundException,SQLException
	{
		String sql = "DELETE FROM " + tableName + " WHERE " + ColumnName + "=?";
		PreparedStatement stmt = c.prepareStatement(sql);
		
		stmt.setInt(1, fieldValue);

		try
		{
			stmt.executeUpdate();
			System.out.println("Deleted records from the table...");	
		}catch(SQLException e)
		{
			System.out.println("Error in Deleting records from the table...");
		}
        stmt.close();
	}
	
	
	public ArrayList<ArrayList<String>> showTableData(String tableName) throws ClassNotFoundException,SQLException
	{
		ArrayList<ArrayList<String>> main = null;
		try
		{
			String sql = "SELECT * FROM " + tableName + ";";
			PreparedStatement stmt = c.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int noOfColumns = rsmd.getColumnCount();
			
			
			String[] columnNames = new String[noOfColumns];
			for (int i = 0; i < noOfColumns; i++)
			{
				columnNames[i] = rsmd.getColumnName(i+1);
			}
			
			System.out.println("\n\nRetreiving records from the table...\n");
			
			for (String cName : columnNames)
			{
				System.out.print(cName + " | ");
			}
			System.out.print("\n\n");
			
			
			main = new ArrayList<ArrayList<String>>();
			String fieldValue;
			while(rs.next())
			{
				//Retrieve by column name
				ArrayList<String> tmpRow = new ArrayList<String>(); 
				for (int i = 0; i < noOfColumns; i++ )
				{
					fieldValue = rs.getString(columnNames[i]);
					//System.out.print(fieldValue + "\t");
					tmpRow.add(fieldValue);
				}
				main.add(tmpRow);
				System.out.println();
			}
			
	        rs.close();
	        stmt.close();
		}
		catch(Exception e)
		{
			System.out.println("\n\nTable Doesn't Exist!");
		}
		return main;
	}
	
	
	
	public void showTableData(String tableName, String columNames) throws ClassNotFoundException,SQLException
	{

		String[] splitedColumns = columNames.split(",");
		int length = splitedColumns.length;
		
		String sql = "SELECT "+ columNames +" FROM "+ tableName + ";";
		PreparedStatement stmt = c.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery(sql);
		
		System.out.println("\n\nRetreiving records from the table...\n");
		
		String fieldValue;
		while(rs.next())
		{
			//Retrieve by column name
			for (int i = 0; i < length; i++)
			{
				fieldValue  = rs.getString(splitedColumns[i]);
				System.out.print(fieldValue + "\t");
			}
			System.out.println();
		}
        rs.close();
        stmt.close();
		
	}
	
	public ArrayList<ArrayList<String>> showTableData(String tableName, String columNames, String rowsAttributes) throws ClassNotFoundException,SQLException
	{
		String[] splitedColumns = columNames.split(",");
		int length = splitedColumns.length;
		
		String sql = "SELECT "+ columNames +" FROM "+ tableName + " WHERE " + rowsAttributes + ";";
                //System.out.println(sql);
		PreparedStatement stmt = c.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery(sql);
		
		//System.out.println("\n\nRetreiving records from the table...\n");
		
		ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
		
		
		String fieldValue;
		while(rs.next())
		{	
			ArrayList<String> row = new ArrayList<String>();
			//Retrieve by column name
			for (int i = 0; i < length; i++)
			{
				fieldValue  = rs.getString(splitedColumns[i]);
				//System.out.print(fieldValue + "\t");
				row.add(fieldValue);
			}
			table.add(row);
			//System.out.println();
		}
        rs.close();
        stmt.close();
        return table;
	}
	
	public ArrayList<ArrayList<String>> customSelection(String sql) throws ClassNotFoundException,SQLException
	{
		ArrayList<ArrayList<String>> main = null;
		try
		{
			PreparedStatement stmt = c.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int noOfColumns = rsmd.getColumnCount();
			
			
			String[] columnNames = new String[noOfColumns];
			for (int i = 0; i < noOfColumns; i++)
			{
				columnNames[i] = rsmd.getColumnName(i+1);
			}
			
			/*
			System.out.println("\n\nRetreiving records from the table...\n");
			
			for (String cName : columnNames)
			{
				System.out.print(cName + " | ");
			}
			System.out.print("\n\n");
			*/
			
			main = new ArrayList<ArrayList<String>>();
			main.add(new ArrayList<String>(Arrays.asList(columnNames)));
			String fieldValue;
			while(rs.next())
			{
				//Retrieve by column name
				ArrayList<String> tmpRow = new ArrayList<String>(); 
				for (int i = 0; i < noOfColumns; i++ )
				{
					fieldValue = rs.getString(columnNames[i]);
					//System.out.print(fieldValue + "\t");
					tmpRow.add(fieldValue);
				}
				main.add(tmpRow);
				System.out.println();
			}
			
	        rs.close();
	        stmt.close();
		}
		catch(Exception e)
		{
			System.out.println("\n\nError: "+sql);
			e.printStackTrace();
		}
		return main;
	}
	
	
	public boolean customInsertion(String sql) throws ClassNotFoundException,SQLException
	{
		boolean result = true;
		try
		{
			PreparedStatement stmt = c.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery(sql);
                        try{
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int noOfColumns = rsmd.getColumnCount();
                        }catch(Exception e){}
                            
	        rs.close();
	        stmt.close();
		}
		catch(Exception e)
		{
			//System.out.println("\n\nTable Doesn't Exist!");
                        e.printStackTrace();
			result = false;
		}
		return result;
	}
	
	public void customDeletion(String sql) throws ClassNotFoundException,SQLException
	{
		PreparedStatement stmt = c.prepareStatement(sql);

		try
		{
			stmt.executeUpdate();
			System.out.println("Deleted records from the table...");	
		}catch(SQLException e)
		{
			System.out.println("Error in Deleting records from the table...");
		}
        stmt.close();
	}
	
	public void deleteTable(String tableName) throws ClassNotFoundException,SQLException
	{
		
		String sql = "DROP TABLE "+ tableName + ";";
		PreparedStatement stmt = c.prepareStatement(sql);
		try
		{
			stmt.executeUpdate();
			System.out.println("Table Deleted...");
        }catch(Exception e)
        {
			e.printStackTrace();
			System.out.println("Error in deleting the table...");
		}
		finally
		{
			stmt.close();
		}
	}
	
	

	
    
	
/*	
    public static void main(String[] args) throws ClassNotFoundException,SQLException
    {
	// Testing ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	/*	
	DatabaseOperator dbOperator1 = new DatabaseOperator();
        dbOperator1.connect("heshan","eradb");
        //dbOperator1.createDatabase("test3Database");
        dbOperator1.showDatabases();
        dbOperator1.useDatabse("HMStest1");
        dbOperator1.showTables();
        /*
        dbOperator1.showTableData("person");
        dbOperator1.showTableData("person","NIC,ID,age");
        dbOperator1.createTable("test3Table","( id INT NOT NULL,age INT NOT NULL,first VARCHAR(255),last VARCHAR(255),PRIMARY KEY ( id ))");
        dbOperator1.addTableRow("test3Table","123,20,Heshan,Eranga");
        dbOperator1.showTableData("test3Table");
        dbOperator1.deleteTableRow("test3Table","first","Heshan");
        dbOperator1.showTableData("test3Table");
        
        System.out.println(dbOperator1.showTableMetaData("test3Table").get(1).get(2));
        
        
        
        dbOperator1.deleteTable("test3Table");
        */
        
       // dbOperator1.showTableData("Person", "NIC,User_ID,Age","Last_name = 'Perera'") ;
        
        //dbOperator1.createTable("Admin","( id INT NOT NULL,age INT NOT NULL,User_Name VARCHAR(30),password VARCHAR(25),PRIMARY KEY ( id ))");
        //dbOperator1.addTableRow("Admin","123,20,admin123,admiN123@");
        //dbOperator1.addTableRow("Admin","456,25,doc123,docT123@");
        
       // System.out.println(dbOperator1.customSelection("select Person.First_Name, Doctor.Available_Days FROM Person LEFT JOIN Doctor ON Person.User_ID = Doctor.User_ID;"));
        
        /*
        dbOperator1.close();
        */
	
    //}

}
	
