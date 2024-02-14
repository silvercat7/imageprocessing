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
        for (int page = 2; page <= 6; page++) {
            PImage p = PDFHelper.getPageImage("assets/OfficialOMRSampleDoc.pdf", page);
            assert p != null;
            DImage img = new DImage(p);
            System.out.println("running filter on page " + (page - 1) + "....");
            DisplayInfoFilter filter = new DisplayInfoFilter();
            filter.processImage(img);
            p = img.getPImage();
            p.save(currentFolder + "assets/test" + (page - 1) + ".png");
//            DisplayWindow.showFor("assets/page" + (page - 1) + ".png");
        }
    }
}