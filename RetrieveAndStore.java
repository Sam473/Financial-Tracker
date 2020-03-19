import java.sql.*; 


/**
 * Class to store and retrieve data from a database
 * @author Sam
 */

public class RetrieveAndStore {

	private static Connection conn;
	private static Statement s;

	//called at start from main
	public static void startDBConnection() {
		try {
			String database = "jdbc:ucanaccess://"+ System.getProperty("user.dir") + "/ProgramDB.mdb";
			conn = DriverManager.getConnection(database, "", "");
			s = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public static void writeToFile(String sqlString) {
		try {
			s.execute(sqlString);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Read file to decide a record number
	}

	public static void readAllRecords(String fileName, int index, int numOFLines) { 
		//Read all records from the database
	}
	
	//Called at exit from main
	public static void closeDBConnection () {
		try {
			s.close(); //Close the statement
			conn.close(); //Close the database
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
