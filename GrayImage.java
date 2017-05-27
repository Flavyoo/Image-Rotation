/*
 ****************************************************************************
 * Flavio Andrade
 * 4-25-2017
 * Compilation:  javac GrayImage.java
 * Execution:    java GrayImage Images/Ferrari.jpeg speed

 *****************************************************************************
 */

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;
import java.io.IOException;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JFrame;

public class GrayImage {
	public BufferedImage image = null;
	public int height, width;
	public String picName;
	public int i = 0;
	public ImageIcon icon;
	public JLabel label;
	public JFrame frame;

    // Make a new GrayImage object.
	public GrayImage(String name) {
        picName = name;
		if (name == null) throw new IllegalArgumentException("Enter a valid file.");
		// Read in the Image
		try { image = ImageIO.read(new File(name));}
		catch (IOException e) {}
		height = image.getHeight();
		width = image.getWidth();
	}

	// Return original image if it has not been modified yet.
	public BufferedImage returnOriginal() {
		return image;
	}

	// Take the image and make it GrayScale.
	public BufferedImage makeGray(int start, int fin) {
		 for (int c = 0; c < width; c++) {
			 for (int h = start; h < fin; h++) {
				int color = image.getRGB(c, h);
				image.setRGB(c, h, this.makeColorGray(new Color(color)).getRGB());
			}
		}
		start = fin;
		return image;
	 }

	 // Take a Color object and make it gray.
	 public Color makeColorGray(Color color) {
		 if (color == null) throw new IllegalArgumentException("Provide a valid color.");
		 int red = color.getRed();
		 int blue = color.getBlue();
		 int green = color.getGreen();
		 double grayValue = (0.299 * red) + (0.587 * blue) + (0.110 * green);
		 int g = (int) Math.round(grayValue);
		 return new Color(g, g, g);
	 }

	 // Set up the display;
	 public void setUp(BufferedImage img, int x, int y, String name) {
	    icon = new ImageIcon(image);
 		label = new JLabel();
 		label.setHorizontalAlignment(JLabel.CENTER);
 		label.setIcon(icon);
 		frame = new JFrame(name);
 		frame.setSize(x, y);
 		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		frame.getContentPane().add(label, BorderLayout.CENTER);
 		frame.setVisible(true);
	 }

	 // Display the image.
	 public void drawImage(BufferedImage img, int x, int y) {
		 this.setUp(img, x, y, picName);
	 }


	// Show the image on the screen after it has been turned to grayscale.
	public void showImage() {
		this.drawImage(this.makeGray(0, height - 1), width, height);
	}


	// Turn the image gray overtime.
	public void animateGray(int h, int x, int y) {
		if (h > height / 100) { h = height / 100; }
		this.setUp(image, x, y, "Animate Gray");
		/* Keep track of previous location, so loop does not start at the
		   begining of image. */
		int i = 0;
		int a = 0;
		int stop = height / h;
		while (true) {
			a += h;
			if (a > h * stop) {
			    this.makeGray(stop * h, height - 1);
				break;
			}
			this.makeGray(i, a);
			i = a;
			icon = new ImageIcon(image);
			label.setIcon(icon);
			frame.getContentPane().add(label, BorderLayout.CENTER);
			frame.setVisible(true);
			frame.repaint();
		}
	}

	// Save the new Image.
	public void save(BufferedImage img, String name) {
		File newImage = new File(name + ".jpg");
		try { ImageIO.write(img, "jpg", newImage); }
		catch(IOException e) {}


	}

	public static void main(String args[]) {
		String name = args[0];
		int h = Integer.parseInt(args[1]);
		GrayImage gr = new GrayImage(name);
		gr.animateGray(h, gr.width, gr.height);
		gr.save(gr.image, "Modified");
	}
}
