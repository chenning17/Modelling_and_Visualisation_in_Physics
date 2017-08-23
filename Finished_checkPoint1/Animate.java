import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

class Animate {	// -------------------------------------------------------------
	static void init(final BufferedImage bi) {
		//for (int x = 0; x < bi.getWidth(); x++) for (int y = 0; y < bi.getHeight(); y++) bi.setRGB(x, y, Math.random() < .5 ? Color.BLACK.getRGB() : Color.WHITE.getRGB());

for (int x = 0; x < bi.getWidth(); x++) for (int y = 0; y < bi.getHeight(); y++) bi.setRGB(x, y, Color.BLACK.getRGB());

	}
	
	static void update(final BufferedImage bi) {
		for(int i=0; i<400; i++){		
		final int x = (int)(Math.random()*bi.getWidth());
		final int y = (int)(Math.random()*bi.getHeight());
		//bi.setRGB(x, y, bi.getRGB(x, y) == Color.BLACK.getRGB() ? Color.WHITE.getRGB() : Color.BLACK.getRGB());

bi.setRGB(x, y, Math.random() < .6 ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
	}
}
	//----------------------------------------------------------------------

	public static void main(final String[] args) throws Exception {
		if (args.length != 3) throw new Exception("Arguments: width[pixels] height[pixels] period[milliseconds]");
		final int W = Integer.parseInt(args[0]);
		final int H = Integer.parseInt(args[1]);
		final long P = Long.parseLong(args[2]);

		final BufferedImage bi = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
		final Object lock = new Object();
		final Frame f = new Frame();
		f.setIgnoreRepaint(true);
		f.setTitle("Animate " + args[0] + " " + args[1] + " " + args[2]);
		f.setVisible(true);
		f.setSize(W, H + f.getInsets().top);
		//f.setExtendedState(Frame.MAXIMIZED_BOTH);
		f.addWindowListener(new WindowAdapter() {public void windowClosing(WindowEvent we) {System.exit(0);}});

		init(bi);
		new Timer().scheduleAtFixedRate(new TimerTask() {public void run() {synchronized(lock) {f.getGraphics().drawImage(bi, 0, f.getInsets().top, f.getWidth(), f.getHeight() - f.getInsets().top, null);}}}, 0, 33);
		new Timer().scheduleAtFixedRate(new TimerTask() {public void run() {synchronized(lock) {update(bi);}}}, 0, P);
	}
}


