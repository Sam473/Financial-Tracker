import java.io.IOException;
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
				if (!doesAccExist()) {
					addAccount();
				}
				System.out.println("An account exists on this device, please login");
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

		RetrieveAndStore
				.sqlExecute("INSERT INTO tblUser (UserName, Password) VALUES ('" + username + "', '" + password + "')");
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

		ResultSet rs = RetrieveAndStore.readAllRecords("tblUser");
		try {
			while (rs.next()) // Loop through the resultset
			{
				// Store username and password to verify inputs
				String realUsername = rs.getString("UserName");
				String realPassword = rs.getString("Password");

				if ((realUsername.equals(username)) && (realPassword.equals(password))) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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

}
