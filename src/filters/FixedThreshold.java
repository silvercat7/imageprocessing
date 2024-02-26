package filters;

import interfaces.PixelFilter;
import core.DImage;

public class FixedThreshold implements PixelFilter {
    private final int threshold;

    public FixedThreshold() {
        threshold = 127;
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (grid[r][c] > threshold) {
                    grid[r][c] = 255;
                } else {
                    grid[r][c] = 0;
                }
            }
        }
        img.setPixels(grid);
        return img;
    }
}