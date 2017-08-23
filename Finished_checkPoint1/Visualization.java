import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;


import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import javax.swing.JLabel;

class Visualization extends Frame {
    private BufferedImage foreground;
    private BufferedImage background;
    private int SLIDE_SCALE = 400;
    private JSlider slider = new JSlider(0,SLIDE_SCALE);   //

    Visualization(int width, int height, final Temperature T) {
        foreground = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        background = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        setVisible(true);
        setSize(width + 400, height + getInsets().top + 400);   //the 400 means the window size is already bigger - is some sort of scaling factor
        addWindowListener(new WindowAdapter() {public void windowClosing(WindowEvent event) {System.exit(0);}});
        
        
        this.add(new JLabel("Temperature"));
        this.setLayout(new BorderLayout());
        this.add(slider, BorderLayout.SOUTH);
        slider.setValue((int)T.getT()*(SLIDE_SCALE/4));
         slider.setVisible(true);
         
         
         
         /////////////////////////////////////////////////
      //     JFrame frame = new JFrame("Tick Slider");
    //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //JSlider jSliderOne = new JSlider();

    // Major Tick 25 - Minor 5
    slider.setMinorTickSpacing(4);
    slider.setMajorTickSpacing(8);
    slider.setPaintTicks(true);
    slider.setSnapToTicks(true);

    //frame.add(jSliderOne, BorderLayout.NORTH);
    //frame.setSize(300, 200);
    //frame.setVisible(true);
         /////////////////////////////////////////
         
         
         
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                int value = slider.getValue();
                /*if (value == 0) {
                    System.out.println("0");
                } else if (value > 0 && value <= 30) {
                    System.out.println("value > 0 && value <= 30");
                } else if (value > 30 && value < 80) {
                    System.out.println("value > 30 && value < 80");
                } else {
                    System.out.println("max");
                }*/
                T.setT((double)value/(SLIDE_SCALE/4));
                System.out.println(T.getT());
            }
        });
       
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

