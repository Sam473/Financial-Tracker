import java.util.ArrayList;
import java.util.Scanner;

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
    private validDate date; // using our Data class
    private Scanner userIn;
    private int guiltyLevel;


    /**
     * Constructor which creates a new purchase
     * @param mainClass link with the main Class so we can get the list of existent categories
     */
    public Entry(App mainClass){
        existentCategories = mainClass.getExistentCategories();
        selectAmount();
        selectCategory();
        selectDate();
        selectGuiltyLevel();
    }

    /**
     * Select a category from the list of existent ones
     */
    public void selectCategory(){
        int index = 1;
        int number;
        for(Category category: existentCategories){
            System.out.println((index) + " " + category.returnName());
            index++;
        }
        System.out.println("Please enter the number of the category you wish to choose");
        try{
            number = Integer.parseInt(userIn.nextLine());
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
     * Make the user enter the amount of the purchase. Give indications
     * that is has to be a positive amount.
     */
    public void selectAmount(){
        userIn = new Scanner(System.in);
        System.out.println(INVALID_MESSAGE);
        String value = userIn.nextLine();
        while(!validate(value)){
            value = userIn.nextLine();
        }
        this.amount = Double.parseDouble(value);
        System.out.println("Successful");
    }

    /**
     * Select a date when the purchase was made
     */
    public void selectDate(){

        this.date = new validDate();
    }

    /**
     * Lets the user input a guilty level based on his purchase:
     */
    public void selectGuiltyLevel(){
        System.out.println("Finally, please enter how guilty you feel about this purchase, on a scale from 1 to 10:");
        this.guiltyLevel = userIn.nextInt();
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
    public String getDate() {
        return date.getDate();
    }

    /**
     * Return the guilt value of the purchase
     * @return String in format Guilt level: value
     */
    public String getGuilt(){
        return "Guilt level-" + (guiltyLevel);
    }
}
