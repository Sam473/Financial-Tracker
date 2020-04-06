import java.io.IOException;
import java.nio.file.*;

public class RequestData {
	String desktop = System.getProperty("user.home") + "/Desktop";


    public void saveDataToDesktop() throws IOException {
        //copy the file to a location decided by the user
    	Path source = Paths.get(System.getProperty("user.dir") + "/ProgramDB.mdb");
    	System.out.println("Please enter a valid directory for example: C:\\Users\\Public");
    	String targetInput = App.userIn.readLine();
        Path target = Paths.get(targetInput + "/ProgramDB.mdb");
        try {
            Files.copy(source, target);
            System.out.println("File password is: gfg64%43df3*f\"A");
        } catch (IOException e1) {
            System.out.println("Please enter a valid directory to save file to");
        }
    }
}
