import java.io.IOException;

public class HandleCategoryRefactor {


    /**
     * Prints the option when handling the user's purchases.
     * @throws IOException 
     */
    public void mainMenu() throws IOException{
    	boolean running = true;
        while (running){
            System.out.println("Please select an option by using the character in brackets:\n" +
                    "1. Add new category\n" +
                    "2. View all categories\n" +
                    "4. Edit details of a category\n" +
                    "5. Remove category\n" +
                    "6. Return to main menu");
            String input = App.userIn.readLine();
            switch (input) {
            case "1":
                addCategory();
                break;
            case "2":
                viewCategories();
                break;
            case "4":
                System.out.println("Please enter the category you would like" +
                        " to edit the details for (exactly as it appears)");
                editDetails();
                break;
            case "5":
                System.out.println("Please enter the category you would like" +
                        " to remove (exactly as it appears)");
                removeCategory();
                break;
            case "6":
                running = false;
                break;
            default:
            	System.out.println("Not an option. Choose again");
            	break;
            }
        }
    }

    /**
     * Creates a new category and adds it
     * to the properties file and to the existent list of categories
     * Note: each new category is automatically created with budget 0 and expenditure 0
     * @throws IOException 
     */
    private void addCategory() throws IOException {
        System.out.println("Enter the name of the category");
        String category = App.userIn.readLine();
    }

    /**
     * Checks to see if the category already exists or not
     * @param name name of the category
     * @return true if the category exists
     *         false if not
     */
    private boolean existsCategory(String name){
    	return false;
    }

    /**
     * List all categories
     */
    private void viewCategories(){
    	
    }

    /**
     * Edit the details of a category (name or budget)
     * @throws IOException 
     * @throws NumberFormatException 
     */
    private void editDetails() throws NumberFormatException, IOException{
    }


    /**
     * Remove a certain category from both the properties file and from the array list
     * Update records for all purchases in that category and move them to the Unknown category
     * @throws IOException 
     */
    private void removeCategory() throws IOException{
        System.out.println("Current categories");
        viewCategories();
    }
}