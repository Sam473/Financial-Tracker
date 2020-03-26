import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Incomes {
    private boolean running;
    private BufferedReader userIn;

    public Incomes() {
    	userIn = new BufferedReader(new InputStreamReader(System.in));
    }
    
    /**
     * will ask for which option on
     */
    public void mainMenu(){
    	running = true;
    	while (running) {
    		System.out.println("Please select an option by using the character in brackets:\n" +
    				"1. Add new income\n" +
    				"2. View all incomes\n" +
    				"3. View total monthly income\n" +
    				"4. Remove income\n" +
    				"5. Return to main menu");
    		try {
    			String input = userIn.readLine();
    			System.out.println(inputChecker(input));
    		} catch(IOException e) {
    			e.printStackTrace(); 
    			System.out.println("Unable to take input from console");
    		}
    	}
    }

    /**
     * takes input from main menu and calls correct method
     * @param input their choice from main menu options
     * @return Success/failure message
     * @throws IOException
     */
    private String inputChecker(String input) throws IOException {
    		switch (input) {
    		case "1":
    			addIncome();
    			return "";
    		case "2":
    			viewIncomes();
    			return "";
    		case "3":
    			totalIncome();
    			return "";
    		case "4":
    			System.out.println("Please enter the income you would like to remove (exactly as it appears)");
    			removeIncome();
    			return "";
    		case "5":
    			running = false;
    			return "Exiting to Main Menu...\n\n";
    		default:
    			return "Not an option. Choose again";

    		}
    }

    /**
     * will sum all incomes
     * @return sum of all income streams
     */
    private void totalIncome() { //Loop through and add all incomes then print per month with formatting of string
    	ResultSet rs = RetrieveAndStore.readAllRecords("tblIncomes");
		try {
			float incomeTotal = 0;
			
			while (rs.next()) //Loop through the resultset to sum total income
			{
				incomeTotal += rs.getFloat("MonthlySalary");
			}
			
			// print the total income
			System.out.format("Total monthly income = %.2f\n", incomeTotal);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void viewIncomes() {
    	ResultSet rs = RetrieveAndStore.readAllRecords("tblIncomes");
		try {
			while (rs.next()) //Loop through the resultset
			{
				//Store each income record to print
				int id = rs.getInt("IncomeID");
				int monthlyIncome = rs.getInt("MonthlySalary");

				// print the results
				System.out.format("%s. Monthly Salary = £%s\n", id, monthlyIncome);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * Takes an input on the frequency of the payment and subsequent inputs to calculate monthly income
     * Will add income stream to the properties file
     * @throws IOException 
     *
     */
    private void addIncome() throws IOException {
        float customPay;
        float monthlySalary = 0;
        boolean validInput = false; //used to verify they choose a frequency option
        System.out.println("How frequently do you get paid?\n" +
                "(H)ourly\n" +
                "(D)aily\n" +
                "(W)eekly\n" +
                "(M)onthly\n" +
                "(Y)early");
        String frequency = userIn.readLine(); //take input from console
        while(!validInput) {
            validInput=true;
            switch (frequency) {
                case "h":
                case "H": //if input is hour
                    System.out.println("How many hours a week?");
                    int hoursPerWeek = Integer.parseInt(userIn.readLine());
                    System.out.println("How much do you get per hour?");
                    customPay = Float.parseFloat(userIn.readLine());
                    monthlySalary = (customPay*hoursPerWeek*52)/12; //work out hours worked per week then convert to monthly
                    break;
                case "d":
                case "D":
                    System.out.println("How much do you get per day?");
                    customPay = Float.parseFloat(userIn.readLine());
                    monthlySalary = (customPay*5*52)/12; //5 days per week then same as weekly pay
                    break;
                case "w":
                case "W":
                    System.out.println("How much do you get per week?");
                    customPay = Float.parseFloat(userIn.readLine());
                    monthlySalary = (customPay*52)/12; //52 weeks in a year, but / by 12 for per month salary
                    break;
                case "m":
                case "M":
                    System.out.println("How much do you get per month?");
                    monthlySalary = Float.parseFloat(userIn.readLine()); //leave it as it is
                    break;
                case "y":
                case "Y":
                    System.out.println("How much do you get per year?");
                    customPay = Float.parseFloat(userIn.readLine());
                    monthlySalary = customPay/12;
                    break;
                default:
                    validInput = false;
                    System.out.println("Not a valid Input, try again:");
                    frequency = userIn.readLine();
            }
        }
        
        //Add the record to the database
		RetrieveAndStore.sqlExecute("INSERT INTO tblIncomes (MonthlySalary) VALUES (" + monthlySalary + ")");
    }

    /**
     * will remove an income from the properties folder
     * @throws IOException
     */
    private void removeIncome() throws IOException {
    	viewIncomes(); //Print all budgets to the console
		System.out.println("Please enter a record number to delete");
		String input = userIn.readLine();
		if (!Validation.isInteger(input)) {
			return;
		}
		if (!Validation.isRangeValid(1, RetrieveAndStore.maxID("tblIncomes", "IncomeID"), Integer.parseInt(input))) {
			return;
		}
		RetrieveAndStore.sqlExecute("DELETE FROM tblIncomes WHERE IncomeID = '" + input + "'"); //Call method to execute deletion
		System.out.format("Record %s deleted successfully\n", input); //Tell the user record has been removed		
    }
}
