package envi.gui;

import envi.action.Actions;
import envi.action.VouseEvent;
import envi.connection.MooseServer;
import envi.experiment.Experimenter;
import envi.experiment.FittsTrial;
import envi.experiment.Mologger;
import envi.experiment.Practicer;
import envi.tools.Config;
import envi.tools.Utils;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.sql.SQLOutput;
import java.time.LocalTime;

public class PracticePanel extends JPanel implements MouseInputListener {

    private final String TAG = "[[PracticePanel]] ";
    private final boolean toLog = false;
    //===================================================

    // Two circles to draw
    private Circle stacle;
    private Circle tarcle;

    // Texts to draw
    private int trialNum = 1;
    private String hintText = "Click was not inside. Please try again.";

    // Graphics
    private Graphics2D graphics2D;

    // Practice vars
    private int winW, winH; // Window
    private int dispW, dispH; // Display area
    private boolean stacleClicked = false;
    private boolean pressedInsideStacle = false;
    private FittsTrial trial;

    // Keys
    private final String SPACE = "SPACE";

    private Action nextPhase = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            MainFrame.get().showPanel(new StartPanel(Config.PROCESS_STATE.WARM_UP));
            setVisible(false);
        }
    };

    // Setting & Drawing  ==========================================================

    /**
     * Constructor
     */
    public PracticePanel() {

        // Set params
        winW = MainFrame.get().getBounds().width;
        winH = MainFrame.get().getBounds().height;

        dispW = winW - (2 * Config.WIN_W_MARGIN);
        dispH = winH - (2 * Config.WIN_H_MARGIN);

        // Set listeners and binding
        addMouseListener(this);

        setFocusable(true);

        getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), SPACE);
        getActionMap().put(SPACE, nextPhase);

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

        // Start the trials
        nextTrial();
    }

    /**
     * Go to the next trial
     */
    public void nextTrial() {
        // Get a random trial
        trial = new FittsTrial(dispW, dispH);

        // Create circles from the trial
        stacle = new Circle(trial.getStaclePosition(),
                Config._stacleRad,
                Config.COLOR_STACLE_DEF);
        tarcle = new Circle(trial.getTarclePosition(),
                trial.getTarRad(),
                Config.COLOR_TARCLE_DEF);

        if (toLog) System.out.println(TAG + "stacle rad = " + stacle.radius);
    }

    /**
     * Main printing function
     * @param graphics Graphics
     */
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // Graphics
        Graphics2D graphics2D = (Graphics2D) graphics; 
        
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
        graphics2D.setFont(Config.S_FONT);
        graphics2D.drawString("S", stacle.cx - 3, stacle.cy + 5);

        //  Target circle
        graphics2D.setColor(tarcle.getColor());
        graphics2D.drawOval(tarcle.tlX, tarcle.tlY, tarcle.getSide(), tarcle.getSide());
        graphics2D.fillOval(tarcle.tlX, tarcle.tlY, tarcle.getSide(), tarcle.getSide());

        //-- Draw stat text
        graphics2D.setColor(Config.COLOR_TEXT_NRM);
        graphics2D.setFont(Config.EXP_INFO_FONT);
        graphics2D.drawString(String.valueOf(trialNum), winW - Config.TEXT_X, Config.TEXT_Y);

        requestFocus();
    }


    // Actions  ====================================================================
    /**
     * Virtual press of the primary mouse button
     */
    public void vPressPrimary() {
        // Position of the curser
        Point crsPos = getCursorPosition();

        if (!stacleClicked) { // Pressing the start
            if (stacle.isInside(crsPos.x, crsPos.y)) {
                pressedInsideStacle = true;
                stacle.setColor(Config.COLOR_STACLE_CLK);
            } else { // NOT INSIDE!
//                errText = Utils.ERR_NOT_INSIDE;
            }
        }

        repaint();
    }

    /**
     * Virtual release of the primary mouse buttons
     */
    public void vReleasePrimary() {
        // Postion of the curser
        Point crsPos = getCursorPosition();

        if (stacleClicked) { // Target Second release
            stacleClicked = false;
            stacle.setColor(Config.COLOR_STACLE_DEF);

            // Next trial
            trialNum++;
            nextTrial();
        } else { // Start release
            if (pressedInsideStacle && stacle.isInside(crsPos.x, crsPos.y)) {
                stacleClicked = true;
            } else { // NOT INSIDE!
//                errText = Utils.ERR_NOT_INSIDE;
                stacle.setColor(Config.COLOR_STACLE_DEF);
            }
        }

        repaint();
    }

    /**
     * Get the cursor poistion (relative to the frame)
     * @return Point (x,y) position of the cursor
     */
    private Point getCursorPosition() {
        Point crsPos = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(crsPos, this);
        return crsPos;
    }


    // MouseListener Methods =======================================================
    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) {
        vPressPrimary(); // Temp
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        vReleasePrimary(); // Temp
        requestFocus();
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseDragged(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) { }

}
