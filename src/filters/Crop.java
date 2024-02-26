package filters;

import interfaces.PixelFilter;
import core.DImage;

public class Crop implements PixelFilter {
    public Crop() {
        System.out.println("filter running...");
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] image = crop(img, 0, 0, 800, 550);
        img.setPixels(image);
        return img;
    }

    public short[][] crop(DImage image, int startRow, int startCol, int endRow, int endCol) {
        short[][] original = image.getBWPixelGrid();
        short[][] cropped = new short[endRow - startRow][endCol - startCol];
        for (int row = 0; row < cropped.length; row++) {
            System.arraycopy(original[startRow + row], startCol, cropped[row], 0, cropped[0].length);
        }
        return cropped;
    }
}