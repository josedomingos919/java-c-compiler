package entities;

import java.io.File;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Input {
    private String fileName = "";
    private String fileContent = "";

    public Input(String fileName) {
        this.fileName = fileName;
        this.fileContent = this.reader();
    }

    public String getFileContent() {
        return this.fileContent;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String reader() {
        String content = "";

        try {
            File myObj = new File(this.fileName);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                content += myReader.nextLine();
            }

            myReader.close();

            return content;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return content;
    }

    public void write(String content) {
        try {
            FileWriter myWriter = new FileWriter(this.fileName);
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writting in file.");
            e.printStackTrace();
        }
    }
}
