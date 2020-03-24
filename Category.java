/**
 * Class for a category
 * @author Lauren and Paul
 */
public class Category {

    // declare fields
    private double budget;
    private double totalAmountSpent;
    private String categoryName;

    /**
     * Constructor which creates a new category with a set budget value
     * @param name name of the category
     * @param budgetValue value of budget
     */
    public Category(String name, double budgetValue) {
        this.budget = budgetValue;
        this.categoryName = name;
    }

    /**
     * Change the value of the budget
     * @param budget new value of the budget
     */
    public void newBudget(double budget){
        this.budget = budget;
    }

    /**
     * Add the amount of a new purchase in the category to the total amount spent
     * @param amountSpent amount of the purchase
     */
    public void addExpenditure(double amountSpent){
        this.totalAmountSpent += amountSpent;
    }

    /**
     * Upon deleting a purchase, remove the expenditure from that category
     * @param amountSpent amount of the purchase to be removed
     */
    public void removeExpenditure(double amountSpent) {
        this.totalAmountSpent -= amountSpent;
    }

    /**
     * Return the budget set for a category
     * @return double budget
     */
    public double getBudget(){
        return budget;
    }

    /**
     * Return the total expenditure
     * @return double totalAmountSpent
     */
    public double getTotalAmountSpent(){
        return totalAmountSpent;
    }

    /**
     * Change the category name if the user entered a wrong name
     * @param newName new category name
     */
    public void changeCategoryName(String newName){
        this.categoryName = newName;
    }

    /**
     * Return the name of the category
     * @return name of category
     */
    public String returnName(){
        return this.categoryName;
    }
}
