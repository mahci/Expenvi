package envi.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import envi.experiment.Practicer;
import envi.tools.Config;
import envi.connection.*;
import envi.tools.Utils;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;


public class Main extends JFrame {

    private final String TAG = "[[Main]] ";
    //======================================

    /***
     * Main method
     * @param args Arguments
     */
    public static void main(String[] args) {

        // Save the screens info (id is set in Config)
        saveScreenInfo();

        // Set the pixel values based on
        Utils.setPxValues();

        // Show StartPanel
        MainFrame.get().showPanel(new StartPanel(Config.PROCESS_STATE.SHOW_CASE));


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

        // Show config.json window
//        ConfigDialog cfgDialog = new ConfigDialog();
//        cfgDialog.pack();
//        cfgDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
//        showDialog(cfgDialog);
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
