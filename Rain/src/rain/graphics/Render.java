/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rain.graphics;

import java.util.Random;

/**
 *
 * @author Deian
 */
public class Render {

    private final int width, height;
    
    public final int MAP_SIZE = 8;
    public final int MASK_SIZE = MAP_SIZE - 1;
    
    public int[] pixels;
    public int[] tiles = new int[MAP_SIZE * MAP_SIZE];

    private Random random = new Random();

    public Render(int width, int height) {
        this.height = height;
        this.width = width;
        pixels = new int[width * height]; // 50,400 ARRAY ELEMENTS

        for (int i = 0; i < MAP_SIZE * MAP_SIZE; i++) {
            tiles[i] = random.nextInt(0xffffff);
        }

        tiles[0] = 0x000000;
    }

    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
        }
    }

    public void render(int xOffset, int yOffset) {
        for (int y = 0; y < height; y++) {
            int yy = y + yOffset;
            for (int x = 0; x < width; x++) {
                int xx = x + xOffset;
                int tileIndex = ((xx >> 4) & MASK_SIZE) + ((yy >> 4) & MASK_SIZE) * MAP_SIZE;
                pixels[x + y * width] = tiles[tileIndex];
            }
        }
    }
}
