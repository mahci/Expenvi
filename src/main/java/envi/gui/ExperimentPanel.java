package envi.gui;

import envi.experiment.*;
import envi.tools.Config;
import envi.action.Actions;
import envi.action.VouseEvent;
import envi.connection.MooseServer;
import envi.tools.Pref;
import envi.tools.Strs;
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

    // Experiment vars
    private boolean startPressed = false;
    private boolean pressedInsideStart = false;

    // Publishing all the movements
    private static PublishSubject<MouseEvent> mouseSubject;

    // [JFT] For faster testing the trials
    private final Action nextTrial = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(toLog) System.out.println(TAG + "SPACE Performed");
            trialDone();
        }
    };

    // The experiment to show
    private Experiment experiment;

    // Indexes
    private int blockNum;
    private int subBlockNum;
    private int trialNum;

    // ===============================================================================

    /**
     * Constructor
     */
    public ExperimentPanel(Experiment exp) {

        // Add mouse movement/click listeners
        addMouseListener(this);
        addMouseMotionListener(this);

        // SPACE => next trrial
        getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true),
                "SPACE");
        getActionMap().put("SPACE", nextTrial);

        // For publishing mouse actions
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
            case Actions.ACT_RELEASE_SEC:
                break;
            }
        });

        experiment = exp;

        init();
        setScene();

        requestFocusInWindow();
    }

    /**
     * Initialize
     */
    public void init() {
        blockNum = 1;
        subBlockNum = 1;
        trialNum = 1;
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

        //--- Show error
        if (!errText.isEmpty()) {
            int errTextX = winW / 2 - 200;
            graphics2D.setColor(Pref.COLOR_ERROR);
            graphics2D.drawString(errText, errTextX, Pref.STAT_MARG_Y_mm);
        }

        requestFocus();
    }

    /**
     * Set the scene for painting
     */
    private void setScene() {
        System.out.println(TAG + blockNum + " | " + subBlockNum + " | " + trialNum);
        FittsTrial trial = experiment
                .getBlock(blockNum)
                .getSubBlock(subBlockNum)
                .getTrial(trialNum);
        stacle = new Circle(
                MainFrame.get().dispToWin(trial.getStaclePosition()),
                MainFrame.get().mm2px(Config._stacleRadMM)
        );
        tarcle = new Circle(
                MainFrame.get().dispToWin(trial.getTarclePosition()),
                MainFrame.get().mm2px(trial.getTarWidth())
        );
        if(toLog) System.out.println(trial.getStaclePosition());
        if(toLog) System.out.println(trial.getTarWidth());
        if(toLog) System.out.println(stacle);
        if(toLog) System.out.println(tarcle);
        // Texts
        blockStatText = "Block: " + blockNum + " | " + experiment.getTotalNSubBlocks();
        trialStatText = "Trial: " + trialNum;
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

        // Next step
        if (experiment
                .getBlock(blockNum)
                .getSubBlock(subBlockNum)
                .hasNext(trialNum)) {
            trialNum++;
        } else { // Trials in the sub-block finished
            if (experiment
                    .getBlock(blockNum)
                    .hasNext(subBlockNum)) { // Next sub-block
                breakDialog();
                subBlockNum++;
                trialNum = 1;
            } else { // Sub-blocks finished
                if (experiment.hasNext(blockNum)) { // Next block
                    breakDialog();
                    blockNum++;
                    subBlockNum = 1;
                    trialNum = 1;
                } else {
                    endDialog();
                }
            }
        }

        setScene();
        repaint();

    }

    /**
     * Clicked outside the start
     */
    private void repeatTrial() {
        errText = Strs.ERR_NOT_INSIDE; // Show error
        startPressed = false; // Reset pressed

        repaint();
    }


    // -------------------------------------------------------------------------------
    //region [virtual acitons]

    /**
     * Virtual press of the primary mouse button
     */
    public void vPressPrimary() {

        // Ckear the error
        errText = "";

        // Position of the curser
        Point crsPos = getCursorPosition();

        // Create the VouseEvent
        VouseEvent ve = new VouseEvent(Actions.ACT.PRESS, crsPos.x, crsPos.y, LocalTime.now());

        // Log the ve in all
        Mologger.get().log(ve, Mologger.LOG_LEVEL.ALL);

        // Check where pressed...
        if (startPressed) { // Target pressing
            // Log the press
//            if (Experimenter.get().realExperiment) Mologger.get().log(ve);
        } else { // Start pressing
            if (stacle.isInside(crsPos.x, crsPos.y)) {
                pressedInsideStart = true;

                // Log in gen
                Mologger.get().log(ve, Mologger.LOG_LEVEL.GEN);

                // change the color of the start circle

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
        if (startPressed) { // Released on the target is clicked => Trial finished
            // Valid release => Log in gen and spec
            Mologger.get().log(ve, Mologger.LOG_LEVEL.GEN);
            Mologger.get().log(ve, Mologger.LOG_LEVEL.SPEC);


            // Report the result to the Experimenter
//            boolean result = tarcle.isInside(crsPos.x, crsPos.y);
//            Experimenter.get().trialDone(result);

            trialDone();
        } else { // Released from the start

            if (pressedInsideStart && stacle.isInside(crsPos.x, crsPos.y)) {
                // Valid release => log in gen and spec
                Mologger.get().log(ve, Mologger.LOG_LEVEL.GEN);
                Mologger.get().log(ve, Mologger.LOG_LEVEL.SPEC);

                trialStarted();

            } else { // Repeat trial
                // Invalid release => log in only gen
                Mologger.get().log(ve, Mologger.LOG_LEVEL.GEN);

                repeatTrial();
            }
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

        // If error is shown, clear it
        if (!errText.isEmpty()) {
            errText = "";
            repaint();
        }
    }

    //endregion

    /**
     * Show the break dialog (between blocks)
     */
    public void breakDialog() {
        MainFrame.get().showDialog(new BreakDialog());
    }

    /**
     * Show the end dialog
     */
    private void endDialog() {
        int input = JOptionPane.showOptionDialog(
                MainFrame.get(),
                Strs.DLG_END_TEXT,
                Strs.DLG_END_TITLE,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                null,
                null);
        if(input == JOptionPane.OK_OPTION)
        {
            System.exit(0);
        }
    }
}
