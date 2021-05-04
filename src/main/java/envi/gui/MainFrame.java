package envi.gui;

import envi.tools.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {

    private static MainFrame self; // Singleton

    /**
     * Constructor
     */
    public MainFrame() {
        // Set the properties of the frame
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Get the instance
     * @return JFrame
     */
    public static MainFrame get() {
        if (self == null) self = new MainFrame();
        return self;
    }

    /***
     * Draw the passed panel
     * @param panel JPanel to draw
     */
    public void showPanel(JPanel panel) {
        getContentPane().removeAll();
        add(panel);
//        pack();
        display();
    }

    /**
     * Show the frame on a specific monitor
     * @param dialog JDialog to show
     */
    public void showDialog(JDialog dialog)
    {
        dialog.pack();
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
//                showPanel(new StartPanel());
            }
        });

        int scrW = Config._scrDims.width;
        int scrH = Config._scrDims.height;

        int frW = dialog.getSize().width;
        int frH = dialog.getSize().height;

        dialog.setLocation(
                ((scrW / 2) - (frW / 2)) + Config._scrDims.x,
                ((scrH / 2) - (frH / 2)) + Config._scrDims.y
        );
        dialog.setVisible(true);
    }

    /**
     * Show a message dialog
     * @param mssg Message to show
     */
    public void showMessageDialog(String mssg) {
        JOptionPane.showMessageDialog(
                this,
                mssg);
    }

    /**
     * Display a panel
     */
    public void display()
    {
        int scrW = Config._scrDims.width;
        int scrH = Config._scrDims.height;

        int frW = getSize().width;
        int frH = getSize().height;

        setLocation(
                ((scrW / 2) - (frW / 2)) + Config._scrDims.x,
                ((scrH / 2) - (frH / 2)) + Config._scrDims.y
        );
        setVisible(true);
    }

}
