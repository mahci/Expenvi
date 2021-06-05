package envi.gui;

import envi.action.Actions;
import envi.action.VouseEvent;
import envi.connection.MooseServer;
import envi.experiment.FittsTrial;
import envi.experiment.FittsTuple;
import envi.experiment.Mologger;
import envi.tools.Config;
import envi.tools.Pref;
import envi.tools.Strs;
import envi.tools.Utils;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.time.LocalTime;

public class ShowcasePanel extends JPanel implements MouseInputListener {

    private final String TAG = "[[ShowcasePanel]] ";
    private final boolean toLog = false;
    // -------------------------------------------------------------------------------

    // Circles to draw
    private Circle stacle;
    private Circle tarcle;

    // Texts to draw
    private String blockStatText = "Showcase";
    private String trialStatText = "";

    // Experiment vars
    private boolean startPressed = false;
    private boolean pressedInsideStart = false;

    // Keys
    private final String SPACE = "SPACE";

    // Next phase action
    private Action nextPhase = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            MainFrame.get().showPanel(new StartPanel(Config.PROCESS_STATE.WARM_UP));
            setVisible(false);
        }
    };

    // ===============================================================================

    /**
     * Constructor
     */
    public ShowcasePanel() {

        // Add mouse movement/click listeners
        addMouseListener(this);
        addMouseMotionListener(this);

        // With SPACE go to the next phase
        getInputMap().put(
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_SPACE,
                        0, true),
                SPACE);
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

        setScene();

        requestFocusInWindow();

//        setFocusable(true);
    }

    /**
     * Main printing function
     * @param graphics Graphics
     */
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;

        int winW = this.getWidth();
        int winH = this.getHeight();

        // Set anti-alias
        graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //-- Draw circles
        // Colors
        Color startColor = startPressed ? Pref.COLOR_START_SEL : Pref.COLOR_START_DEF;

        // Start circle
        graphics2D.setColor(startColor);
        graphics2D.drawOval(stacle.tlX, stacle.tlY, stacle.getSide(), stacle.getSide());
        graphics2D.fillOval(stacle.tlX, stacle.tlY, stacle.getSide(), stacle.getSide());
//        if(toLog) System.out.println(TAG + "stacle: " + stacle.tlX + " , " + stacle.tlY);
        graphics2D.setColor(Pref.COLOR_TEXT);
        graphics2D.setFont(Pref.S_FONT);
        graphics2D.drawString("S",
                stacle.cx - MainFrame.get().mm2px((Pref.S_TEXT_X_OFFSET_mm)),
                stacle.cy + MainFrame.get().mm2px(Pref.S_TEXT_Y_OFFSET_mm));

        //  Target circle
        graphics2D.setColor(Pref.COLOR_TARGET_DEF);
        graphics2D.drawOval(tarcle.tlX, tarcle.tlY, tarcle.getSide(), tarcle.getSide());
        graphics2D.fillOval(tarcle.tlX, tarcle.tlY, tarcle.getSide(), tarcle.getSide());
//        if(toLog) System.out.println(TAG + "tarcle: " + tarcle.tlX + " , " + tarcle.tlY);

        //--- Draw stat rectangles and texts
        graphics2D.setColor(Pref.COLOR_TEXT);
        graphics2D.setFont(Pref.STAT_FONT);

        int rect1W = MainFrame.get().mm2px(Pref.STAT_RECT_WIDTH_mm) * 3/4;
        int rect2W = MainFrame.get().mm2px(Pref.STAT_RECT_WIDTH_mm);
        int rectH = MainFrame.get().mm2px(Pref.STAT_RECT_HEIGHT_mm);
        int rect2X = winW - (MainFrame.get().mm2px(Pref.STAT_MARG_X_mm) +
                MainFrame.get().mm2px(Pref.STAT_RECT_WIDTH_mm));
        int rect1X = winW - (MainFrame.get().mm2px(Pref.STAT_MARG_X_mm) + rect1W + rect2W);
        int rect1Y = MainFrame.get().mm2px(Pref.STAT_MARG_Y_mm);
        graphics2D.drawRect(rect1X, rect1Y, rect1W, rectH);
        graphics2D.drawRect(rect2X, rect1Y, rect2W, rectH);

        int text1X = rect1X + MainFrame.get().mm2px(Pref.STAT_TEXT_X_PAD_mm);
        int text1Y = rect1Y + MainFrame.get().mm2px(Pref.STAT_TEXT_Y_PAD_mm);
        int text2X = rect2X + MainFrame.get().mm2px(Pref.STAT_TEXT_X_PAD_mm);
        graphics2D.setFont(Pref.STAT_FONT);
        graphics2D.drawString(trialStatText, text1X, text1Y);
        graphics2D.drawString(blockStatText, text2X, text1Y);

        requestFocus();
    }

    /**
     * Set a random scene for painting
     */
    private void setScene() {

        // Generate a trial based on a random combination
        FittsTrial trial = new FittsTrial(FittsTuple.randFittsTuple());

        // Create the circles
        stacle = new Circle(
                MainFrame.get().dispToWin(trial.getStaclePosition()),
                MainFrame.get().mm2px(Config._stacleRadMM)
        );
        tarcle = new Circle(
                MainFrame.get().dispToWin(trial.getTarclePosition()),
                MainFrame.get().mm2px(trial.getTarWidth())
        );
    }

    /**
     * Start is clicked
     */
    private void trialStarted() {
        startPressed = true; // Pressed

        repaint();
    }

    /**
     * Click attempted on Target
     */
    private void trialDone() {

        startPressed = false; // Reset pressed

        setScene();
        repaint();

    }


    // ===============================================================================
    //region [Actions]
    /**
     * Virtual press of the primary mouse button
     */
    public void vPressPrimary() {

        // Position of the curser
        Point crsPos = getCursorPosition();

        // Check where pressed...
        if (startPressed) { // Target pressing
            // Log the press
        } else { // Start pressing
            if (stacle.isInside(crsPos.x, crsPos.y)) {
                pressedInsideStart = true;
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

        // Check where released...
        if (startPressed) { // Released on the target is clicked => Trial finished
            trialDone();
        } else { // Released from the start
            if (pressedInsideStart && stacle.isInside(crsPos.x, crsPos.y)) {
                trialStarted();
            }
        }
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

    //endregion

    // ===============================================================================
    //region [Overrides]

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

    //endregion

}
