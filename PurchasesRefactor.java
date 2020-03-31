import java.io.IOException;
import java.util.Scanner;

/**
 * Class which handles the purchases (the user entries)
 * @author Paul
 */
public class PurchasesRefactor {
    /**
     * Prints the option when handling the user's purchases.
     * takes input from main menu and calls correct method
     * @throws IOException 
     */
    public void mainMenu() throws IOException{
        boolean running = true;
        while (running){
            System.out.println("Please select an option by using the character in brackets:\n" +
                    "1. Add new purchase\n" +
                    "2. View all purchases\n" +
                    "3. Remove purchase\n" +
                    "4. Return to main menu");
            String input = App.userIn.readLine();
            
            switch (input) {
            case "1":
                addPurchase();
                System.out.println("Successfully added a new purchase");
                break;
            case "2":
                viewPurchases();
                break;
            case "3":
                System.out.println("Please enter the purchase you would like" +
                        " to remove (exactly as it appears)");
                removePurchase();
                System.out.println("Successfully removed purchase");
                break;
            case "4":
                running = false;
                System.out.println("Exiting to Main Menu...\n\n");
                break;
            default:
                System.out.println("Not an option. Choose again");
        }
        }
    }

    /**
     * Creates a new purchase (user entry) and adds it
     * to the properties file
     * Also, adds the amount to the total expenditure of a category
     */
    private void addPurchase(){
    }

    /**
     * List all purchases
     */
    private void viewPurchases(){
    }

    /**
     * Remove a certain purchase and the amount from the category
     * Update records for the properties file
     * @throws IOException 
     */
    private void removePurchase() throws IOException{
        System.out.println("Current Purchases");
        viewPurchases();
        String value = App.userIn.readLine();
    }
}
