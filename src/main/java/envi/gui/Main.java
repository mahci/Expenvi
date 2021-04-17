package envi.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import envi.tools.Config;
import envi.connection.*;


public class Main extends JFrame {

//    private static Main frame;
//
//    private Container container;
//
//    private static Rectangle windowSize;
//
//    private ExperimentPanel drawingPanel;
//    private StartPanel startPanel;
//
//    private BufferedImage graphicsContext;

    public Main() {

//        frame = new Main();
//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        container = getContentPane();
//
//        // Add the start panel
//        startPanel = new StartPanel();
////        container.add(startPanel, BorderLayout.CENTER);
//        this.add(startPanel);
//
//        this.setTitle("Experiment!");
//        this.pack();
    }

//    public static Main getFrame() {
//        if (frame == null) frame = new Main();
//        return frame;
//    }

    /***
     * Draw the passed panel
     * @param jp JPanel to draw
     */
//    public void drawPanel(JPanel jp) {
//        container.removeAll();
//        container.add(jp, BorderLayout.CENTER);
//        this.revalidate();
//    }



    /***
     * Main method
     * @param args
     */
    public static void main(String[] args) {

        // Save the screens info (id is set in Config)
        saveScreenInfo();

        // Prepare the window and show the frame
//        JFrame mainFrame = frame;
////
//        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


//        showFrame();

        // Show config window
//        ConfigDialog cfgDialog = new ConfigDialog();
//        cfgDialog.pack();
//        cfgDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
//        showDialog(cfgDialog);

        MainFrame.get().showPanel(new StartPanel());
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
     * Save the screen bounds for future uses
     */
    public static void saveScreenInfo() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();

        Config.NUM_SCREENS = gd.length;
        Config.SCR_BOUNDS = gd[Config.SCR_ID].getDefaultConfiguration().getBounds();
    }

}
