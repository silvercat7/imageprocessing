import FileIO.PDFHelper;
import core.DImage;
import javax.swing.*;
import java.io.File;
import java.util.Objects;

public class OpticalMarkReaderMain {
    public static void main(String[] args) {
        String pathToPdf = fileChooser();
        System.out.println("loading pdf at " + pathToPdf);
//        parsePDF(pathToPdf);
        DImage image = new DImage(Objects.requireNonNull(PDFHelper.getPageImage(pathToPdf, 1)));
        short[][] page = image.getBWPixelGrid();
//        char[] answers = parseQuestions(crop(page, 120, 270, 1320, 400));
        char [][] answers = parsePage(page);
        for (int col = 0; col < 2; col++) {
            for (int i = 1; i <= 25; i++) {
                char answer = answers[col][i - 1];
                System.out.println("question " + (i + (col * 25)) + ": " + answer);
            }
        }
//        for (int i = 0; i < 25; i++) {
//            char answer = parseAnswer(crop(page, (i * 50) + 100, 100, (i * 50) + 150, 230));
//            System.out.println("question " + (i + 1) + ": " + answer);
//        }
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
    
    public static void parsePDF(String path) {
        for (int i = 1; i <= 6; i++) {
            DImage page = new DImage(Objects.requireNonNull(PDFHelper.getPageImage(path, i)));
            char[][] answers = parsePage(page.getBWPixelGrid());
            int question = 1;
            for (int col = 0; col < answers[0].length; col++) {
                for (int row = 0; row < answers.length; row++) {
                    System.out.println("question " + question + ": " + answers[row][col]);
                    question++;
                }
            }
        }
    }

    public static char[][] parsePage(short[][] page) {
        char[][] answers = new char[2][25];
        answers[0] = parseQuestions(crop(page, 100, 100, 1350, 230));
        answers[1] = parseQuestions(crop(page, 270, 125, 1520, 400));
        return answers;
    }

    public static char[] parseQuestions(short[][] column) {
        char[] answers = new char[25];
        for (int i = 0; i < 25; i++) {
            answers[i] = parseAnswer(crop(column, (i * 50), 0, (i * 50) + 50, column[0].length));
        }
        return answers;
    }

    public static char parseAnswer(short[][] question) {
        int number = -1;
        double closestToWhite = 0;
        double closestToBlack = 255;
        for (int i = 0; i < 5; i++) {
            double total = 0;
            for (int row = 0; row < question.length; row++) {
                for (int col = i * 20; col < (i * 20) + 20; col++) {
                    total += question[row][col];
                }
            }
            double average = total / (20 * question.length);
            if (average > closestToWhite) {
                closestToWhite = average;
            }
            if (average < closestToBlack) {
                number = i;
                closestToBlack = average;
            }
        }
        if (closestToWhite - closestToBlack <= 5) { //this threshold still pops up false positives
            return '0';
        } else {
            return getLetter(number);
        }
    }

    public static char getLetter(int number) {
        if (number == 0) {
            return 'A';
        } else if (number == 1) {
            return 'B';
        } else if (number == 2) {
            return 'C';
        } else if (number == 3) {
            return 'D';
        } else {
            return 'E';
        }
    }

    public static short[][] crop(short[][] original, int startRow, int startCol, int endRow, int endCol) {
        short[][] cropped = new short[endRow - startRow][endCol - startCol];
        for (int row = 0; row < cropped.length && row + startRow < original.length; row++) {
            for (int col = 0; col < cropped[0].length; col++) {
                cropped[row][col] = original[startRow + row][startCol + col];
            }
        }
        return cropped;
    }
}