import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Main class of our software
 */
public class App {

    // declare fields
    Incomes userIncomes;
    RecurringOutgoings userRecOutgoings;
    BudgetTime userBudget;
    UserEntries userEntries;
    HandleCategories userCategories;
    Savings savings;
    Tips tips;
    GenerateAnalysis analysis;
    private boolean running;
    private boolean givenTip = false;
    public static BufferedReader userIn; //Buffered reader declared to take user input within the project

    /**
     * Constructor -- creates preset Categories and adds them
     *             -- creates the objects for handling each menu
     */
    public App(){
        userIncomes = new Incomes();
        userRecOutgoings = new RecurringOutgoings();
        userBudget = new BudgetTime();
        userEntries = new UserEntries();
        userCategories = new HandleCategories();
        analysis = new GenerateAnalysis();
        running = true;
		RetrieveAndStore.startDBConnection();
		savings = new Savings();
		userIn = new BufferedReader(new InputStreamReader(System.in));
		tips = new Tips();
    }

    /**
     * Main menu of the main class
     * Display until user exits the program
     */
    private void mainMenu() {
        while (running) {
            System.out.println("Please choose an option from the following:");
            System.out.println(printMainMenu());
            if(!givenTip) {
                giveTip();
                givenTip = true;
            }
            try {
				handleMainMenuInput(userIn.readLine());
			} catch (IOException e) { //Catches exceptions with reading console inputs
				e.printStackTrace();
			}
        }
    }

    /**
     * @return the main menu as a String
     */
    private String printMainMenu(){
        return "1. Manage purchases\n" +
                "2. Manage categories\n" +
                "3. Manage incomes\n" +
                "4. Manage recurring outgoings\n" +
                "5. Manage budget\n" +
                "6. Manage Savings Pools\n" +
                "7. View your disposable income\n" +
                "8. See your progress with graphs\n" +
              	"9. Request data\n" +
                "10. Get a tip or some motivation\n" +
                "11. Exit";
    }

    /**
     * Accept input from the user and handle the option accordingly
     * @param input option entered by the user
     * @throws IOException
     */
    private void handleMainMenuInput(String input) throws IOException {
        System.out.print("\nGoing to: ");
        switch (input) {
            case "1":
                System.out.println("Purchases\n\n");
                userEntries.mainMenu();
                break;
            case "2":
                System.out.println("Categories\n\n");
                userCategories.mainMenu();
                break;
            case "3":
                System.out.println("Incomes\n\n");
                userIncomes.mainMenu();
                break;
            case "4":
                System.out.println("Recurring Outgoings\n\n");
                userRecOutgoings.mainMenu();
                break;
            case "5":
                System.out.println("Budgets");
                userBudget.mainMenu();
                break;
            case "6":
                System.out.println("Savings Pools");
                savings.mainMenu();
                break;
            case "7":
            	  System.out.println("Request Data\n\n");
                System.out.println("\rYour disposable income:\n£" + (userIncomes.totalIncome() - userRecOutgoings.totalOutgoings()) + "\n\n");
                break;
            case "8":
                System.out.println("Analysis\n\n");
                analysis.mainMenu();
                break;
            case "9":
                System.out.println("Request Data\n\n");
                RequestData data = new RequestData();
                data.saveData();
                break;
            case "10":
                System.out.print("\r");
                giveTip();
                System.out.println("\n");
                break;
            case "11":
            	System.out.println("\rThank you for using the financial budget app");
                running = false;
                RetrieveAndStore.closeDBConnection();
                System.exit(0); //required to close all Jframes
                break;
            default:
                System.out.println("\rNot an option, try again");
        }
    }

    /**
     * Will give user a random tip or motivational quote
     */
    private void giveTip(){
        try {
            String tipMotivation =  (new Random().nextInt(2) == 0) ? // choose random number out of 1 or 2
                    "TIP: " + tips.getTipMotivation("tip") : tips.getTipMotivation("motivation");
            System.out.println(tipMotivation);
        } catch (FileNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method - verifies user login then redirects to app
     * @param args cmd line args
     */
    public static void main(String[] args) {
    	Login login = new Login();
    	App myApp = new App();
    	
    	try {
			if (login.mainMenu()) { //if they login they are allowed access to app
			    myApp.mainMenu();
			}
		} catch (IOException e) { //Catches exception with reading login details
			e.printStackTrace();
		}
    }

}
