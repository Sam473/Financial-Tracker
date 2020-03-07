import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Class which represents a new Entry (a new purchase)
 * @author Lauren and Paul
 */
public class Entry {

    // declare the fields
    private double amount;
    private Category category;
    private ArrayList<Category> existentCategories;
    // in case user inputs an invalid amount
    private static final String INVALID_MESSAGE = "Please enter a positive amount, with maximum 2 decimals.";
    private Date date; // using our Data class
    private BufferedReader userIn;


    /**
     * Constructor if user enters the details of the purchase here and not in the main class
     * @param mainClass link with the main Class so we can get the list of existent categories
     */
    public Entry(App mainClass){
        // check this on Monday when debugging or with mainClass
        existentCategories = mainClass.getCategories();
        try{
            userIn = new BufferedReader(new InputStreamReader(System.in));
            System.out.println(INVALID_MESSAGE);
            String value = userIn.readLine();
            while(validate(value)){
                value = userIn.readLine();
            }
            this.amount = Double.parseDouble(value);
            selectCategory();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Select a category from the list of existent ones
     * @throws IOException
     */
    private void selectCategory() throws IOException{
        int index = 1;
        int number;
        for(Category category: existentCategories){
            System.out.println(String.valueOf(index) + category.returnName());
            index++;
        }
        System.out.println("Please enter the number of the category you wish to choose");
        try{
            number = Integer.parseInt(userIn.readLine());
        }
        catch (NumberFormatException e){
            System.out.println("We cannot find the selected category. The default one" +
                    " is the first one. You can modify this later");
            number = 1;
        }
        number -= 1;
        try{
            this.category = existentCategories.get(number);
        } catch(IndexOutOfBoundsException e){
            System.out.println("We cannot find the selected category. The default one" +
                    " is the first one. You can modify this later");
            this.category = existentCategories.get(0);
        }
    }

    /**
     * Validate a String to make sure it is in double format.
     * @param value String to be validated
     * @return true if String is in double format
     *          false if the String is not in double format
     */
    private boolean validate(String value){
        try {
            double amount = Double.parseDouble(value);
            if (amount <= 0) {
                System.out.println(INVALID_MESSAGE);
                return false;
            }
            return true;
        } catch (NumberFormatException e){
            System.out.println(INVALID_MESSAGE);
            return false;
        }
    }

    /**
     * Constructor which initializes a new purchase if details have been entered in the main class
     * @param amount float representing the amount
     * @param category Category where the user has spent the money
     * @param date date of purchase
     */
    public Entry(float amount, Category category, Date date){
        this.amount = amount;
        this.category = category;
        this.date = date;
        category.addExpenditure(amount);
    }

    /**
     * Return the amount
     * @return amount spent
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Return the category of the purchase
     * @return category of purchase
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Return the date of the purchase
     * @return date of purchase
     */
    public Date getDate() {
        return date;
    }
}
