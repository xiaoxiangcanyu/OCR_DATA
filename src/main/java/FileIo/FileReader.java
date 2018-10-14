package FileIo;

import java.io.*;

public class FileReader {
    public static void main(String[] args) {
        File file =new File("C:\\Users\\songyu\\Desktop\\IO_Test\\1.txt");
        File file1 =new File("C:\\Users\\songyu\\Desktop\\IO_Test\\2.txt");
        try (
                Reader fileReader =new java.io.FileReader(file);
                FileWriter fileWriter =new FileWriter(file1,true);
                )
        {
            char[] buffer =new char[2];
            int len=fileReader.read(buffer);
            while (len!=-1){
                fileWriter.write(buffer,0,len);
                len=fileReader.read(buffer);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        }


}
