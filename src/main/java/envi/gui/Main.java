package envi.gui;

import javax.swing.*;
import java.awt.*;

import envi.experiment.Experimenter;
import envi.tools.Config;
import envi.connection.*;
import envi.tools.Utils;


public class Main {

    private static final String TAG = "[[Main]] ";
    private static final boolean toLog = true;
    //======================================

    /***
     * Main method
     * @param args Arguments
     */
    public static void main(String[] args) {

        // Save the screens info (id is set in Config)
        saveScreenInfo();

        // Start the experiment process
        Experimenter.get().start(Experimenter.PHASE.SHOWCASE);

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

        Config._nScr = gd.length;
        Config._scrDims = gd[Config._scrId].getDefaultConfiguration().getBounds();
    }

}
