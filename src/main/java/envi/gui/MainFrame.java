package envi.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import envi.Config;
import envi.connection.*;


public class MainFrame extends JFrame {

    private static MainFrame frame;

    private Container container;

    private static Rectangle windowSize;

    private ExperimentPanel drawingPanel;
    private StartPanel startPanel;

    private BufferedImage graphicsContext;

    public MainFrame() {

        container = getContentPane();

        // Add the start panel
        startPanel = new StartPanel();
        container.add(startPanel, BorderLayout.CENTER);

        this.setTitle("Experiment!");
        this.pack();
    }

    public static MainFrame getFrame() {
        if (frame == null) frame = new MainFrame();
        return frame;
    }

    /***
     * Draw the passed panel
     * @param jp JPanel to draw
     */
    public void drawPanel(JPanel jp) {
        container.removeAll();
        container.add(jp, BorderLayout.CENTER);
        this.revalidate();
    }

    /***
     * Show a dialog (bc it needs a frame
     * @param mssg Message to show
     */
    public void showMessageDialog(String mssg) {
        JOptionPane.showMessageDialog(frame, mssg);
    }

    /***
     * Main method
     * @param args
     */
    public static void main(String[] args) {

        // Save the screens info (id is set in Config)
        saveScreenInfo();

        // Prepare the window and show the frame
        JFrame windowFrame = MainFrame.getFrame();

        windowFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        showFrame(windowFrame);

        // Show config window
        ConfigFrame cFrame = new ConfigFrame();
        cFrame.pack();
        cFrame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        showDialog(cFrame);


        // [TEST]
//        System.out.println(Utils.mm2px(10));

        // Start the server
        MooseServer.get().start();

        // Close the server on close
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                MooseServer.get().close();
            }
        });

    }

    /**
     * Show the frame on a specific monitor
     * @param frame Frame to display
     */
    public static void showFrame(JFrame frame)
    {
        int scrW = Config.SCR_BOUNDS.width;
        int scrH = Config.SCR_BOUNDS.height;

        int frW = frame.getSize().width;
        int frH = frame.getSize().height;

        frame.setLocation(
                ((scrW / 2) - (frW / 2)) + Config.SCR_BOUNDS.x,
                ((scrH / 2) - (frH / 2)) + Config.SCR_BOUNDS.y
        );
        frame.setVisible(true);
    }

    /**
     * Show the frame on a specific monitor
     * @param dialog JDialog to show
     */
    public static void showDialog(JDialog dialog)
    {
        int scrW = Config.SCR_BOUNDS.width;
        int scrH = Config.SCR_BOUNDS.height;

        int frW = dialog.getSize().width;
        int frH = dialog.getSize().height;

        dialog.setLocation(
                ((scrW / 2) - (frW / 2)) + Config.SCR_BOUNDS.x,
                ((scrH / 2) - (frH / 2)) + Config.SCR_BOUNDS.y
        );
        dialog.setVisible(true);
    }

    /**
     * Save the screen bounds for future uses
     */
    public static void saveScreenInfo() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();

        Config.NUM_SCREENS = gd.length;
        Config.SCR_BOUNDS = gd[Config.SCR_ID].getDefaultConfiguration().getBounds();
    }

}
