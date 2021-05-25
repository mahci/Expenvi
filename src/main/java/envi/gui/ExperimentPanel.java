package envi.gui;

import envi.tools.Config;
import envi.action.Actions;
import envi.action.VouseEvent;
import envi.connection.MooseServer;
import envi.experiment.Experimenter;
import envi.experiment.Mologger;
import envi.tools.Utils;
import io.reactivex.rxjava3.subjects.PublishSubject;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.time.LocalTime;

public class ExperimentPanel extends JPanel implements MouseInputListener {

    private final String TAG = "[[ExperimentPanel]] ";
    private final boolean toLog = true;
    // -------------------------------------------------------------------------------

    // Circles to draw
    private Circle stacle;
    private Circle tarcle;

    // Texts to draw
    private String blockStatText = "";
    private String trialStatText = "";
    private String errText = "";

    // Graphics
    private Graphics2D graphics2D;

    // Experiment vars
    private boolean startClicked = false;
    private boolean pressedInsideStacle = false;

    // Publishing all the movements
    private static PublishSubject<MouseEvent> mouseSubject;

    // [JFT] For faster testing the trials
    private final Action nextTrial = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(toLog) System.out.println(TAG + "SPACE Performed");
            Experimenter.get().trialDone(true);
        }
    };

    // ===============================================================================

    /***
     * Constructor
     */
    public ExperimentPanel() {
        addMouseListener(this);
        addMouseMotionListener(this);
        getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true),
                "SPACE");
        getActionMap().put("SPACE", nextTrial);

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

        requestFocusInWindow();
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

        graphics2D.setColor(Config._starcleTextColor);
        graphics2D.setFont(new Font(Config.FONT_STYLE, Font.PLAIN, 14));
        graphics2D.drawString("S", stacle.cx - 3, stacle.cy + 5);

        //  Target circle
        graphics2D.setColor(tarcle.getColor());
        graphics2D.drawOval(tarcle.tlX, tarcle.tlY, tarcle.getSide(), tarcle.getSide());
        graphics2D.fillOval(tarcle.tlX, tarcle.tlY, tarcle.getSide(), tarcle.getSide());

        //-- Draw stat texts
        graphics2D.setColor(Config._normalTextColor);
        graphics2D.setFont(new Font(Config.FONT_STYLE, Font.PLAIN, Config.EXP_INFO_FONT_SIZE));
        graphics2D.drawString(blockStatText, winW - Config.TEXT_X, Config.TEXT_Y);
        graphics2D.drawString(trialStatText, winW - Config.TEXT_X, Config.TEXT_Y + 20);

        // -- Show error
        if (!errText.isEmpty()) {
            int errTextX = winW / 2 - 200;
            graphics2D.setColor(Config._errorTextColor);
            graphics2D.setFont(new Font(Config.FONT_STYLE, Font.PLAIN, Config.EXP_INFO_FONT_SIZE));
            graphics2D.drawString(errText, errTextX, Config.ERROR_Y);

            errText = ""; // Clear the error
        }

        requestFocus();

    }

    /***
     * Set the circles (with their default colors)
     * @param c1 Start circle
     * @param c2 Target circle
     */
    public void setCircles(Circle c1, Circle c2) {
        stacle = c1;
        stacle.setColor(Config._starcleDefColor);
        tarcle = c2;
        tarcle.setColor(Config._tarcleDefColor);
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

    // -------------------------------------------------------------------------------
    //region [virtual acitons]

    /**
     * Virtual press of the primary mouse button
     */
    public void vPressPrimary() {
        // Position of the curser
        Point crsPos = getCursorPosition();

        // Create the VouseEvent
        VouseEvent ve = new VouseEvent(Actions.ACT.PRESS, crsPos.x, crsPos.y, LocalTime.now());

        // Log the ve in all
        Mologger.get().log(ve, Mologger.LOG_LEVEL.ALL);

        // Check where pressed...
        if (startClicked) { // Target pressing
            // Log the press
            if (Experimenter.get().realExperiment) Mologger.get().log(ve);

        } else { // Start pressing
            if (stacle.isInside(crsPos.x, crsPos.y)) {
                pressedInsideStacle = true;

                // Log in gen
                Mologger.get().log(ve, Mologger.LOG_LEVEL.GEN);

                // change the color of the start circle
                stacle.setColor(Config._starcleClickedColor);
            } else { // Show error (NOT INSIDE)
                errText = Config.ERR_NOT_INSIDE;
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

        // Log the ve in all
        Mologger.get().log(ve, Mologger.LOG_LEVEL.ALL);

        // Check where released...
        if (startClicked) { // Released on the target is clicked => Trial finished
            // Valid release => Log in gen and spec
            Mologger.get().log(ve, Mologger.LOG_LEVEL.GEN);
            Mologger.get().log(ve, Mologger.LOG_LEVEL.SPEC);

            // Falsify the startClicked
            startClicked = false;

            // Report the result to the Experimenter
            boolean result = tarcle.isInside(crsPos.x, crsPos.y);
            Experimenter.get().trialDone(result);

        } else { // Released from the start
            if (pressedInsideStacle && stacle.isInside(crsPos.x, crsPos.y)) {
                startClicked = true;

                // Valid release => log in gen and spec
                Mologger.get().log(ve, Mologger.LOG_LEVEL.GEN);
                Mologger.get().log(ve, Mologger.LOG_LEVEL.SPEC);

            } else { // Show error (NOT INSIDE) and change back the Stacle color
                errText = Config.ERR_NOT_INSIDE;
                stacle.setColor(Config._starcleDefColor);

                // Invalid release => log in only gen
                Mologger.get().log(ve, Mologger.LOG_LEVEL.GEN);
            }

            repaint();
        }
    }

    //endregion

    // -------------------------------------------------------------------------------
    /**
     * Get the cursor poistion (relative to the frame)
     * @return Point (x,y) position of the cursor
     */
    private Point getCursorPosition() {
        Point crsPos = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(crsPos, this);
        return crsPos;
    }

    // -------------------------------------------------------------------------------
    //region [Overrides]
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        vPressPrimary(); // Temp
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        vReleasePrimary(); // Temp
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
        // Log every move on all log
        Mologger.get().log(e, Mologger.LOG_LEVEL.ALL, LocalTime.now());

        // If homing on the mouse, log the homing time
        if (Experimenter.get().getHomingStart() > 0) {
            long homingTime = Utils.now() - Experimenter.get().getHomingStart();
            Mologger.get().log(homingTime, "Homing Time", Mologger.LOG_LEVEL.GEN);
            Experimenter.get().setHomingStart(0); // Reset the time
        }
    }

    //endregion

}
