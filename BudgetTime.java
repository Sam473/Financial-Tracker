import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class to take Budget input and store it
 * @author Sam
 */

public class BudgetTime {

	//Buffered reader declared to take user input within this class
	private BufferedReader userIn;

	 //constructor used to initialise reading from client
	public BudgetTime() {
		userIn = new BufferedReader(new InputStreamReader(System.in));
	}

	/**
	 * Set recurring budget over a time period
	 * @throws IOException
	 * 		Handled in main along with other IO exceptions to reduce error handling code
	 */
	private void setBudgetForTimePeriod() throws IOException {
		int numberOfDays = 	timeInput(); //Gets time input from user and converts to days
		if (numberOfDays == 0) {
			return; //Quit if time input failed
		}
		int budgetAmount = 0; //Variable to hold the budget before it is stored
		String input;

		System.out.println("Please enter a budget for the selected time period");
		input = userIn.readLine();
		if (!Validation.isInteger(input)) {
			return;
		}
		budgetAmount = Integer.parseInt(input);
		
		RetrieveAndStore.startDBConnection();
		RetrieveAndStore.writeToFile("INSERT INTO tblBudget (BudgetAmount, NumberOfDays) VALUES (" + budgetAmount + ", " + numberOfDays + ")");
		//Write SQL statement here then pass to method

		System.out.println("Success a budget has been set for £" + budgetAmount + " every " + numberOfDays + " days!");
	}
	
	private void deleteBudget() {
		//To delete just print the whole DB and ask them which one to delete
		//For amend choose the record in the same way
	}

	/**
	 * Take input of time period and convert to days
	 * @return time period for goal in days
	 * @throws IOException
	 * 		Handled in main along with other IO exceptions to reduce error handling code
	 */
	private int timeInput() throws IOException {
		int timePeriod = 0;
		int lengthTime = 0;
		String input;

		System.out.println("Would you like to input your budget in: \n 1.Days \n 2.Calendar Months \n 3.Years");
		input = userIn.readLine();
		if (!Validation.isInteger(input)) {
			return 0;
		}	
		timePeriod = Integer.parseInt(input);
		if (!Validation.isRangeValid(1 , 3, timePeriod)) {
			return 0;
		}

		System.out.println("Please enter the number of days/months/years you would like this goal for");
		input = userIn.readLine();
		if (!Validation.isInteger(input)) {
			return 0;
		}	
		lengthTime = Integer.parseInt(input);

		//if statement to normalise input to number of days ready to be stored
		if (timePeriod == 1) {
			return lengthTime;
		} else if (timePeriod == 2) {
			lengthTime *= 28;
			return lengthTime;
		} else if (timePeriod == 3) {
			lengthTime *= 365;
			return lengthTime;
		}
		return 0;
	}
	
		public void chooseOperation() {
			//Choose to delete amend or add records 
			//Catch all issues by surrounfing whole select statement in a catch
			//This is what is called from the main
		}

	public static void main(String[] args) {
		try {
			new BudgetTime().setBudgetForTimePeriod();
		}  catch (IOException e) {
			e.printStackTrace();
			System.out.println("Unable to take input from console");
		}
	}
}
