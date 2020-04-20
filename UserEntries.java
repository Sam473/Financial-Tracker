import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

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
					"4. View sorted purchases\n" +
					"5. Return to main menu");
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
				constructArray();
				break;
			case "5":
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
		int guilt = 0;
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

            date.newDate();

			System.out.println("Please type a level of guilt for the purchase between 1 and 10");
			String inp = App.userIn.readLine();
			if (Validation.isInteger(inp)) {
				guilt = Integer.parseInt(inp);
				if (!Validation.isRangeValid(1, 10, guilt)) {
					return;
				}
			}

			RetrieveAndStore.sqlExecute("INSERT INTO tblPurchases (PurchaseID, PurchaseAmount, PurchaseDate, GuiltyLevel, " +
					"Category) VALUES ("+ (RetrieveAndStore.maxID("tblPurchases", "PurchaseID") + 1) + ", " +
					amount + ", '" + date.getDate() + "', " + guilt + ",'" + category + "')");
            RetrieveAndStore.sqlExecute("UPDATE tblCategory SET Expenditure = Expenditure +" + amount + " WHERE CategoryName = '" + category + "'");
            RetrieveAndStore.rowNumberUpdater("tblPurchases","PurchaseID");
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
	 * Method iterates through the result set and looks for the purchases
	 * which have the month identical with the current one and year.
	 * Computes all the expenditure, divides it by the number of days (latest purchase) and then
	 * multiplies by 30 (approximation) to estimate the spending for this month.
	 * NOTE: does not take into account regular outgoings or incomes
	 */
	public void estimateSpending(){
		ResultSet rs = RetrieveAndStore.readAllRecords("tblPurchases");
		try {
			// get details regarding current month
			Date today = new Date();
			String thisMonth = processMonth(today.toString().split(" ")[1]);
			String year = today.toString().split(" ")[5];
			double totalAmount = 0;
			int maxDay = Integer.parseInt(today.toString().split(" ")[2]);
			while(rs.next()){
				// check that the purchase is for the current month
				String date = rs.getString("PurchaseDate");
				if(date.split("/")[1].equals(thisMonth) && date.split("/")[2].equals(year)){
					double amount = rs.getDouble("PurchaseAmount");
					String day = date.split("/")[0];
					totalAmount += amount;
					// update the latest day of purchase
					if(Integer.parseInt(day) > maxDay) maxDay = Integer.parseInt(day);
				}
			}
			System.out.format("Estimated is: %.2f\n", totalAmount / maxDay * 30);
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * Method is the same as the one in validDate class
	 * @param month String representing month to be processed
	 * @return number of the month as a String
	 */
	private static String processMonth(String month){
		switch (month){
		case "Jan": return "01";
		case "Feb": return "02";
		case "Mar": return "03";
		case "Apr": return "04";
		case "May": return "05";
		case "Jun": return "06";
		case "Jul": return "07";
		case "Aug": return "08";
		case "Sep": return "09";
		case "Oct": return "10";
		case "Nov": return "11";
		case "Dec": return "12";
		default:
			return "";
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
			RetrieveAndStore.rowNumberUpdater("tblPurchases","PurchaseID");
			System.out.println("Purchase deleted successfully");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//delete purchase from tblCategory, tblPurchases
	}

	public void constructArray() throws IOException {
		String[][] records = new String[RetrieveAndStore.maxID("tblPurchases", "PurchaseID")][5];
		ResultSet rs = RetrieveAndStore.readAllRecords("tblPurchases");
		int i = 0;
		try {
			while (rs.next()) //Loop through the resultset and build an array of records
			{
				records[i][0] = Integer.toString(rs.getInt("PurchaseID"));
				records[i][1] = Float.toString(rs.getFloat("PurchaseAmount"));
				records[i][2] = rs.getString("PurchaseDate");
				records[i][3] = Integer.toString(rs.getInt("GuiltyLevel"));
				records[i][4] = rs.getString("Category");

				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//User decides what to sort by
		int sortBy = 1;
    	System.out.println("What would you like to sort by:\n" +
                "1. Level of guilt\n" +
                "2. Cost of Purchase\n" +
                "3. Date Added\n");
        String input = App.userIn.readLine();

        switch (input) {
        case "1":
            sortBy = 3;
            break;
        case "2":
        	sortBy = 1;
            break;
        case "3":
            return;
        default:
            System.out.println("Not an option. Choose again");
            return;
    }
		//Sort the data
		QuickSort.sort(records, 0, i-1, sortBy);
		//Output formatted clearly
		for (int j=0; j<records.length; ++j) {
			String id = records[j][0];
			String amount = records[j][1];
			String date = records[j][2];
			String guilt = records[j][3];
			String category = records[j][4];
			System.out.format("%s. Amount = £%s, Date = %s, Guilt Level = %s, Category = %s\n", id, amount, date, guilt, category);
		}
	}
}
