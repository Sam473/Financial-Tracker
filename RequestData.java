import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class RequestData {
	String desktop = System.getProperty("user.home") + "/Desktop";


    public void saveDataToDesktop() {
        try {
            PrintWriter printWriter = new PrintWriter(desktop + "/Your-Info-From-Budget-Tracker.txt", "UTF-8");
            String[][] requirementsList = properties.getFileRequirements();
            System.out.println(requirementsList.length);
            for (int i = 0; i < requirementsList.length; ++i){
                printWriter.write(requirementsList[i][0] + ": " + properties.getProperty(requirementsList[i][0]) + "\n");
            }
            printWriter.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
