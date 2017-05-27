 /****************************************************************************
 * Flavio Andrade
 * 4-27-2017
 * Compilation:  javac Flip.java
 * Execution:    java Flip Images/Ferrari.jpeg

 Use this class to  flip and image horizontally or vertically.
 *****************************************************************************
 */


import java.awt.image.BufferedImage;

public class Flip {
	public BufferedImage image, newImage;
	public Rotate rot;
	public String name;
	public int width, height, diagonal;

	// Construct a new Flip object by reading an image file.
	public Flip (String filename) {
		name = filename;
		rot = new Rotate(name);
		diagonal = rot.diagonal;
		width = rot.width;
		height = rot.height;
	}

	/* Flip the image horizontally or vertically. True for horizontal flip,
	   false for vertical.
	*/
	public void flip(boolean a) {
		newImage = new BufferedImage(diagonal + 10, diagonal + 10, BufferedImage.TYPE_INT_RGB);
		int x = 0;
		int y = 0;
		for (int h = 0; h < height; h++) {
			for (int c = 0; c < width; c++) {
				x = c - width / 2;
				y = h - height / 2;
				int color = rot.image.getRGB(c, h);
				// Flip horizontal, else flip vertically.
				if (a)
				    newImage.setRGB(x + (diagonal + 10) / 2, -y + (diagonal + 10) / 2, color);
				else {
					newImage.setRGB(-x + (diagonal + 10) / 2, y + (diagonal + 10) / 2, color);
				}
			}
		}
	}

    // Save the image to the current directory.
	public void save(String name) {
		rot.gray.save(this.newImage, name);
	}

	// Test case.
	public static void main(String[] args) {
		String name = args[0];
		Flip flip = new Flip(name);
		flip.flip(true);
		flip.rot.showImage(flip.newImage);
		flip.save("flipped");
	}
}
