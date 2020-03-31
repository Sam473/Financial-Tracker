import java.util.Scanner;

/**
 * Class which handles the purchases (the user entries)
 * @author Paul
 */
public class UserEntries {
    private boolean running;


    /**
     * Prints the option when handling the user's purchases.
     */
    public void mainMenu(){
        running = true;
        Scanner scanner = new Scanner(System.in);
        while (running){
            System.out.println("Please select an option by using the character in brackets:\n" +
                    "1. Add new purchase\n" +
                    "2. View all purchases\n" +
                    "3. Remove purchase\n" +
                    "4. Return to main menu");
            String input = scanner.nextLine();
            System.out.println(inputChecker(input));
        }
    }

    /**
     * takes input from main menu and calls correct method
     * @param input their choice from main menu options
     * @return Success/failure message
     */
    private String inputChecker(String input){
        switch (input) {
            case "1":
                addPurchase();
                return "Successfully added new purchase\n";
            case "2":
                viewPurchases();
                return "";
            case "3":
                System.out.println("Please enter the purchase you would like" +
                        " to remove (exactly as it appears)");
                removePurchase();
                return "Successfully removed purchase";
            case "4":
                running = false;
                return "Exiting to Main Menu...\n\n";
            default:
                return "Not an option. Choose again";

        }
    }

    /**
     * Creates a new purchase (user entry) and adds it
     * to the properties file
     * Also, adds the amount to the total expenditure of a category
     */
    private void addPurchase(){
        // add purchase to file
        Entry newPurchase = new Entry(mainClass);
        String purchase = newPurchase.getAmount() + " " + newPurchase.getCategory().returnName() +
                 " " +newPurchase.getDate() + " " + newPurchase.getGuilt();
        String value = (properties.getProperty("purchases").equals("")) ? purchase : "," + purchase;
        properties.setProperty("purchases", properties.getProperty("purchases") + value);
        // add expenditure to the category
        // make use of format name-budget-expenditure
        Category category = newPurchase.getCategory();
        System.out.println(category.returnName() + "-" + category.getBudget() +
                "-" + (newPurchase.getAmount() + category.getTotalAmountSpent()));
        properties.setProperty("categories", properties.getProperty("categories").
                replaceAll(category.returnName() + "-" + category.getBudget() + "-" +
                        category.getTotalAmountSpent(), category.returnName() + "-" + category.getBudget() +
                "-" + (newPurchase.getAmount() + category.getTotalAmountSpent())));
        newPurchase.getCategory().addExpenditure(newPurchase.getAmount());
    }

    /**
     * List all purchases
     */
    private void viewPurchases(){
        String[] purchases = properties.getProperty("purchases").split(",");
        for (String purchase : purchases) {
            // don't print the default 0
            if(!purchase.equals("0")) System.out.println(purchase);
        }
    }

    /**
     * Remove a certain purchase and the amount from the category
     * Update records for the properties file
     */
    private void removePurchase(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Current Purchases");
        viewPurchases();
        String value = scanner.nextLine();
        // find the name of the category and remove the expenditure from it
        String categoryName = value.split(" ")[1];
        // amount of purchase
        double amount = Double.parseDouble(value.split(" ")[0]);
        // find category
        for(Category category: mainClass.existentCategories){
            if(category.returnName().equals(categoryName)){
                // remove expenditure
                properties.setProperty("categories", properties.getProperty("categories").
                        replaceAll(category.returnName() + "-" + category.getBudget() +
                                "-" + category.getTotalAmountSpent(), category.returnName() + "-" +
                                category.getBudget() + "-" + (category.getTotalAmountSpent() - amount) ));
                // update the object as well
                category.removeExpenditure(amount);
            }
        }
        // delete from file
        properties.setProperty("purchases",properties.getProperty("purchases").
                replaceAll(","+value,""));
    }
}
