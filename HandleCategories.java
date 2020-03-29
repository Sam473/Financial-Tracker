import java.io.IOException;

/**
 * Class which handles the category management
 * @author Paul
 */
public class HandleCategories {
    private final PropertiesSetup properties;
    private boolean running;
    private final App mainClass;

    /**
     * Constructor
     * @param properties PropertiesSetup object --> properties file
     * @param mainClass Main class so we get a link to all the existent categories
     */
    public HandleCategories(PropertiesSetup properties, App mainClass) {
        this.properties = properties;
        this.mainClass = mainClass;
    }

    /**
     * Prints the option when handling the user's purchases.
     * @throws IOException 
     */
    public void mainMenu() throws IOException{
        running = true;
        while (running){
            System.out.println("Please select an option by using the character in brackets:\n" +
                    "1. Add new category\n" +
                    "2. View all categories\n" +
                    "3. See details of a category\n" +
                    "4. Edit details of a category\n" +
                    "5. Remove category\n" +
                    "6. Return to main menu");
            String input = App.userIn.readLine();
            System.out.println(inputChecker(input));
        }
    }

    /**
     * takes input from main menu and calls correct method
     * @param input their choice from main menu options
     * @return Success/failure message
     * @throws IOException 
     */
    private String inputChecker(String input) throws IOException{
        switch (input) {
            case "1":
                addCategory();
                return "\n";
            case "2":
                viewCategories();
                return "";
            case "3":
                System.out.println("Please enter the category you would like" +
                        " to see the details for (exactly as it appears)");
                seeDetails();
                return "";
            case "4":
                System.out.println("Please enter the category you would like" +
                        " to edit the details for (exactly as it appears)");
                editDetails();
                return "";
            case "5":
                System.out.println("Please enter the category you would like" +
                        " to remove (exactly as it appears)");
                removeCategory();
                return "";
            case "6":
                running = false;
                return "Exiting to Main Menu...\n\n";
            default:
                return "Not an option. Choose again";
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
        if(!existsCategory(category)){
            mainClass.existentCategories.add(new Category(category, 0));
            // format accordingly for properties file
            category += "-0.0-0.0";
            String value = (properties.getProperty("categories").equals("")) ? category : "," + category;
            properties.setProperty("categories", properties.getProperty("categories") + value);
            System.out.print("Successfully added the category");
        }
        else{
            System.out.println("This category already exists.");
        }
    }

    /**
     * Checks to see if the category already exists or not
     * @param name name of the category
     * @return true if the category exists
     *         false if not
     */
    private boolean existsCategory(String name){
        for(Category category: mainClass.existentCategories){
            if(name.equals(category.returnName())){
                return true;
            }
        }
        return false;
    }

    /**
     * List all categories
     */
    private void viewCategories(){
        String[] categories = properties.getProperty("categories").split(",");
        for (String category : categories) {
            // Since in the properties file first item is "0" you don't want to print that
            if(!category.equals("0")) System.out.println(category.split("-")[0]); // print just the name
        }
    }

    /**
     * Edit the details of a category (name or budget)
     * @throws IOException 
     * @throws NumberFormatException 
     */
    private void editDetails() throws NumberFormatException, IOException{
        System.out.println("Current categories");
        viewCategories();
        String value = App.userIn.readLine();
        // cannot edit "Unknown"
        if(value.equals("Unknown")){
            System.out.println("There are no details for this category");
            return;
        }
        // find the category object in the array list
        Category categoryToEdit = null;
        for(Category category: mainClass.existentCategories){
            if(value.equals(category.returnName())){
                categoryToEdit = category;
                break;
            }
        }
        // Check that it was found
        if(categoryToEdit != null){
            System.out.println("Input 1 to edit the name\n" + "Input 2 to edit the budget\n");
            int option = Integer.parseInt(App.userIn.readLine());
            switch (option){
                case 1:
                    System.out.println("Enter the new name of the category: ");
                    String newName = App.userIn.readLine();
                    String oldName = categoryToEdit.returnName();
                    if(existsCategory(newName)){
                        System.out.println("A category with this name already exists so we can't edit the name.");
                        return;
                    }
                    // update records for both purchases and categories in the properties file
                    categoryToEdit.changeCategoryName(newName);
                    properties.setProperty("categories", properties.
                            getProperty("categories").replaceAll(oldName, newName));
                    properties.setProperty("purchases", properties.getProperty("purchases").
                            replaceAll(oldName, newName));
                    return;
                case 2:
                    System.out.println("Enter the new budget: ");
                    double newBudget = Float.parseFloat(App.userIn.readLine());
                    double oldBudget = categoryToEdit.getBudget();
                    categoryToEdit.newBudget(newBudget);
                    // update records for the budget
                    System.out.println(categoryToEdit.returnName() + "-" +  oldBudget);
                    properties.setProperty("categories", properties.getProperty("categories").
                            replaceAll(categoryToEdit.returnName() + "-" + oldBudget,
                                    categoryToEdit.returnName() + "-" + newBudget));
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
        else{
            System.out.println("Couldn't find the category");
        }
    }

    /**
     * See details of a category
     * @throws IOException 
     */
    private void seeDetails() throws IOException{
        System.out.println("Current categories");
        viewCategories();
        String value = App.userIn.readLine();
        // Unknown has no details
        if(value.equals("Unknown")){
            System.out.println("There are no details about this category");
            return;
        }
        // find the category object in the array list and print the details
        for(Category category: mainClass.existentCategories){
            if(value.equals(category.returnName())){
                System.out.println("Budget set for " + value + ": " + category.getBudget());
                System.out.println("Total expenditure for " + value + ": " + category.getTotalAmountSpent());
                return;
            }
        }
    }

    /**
     * Remove a certain category from both the properties file and from the array list
     * Update records for all purchases in that category and move them to the Unknown category
     * @throws IOException 
     */
    private void removeCategory() throws IOException{
        System.out.println("Current categories");
        viewCategories();
        String value = App.userIn.readLine();
        // The preset categories cannot be removed
        if(value.equals("Unknown") || value.equals("Clothes") ||
                value.equals("Transport") || value.equals("Groceries")){
            System.out.println("You cannot remove this category");
            return;
        }
        // find the category object in the array list and remove it as well
        Category categoryToRemove = null;
        for(Category category: mainClass.existentCategories){
            if(value.equals(category.returnName())){
                categoryToRemove = category;
                break;
            }
        }
        // update the records
        if(categoryToRemove != null){
            properties.setProperty("purchases", properties.getProperty("purchases").
                    replaceAll(value, "Unknown"));
            properties.setProperty("categories",properties.getProperty("categories").
                replaceAll(","+value + "-" + categoryToRemove.getBudget() + "-"
                        + categoryToRemove.getTotalAmountSpent(),""));
            mainClass.existentCategories.remove(categoryToRemove);
        }
        else System.out.println("Category not found");
    }

}
