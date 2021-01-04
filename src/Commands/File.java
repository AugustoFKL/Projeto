package Commands;

import java.io.*;
import java.io.IOException;

public class File {

    private final String FILE_NAME = "C:\\Users\\augusto.fotino\\Desktop\\ParametersReading.txt";
    private java.io.File myFile;

    public void fileCreator() {
        setMyFile(new java.io.File(FILE_NAME));
    }

    public java.io.File getMyFile() { return myFile;}

    public void setMyFile(java.io.File myFile) {
        this.myFile = myFile;
    }

    public void write(final String message) throws IOException {
        FileWriter myWriter;
        if (message.equals("")){
            myWriter = new FileWriter(getMyFile().getPath(), false);
            myWriter.write("");
        }else{
            myWriter = new FileWriter(getMyFile().getPath(), true);
            myWriter.write(message + "\r\n");
        }
        myWriter.close();
    }

}
