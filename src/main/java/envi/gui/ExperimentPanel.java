package envi.gui;

import envi.tools.Config;
import envi.tools.Utils;
import envi.action.Actions;
import envi.action.VouseEvent;
import envi.connection.MooseServer;
import envi.experiment.Experimenter;
import envi.experiment.Mologger;
import io.reactivex.rxjava3.subjects.PublishSubject;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.time.LocalTime;

public class ExperimentPanel extends JPanel implements MouseInputListener {

    private final String TAG = "[[ExperimentPanel]] ";

    // Two circles to draw
    private Circle stacle;
    private Circle tarcle;

    // Text to draw
    private String blockStatText = "";
    private String trialStatText = "";
    private String errText = "";

    // Graphics
    private Graphics2D graphics2D;

    // Experiment vars
    private boolean startClicked = false;
    private int trialNum;
    private boolean pressedInsideStacle = false;

    // Publishing all the movements
    private static PublishSubject<MouseEvent> mouseSubject;

    /***
     * Constructor
     */
    public ExperimentPanel() {
        addMouseListener(this);
        addMouseMotionListener(this);

        mouseSubject = PublishSubject.create();

        // Subscribe to the actions Publisher from MooseServer
        MooseServer.get().actionSubject.subscribe(action -> {
            System.out.println(TAG + " <- " + action);
            switch (action) {
                case Actions.ACT_CLICK:
                    vPressPrimary();
                    vReleasePrimary();
                    break;
                case Actions.ACT_PRESS_PRI:
                    vPressPrimary();
                    break;
                case Actions.ACT_RELEASE_PRI:
                    vReleasePrimary();
                    break;
                case Actions.ACT_PRESS_SEC:
                    break;
                case Actions.ACT_RELEASE_SEC:
                    break;
            }
        });
    }

    /***
     * Main printing function
     * @param graphics Graphics
     */
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        int winW = this.getWidth();
        int winH = this.getHeight();

        graphics2D = (Graphics2D) graphics;

        // Set anti-alias
        graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //-- Draw circles
        // Start circle
        graphics2D.setColor(stacle.getColor());
        graphics2D.drawOval(stacle.tlX, stacle.tlY, stacle.getSide(), stacle.getSide());
        graphics2D.fillOval(stacle.tlX, stacle.tlY, stacle.getSide(), stacle.getSide());

        graphics2D.setColor(Config.COLOR_TEXT_START);
        graphics2D.setFont(new Font(Config.FONT_STYLE, Font.PLAIN, 14));
        graphics2D.drawString("S", stacle.cx - 3, stacle.cy + 5);

        //  Target circle
        graphics2D.setColor(tarcle.getColor());
        graphics2D.drawOval(tarcle.tlX, tarcle.tlY, tarcle.getSide(), tarcle.getSide());
        graphics2D.fillOval(tarcle.tlX, tarcle.tlY, tarcle.getSide(), tarcle.getSide());

        //-- Draw stat texts
        graphics2D.setColor(Config.COLOR_TEXT_NRM);
        graphics2D.setFont(new Font(Config.FONT_STYLE, Font.PLAIN, Config.FONT_SIZE));
        graphics2D.drawString(blockStatText, winW - Config.TEXT_X, Config.TEXT_Y);
        graphics2D.drawString(trialStatText, winW - Config.TEXT_X, Config.TEXT_Y + 20);

        // -- Show error
        if (!errText.isEmpty()) {
            int errTextX = winW / 2 - 200;
            graphics2D.setColor(Config.COLOR_TEXT_ERR);
            graphics2D.setFont(new Font(Config.FONT_STYLE, Font.PLAIN, Config.FONT_SIZE));
            graphics2D.drawString(errText, errTextX, Config.ERROR_Y);

            errText = ""; // Clear the error
        }

    }

    /***
     * Set the circles (with their default colors)
     * @param c1 Start circle
     * @param c2 Target circle
     */
    public void setCircles(Circle c1, Circle c2) {
        stacle = c1;
        stacle.setColor(Config.COLOR_STACLE_DEF);
        tarcle = c2;
        tarcle.setColor(Config.COLOR_TARCLE_DEF);
//        System.out.println(TAG + "Stacle: " + stacle.paramsString());
    }

    /***
     * Set the stat texts to draw
     * @param blkTxt Block stat text
     * @param trlTxt Trial stat text
     */
    public void setStatTexts(String blkTxt, String trlTxt) {
        blockStatText = blkTxt;
        trialStatText = trlTxt;
    }

    // -----------------------------------------------------------------------
    // Virtual actions

    /**
     * Virtual press of the primary mouse button
     */
    public void vPressPrimary() {
        System.out.println(TAG + "Primary PRESS");
        // Postion of the curser
        Point crsPos = getCursorPosition();

        // Create the VouseEvent
        VouseEvent ve = new VouseEvent(Actions.ACT.PRESS, crsPos.x, crsPos.y, LocalTime.now());

        if (startClicked) { // Going for the target
            // Log the press
            Mologger.get().log(ve);

        } else { // Start of the trial
            if (stacle.isInside(crsPos.x, crsPos.y)) {
                System.out.println(TAG + "Pressed inside the Stacle");
                pressedInsideStacle = true;

                // change the color of the start circle
                stacle.setColor(Config.COLOR_STACLE_CLK);
            } else { // Show error (NOT INSIDE)
                errText = Utils.ERR_NOT_INSIDE;
            }

            repaint();
        }
    }

    /**
     * Virtual release of the primary mouse buttons
     */
    public void vReleasePrimary() {
        // Postion of the curser
        Point crsPos = getCursorPosition();

        // Create the VouseEvent
        VouseEvent ve = new VouseEvent(Actions.ACT.RELEASE, crsPos.x, crsPos.y, LocalTime.now());

        if (startClicked) { // The target is clicked
            // Log the release
            Mologger.get().log(ve);

            // Falsify the startClicked
            startClicked = false;

            // Report the result to the Experimenter
            boolean result = tarcle.isInside(crsPos.x, crsPos.y);
            Experimenter.get().trialDone(result);
        } else { // First click of the trial
            if (pressedInsideStacle && stacle.isInside(crsPos.x, crsPos.y)) {
                startClicked = true;

                // Log the relase
                Mologger.get().log(ve);

            } else { // Show error (NOT INSIDE) and change back the Stacle color
                errText = Utils.ERR_NOT_INSIDE;
                stacle.setColor(Config.COLOR_STACLE_DEF);
            }

            repaint();
        }
    }

    /**
     * Cancel press
     */
    public void vCancelPress() {
        // Create and log the VouseEvent
        Point crsPos = getCursorPosition();
        VouseEvent ve = new VouseEvent(Actions.ACT.CANCEL, crsPos.x, crsPos.y, LocalTime.now());
        Mologger.get().log(ve);

        // Falsify all flags and texts
        startClicked = false;
        pressedInsideStacle = false;
        errText = "";

        // Colors
        stacle.setColor(Config.COLOR_STACLE_DEF);
    }

    // -----------------------------------------------------------------------

    /**
     * Get the cursor poistion (relative to the frame)
     * @return Point (x,y) position of the cursor
     */
    private Point getCursorPosition() {
        Point crsPos = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(crsPos, this);
        return crsPos;
    }


    // Overrides ===============================================================
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        vPressPrimary();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        vReleasePrimary();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (startClicked) {
            Mologger.get().log(e, LocalTime.now());
        }
    }

}
