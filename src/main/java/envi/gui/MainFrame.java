package envi.gui;

import envi.tools.Config;
import envi.tools.Pair;
import envi.tools.Pref;
import envi.tools.Utils;

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

    /**
     * Return the display area (removing the margins)
     * @return Display area
     */
    public Pair<Integer, Integer> getDispArea() {
        return new Pair<>(
                getWidthMM() - 2 * Pref.WIN_MARG_W_mm,
                getHeightMM() - 2 * Pref.WIN_MARG_H_mm);
    }

    public int getWidthMM() {
        return px2mm(getWidth());
    }

    public int getHeightMM() {
        return px2mm(getHeight());
    }

    /**
     * Display area coordinates --> Windows coordinates
     * @param inPoint Point in display area
     * @return Window's coordinates
     */
    public Point dispToWin(Point inPoint) {
        inPoint.translate(Pref.WIN_MARG_W_mm, Pref.WIN_MARG_H_mm);
        return pointMM2Px(inPoint);
    }

    /**
     * Convert mm to pixels
     * @param mm Millimeters
     * @return Pixels
     */
    public int mm2px(int mm) {
        return (int)((mm / Utils.MM_IN_INCH) * Config._dpi);
    }

    /**
     * Convert pixels to mm
     * @param px Pixels
     * @return Millimeters
     */
    public static int px2mm(int px) {
        return (int)((px / Config._dpi) * Utils.MM_IN_INCH);
    }

    public Point pointMM2Px(Point mmP) {
        return new Point(mm2px(mmP.x), mm2px(mmP.y));
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
