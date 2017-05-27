/*
 ****************************************************************************
 * Flavio Andrade
 * 4-25-2017
 * Compilation:  javac Rotate.java
 * Execution:    java Rotate Images/Ferrari.jpeg degrees


 GrayImage.java has to be compiled before Rotate.java can work.
 To display a rotated image without animation call rotateIt(degrees) and then showImage(image).
 To rotate the image forever, call rotate().

 Use a positive degree to rotate counter-clockwise or a negative degree to rotate clockwise.
 *****************************************************************************
 */
import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import java.lang.Math;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class Rotate {
	public BufferedImage image, newImage;
	public GrayImage gray;
	public String name;
	public int width, height, diagonal;

	public Rotate (String filename) {
		name = filename;
		// Gray Image object
		gray = new GrayImage(name);

		int gW = gray.image.getWidth();
		int gH = gray.image.getHeight();
		// Scale the image if it is too big.
		if (gW > 400 || gH > 400) {
			image = this.scale(gray.image, 0.5, 0.5);
		}
		else {
			// The original image
			image = gray.returnOriginal();
		}

		width = image.getWidth();
		height = image.getHeight();
		// Height and width
		this.diagonal();
	}


	// Compute length of the diagonal.
	private void diagonal() {
		diagonal = (int) Math.sqrt(width * width + height * height);
	}

	//Scale the image for faster animation
	public BufferedImage scale(BufferedImage img, double xscale, double yscale) {
		int w = (int) (img.getWidth() * xscale);
		int h = (int) (img.getHeight() * yscale);
		BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		AffineTransform af = new AffineTransform();
		af.scale(xscale, yscale);
		AffineTransformOp scale = new AffineTransformOp(af, AffineTransformOp.TYPE_BILINEAR);
		result = scale.filter(img, result);
		return result;
	}

	// Take the image and rotate it n degrees.
	private int[] newCoordinate(double degrees, int c, int h)   {
		double radians = Math.toRadians(degrees);
		// Rotational matrix operation on the coordinate (c, h);
		int x = (int) Math.round(Math.cos(radians) * c - Math.sin(radians) * h);
		int y = (int) Math.round(Math.sin(radians) * c + Math.cos(radians) * h);
		int[] coordinates = { x, y };
		return coordinates;
	}

	// Take the image and rotate it.
	public void rotateIt(double degrees) {
		newImage = new BufferedImage(diagonal + 10, diagonal + 10, BufferedImage.TYPE_INT_RGB);
		int x = 0;
		int y = 0;
		for (int h = 0; h < height; h++) {
			for (int c = 0; c < width; c++) {
				// Shift x and y to the middle of the rotating image.
				x = c - width / 2;
				y = h - height / 2;
				int[] a = this.newCoordinate(degrees, y, x);
				int color = image.getRGB(c, h);
				newImage.setRGB(a[1] + (diagonal + 10) / 2, a[0] + (diagonal + 10) / 2, color);
			}
		}
	}

	// show the new rotated image.
	public void showImage(BufferedImage img) {
		ImageIcon icon = new ImageIcon(img);
 		JLabel label = new JLabel();
 		label.setHorizontalAlignment(JLabel.CENTER);
 		label.setIcon(icon);
 		JFrame frame = new JFrame(name);
 		frame.setSize(diagonal, diagonal);
 		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		frame.getContentPane().add(label, BorderLayout.CENTER);
 		frame.setVisible(true);
	}

	// Save the file.
	public void save(String name) {
		gray.save(this.newImage, name);
	}

	// Animation to rotate the image.
	public void rotate(double d) {
		//BufferedImage img = this.rotateIt(0, this.image);
		gray.setUp(image, diagonal + 10, diagonal + 10, "Rotation");
		int r = 0;
		// Slow and not smooth with larger images.
		while (true) {
			this.rotateIt(r += d);
			gray.icon = new ImageIcon(this.newImage);
			gray.label.setIcon(gray.icon);
			gray.label.setHorizontalAlignment(JLabel.CENTER);
			gray.frame.getContentPane().add(gray.label, BorderLayout.CENTER);
			gray.frame.setVisible(true);
			gray.frame.repaint();
		}
		//this.save("Rotated");
	}


	// Test Case.
	public static void main(String args[]) throws InterruptedException {
		String name = args[0];
		double degrees = Double.parseDouble(args[1]);
		Rotate rot = new Rotate(name);
		rot.rotate(degrees);
	  }
	}
