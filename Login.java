import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

	// returns whether or not they logged in
	public boolean mainMenu() throws IOException {
		boolean running = true;
		
		while (running == true) {
			System.out.println("Please select an option:\n" + "1. Login\n"
					+ "2. Register\n3. Exit the app\n");
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
		return false;
	}

	private void addAccount() throws IOException {
		System.out.println("Please enter your username: ");
		String username = App.userIn.readLine();
		System.out.println("Please enter your password: ");
		String password = App.userIn.readLine();
		String hashedPassword = hashPasswordMD5(password);

		RetrieveAndStore
				.sqlExecute("INSERT INTO tblUser (UserName, Password) VALUES ('" + username + "', '" + hashedPassword + "')");
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
		String hashedPassword = hashPasswordMD5(password);

		ResultSet rs = RetrieveAndStore.readAllRecords("tblUser");
		try {
			while (rs.next()) // Loop through the resultset
			{
				// Store username and password to verify inputs
				String realUsername = rs.getString("UserName");
				String realPassword = rs.getString("Password");

				if ((realUsername.equals(username)) && (realPassword.equals(hashedPassword))) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Username or password incorrect");
		return false;
	}

	private boolean doesAccExist() {
		if (RetrieveAndStore.maxID("tblUser", "UserID") > 0) {
			return true;
		}
		return false;
	}
	
	private String hashPasswordMD5(String passwordToHash) {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes 
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } 
        catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
        }
		return generatedPassword;
    }

}
