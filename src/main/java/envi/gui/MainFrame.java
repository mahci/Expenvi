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
        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

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

    /***
     * Show a dialog (bc it needs a frame
     * @param mssg Message to show
     */
    public void showMessageDialog(String mssg) {
        JOptionPane.showMessageDialog(this, mssg);
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
                showPanel(new StartPanel());
            }
        });

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
     * Show the frame on a specific monitor
     */
    public void display()
    {
        int scrW = Config.SCR_BOUNDS.width;
        int scrH = Config.SCR_BOUNDS.height;

        int frW = getSize().width;
        int frH = getSize().height;

        setLocation(
                ((scrW / 2) - (frW / 2)) + Config.SCR_BOUNDS.x,
                ((scrH / 2) - (frH / 2)) + Config.SCR_BOUNDS.y
        );
        setVisible(true);
    }

}
