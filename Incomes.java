import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Incomes {

    /**
     * Presents users with option and calls correct method based on input
     */
    public void mainMenu(){
    	boolean running = true;
    	while (running) {
    		System.out.println("Please select an option by using the character in brackets:\n" +
    				"1. Add new income\n" +
    				"2. View all incomes\n" +
    				"3. View total monthly income\n" +
                    "4. Edit an income\n" +
    				"5. Remove income\n" +
    				"6. Return to main menu");
    		try {
    			String input = App.userIn.readLine();
                switch (input) {
                    case "1":
                        addIncome();
                        break;
                    case "2":
                        viewIncomes();
                        break;
                    case "3":
                        System.out.println(totalIncome());
                        break;
                    case "4":
                        editIncome();
                        break;
                    case "5":
                        System.out.println("Please enter the income you would like to remove (exactly as it appears)");
                        removeIncome();
                        break;
                    case "6":
                        running = false;
                        System.out.println("Exiting to Main Menu...\n\n");
                        break;
                    default:
                        System.out.println("Not an option. Choose again");

                }
    		} catch(IOException e) {
    			e.printStackTrace();
    			System.out.println("Unable to take input from console");
    		} catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error with DB");
            }
        }
    }


    /**
     * will sum all incomes
     * @return sum of all income streams
     */
    public float totalIncome() { //Loop through and add all incomes then print per month with formatting of string
    	ResultSet rs = RetrieveAndStore.readAllRecords("tblIncomes");
        float incomeTotal = 0;
		try {

			while (rs.next()) //Loop through the resultset to sum total income
			{
				incomeTotal += rs.getFloat("MonthlySalary");
			}


		} catch (SQLException e) {
			e.printStackTrace();
		}
        return incomeTotal;
    }

    private void viewIncomes() {
    	ResultSet rs = RetrieveAndStore.readAllRecords("tblIncomes");
		try {
			while (rs.next()) //Loop through the resultset
			{
				//Store each income record to print
				int id = rs.getInt("IncomeID");
				double monthlyIncome = rs.getDouble("MonthlySalary");
				String name = rs.getString("IncomeName");

				// print the results
                System.out.println(String.format("\n%d: %s\n£%.2f\n", id,
                        name, monthlyIncome));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    /**
     * Takes an input on the frequency of the payment and subsequent inputs to calculate monthly income
     * Will add income stream to the properties file
     * @throws IOException
     *
     */
    private void addIncome() throws IOException, SQLException {
        // Name
        boolean cont = false;
        System.out.println("First, set a name for this category?");
        String name = App.userIn.readLine();
        while(!cont){
            if(RetrieveAndStore.exists("tblIncomes","IncomeName",name)){
                System.out.println("A pool with that name already exists, try another:");
                name = App.userIn.readLine();
            } else {
                cont = true;
            }
        }

        double customPay;
        double monthlySalary = 0;
        boolean validInput = false; //used to verify they choose a frequency option
        System.out.println("How frequently do you get paid?\n" +
                "(H)ourly\n" +
                "(D)aily\n" +
                "(W)eekly\n" +
                "(M)onthly\n" +
                "(Y)early");
        String frequency = App.userIn.readLine(); //take input from console
        while(!validInput) {
            validInput=true;
            switch (frequency) {
                case "h":
                case "H": //if input is hour
                    System.out.println("How many hours a week?");
                    String amountString = App.userIn.readLine();
                    double hoursPerWeek;
                    if(!Validation.isDouble(amountString) || (hoursPerWeek = Double.parseDouble(amountString)) <= 0) {
                        System.out.println("Invalid amount given\n\n");
                        return;
                    }

                    if (amountString.contains(".")) {
                        int decimalPos = amountString.indexOf('.');
                        if ((amountString.length() - 1) - decimalPos > 2) {
                            System.out.println("Invalid number, too precise\n\n");
                            return;
                        }
                    }
                    System.out.println("How much do you get per hour?");
                    customPay = Float.parseFloat(App.userIn.readLine());
                    monthlySalary = ((customPay*hoursPerWeek*52)/12); //work out hours worked per week then convert to monthly
                    break;
                case "d":
                case "D":
                    System.out.println("How much do you get per day?");
                    amountString = App.userIn.readLine();
                    if(!Validation.isDouble(amountString) || (customPay = Double.parseDouble(amountString)) <= 0) {
                        System.out.println("Invalid amount given\n\n");
                        return;
                    }

                    if (amountString.contains(".")) {
                        int decimalPos = amountString.indexOf('.');
                        if ((amountString.length() - 1) - decimalPos > 2) {
                            System.out.println("Invalid number, too precise\n\n");
                            return;
                        }
                    }
                    customPay = Float.parseFloat(App.userIn.readLine());
                    monthlySalary = (customPay*5*52)/12; //5 days per week then same as weekly pay
                    break;
                case "w":
                case "W":
                    System.out.println("How much do you get per week?");
                    amountString = App.userIn.readLine();
                    if(!Validation.isDouble(amountString) || (customPay = Double.parseDouble(amountString)) <= 0) {
                        System.out.println("Invalid amount given\n\n");
                        return;
                    }

                    if (amountString.contains(".")) {
                        int decimalPos = amountString.indexOf('.');
                        if ((amountString.length() - 1) - decimalPos > 2) {
                            System.out.println("Invalid number, too precise\n\n");
                            return;
                        }
                    }
                    monthlySalary = (customPay*52)/12; //52 weeks in a year, but / by 12 for per month salary
                    break;
                case "m":
                case "M":
                    System.out.println("How much do you get per month?");
                    amountString = App.userIn.readLine();
                    if(!Validation.isDouble(amountString) || (monthlySalary = Double.parseDouble(amountString)) <= 0) {
                        System.out.println("Invalid amount given\n\n");
                        return;
                    }

                    if (amountString.contains(".")) {
                        int decimalPos = amountString.indexOf('.');
                        if ((amountString.length() - 1) - decimalPos > 2) {
                            System.out.println("Invalid number, too precise\n\n");
                            return;
                        }
                    }
                    break;
                case "y":
                case "Y":
                    System.out.println("How much do you get per year?");
                    amountString = App.userIn.readLine();
                    if(!Validation.isDouble(amountString) || (customPay = Double.parseDouble(amountString)) <= 0) {
                        System.out.println("Invalid amount given\n\n");
                        return;
                    }

                    if (amountString.contains(".")) {
                        int decimalPos = amountString.indexOf('.');
                        if ((amountString.length() - 1) - decimalPos > 2) {
                            System.out.println("Invalid number, too precise\n\n");
                            return;
                        }
                    }
                    monthlySalary = customPay/12;
                    break;
                default:
                    validInput = false;
                    System.out.println("Not a valid Input, try again:");
                    frequency = App.userIn.readLine();
            }
        }

        //Add the record to the database
		RetrieveAndStore.sqlExecute("INSERT INTO tblIncomes (MonthlySalary) VALUES (" + monthlySalary + ")");
        RetrieveAndStore.rowNumberUpdater("tblIncomes", "IncomeID");
        RetrieveAndStore.rowNumberUpdater("tblIncomes","IncomeID");
    }

    private void editIncome() throws SQLException, IOException {
        ResultSet rs = RetrieveAndStore.readAllRecords("tblIncomes");
        while(rs.next()){
            System.out.println(String.format("\n%d: %s\n£%.2f\n", rs.getInt("IncomeID"),
                    rs.getString("IncomeName"), rs.getDouble("MonthlySalary")));
        }
        System.out.println("Please enter the ID of the income you wish to update");
        String idString = App.userIn.readLine();
        if(!(Validation.isInteger(idString))) return;
        int id = Integer.parseInt(idString);
        if(!Validation.isRangeValid(0,RetrieveAndStore.maxID("tblIncomes","IncomeID"),id)) return;
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
                    if(RetrieveAndStore.exists("tblIncomes","IncomeName",newValueString)){
                        System.out.println("An income with that name already exists, try another:");
                        newValueString = App.userIn.readLine();
                    } else {
                        cont = true;
                    }
                }
                RetrieveAndStore.sqlExecute(String.format("UPDATE %s SET %s = '%s' WHERE %s = %d","tblIncomes",columnName,
                        newValueString, "IncomeID", id));
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
                        RetrieveAndStore.sqlExecute(String.format("UPDATE %s SET %s = %.2f WHERE %s = %d","tblIncomes",columnName,
                                newAmount, "IncomeID", id));
                    }
                }
                System.out.println("Successfully updated field!");
                break;
        }

    }

    /**
     * will remove an income from the properties folder
     * @throws IOException
     */
    private void removeIncome() throws IOException {
    	viewIncomes(); //Print all budgets to the console
		System.out.println("Please enter a record number to delete");
		String input = App.userIn.readLine();
		if (!Validation.isInteger(input)) {
			return;
		}
		if (!Validation.isRangeValid(1, RetrieveAndStore.maxID("tblIncomes", "IncomeID"), Integer.parseInt(input))) {
			return;
		}
		RetrieveAndStore.sqlExecute("DELETE FROM tblIncomes WHERE IncomeID = '" + input + "'"); //Call method to execute deletion
		System.out.format("Record %s deleted successfully\n", input); //Tell the user record has been removed
        RetrieveAndStore.rowNumberUpdater("tblIncomes","IncomeID");
    }
}

/* TODO
    comments
 */
