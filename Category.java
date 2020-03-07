import java.nio.channels.ScatteringByteChannel;
import java.util.Arrays;

/*
* @author Lauren Pococke
* */

public class Category {
    private String[] categoryArray = new String[100];

    public void Category() {
        String[] categoryArray = {"Groceries", "Eating out", "Transport", "Entertainment", "Shopping"};
    }

    public void main(String category){
        String check = this.validation(category);
        if(check.equals("Valid")){
            this.addNewCategory(category);
        }
        else{
            System.out.println(check);
        }
    }

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

    public void addNewCategory(String category) {
        for (int i = 0; i < 100; i++) {
            if (categoryArray[i] != null) {
                categoryArray[i] = category;
                break;
            }
        }
    }

    public void changeCategoryName(String category, String newName){
        for(int i = 0; i < 100; i++){
            if(categoryArray[i].equals(category)){
                categoryArray[i] = newName;
            }
        }
    }

    public String[] returnName(){
        return categoryArray;
    }
}
