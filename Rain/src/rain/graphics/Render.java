/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rain.graphics;

/**
 *
 * @author Deian
 */
public class Render {

    private final int width, height;
    public int[] pixels;

    int xtime = 100;
    int ytime = 50;

    int counter = 0;

    public Render(int width, int height) {
        this.height = height;
        this.width = width;
        pixels = new int[width * height]; // 50,400 ARRAY ELEMENTS
    }

    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
        }
    }

    public void render() {
        counter++;
        if (counter % 10 == 0) {
            xtime++;
        }
        if (counter % 10 == 0) {
            ytime++;
        }

        for (int y = 0; y < height; y++) {
            if (ytime < 0 || ytime >= height) {
                break;
            }
            for (int x = 0; x < width; x++) {
                if (xtime < 0 || xtime >= width) {
                    break;
                }
                pixels[xtime + ytime * width] = 0xff00ff;
            }
        }
    }
}
