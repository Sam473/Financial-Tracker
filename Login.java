import java.io.IOException;

public class Login {

	//returns whether or not they logged in
	public boolean mainMenu() throws IOException {
		System.out.println("Please select an option by using the character in brackets:\n" + "1. I have an account"
				+ "2. I need to register");
		String input = App.userIn.readLine();
		switch (input) {
		case "1":
			return checkDetails();
		case "2":
			if (!doesAccExist()) {
				return addAccount();
			}
		default:
			System.out.println("Not an option, try again.");
			break;
		}
		return false;
	}
	
	public boolean addAccount() {
		return false;
	}
	
	public boolean checkDetails() {
		return false;
	}
	
	public boolean doesAccExist() {
		return false;
	}

}
