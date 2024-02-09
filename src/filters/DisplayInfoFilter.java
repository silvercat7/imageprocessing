package filters;

import interfaces.PixelFilter;
import core.DImage;

public class DisplayInfoFilter implements PixelFilter {
    public DisplayInfoFilter() {
        System.out.println("filter running...");
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] image = crop(img, 1000, 0, 1500, 550);
        int blackCount = 0;
        int whiteCount = 0;
        for (int row = 0; row < image.length; row++) {
            for (int col = 0; col < image[0].length; col++) {
                if (image[row][col] < 10) blackCount++;
                if (image[row][col] > 240) whiteCount++;
            }
        }
        System.out.println(blackCount + " nearly black pixels and " + whiteCount + " nearly white pixels");
        img.setPixels(image);
        return img;
    }

    public short[][] crop(DImage image, int startRow, int startCol, int endRow, int endCol) {
        short[][] original = image.getBWPixelGrid();
        short[][] cropped = new short[endRow - startRow][endCol - startCol];
        for (int row = 0; row < cropped.length; row++) {
            for (int col = 0; col < cropped[0].length; col++) {
                cropped[row][col] = original[startRow + row][startCol + col];
            }
        }
        return cropped;
    }
}