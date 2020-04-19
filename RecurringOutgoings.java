import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class RecurringOutgoings {


    /**
     * will ask for which option on
     * takes input from main menu and calls correct method
     * @throws IOException
     */
    public void mainMenu() throws IOException {
        boolean running = true;
        try {
            while (running) {
                System.out.println("Please select an option by using the character in brackets:\n" +
                        "1. Add new regular payment (Monthly)\n" +
                        "2. View all outgoings\n" +
                        "3. Your total outgoings\n" +
                        "4. Edit an outgoing\n" +
                        "5. Remove outgoing\n" +
                        "6. Return to main menu");
                String input = App.userIn.readLine();
                switch (input) {
                    case "1":
                        addOutgoing();
                        break;
                    case "2":
                        viewOutgoings();
                        break;
                    case "3":
                        System.out.println("Your total outgoings:\n£" + totalOutgoings());
                        break;
                    case "4":
                        editOutgoing();
                        break;
                    case "5":
                        removeOutgoing();
                        System.out.println("Successfully removed outgoing");
                        break;
                    case "6":
                        running = false;
                        System.out.println("Exiting to Main Menu...\n\n");
                        break;
                    default:
                        System.out.println("Not an option, try again.");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error with DB");
        }
    }

    /**
     * will sum all outgoings
     * @return sum of all outgoings
     */
    public float totalOutgoings()  {
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
				double amount = rs.getDouble("OutgoingAmount");

				// print the results
				System.out.format("\n%s: %s\n£%.2f\n", id, name, amount);
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
        String amountString = App.userIn.readLine();
        double amount;
        if(!Validation.isDouble(amountString) || (amount = Double.parseDouble(amountString)) <= 0) {
            System.out.println("Invalid amount given\n");
            return;
        }

        if (amountString.contains(".")) {
            int decimalPos = amountString.indexOf('.');
            if ((amountString.length() - 1) - decimalPos > 2) {
                System.out.println("Invalid number, too precise\n\n");
                return;
            }
        }
        System.out.println("What is the name of this payment?");
        String name = App.userIn.readLine();

        RetrieveAndStore.sqlExecute("INSERT INTO tblOutgoings (OutgoingName, OutgoingAmount) VALUES ('" + name + "', "
				+ amount + ")");
        System.out.println("Successfully added new outgoing\n");
        RetrieveAndStore.rowNumberUpdater("tblOutgoings","OutgoingID");

    }

    private void editOutgoing() throws SQLException, IOException {
        ResultSet rs = RetrieveAndStore.readAllRecords("tblOutgoings");
        while(rs.next()){
            System.out.println(String.format("\n%d: %s\n£%.2f\n", rs.getInt("OutgoingID"),
                    rs.getString("OutgoingName"), rs.getDouble("OutgoingAmount")));
        }
        System.out.println("Please enter the ID of the outgoing you wish to update");
        String idString = App.userIn.readLine();
        if(!(Validation.isInteger(idString))) return;
        int id = Integer.parseInt(idString);
        if(!Validation.isRangeValid(0,RetrieveAndStore.maxID("tblOutgoings","OutgoingID"),id)) return;
        System.out.println("Great, and which field would you like to update?");
        ResultSetMetaData rsmd = rs.getMetaData();
        for (int i = 2; i <= rsmd.getColumnCount(); i++) { // Loop through all columns and print column name
            System.out.println(String.format("%d: %s", i-1, rsmd.getColumnName(i)));
        }
        // They choose one of the columns printed above
        String columnIntString = App.userIn.readLine();
        if (!Validation.isInteger(columnIntString)) return;
        int columnInt = Integer.parseInt(columnIntString)+1;
        if (!Validation.isRangeValid(2, rsmd.getColumnCount(), columnInt)) return;
        String columnName = rsmd.getColumnName(columnInt);
        System.out.println("Please input a new value for " + columnName);
        String newValueString = App.userIn.readLine();
        boolean cont = false;
        switch (columnInt){
            case 2:
                // Name
                while(!cont){
                    if(RetrieveAndStore.exists("tblOutgoings","OutgoingName",newValueString)){
                        System.out.println("An outgoing with that name already exists, try another:");
                        newValueString = App.userIn.readLine();
                    } else {
                        cont = true;
                    }
                }
                RetrieveAndStore.sqlExecute(String.format("UPDATE %s SET %s = '%s' WHERE %s = %d","tblOutgoings",columnName,
                        newValueString, "OutgoingID", id));
                System.out.println("Successfully updated field!");
                break;
            case 3:
                double newAmount;
                while(!cont){
                    if(!(Validation.isDouble(newValueString))){
                        System.out.println("Invalid amount, try another:");
                        newValueString = App.userIn.readLine();
                    } else {
                        newAmount = Double.parseDouble(newValueString);
                        cont = true;
                        RetrieveAndStore.sqlExecute(String.format("UPDATE %s SET %s = %.2f WHERE %s = %d","tblOutgoings",columnName,
                                newAmount, "OutgoingID", id));
                    }
                }
                System.out.println("Successfully updated field!");
                break;
            default:
                System.out.println("Not an option");
                break;
        }

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
        RetrieveAndStore.rowNumberUpdater("tblOutgoings","OutgoingID");
    }
}
