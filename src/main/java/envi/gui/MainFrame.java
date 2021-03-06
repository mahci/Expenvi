package envi.gui;

import envi.tools.Configs;
import envi.tools.Pair;
import envi.tools.Prefs;
import envi.tools.Utils;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class MainFrame extends JFrame {

    private String TAG = "[[MainFrame]] ";
    private boolean toLog = false;
    // -------------------------------------------------------------------------------

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
    public Pair<Integer, Integer> getDispAreaMM() {
        return new Pair<>(
                getWidthMM() - 2 * Prefs.WIN_MARG_W_mm,
                getHeightMM() - 2 * Prefs.WIN_MARG_H_mm);
    }

    public Pair<Integer, Integer> getDispArea() {
        if (toLog) System.out.println("H_Margin (px) = " + mm2px(Prefs.WIN_MARG_H_mm));
        return new Pair<>(
                getWidth() - 2 * mm2px(Prefs.WIN_MARG_W_mm),
                getHeight() - 2 * mm2px(Prefs.WIN_MARG_H_mm)
        );
    }

    public int getWidthMM() {
        return (int) px2mm(getWidth());
    }

    public int getHeightMM() {
        return (int) px2mm(getHeight());
    }

    /**
     * Display area coordinates --> Windows coordinates
     * @param inPoint Point in display area
     * @return Window's coordinates
     */
//    public Point dispToWinMM(Point inPoint) {
//        inPoint.translate(Prefs.WIN_MARG_W_mm, Prefs.WIN_MARG_H_mm);
//        return pointMM2Px(inPoint);
//    }

    public Point dispToWin(Point inPoint) {
        Point p = inPoint;
        p.translate(
                mm2px(Prefs.WIN_MARG_W_mm),
                mm2px(Prefs.WIN_MARG_H_mm));
        return p;
    }

    /**
     * Convert mm to pixels
     * @param mm Millimeters
     * @return Pixels
     */
    public static int mm2px(double mm) {
        return (int)((mm / Utils.MM_IN_INCH) * Configs._dpi);
    }

    /**
     * Convert pixels to mm
     * @param px Pixels
     * @return Millimeters
     */
    public static double px2mm(int px) {
        return ((double) px / Configs._dpi) * Utils.MM_IN_INCH;
    }

    /***
     * Draw the passed panel
     * @param panel JPanel to draw
     */
    public void showPanel(JPanel panel) {
        getContentPane().removeAll();
        revalidate();
        add(panel);
        getContentPane().invalidate();
        getContentPane().validate();
//        pack();
        display();
    }

    public void removePanels() {
        for (Component c : getAllComponents(this)) {
            c.disable();
        }
        getContentPane().removeAll();
        revalidate();
    }

    public java.util.List<Component> getAllComponents(final Container c) {
        Component[] comps = c.getComponents();
        java.util.List<Component> compList = new ArrayList<Component>();
        for (Component comp : comps) {
            compList.add(comp);
            if (comp instanceof Container)
                compList.addAll(getAllComponents((Container) comp));
        }
        return compList;
    }

    /**
     * Show the frame on a specific monitor
     * @param dialog JDialog to show
     */
    public void showDialog(JDialog dialog)
    {
        dialog.pack();
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

//        dialog.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosed(WindowEvent e) {
////                showPanel(new StartPanel());
//            }
//        });

        int scrW = Configs._scrDims.width;
        int scrH = Configs._scrDims.height;

        int frW = dialog.getSize().width;
        int frH = dialog.getSize().height;

        dialog.setLocation(
                ((scrW / 2) - (frW / 2)) + Configs._scrDims.x,
                ((scrH / 2) - (frH / 2)) + Configs._scrDims.y
        );
        dialog.setVisible(true);
    }

//    public void showBreak() {
//        BreakDialog dialog = new BreakDialog(this);
//
//        dialog.pack();
//        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
//
//        int scrW = Configs._scrDims.width;
//        int scrH = Configs._scrDims.height;
//
//        int frW = dialog.getSize().width;
//        int frH = dialog.getSize().height;
//
//        dialog.setLocation(
//                ((scrW / 2) - (frW / 2)) + Configs._scrDims.x,
//                ((scrH / 2) - (frH / 2)) + Configs._scrDims.y
//        );
//        dialog.setVisible(true);
//    }

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
        int scrW = Configs._scrDims.width;
        int scrH = Configs._scrDims.height;

        int frW = getSize().width;
        int frH = getSize().height;

        setLocation(
                ((scrW / 2) - (frW / 2)) + Configs._scrDims.x,
                ((scrH / 2) - (frH / 2)) + Configs._scrDims.y
        );
        setVisible(true);
    }

    public void playSound(String resFileName) {
//        if (Objects.equals(getFocusOwner(), this)) {
//            Utils.playSound(Prefs.TARGET_MISS_ERR_SOUND);
//        }

        try {
            ClassLoader classLoader = MainFrame.class.getClassLoader();
            File soundFile = new File(classLoader.getResource(resFileName).getFile());
            URI uri = soundFile.toURI();
            URL url = uri.toURL();

            AudioClip clip = Applet.newAudioClip(url);
//            AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundFile);
            clip.play();

        } catch ( NullPointerException
                | IOException e
        ) {
            e.printStackTrace();
        }

    }

}
