package ie.dcu.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CreateCircle {
	public CreateCircle() {
		for (int i = 10; i <= 300; i+=10) {
        	createCircles(i, i/10);
		}
        for (int i = 290, j=0; i >= 10; i-=10, j++) {
        	createCircles(i, 30 + j);
		}
	}
	private static void createCircles(int radius, int index) {
		BufferedImage bufferedImage = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		Color c = Color.WHITE;
		g2d.setColor(c);
		g2d.drawArc(400-radius, 400-radius, 2*radius, 2*radius, 0, 360);
		g2d.dispose();
		
		try {
			ImageIO.write(bufferedImage, "png", new File("dummyCircleData// " + index + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
