import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Sam
 *
 */
public class Login {

	/**
	 * Main menu for the login form 
	 * @return whether the login was successful. 
	 * @throws IOException
	 */
	public boolean mainMenu() throws IOException {
		//Boolean value to decide whether to loop through the menu again
		boolean running = true;

		while (running) {
			System.out.println("Please select an option:\n" + "1. Login\n" + "2. Register\n3. Exit the app");
			String input = App.userIn.readLine();

			switch (input) {
			case "1":
				return checkDetails();
			case "2":
				if (doesAccExist()) {
					System.out.println("An account exists on this device, please login");
					break;
				}
				addAccount();
				break;
			case "3":
				System.out.println("Thank you for using the financial budget app");
				running = false;
				RetrieveAndStore.closeDBConnection();
				break;
			default:
				System.out.println("Not an option, try again.");
				break;
			}
		}
		return false; //returns false to show login failed if the user decides to leave
	}

	/**
	 * Takes user input and adds user to the database
	 * @throws IOException
	 */
	private void addAccount() throws IOException {
		System.out.println("Please enter your username: ");
		String username = App.userIn.readLine();
		System.out.println("Please enter your password: ");
		String password = App.userIn.readLine();
		//Hash the password before adding to the DB to ensure security
		String hashedPassword = hashPasswordMD5(password);

		RetrieveAndStore.sqlExecute(
				"INSERT INTO tblUser (UserName, Password) VALUES ('" + username + "', '" + hashedPassword + "')");
	}

	/**
	 * @return if the login succeeds
	 * @throws IOException
	 */
	private boolean checkDetails() throws IOException {
		System.out.println("Please enter your username: ");
		String username = App.userIn.readLine();
		System.out.println("Please enter your password: ");
		String password = App.userIn.readLine();
		//hash the password before checking against hashed password from the DB
		String hashedPassword = hashPasswordMD5(password);

		ResultSet rs = RetrieveAndStore.readAllRecords("tblUser");
		try {
			while (rs.next()) // Loop through the user table
			{
				// Store username and password to verify inputs
				String realUsername = rs.getString("UserName");
				String realPassword = rs.getString("Password");

				//compare inputs to db records
				if ((realUsername.equals(username)) && (realPassword.equals(hashedPassword))) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Username or password incorrect"); //All in one statement at the end - secure coding
		return false;
	}

	/**
	 * check the DB for previously defined accounts
	 * @return if there is an account already
	 */
	private boolean doesAccExist() {
		//If there is a record then we don't want to create a new acc
		if (RetrieveAndStore.maxID("tblUser", "UserID") > 0) { 
			return true;
		}
		return false;
	}

	/**
	 * Method to hash the password with the md5 hashing algorithm to ensure user security
	 * Simpler hashing algorithm without salt as this is not necessary in this case.
	 * @param passwordToHash - the user input to be md5 hashed
	 * @return generatedPassword - the md5 hashed password to be stored securely
	 */
	private String hashPasswordMD5(String passwordToHash) {
		String generatedPassword = null;
		try {
			// Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance("MD5");
			// Add password bytes to digest
			md.update(passwordToHash.getBytes());
			// Get the hash's bytes
			byte[] bytes = md.digest();
			// This bytes[] has bytes in decimal format;
			// Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			// Get complete hashed password in hex format
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

}
