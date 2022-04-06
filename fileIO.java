import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class fileIO {
    
    public static void newFile(String fileName) {

        try {
            File nFile = new File(fileName);
            if (nFile.createNewFile()) {
                System.out.println("File created was : " + nFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error has occurred");
            e.printStackTrace();
        }
    }   

//  --- CHANGE BELOW ---

    // This method reads text from the file
    public static String readFile(String fileName) {
        String content = "";
        try {
            File nFile = new File(fileName);
            Scanner scanner = new Scanner(nFile);
            // System.out.println("The content of the file is as follows");
            // While end of document has not been reached loop to the end of the document
            while (scanner.hasNextLine()) {
                String fileData = scanner.nextLine();
                content += fileData;
            }
            scanner.close();
        } catch (FileNotFoundException ex) {
            System.out.println("An error has occurred");
            ex.printStackTrace();
        }
        return content;

    }

    
    // This method writes text to a file
    public static void writeFile(String fileName, String Data) {
        try {
            // Remove the true if you want it to put all new information on a new line
            FileWriter myWriter = new FileWriter(fileName, true);
            myWriter.write(Data);
            myWriter.close();
            System.out.println("Successfully saved");
        } catch (IOException ex) {
            System.out.println("An error has occurred");
            ex.printStackTrace();
        }
    }

    // Search file for text 
    public static boolean fileSearch(String fileName, String search){
        String content = readFile(fileName);
        return content.contains("Username: " + search);
    }
    

}
