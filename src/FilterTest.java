import FileIO.PDFHelper;
import filters.DisplayInfoFilter;
import core.DImage;
import core.DisplayWindow;
import processing.core.PImage;

public class FilterTest {
    public static String currentFolder = System.getProperty("user.dir") + "/";

    public static void main(String[] args) {
        runFilter();
    }

    private static void runFilter() {
        System.out.println("loading pdf....");
        for (int page = 1; page <= 6; page++) {
            PImage p = PDFHelper.getPageImage("assets/OfficialOMRSampleDoc.pdf", page);
            assert p != null;
            DImage img = new DImage(p);
            System.out.println("running filter on page " + page + "....");
            DisplayInfoFilter filter = new DisplayInfoFilter();
            filter.processImage(img);
            p = img.getPImage();
            p.save(currentFolder + "assets/page" + page + ".png");
            DisplayWindow.showFor("assets/page" + page + ".png");
        }
    }

    private static void saveAndDisplay(int page) {
        PImage img = PDFHelper.getPageImage("assets/OfficialOMRSampleDoc.pdf", page);
        assert img != null;
        img.save(currentFolder + "assets/page" + page + ".png");
        DisplayWindow.showFor("assets/page1.png");
    }
}