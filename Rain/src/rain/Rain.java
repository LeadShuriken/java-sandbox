/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rain;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import rain.graphics.Render;

/**
 * @author Deian
 */
public class Rain extends Canvas implements Runnable {

    public static int width = 300;
    public static int height = width / 16 * 9;
    public static int scale = 3;
    public static String title = "RAIN";
    
    private int xOffset = 0;
    private int yOffset = 0;
    
    private Thread thread;
    private JFrame frame;
    private boolean running = false;
    private Render screen;

    private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    public Rain() {
        Dimension size = new Dimension(width * scale, height * scale);
        setPreferredSize(size);

        screen = new Render(width, height);
        frame = new JFrame();
    }

    public void update() {
        xOffset++;
        yOffset++;
    }

    public synchronized void start() {
        running = true;
        thread = new Thread(this, "Display");
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 60.0;
        double delta = 0;
        int frames = 0;
        int updates = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                update();
                updates++;
                delta--;
            }
            render();
            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frame.setTitle(title + " | " + updates + "ups, " + frames + " fps");
                updates = 0;
                frames = 0;
            }
        }
        stop();
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();

        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.clear();
        screen.render(xOffset, yOffset);

        System.arraycopy(screen.pixels, 0, pixels, 0, pixels.length);
        Graphics g = bs.getDrawGraphics(); 

        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        Rain rainGame = new Rain();

        rainGame.frame.setResizable(false);
        rainGame.frame.setTitle(rainGame.title);
        rainGame.frame.add(rainGame);
        rainGame.frame.pack();
        rainGame.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rainGame.frame.setLocationRelativeTo(null);
        rainGame.frame.setVisible(true);

        rainGame.start();
    }
}
