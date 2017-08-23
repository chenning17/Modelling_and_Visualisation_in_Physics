import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

class Visualization extends Frame {
	private BufferedImage foreground;
	private BufferedImage background;

	Visualization(int width, int height) {
		foreground = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		background = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		setVisible(true);
		setSize(width + 500, height + getInsets().top + 500);
		addWindowListener(new WindowAdapter() {public void windowClosing(WindowEvent event) {System.exit(0);}});
	}

	void set(int column, int row, Color color) {
		background.setRGB(column, row, color.getRGB());
	}

	void draw() {
		foreground.setData(background.getData());
		getGraphics().drawImage(foreground, 0, getInsets().top, getWidth(), getHeight() - getInsets().top, null);
	}

	public void paint(Graphics graphics) {
		graphics.drawImage(foreground, 0, getInsets().top, getWidth(), getHeight() - getInsets().top, null);
	}
}

