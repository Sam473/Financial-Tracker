import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class which handles the category management
 * @author Paul
 */
public class HandleCategories {
    /**
     * Prints the option when handling the user's purchases.
     * @throws IOException 
     */
    public void mainMenu() throws IOException{
    	boolean running = true;
        while (running){
            System.out.println("Please select an option by using the number associated with each command:\n" +
                    "1. Add new category\n" +
                    "2. View all categories\n" +
                    "3. Edit name of a category\n" +
                    "4. Remove category\n" +
                    "5. Return to main menu");
            String input = App.userIn.readLine();
            switch (input) {
            case "1":
                addCategory();
                break;
            case "2":
                viewCategories();
                break;
            case "3":
                System.out.println("Please enter the category you would like" +
                        " to edit the name for (exactly as it appears)");
                editDetails();
                break;
            case "4":
                System.out.println("Please enter the category you would like" +
                        " to remove (exactly as it appears)");
                removeCategory();
                break;
            case "5":
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
        if (category.equals("")) {
            System.out.println("Empty name...\n\n");
            return;
        }

        if(!existsCategory(category)){
            RetrieveAndStore.sqlExecute("INSERT INTO tblCategory (CategoryID, CategoryName, Expenditure) VALUES" +
                    " (" + (RetrieveAndStore.maxID("tblCategory", "CategoryID") + 1)  + ", '" + category + "', 0 )");
            System.out.print("Successfully added the category");
        } else{
        	System.out.println("This category already exists.");
        }
        RetrieveAndStore.rowNumberUpdater("tblCategory","CategoryID");
    }

    /**
     * Checks to see if the category already exists or not
     * @param name name of the category
     * @return true if the category exists
     *         false if not
     */
    public static boolean existsCategory(String name){
    	ResultSet rs = RetrieveAndStore.readAllRecords("tblCategory");
		try {
			while (rs.next()) //Loop through the resultset
			{
				if (name.equals(rs.getString("CategoryName"))) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return false;
    }

    /**
     * List all categories
     */
    private void viewCategories(){
    	ResultSet rs = RetrieveAndStore.readAllRecords("tblCategory");
		try {
			while (rs.next()) //Loop through the resultset
			{
				int id = rs.getInt("CategoryID");
				String name = rs.getString("CategoryName");
				int expenditure = rs.getInt("Expenditure");
				
				System.out.format("%s. Category Name = %s, Expenditure = £%s\n", id, name, expenditure);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void outputCategoryNames() {
    	ResultSet rs = RetrieveAndStore.readAllRecords("tblCategory");
		try {
			while (rs.next()) //Loop through the result set
			{
				System.out.println(rs.getString("CategoryName"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * Edit the details of a category (name or budget)
     * @throws IOException 
     * @throws NumberFormatException 
     */
    private void editDetails() throws NumberFormatException, IOException{
        viewCategories();
    	System.out.println("Please enter a category number to amend");
        String value = App.userIn.readLine();
        if (!Validation.isInteger(value)) { //Record number input validated
			return;
		}
		int recordNumber = Integer.parseInt(value); 
		if (!Validation.isRangeValid(1, RetrieveAndStore.maxID("tblCategory", "CategoryID"), recordNumber)) {
			return;
		}

		// Modified method to change name only (no need for budgets here anymore)

        System.out.println("Enter the new name of the category: ");
        String newName = App.userIn.readLine();

        if(existsCategory(newName)){
            System.out.println("A category with this name already exists so we can't edit the name.");
            return;
        }
        RetrieveAndStore.sqlExecute("UPDATE tblCategory SET CategoryName = '" + newName + "' WHERE CategoryID = " + recordNumber);
    }

    /**
     * Remove a certain category from both the properties file and from the array list
     * Update records for all purchases in that category and move them to the Unknown category
     * @throws IOException 
     */
    private void removeCategory() throws IOException{
        viewCategories();
        String value = App.userIn.readLine();
        System.out.println("Please enter a category to be deleted");
        if(value.equals("Unknown") || value.equals("Clothes") ||
                value.equals("Transport") || value.equals("Groceries")){
            System.out.println("You cannot remove this category");
            return;
        }
        ResultSet rs = RetrieveAndStore.readAllRecords("tblCategory");
		try {
			while (rs.next()) //Loop through the resultset
			{
				String name = rs.getString("CategoryName");
				int expenditure = rs.getInt("Expenditure");
				
				if (name.equals(value)) { //check this works
					RetrieveAndStore.sqlExecute("UPDATE tblCategory SET Expenditure = Expenditure + " + expenditure + " WHERE CategoryName = 'Unknown'");
					RetrieveAndStore.sqlExecute("DELETE FROM tblCategory WHERE CategoryName = '" + value + "'");
                    RetrieveAndStore.rowNumberUpdater("tblCategory","CategoryID");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
}