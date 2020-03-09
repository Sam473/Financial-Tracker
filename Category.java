import java.util.Arrays;

/*
* @author Lauren Pococke
* */

public class Category {
    private double budget = 0;
    private double totalAmountSpent;
    private String categoryName;

    public Category(String name, int budgetValue) {
        this.budget = budgetValue;
        this.categoryName = name;
    }

    public void newBudget(double budget){
        this.budget = budget;
    }

    public void addExpenditure(double amountSpent){
        this.totalAmountSpent += amountSpent;
    }

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
