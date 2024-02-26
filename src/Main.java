import FileIO.PDFHelper;
import core.DImage;
import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;

public class Main {
    private static final ArrayList<ArrayList<ArrayList<Character>>> answers = parsePDF(fileChooser());
    private static final ArrayList<ArrayList<Character>> key = answers.get(0);

    public static void main(String[] args) {
        outputResults();
        outputAnalysis();
    }

    private static String fileChooser() {
        String userDirLocation = System.getProperty("user.dir");
        File userDir = new File(userDirLocation);
        JFileChooser fc = new JFileChooser(userDir);
        fc.showOpenDialog(null);
        File file = fc.getSelectedFile();
        return file.getAbsolutePath();
    }

    public static void outputResults() {
        try {
            PrintWriter results = new PrintWriter(new FileWriter("results.txt"));
            for (int i = 1; i < 6; i++) {
                results.println(createLine(i, scoreTest(answers.get(i), key)));
            }
            results.close();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    public static void outputAnalysis() {
        try {
            PrintWriter analysis = new PrintWriter(new FileWriter("analysis.txt"));
            for (int i = 1; i <= scoreTest(answers.get(0), key).size(); i++) {
                analysis.print("question " + i + ": ");
                int incorrect = 0;
                for (int j = 1; j < answers.size(); j++) {
                    if (!scoreTest(answers.get(j), key).get(i - 1)) {
                        incorrect++;
                    }
                }
                if (incorrect == 1) {
                    analysis.println(incorrect + " incorrect answer");
                } else {
                    analysis.println(incorrect + " incorrect answers");
                }
            }
            analysis.close();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
    
    public static ArrayList<ArrayList<ArrayList<Character>>> parsePDF(String path) {
        ArrayList<ArrayList<ArrayList<Character>>> answers = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            DImage page = new DImage(Objects.requireNonNull(PDFHelper.getPageImage(path, i)));
            answers.add(parsePage(page.getBWPixelGrid()));
        }
        return answers;
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
        double secondClosestToBlack = 255;
        for (int i = 0; i < 5; i++) {
            double average = getAverage(question, i);
            if (average > closestToWhite) {
                closestToWhite = average;
            }
            if (average < closestToBlack) {
                number = i;
                closestToBlack = average;
            } else if (average < secondClosestToBlack) {
                secondClosestToBlack = average;
            }
        }
        if (closestToWhite - closestToBlack <= 5 || secondClosestToBlack - closestToBlack <= 5) {
            return '0';
        } else {
            return getLetter(number);
        }
    }

    public static double getAverage(short[][] question, int option) {
        double total = 0;
        for (short[] row : question) {
            for (int col = option * 25; col < (option * 25) + 25; col++) {
                total += row[col];
            }
        }
        return total / (25 * question.length);
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

    public static ArrayList<Boolean> scoreTest(ArrayList<ArrayList<Character>> answers, ArrayList<ArrayList<Character>> key) {
        ArrayList<Boolean> results = new ArrayList<>();
        for (int col = 0; col < answers.size(); col++) {
            for (int row = 0; row < answers.get(col).size(); row++) {
                if (answers.get(col).get(row) == key.get(col).get(row)) {
                    if (key.get(col).get(row) == '0') {
                        break;
                    }
                }
                results.add(answers.get(col).get(row) == key.get(col).get(row));
            }
        }
        return results;
    }

    public static String createLine(int page, ArrayList<Boolean> score) {
        StringBuilder line = new StringBuilder("test " + page + ": ");
        int totalCorrect = 0;
        for (Boolean correct : score) {
            if (correct) {
                totalCorrect++;
            }
        }
        line.append(totalCorrect).append(" correct\n        incorrect questions: ");
        for (int question = 1; question <= score.size(); question++) {
            if (!score.get(question - 1)) {
                line.append(question).append(", ");
            }
        }
        if (line.toString().endsWith(", ")) {
            line = new StringBuilder(line.substring(0, line.length() - 2));
        } else {
            line.append("none");
        }
        return line.toString();
    }

    public static short[][] crop(short[][] original, int startRow, int startCol, int endRow, int endCol) {
        short[][] cropped = new short[endRow - startRow][endCol - startCol];
        for (int row = 0; row < cropped.length && row + startRow < original.length; row++) {
            System.arraycopy(original[row + startRow], startCol, cropped[row], 0, cropped[0].length);
        }
        return cropped;
    }
}