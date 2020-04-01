import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RecurringOutgoings {


    /**
     * will ask for which option on
     * takes input from main menu and calls correct method
     * @throws IOException
     */
    public void mainMenu() throws IOException {
        boolean running = true;
        while (running) {
            System.out.println("Please select an option by using the character in brackets:\n" +
                    "1. Add new regular payment (Monthly)\n" +
                    "2. View all outgoings\n" +
                    "3. Your total outgoings\n" +
                    "4. Remove outgoing\n" +
                    "5. Return to main menu");
            String input = App.userIn.readLine();
            switch (input) {
            case "1":
                addOutgoing();
                System.out.println("Successfully added new outgoing\n");
                break;
            case "2":
                viewOutgoings();
                break;
            case "3":
            	System.out.println("Your total outgoings:\n�" + totalOutgoings());
            	break;
            case "4":
                removeOutgoing();
                System.out.println("Successfully removed outgoing");
                break;
            case "5":
                running = false;
                System.out.println("Exiting to Main Menu...\n\n");
                break;
            default:
            	System.out.println("Not an option, try again.");
                break;
        }
        }
    }

    /**
     * will sum all outgoings
     * @return sum of all outgoings
     */
    private float totalOutgoings()  {
    	ResultSet rs = RetrieveAndStore.readAllRecords("tblOutgoings");
    	float total = 0;
		try {
			while (rs.next()) // Loop through the resultset
			{
				total += rs.getInt("OutgoingAmount"); //Sum total of all outgoings
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total;
    }

    private void viewOutgoings() {
    	ResultSet rs = RetrieveAndStore.readAllRecords("tblOutgoings");
		try {
			while (rs.next()) // Loop through the resultset
			{
				// Store each outgoing record to print
				int id = rs.getInt("OutgoingID");
				String name = rs.getString("OutgoingName");
				float amount = rs.getInt("OutgoingAmount");

				// print the results
				System.out.format("%s. Name = %s, Amount = �%s\n", id, name, amount);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


    /**
     * will add an outgoing to the outgoings in properties file
     * @throws IOException
     * @throws NumberFormatException
     */
    private void addOutgoing() throws NumberFormatException, IOException {
        System.out.println("How much will you be paying per month?");
        float amount = Float.parseFloat(App.userIn.readLine());
        System.out.println("What is the name of this payment?");
        String name = App.userIn.readLine();

        RetrieveAndStore.sqlExecute("INSERT INTO tblOutgoings (OutgoingName, OutgoingAmount) VALUES ('" + name + "', "
				+ amount + ")");

    }

    private void removeOutgoing() throws IOException {
        viewOutgoings();
        System.out.println("Please type the record number of the record you would like to delete");
        String value = App.userIn.readLine();
        if (!Validation.isInteger(value)) {
			return;
		}
		if (!Validation.isRangeValid(1, RetrieveAndStore.maxID("tblOutgoings", "OutgoingID"), Integer.parseInt(value))) {
			return;
		}
		RetrieveAndStore.sqlExecute("DELETE FROM tblOutgoings WHERE OutgoingID = " + value); // Call method to execute
																							   // deletion
		System.out.format("Record %s deleted successfully\n", value); // Tell the user record has been removed
    }
}
