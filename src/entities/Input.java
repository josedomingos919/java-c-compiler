package entities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileInputStream;

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
            File file = new File(this.fileName);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];

            fis.read(data);
            fis.close();

            content = new String(data, "UTF-8");

        } catch (Exception s) {
            return content;
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
