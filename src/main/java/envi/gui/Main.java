package envi.gui;

import java.awt.*;

import envi.experiment.Experimenter;
import envi.tools.Configs;
import envi.connection.*;


public class Main {

    private static final String TAG = "[[Main]] ";
    private static final boolean toLog = true;
    //======================================

    /***
     * Main method
     * @param args Arguments
     */
    public static void main(String[] args) {

        // Save the screens info (id is set in Configs)
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

    }

    /**
     * Save the screen bounds for future uses
     */
    public static void saveScreenInfo() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();

        Configs._nScr = gd.length;
        Configs._scrDims = gd[Configs._scrId].getDefaultConfiguration().getBounds();
    }

}
