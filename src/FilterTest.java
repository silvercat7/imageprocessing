import FileIO.PDFHelper;
import filters.DisplayInfoFilter;
import core.DImage;
import core.DisplayWindow;
import processing.core.PImage;

public class FilterTest {
    public static String currentFolder = System.getProperty("user.dir") + "/";

    public static void main(String[] args) {
        runFilter(1);
    }

    private static void runFilter(int page) {
        System.out.println("loading pdf....");
        PImage p = PDFHelper.getPageImage("assets/OfficialOMRSampleDoc.pdf", 1);
        DImage img = new DImage(p);
        System.out.println("running filter on page 1....");
        DisplayInfoFilter filter = new DisplayInfoFilter();
        filter.processImage(img);
        p = img.getPImage();
        p.save(currentFolder + "assets/page" + page + ".png");
        DisplayWindow.showFor("assets/page1.png");
    }

    private static void saveAndDisplay(int page) {
        PImage img = PDFHelper.getPageImage("assets/OfficialOMRSampleDoc.pdf", page);
        img.save(currentFolder + "assets/page" + page + ".png");
        DisplayWindow.showFor("assets/page1.png");
    }
}