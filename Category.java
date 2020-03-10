import java.util.Arrays;

/*
* @author Lauren Pococke
* */

public class Category {
    private double budget = 0;
    private double totalAmountSpent;
    private String categoryName;

    /*A new object of this class will be created for each
    new category
    This constructor used for custom categories*/
    public Category(String name, int budgetValue) {
        this.budget = budgetValue;
        this.categoryName = name;
    }

    /*User can set a monthly budget for this specific
    category - this can be changed by the user*/
    public void newBudget(double budget){
        this.budget = budget;
    }

    /*When the user adds a purchase, they select a
    pre-existing category. This value spent is added
    to the total amount spent in this category.*/
    public void addExpenditure(double amountSpent){
        this.totalAmountSpent += amountSpent;
    }

    /*Incase the user writes a spelling mistake initally,
    they can change the category name.*/
    public void changeCategoryName(String newName){
        this.categoryName = newName;
    }

    public String returnName(){
        return this.categoryName;
    }

    /*
    public String validation(String category){
        if(category.equals("")){
            return "Null category";
        }
        else if(Arrays.asList(categoryArray).contains(category)){
            return "Category already exists.";
        }
        else{
            return "Valid";
        }
    }
    */
}
