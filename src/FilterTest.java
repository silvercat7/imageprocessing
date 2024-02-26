import FileIO.PDFHelper;
import filters.Crop;
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
        PImage p = PDFHelper.getPageImage("assets/OfficialOMRSampleDoc.pdf", 1);
        assert p != null;
        DImage img = new DImage(p);
        System.out.println("running filter on the key...");
        Crop f = new Crop();
        f.processImage(img);
        p = img.getPImage();
        p.save(currentFolder + "assets/key.png");
        DisplayWindow.showFor("assets/key.png");
        for (int page = 2; page <= 6; page++) {
            saveAndDisplay(page);
        }
    }

    private static void saveAndDisplay(int page) {
        PImage pImage = PDFHelper.getPageImage("assets/OfficialOMRSampleDoc.pdf", page);
        assert pImage != null;
        DImage dImage = new DImage(pImage);
        System.out.println("running filter on test " + (page - 1) + "....");
        Crop filter = new Crop();
        filter.processImage(dImage);
        pImage = dImage.getPImage();
        pImage.save(currentFolder + "assets/test" + (page - 1) + ".png");
        DisplayWindow.showFor("assets/test" + (page - 1) + ".png");
    }
}