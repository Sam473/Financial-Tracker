import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Main class of our software
 */
public class App {

    // declare fields
    ArrayList<Category>existentCategories;
    BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Constructor -- creates preset Categories and adds them
     */
    public App(){
        existentCategories = new ArrayList<>();
        existentCategories.add(new Category("Clothes"));
        existentCategories.add(new Category("Transport"));
        existentCategories.add(new Category("Groceries"));
    }

    /**
     * Return the list of existent categories
     * @return existent categories
     */
    public ArrayList<Category> getExistentCategories(){
        return existentCategories;
    }

    /**
     * Adds a new category
     * @param categoryName new category
     */
    private void addCategory(String categoryName){
        // doesn't check if existent yet
        existentCategories.add(new Category(categoryName));
    }

    /**
     * Main method
     * @param args cmd line args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        App myApp = new App();
        while(true){
            System.out.println("Press 1 to enter a new Entry or 0 to exit");
            int value = Integer.parseInt(myApp.userIn.readLine());
            if(value == 0){
                System.exit(0);
            }
            if(value == 1){
                new Entry(myApp);
            }
        }
    }

}
