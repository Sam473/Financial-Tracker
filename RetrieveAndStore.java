import java.sql.*; 


/**
 * Class to store and retrieve data from a database
 * @author Sam
 */

public class RetrieveAndStore {

	private static Connection conn;
	private static Statement s;

	public static void startDBConnection() {
		try {
			String database = "jdbc:ucanaccess://"+ System.getProperty("user.dir") + "/ProgramDB.mdb";
			conn = DriverManager.getConnection(database, "", "");
			s = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//command.close();  // Close the statement
		//conn.close(); // Close the database. Its no more required
	}

	public static void writeToFile(String sqlString) {
		try {
			s.execute(sqlString);
			s.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Read file to decide a record number
	}

	public void readFromFile(String fileName, int index, int numOFLines) { 
		
	}

	private int numOfLines() { //To figure out ID
		return 0;
	}

}
