package ie.dcu.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import javax.imageio.ImageIO;

import com.google.common.io.Files;

import ie.dcu.process.MCPolygons;

public class CreateCircle {
	public CreateCircle() {
		for (int i = 1; i <= 101; i++) {
        	createCircles(i, i);
		}
        for (int i = 100, j=0; i > 0; i--, j++) {
        	createCircles(i, 101 + j);
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
			ImageIO.write(bufferedImage, "png", new File(ImageConstants.FF_DATA_FOLDER + "// " + index + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MCPolygons marchingCube= new MCPolygons();
		String currentDir = System.getProperty("user.dir");
		File fileSelections = new File(currentDir + "\\" + ImageConstants.FF_DATA_FOLDER);
		if(!fileSelections.exists()) {
			new CreateCircle();
		}
			
		marchingCube.initiateMCProcess(fileSelections.listFiles(), currentDir, false);
	}

}
