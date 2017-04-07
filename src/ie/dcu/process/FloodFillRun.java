package ie.dcu.process;

import java.io.IOException;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import ie.dcu.ui.ImageConstants;

public class FloodFillRun {

  public FloodFillRun(int imageNumber) throws IOException {
    BufferedImage image = ImageIO.read(new File( ImageConstants.FLOODFILL_IP_DIR +  imageNumber + ".png"));
    		
    new FloodFill().floodFill(image, new Point(0, 0), Color.WHITE, Color.BLACK);
    new FloodFill().floodFill(image, new Point(500, 0), Color.WHITE, Color.BLACK);
    new FloodFill().floodFill(image, new Point(0, 500), Color.WHITE, Color.BLACK);
    new FloodFill().floodFill(image, new Point(500, 500), Color.WHITE, Color.BLACK);
    ImageIO.write(image, "png", new File(ImageConstants.FLOODFILL_OUTPUT_DIR +  imageNumber + ".png"));
  }
 
  public static void main(String[] args) throws IOException {
    for (int i = 0; i < 361; i++) {
    	new FloodFillRun(i);
	}
  }
}