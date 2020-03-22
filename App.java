import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class of our software
 */
public class App {

    // declare fields
    ArrayList<Category>existentCategories;
    Incomes userIncomes;
    RecurringOutgoings userRecOutgoings;
    PropertiesSetup properties;
    BudgetTime userBudget;
    Scanner userIn = new Scanner(System.in);
    userEntries userEntries;
    HandleCategories userCategories;
    private boolean running;
    
    /**
     * Constructor -- creates preset Categories and adds them
     *             -- creates the objects ofr handling each menu
     */
    public App(){
        existentCategories = new ArrayList<>();
        properties = new PropertiesSetup();
        userIncomes = new Incomes(properties);
        userRecOutgoings = new RecurringOutgoings(properties);
        userBudget = new BudgetTime();
        userEntries = new userEntries(properties, this);
        userCategories = new HandleCategories(properties, this);
        running = true;
		RetrieveAndStore.startDBConnection();
		initializeCategories();
    }

    /**
     * initialize the categories and add them to the array list
     * Because in the properties file each category has the format:
     * name-budget-expenditure we need to update the array list every time the code
     * is run from scratch so we don't lose data
     * @author Paul (if you have questions)
     */
    private void initializeCategories(){
        String[] categories = properties.getProperty("categories").split(",");
        for (String category : categories) {
            if(!category.equals("0")){
                String[] elements = category.split("-");
                existentCategories.add(new Category(elements[0], Double.parseDouble(elements[1])));
                existentCategories.get(existentCategories.size()-1).addExpenditure(Double.parseDouble(elements[2]));
            }
        }
    }

    /**
     * Main menu of the main class
     * Display until user exits the program
     */
    private void mainMenu() {
        while (running) {
            System.out.println(printMainMenu());
            System.out.println("Going to: ");
            handleMainMenuInput(userIn.nextLine());
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
              	"6. Request data\n" +
                "7. Exit";
    }

    /**
     * Accept input from the user and handle the option accordingly
     * @param input option entered by the user
     */
    private void handleMainMenuInput(String input){
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
            	RequestData data = new RequestData(properties);
                data.saveDataToDesktop();
                break;
            case "7":
            	System.out.println("Thank you for using the financial budget app");
                running = false;
                RetrieveAndStore.closeDBConnection();
                break;
            default:
                System.out.println("Not an option, try again");
        }
    }

    /**
     * Return the list of existent categories
     * @return existent categories
     */
    public ArrayList<Category> getExistentCategories(){
        return existentCategories;
    }

    /**
     * Main method
     * @param args cmd line args
     */
    public static void main(String[] args) {
        App myApp = new App();
        myApp.mainMenu();
    }

}
