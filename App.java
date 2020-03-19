import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
    BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
    private boolean running;
    
    /**
     * Constructor -- creates preset Categories and adds them
     */
    public App(){
        existentCategories = new ArrayList<>();
        properties = new PropertiesSetup();
        userIncomes = new Incomes(properties);
        userRecOutgoings = new RecurringOutgoings(properties);
        userBudget = new BudgetTime();
        running = true;
		RetrieveAndStore.startDBConnection();

//        existentCategories.add(new Category("Clothes"));
//        existentCategories.add(new Category("Transport"));
//        existentCategories.add(new Category("Groceries"));
    }




    private void mainMenu() {
        while (running) {
            try {
                System.out.println(printMainMenu());
                System.out.println("Going to: ");
                handleMainMenuInput(userIn.readLine());
        } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private String printMainMenu(){
        return "1. Manage purchases\n" +
                "2. Manage categories\n" +
                "3. Manage incomes\n" +
                "4. Manage recurring outgoings\n" +
                "5. Manage budget\n" +
                "6. Exit";
              7. Request data
    }

    private void handleMainMenuInput(String input) {
        switch (input) {
            case "1":
                System.out.println("WIP\n\n");
                break;
            case "2":
                System.out.println("WIP\n\n");
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
            	System.out.println("Thank you for using the financial budget app");
                running = false;
                RetrieveAndStore.closeDBConnection();
                break;
          case "7":
            RequestData data = new RequestData(properties);
                data.saveDataToDesktop();
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
     * Adds a new category
     * @param categoryName new category
     */
    private void addCategory(String categoryName){
        // doesn't check if existent yet
        //existentCategories.add(new Category(categoryName));
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
