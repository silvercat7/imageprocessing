import FileIO.PDFHelper;
import core.DImage;
import filters.DisplayInfoFilter;

import javax.swing.*;
import java.io.File;

public class OpticalMarkReaderMain {
    public static void main(String[] args) {
        String pathToPdf = fileChooser();
        System.out.println("loading pdf at " + pathToPdf);
        DImage image = new DImage(PDFHelper.getPageImage(pathToPdf, 1));
        short[][] img = image.getBWPixelGrid();
        short[][] question1 = crop(img, 100, 100, 150, 230);
        System.out.println(parseAnswer(question1));
        /*
        Your code here to...
        (1).  Load the pdf
        (2).  Loop over its pages
        (3).  Create a DImage from each page and process its pixels
        (4).  Output 2 csv files
         */
    }

    private static String fileChooser() {
        String userDirLocation = System.getProperty("user.dir");
        File userDir = new File(userDirLocation);
        JFileChooser fc = new JFileChooser(userDir);
        int returnVal = fc.showOpenDialog(null);
        File file = fc.getSelectedFile();
        return file.getAbsolutePath();
    }

    public void parsePage() {

    }

    public void parseColumn(short[][] page) {
        //25 times
        //start at 100, 100
        //next question is 50 pixels down
        //second column starts at 270, 125
    }

    public static int parseAnswer(short[][] question) {
        int number = -1;
        double closestToBlack = 255;
        for (int i = 0; i < 5; i++) {
            double total = 0;
            for (int col = i * 26; col < (i * 26) + 26; col++) {
                for (int row = 0; row < question.length; row++) {
                    System.out.println("row " + row + ", col " + col);
                    total += question[row][col];
                }
            }
            for (int row = 0; row < question.length; row++) {
                for (int col = i * 26; col < (i * 26) + 26 && col < question[0].length; col++) {
                    System.out.println("row " + row + ", col " + col);
                    total += question[row][col];
                }
            }
            double average = total / 676;
            System.out.println("average: " + average);
            if (average < closestToBlack) {
                number = i;
                closestToBlack = average;
            }
        }
        return number;
    }


    public static short[][] crop(short[][] original, int startRow, int startCol, int endRow, int endCol) {
        short[][] cropped = new short[endRow - startRow][endCol - startCol];
        for (int row = 0; row < cropped.length; row++) {
            for (int col = 0; col < cropped[0].length; col++) {
                cropped[row][col] = original[startRow + row][startCol + col];
            }
        }
        return cropped;
    }
}