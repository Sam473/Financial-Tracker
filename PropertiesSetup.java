import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesSetup {

    // for the categories the format is : name-budget-expenditure
    private String[][] fileRequirements = {{"userName","defaultUser"},{"incomes",""},
            {"purchases", ""},{"categories", "0,Unknown-0.0-0.0,Clothes-0.0-0.0" +
            ",Groceries-0.0-0.0,Transport-0.0-0.0",},{"outgoings",""}};
    File file = new File("user_data.properties");
    Properties properties = new Properties();

    public PropertiesSetup() {
        openNewFile();
    }

    /**
     * used to load a file into properties object
     *
     */
    private void openNewFile() {
        try {
            if (file.createNewFile()) {
                System.out.println("Created new file");
            } else {
                System.out.println("File already exists...");
            }
            properties.load(new FileInputStream(file)); //load the user_data.properties
            checkFileFormat();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * will check all the required keys are in properties file. If not, will create it
     *
     */
    private void checkFileFormat() {
        for (String[] key : fileRequirements){
            if (properties.getProperty(key[0]) == null) {
                properties.setProperty(key[0], key[1]);
            }
        }
        saveProperties();
    }


    /**
     * saves changes to the file so other applications can see it
     *
     */
    private void saveProperties() {
        try {
            FileOutputStream fr = new FileOutputStream(file);
            properties.store(fr, "Properties");
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String input) {
        return properties.getProperty(input);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        saveProperties();
    }

    public String[][] getFileRequirements(){
        return fileRequirements;
    }
}
