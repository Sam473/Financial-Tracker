import java.sql.*; 


/**
 * Class to store and retrieve data from a database
 * @author Sam
 */

public class RetrieveAndStore {

	private static Connection conn;
	private static Statement s;

	/**
	 * Called from main to connect to the database
	 */
	public static void startDBConnection() {
		try {
			String database = "jdbc:ucanaccess://"+ System.getProperty("user.dir") + "/ProgramDB.mdb";
			conn = DriverManager.getConnection(database, "", "gfg64%43df3*f\"A");
			s = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * Executes SQL queries to interact with file
	 * @param sqlString
	 * 		String of sql to be executed upon the database
	 */
	public static void sqlExecute(String sqlString) {
		try {
			s.execute(sqlString);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Executes SQL query to read all records from a table and return
	 * @param table
	 * 		table to read from with SQL
	 * @return ResultSet
	 * 		Returns set containing all records within the specified table
	 */
	public static ResultSet readAllRecords(String table) { 
		//Read all records from the database
		ResultSet rs = null;
		try {
			rs = s.executeQuery("SELECT * FROM "+ table);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * Executes SQL query to find max ID in a table
	 * @param table
	 * 		table to read from with SQL
	 * @param columnName
	 * 		Column in the DB table to read max ID from
	 * @return maximum ID in the specified location as an integer
	 */
	public static int maxID (String table ,String columnName) {
		int max = 0; //Stores the max ID
		try {
			ResultSet rs =  s.executeQuery("SELECT " + columnName + " FROM " + table);
			while (rs.next()) { //Loop through all IDs to find the max
				if (rs.getInt(columnName) > max) {
					max = rs.getInt(columnName);
				}
			}
		} catch (SQLException e) { 
			System.out.println("Unable to take input from console");
			e.printStackTrace();
		}
		return max; 
	}
	
	/**
	 * Called from main to connect to the database
	 */
	public static void closeDBConnection () {
		try {
			s.close(); //Close the statement
			conn.close(); //Close the database
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
