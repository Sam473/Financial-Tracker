import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class which handles the purchases (the user entries)
 * @author Paul
 */
public class UserEntries {
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
     * @throws IOException 
     */
    private void addPurchase() throws IOException{
    	//Output the categories
    	HandleCategories.outputCategoryNames();
    	System.out.println("Please type a category name for the purchase");
        String category = App.userIn.readLine();
        
        if(HandleCategories.existsCategory(category)){
        	
        	validDate date;
        	date = new validDate();
        	
        	System.out.println("Please type a amount for the purchase");
        	String amountString = App.userIn.readLine();
        	double amount;
            if(!Validation.isDouble(amountString) || (amount = Double.parseDouble(amountString)) <= 0) {
                System.out.println("Invalid amount given\n\n");
                return;
            }

            if (amountString.contains(".")) {
                int decimalPos = amountString.indexOf('.');
                if ((amountString.length() - 1) - decimalPos > 2) {
                    System.out.println("Invalid number, too precise\n\n");
                    return;
                }
            }
            System.out.println("You entered: " + amount);
        	
            System.out.println("Please type a level of guilt for the purchase");
            String guilt = App.userIn.readLine();
        	
        	
            RetrieveAndStore.sqlExecute("INSERT INTO tblPurchases (PurchaseAmount, PurchaseDate, GuiltyLevel, Category) VALUES (" + amount + ", '" + date + "', " + guilt + ",'" + category + "')");
            RetrieveAndStore.sqlExecute("UPDATE tblCategory SET Expenditure = Expenditure +" + amount + " WHERE CategoryName = '" + category + "'");
        } else{
        	System.out.println("This category doesn't exist");
        }
    	
    	//Ask them to select a category
    	//Ask for purchase details
    	//Put into db table for purchases and table for categories
    }

    /**
     * List all purchases
     */
    private void viewPurchases(){
    	ResultSet rs = RetrieveAndStore.readAllRecords("tblPurchases");
		try {
			while (rs.next()) //Loop through the resultset
			{
				int id = rs.getInt("PurchaseID");
				String category = rs.getString("Category");
				String date = rs.getString("PurchaseDate");
				double amount = rs.getDouble("PurchaseAmount");
				int guilt = rs.getInt("GuiltyLevel");
				
				System.out.format("%s. Date = %s, Amount = £%.2f, Guilt Level = %s, Category = %s\n", id, date, amount, guilt, category);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * Remove a certain purchase and the amount from the category
     * Update records for the properties file
     * @throws IOException 
     */
    private void removePurchase() throws IOException{
        viewPurchases();
        System.out.println("Enter a purchase ID for the purchase you want to be deleted");
        ResultSet rs = RetrieveAndStore.readAllRecords("tblPurchases");
        int value = Integer.parseInt(App.userIn.readLine());
		try {
			while (rs.next()) //Loop through the resultset
			{
				if (value == rs.getInt("PurchaseID")) {
					int expenditure = rs.getInt("PurchaseAmount");
					String category = rs.getString("Category");
					RetrieveAndStore.sqlExecute("UPDATE tblCategory SET Expenditure = Expenditure -" + expenditure + " WHERE CategoryName = '" + category + "'");
				}
				RetrieveAndStore.sqlExecute("DELETE FROM tblPurchases WHERE PurchaseID = '" + value + "'");
			}
			System.out.println("Purchase deleted successfully");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //delete purchase from tblCategory, tblPurchases
    }
}
