import FileIO.PDFHelper;
import core.DImage;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class OpticalMarkReaderMain {
    public static void main(String[] args) {
        String pathToPdf = fileChooser();
        System.out.println("loading pdf at " + pathToPdf);
        parsePDF(pathToPdf);
        /*
        to do:
        output csv files
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
            System.out.println("page " + i + ":");
            DImage page = new DImage(Objects.requireNonNull(PDFHelper.getPageImage(path, i)));
            ArrayList<ArrayList<Character>> answers = parsePage(page.getBWPixelGrid());
            int question = 1;
            for (int col = 0; col < answers.size(); col++) {
                for (int row = 0; row < answers.get(col).size(); row++) {
                    System.out.println("question " + question + ": " + answers.get(col).get(row));
                    question++;
                }
            }
            System.out.println("-------------------------");
        }
    }

    public static ArrayList<ArrayList<Character>> parsePage(short[][] page) {
        ArrayList<ArrayList<Character>> answers = new ArrayList<>();
        answers.add(parseQuestions(crop(page, 100, 100, 1350, 230)));
        answers.add(parseQuestions(crop(page, 120, 270, 1320, 400)));
        return answers;
    }

    public static ArrayList<Character> parseQuestions(short[][] column) {
        ArrayList<Character> answers = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            answers.add(parseAnswer(crop(column, (i * 50), 0, (i * 50) + 50, column[0].length)));
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
                for (int col = i * 25; col < (i * 25) + 25; col++) {
                    total += question[row][col];
                }
            }
            double average = total / (25 * question.length);
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
                cropped[row][col] = original[row + startRow][col + startCol];
            }
        }
        return cropped;
    }
}