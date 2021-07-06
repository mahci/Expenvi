package envi.gui;

import envi.experiment.*;
import envi.log.*;
import envi.tools.Configs;
import envi.action.Actions;
import envi.connection.MooseServer;
import envi.tools.Prefs;
import envi.tools.Strs;
import envi.tools.Utils;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Objects;

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
    private boolean targetPressed = false;
    private boolean trialStarted = false;

    // Publishing all the movements
    private static PublishSubject<MouseEvent> mouseSubject;
    private Disposable disposable;

    // [JFT] For faster testing the trials
    private final Action nextTrial = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
//            if(toLog) System.out.println(TAG + "SPACE Performed");
//            trialDone();
        }
    };

    private Experiment experiment; // The experiment to show
    private FittsTrial trial; // The trial to show
    private int technique; // Technique of the experiment (ordinal)
    private int phase; // Which phase? (ordinal)

    // Indexesx
    private int blockNum;
    private int subBlockNum;
    private int overallSBlockNum;
    private int trialNum;

    // Break flag (for homing time)
    private boolean inBreak;
    
    private long triallBeginTime;
    private long sBlockBeginTime;
    private long stPressTime;
    private long stReleaseTime;
    private long tgPressTime;
    private long tgReleaseTime;
    private long sBlockEndTime;
    private long homingTime;

    private long startExitTime;
    private Point startExitPoint;

    private long targetFirstEntryTime;
    private Point targetFirstEntryPoint;
    private long targetLastEntryTime;
    private Point targetLastEntryPoint;
    private int nTargetEntries = 0;
    
    private Point stPressPoint;
    private double stPressDist;

    private Point stReleasePoint;
    private double stReleaseDist;
    
    private Point tgPressPoint;
    private double tgPressDist;

    private Point tgReleasePoint;
    private double tgReleaseDist;

    private int tgHit; // Hit = 1, miss = 0, start double click = -1xxz

    private boolean startExited = false;
    private boolean targetEntered = false;

    private boolean pressed;

    // Dummy Component for logging
    private Button btn = new Button();

    // Log info
    GeneralLogInfo mGeneralLogInfo = new GeneralLogInfo();
    TrialLogInfo mTrialLogInfo = new TrialLogInfo();
    InstantsLogInfo mInstantsLogInfo = new InstantsLogInfo();
    TimesLogInfo mTimesLogInfo = new TimesLogInfo();

    // ===============================================================================

    /**
     * Constructor
     */
    public ExperimentPanel(Experiment exp, Configs.TECH tech, Experimenter.PHASE phs) {

        // Add mouse movement/click listeners
        addMouseListener(this);
        addMouseMotionListener(this);

        // SPACE => next trrial [JFT]
//        getInputMap().put(
//                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true),
//                "SPACE");
//        getActionMap().put("SPACE", nextTrial);

        // For publishing mouse actions
        mouseSubject = PublishSubject.create();

        // Subscribe to the actions Publisher from MooseServer
        disposable = MooseServer.get().actionSubject.subscribe(action -> {
            if (toLog) System.out.println(TAG + " <- " + action);
            switch (action) {
                case Actions.ACT_CLICK:
                    vClickPrimary();
                    break;
                case Actions.ACT_PRESS_PRI:
                    vPressPrimary();
                    break;
                case Actions.ACT_RELEASE_PRI:
                    vReleasePrimary();
                    break;
                case Actions.ACT_CANCEL:
                    vCancel();
                    break;
                case Actions.ACT_PRESS_SEC:
            case Actions.ACT_RELEASE_SEC:
                break;
            }
        });

        // Set vars
        experiment = exp;
        technique = tech.ordinal();
        phase = phs.ordinal();

        // Set log vars
        mGeneralLogInfo.phase = phs;
        mGeneralLogInfo.technique = tech;

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
        overallSBlockNum = 1;
        trialNum = 1;

        // Set log vars
        mGeneralLogInfo.subBlockNum = 1;
        mGeneralLogInfo.trialNum = 1;

        // Sync with the Moose
        MooseServer.get().sendMssg(Strs.MSSG_SUBBLOCK, overallSBlockNum);
        MooseServer.get().sendMssg(Strs.MSSG_TRIAL, trialNum);

        // Save the start time
        triallBeginTime = Utils.nowInMillis();
        sBlockBeginTime = Utils.nowInMillis();

    }


    /**
     * Main printing function
     * @param graphics Graphics
     */
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;

        // Widtd/height of the window
        int winW = this.getWidth();
        int winH = this.getHeight();

        // Set anti-alias
        graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //-- Draw circles
        // Colors
        Color startColor = Prefs.COLOR_START_DEF;
        if (startPressed || trialStarted) startColor = Prefs.COLOR_START_SEL;
        Color targetColor = Prefs.COLOR_TARGET_DEF;
        if (targetPressed) targetColor = Prefs.COLOR_TARGET_SEL;

        // Start circle
        graphics2D.setColor(startColor);
        graphics2D.drawOval(stacle.tlX, stacle.tlY, stacle.getSide(), stacle.getSide());
        graphics2D.fillOval(stacle.tlX, stacle.tlY, stacle.getSide(), stacle.getSide());
//        if(toLog) System.out.println(TAG + "stacle: " + stacle.tlX + " , " + stacle.tlY);
        graphics2D.setColor(Prefs.COLOR_TEXT);
        graphics2D.setFont(Prefs.S_FONT);
        graphics2D.drawString("S",
                stacle.cx - MainFrame.mm2px((Prefs.S_TEXT_X_OFFSET_mm)),
                stacle.cy + MainFrame.mm2px(Prefs.S_TEXT_Y_OFFSET_mm));

        //  Target circle
        graphics2D.setColor(targetColor);
        graphics2D.drawOval(tarcle.tlX, tarcle.tlY, tarcle.getSide(), tarcle.getSide());
        graphics2D.fillOval(tarcle.tlX, tarcle.tlY, tarcle.getSide(), tarcle.getSide());
//        if(toLog) System.out.println(TAG + "tarcle: " + tarcle.tlX + " , " + tarcle.tlY);

        //--- Draw stat rectangles and texts
        graphics2D.setColor(Prefs.COLOR_TEXT);
        graphics2D.setFont(Prefs.STAT_FONT);

        int rect1W = MainFrame.mm2px(Prefs.STAT_RECT_WIDTH_mm) * 3/4;
        int rect2W = MainFrame.mm2px(Prefs.STAT_RECT_WIDTH_mm);
        int rectH = MainFrame.mm2px(Prefs.STAT_RECT_HEIGHT_mm);
        int rect2X = winW - (MainFrame.mm2px(Prefs.STAT_MARG_X_mm) +
                MainFrame.mm2px(Prefs.STAT_RECT_WIDTH_mm));
        int rect1X = winW - (MainFrame.mm2px(Prefs.STAT_MARG_X_mm) + rect1W + rect2W);
        int rect1Y = MainFrame.mm2px(Prefs.STAT_MARG_Y_mm);
        graphics2D.drawRect(rect1X, rect1Y, rect1W, rectH);
        graphics2D.drawRect(rect2X, rect1Y, rect2W, rectH);

        int text1X = rect1X + MainFrame.mm2px(Prefs.STAT_TEXT_X_PAD_mm);
        int text1Y = rect1Y + MainFrame.mm2px(Prefs.STAT_TEXT_Y_PAD_mm);
        int text2X = rect2X + MainFrame.mm2px(Prefs.STAT_TEXT_X_PAD_mm);
        graphics2D.setFont(Prefs.STAT_FONT);
        graphics2D.drawString(trialStatText, text1X, text1Y);
        graphics2D.drawString(blockStatText, text2X, text1Y);

        //--- Draw technique text
        int techTextX = MainFrame.mm2px(Prefs.STAT_MARG_X_mm);
        int techTextY = MainFrame.mm2px(Prefs.STAT_MARG_Y_mm)
                + MainFrame.mm2px(Prefs.STAT_TEXT_Y_PAD_mm);
        graphics2D.drawString(
                "Technique: " + Experimenter.get().getTechnique().toString(),
                techTextX, techTextY);

        //--- Show colorful rectangle (of on break)
        if (inBreak) {
            graphics2D.setColor(Prefs.COLOR_BREAK_BACK);
            graphics2D.fillRect(0, 0, winW, winH);
        }

//        requestFocus();
    }

    /**
     * Set the scene for painting
     */
    private void setScene() {
//        if (toLog) System.out.println(TAG + blockNum + " | " + subBlockNum + " | " + trialNum);
        trial = experiment
                .getBlock(blockNum)
                .getSubBlock(subBlockNum)
                .getTrial(trialNum);

        Point staclePosition = MainFrame.get().dispToWin(trial.getStaclePosition());
        Point tarclePosition = MainFrame.get().dispToWin(trial.getTarclePosition());

        stacle = new Circle(staclePosition, trial.getStRad());
        tarcle = new Circle(tarclePosition, trial.getTarRad());
//        if(toLog) System.out.println(TAG + trial.getStaclePosition());
//        if(toLog) System.out.println(TAG + trial.getTarRad());
//        if(toLog) System.out.println(TAG + stacle);
//        if(toLog) System.out.println(TAG + tarcle);
        // Texts
        blockStatText = "Block: " + overallSBlockNum + " | " + experiment.getTotalNSubBlocks();
        trialStatText = "Trial: " + trialNum;

        // Set the start time
        triallBeginTime = Utils.nowInMillis();
        if (inBreak) sBlockBeginTime = Utils.nowInMillis();

        // Update log info
        mTrialLogInfo.fittsTuple = trial.getVars();
        mTrialLogInfo.startPosition = staclePosition;
        mTrialLogInfo.targetPosition = tarclePosition;
        mInstantsLogInfo.trialShowInst = Utils.nowInMillis();

        repaint();
    }

    /**
     * Click attempted on Target
     */
    private void trialDone() {
        if (toLog) System.out.println(TAG + "TrialDone");
        trialStarted = false;
        startPressed = false;
        startExited = false;
        targetEntered = false;
        targetPressed = false;

        logTrial(); // Log all the trial info

        //-- Next step:
        if (experiment.getBlock(blockNum).getSubBlock(subBlockNum).hasNext(trialNum)) { // More trials
            nextTrial();
        } else { // Trials in the sub-block finished
            // Log the subblock
            sBlockEndTime = Utils.nowInMillis();
            logSBlock();

            // Was it the last subblock?
            if (experiment.isFinished(overallSBlockNum)) {
                // Finish alll the logs
                Mologger.get().closeLogs();

                // Experimenter takes control
                if (disposable != null) disposable.dispose();
                Experimenter.get().endPhase();
            } else {
                showBreak();
            }
        }

    }

    /**
     * Trial is cancelled on the start => no change on the numbers, just reset the colors
     */
    private void trialStartOver() {
        if(toLog) System.out.println(TAG + "trialRepaint");
        trialStarted = false;
        startPressed = false;
        startExited = false;
        targetEntered = false;
        targetPressed = false;

        repaint();
    }

    /**
     * Cancelled on the target => reshuffle the trial to try later
     */
    private void trialRepeat() {
        if(toLog) System.out.println(TAG + "trialRepeat");
        trialStarted = false;
        startPressed = false;
        startExited = false;
        targetEntered = false;
        targetPressed = false;

        logTrial(); // Log all the trial info

        // Reshuffle and get the next trial
        experiment.getBlock(blockNum).getSubBlock(subBlockNum).reshuffleTrial(trialNum);
        nextTrial();
    }

    /**
     * Go to the next trial
     */
    public void nextTrial() {
        trialNum++;
        setScene();

        // Sync with the Moose
        MooseServer.get().sendMssg(Strs.MSSG_TRIAL, trialNum);

        // Update the log info
        mGeneralLogInfo.trialNum = trialNum;
    }

    /**
     * Go to the next subblock (may be the next block)
     */
    public void nextSubblock(){
        // Check if more subblocks from the same block or we should go to the next
        if (experiment.getBlock(blockNum).hasNext(subBlockNum)) {
            subBlockNum++;
        } else {
            blockNum++;
            subBlockNum = 1;
        }

        overallSBlockNum++;
        trialNum = 1;
        setScene();

        // Update the log info
        mGeneralLogInfo.subBlockNum = overallSBlockNum;
        mGeneralLogInfo.trialNum = trialNum;

        // Sync with the Moose
        MooseServer.get().sendMssg(Strs.MSSG_SUBBLOCK, overallSBlockNum);
        MooseServer.get().sendMssg(Strs.MSSG_TRIAL, trialNum);
    }

    /**
     * Log all the trial info
     */
    private void logTrial() {

        // Save log info
        mTimesLogInfo.trialTime = (int) (Utils.nowInMillis() - mInstantsLogInfo.trialShowInst);
        mTrialLogInfo.selectionTime = (int) (mInstantsLogInfo.targetPressInst - mInstantsLogInfo.startReleaseInst);

        // Log and reset the trial info
        Mologger.get().logTrial(mGeneralLogInfo, mTrialLogInfo);
        Mologger.get().logInst(mGeneralLogInfo, mInstantsLogInfo);
        Mologger.get().logTime(mGeneralLogInfo, mTimesLogInfo);

        mTrialLogInfo.reset(); // Reset all the trial infos

    }

    /**
     * Log all the subBlock info
     */
    private void logSBlock() {
        // Log the subblock and reset the trial times
        mTimesLogInfo.trialTime = 0;
        mTimesLogInfo.subblockTime = (int) (sBlockEndTime - sBlockBeginTime);
        Mologger.get().logTime(mGeneralLogInfo, mTimesLogInfo);
        mTimesLogInfo.subblockTime = 0; // Reset after logging
    }

    //region [Setters]

    /**
     * Set the technique
     * @param tech TECH
     */
    public void setTechnique(Configs.TECH tech) {
        technique = tech.ordinal();
    }

    /**
     * Set the phase
     * @param phs PHASE
     */
    public void setPhase(Experimenter.PHASE phs) {
        phase = phs.ordinal();
    }

    //endregion

    // -------------------------------------------------------------------------------
    //region [virtual acitons]

    /**
     * Virtual press of the primary mouse button
     */
    public void vPressPrimary() {
        Point crsPos = getCursorPosition(); // Position of the curser

        // Log EVENT
        MouseEvent vMouseEvent = new MouseEvent(
                btn, MouseEvent.MOUSE_PRESSED, Utils.nowInMillis(),
                0, crsPos.x, crsPos.y, 0, false);
        Mologger.get().logEvent(mGeneralLogInfo, vMouseEvent);

        // Check where pressed...
        if (trialStarted) { // Trial is started

            // (temporarily) save log info
            mInstantsLogInfo.targetPressInst = Utils.nowInMillis();
            mTrialLogInfo.targetPressPoint = crsPos;
            mTrialLogInfo.targetPressDist = MainFrame.px2mm(tarcle.distToCenter(crsPos));

            // Where did the press happened?
            if (tarcle.isInside(crsPos)) targetPressed = true;
            else pressed = true;

        } else { // Starting with the trial

            // Save log infos
            mInstantsLogInfo.startPressInst = Utils.nowInMillis();
            mTrialLogInfo.startPressPoint = crsPos;
            mTrialLogInfo.startPressDist = MainFrame.px2mm(stacle.distToCenter(crsPos));

            // Was the press inside Start?
            if (stacle.isInside(crsPos)) {
                startPressed = true;
            }

        }

        repaint();
    }

    /**
     * Virtual release of the primary mouse buttons
     */
    public void vReleasePrimary() {
        Point crsPos = getCursorPosition(); // Curser position

        // Log EVENT
        MouseEvent vMouseEvent = new MouseEvent(
                btn, MouseEvent.MOUSE_RELEASED, Utils.nowInMillis(),
                0, crsPos.x, crsPos.y, 0, false);
        Mologger.get().logEvent(mGeneralLogInfo, vMouseEvent);

        if (trialStarted) { // Releasing inside/outside target

            // Save log infos
            mInstantsLogInfo.targetReleaseInst = Utils.nowInMillis();
            mTrialLogInfo.targetReleasePoint = crsPos;
            mTrialLogInfo.targetReleaseDist = MainFrame.px2mm(tarcle.distToCenter(crsPos));

            pressed = false; // No longer pressed

            if (!tarcle.isInside(crsPos)) { // Released outside the target

                Utils.playSound(Prefs.TARGET_MISS_ERR_SOUND); // Target MISS error

                if (stacle.isInside(crsPos)) { // Double click on start
                    mTrialLogInfo.result = -1;
                } else { // Target MISS
                    mTrialLogInfo.result = 0;
                }

            } else { // Released inside the Target => HIT
                Utils.playSound(Prefs.TARGET_HIT_SOUND);
                mTrialLogInfo.result = 1;
            }

            trialDone(); // Trial is done (in any way)

        } else { // Releasing after Start is pressed

            // Save log info
            mInstantsLogInfo.startReleaseInst = Utils.nowInMillis();
            mTrialLogInfo.startReleasePoint = crsPos;
            mTrialLogInfo.startReleaseDist = MainFrame.px2mm(stacle.distToCenter(crsPos));

            startPressed = false;

            if (stacle.isInside(crsPos)) { // Released inside Start
                System.out.println(TAG + "Released inside start");
                trialStarted = true;
                repaint();
            } else { // Released outside Start
                System.out.println(TAG + "Released outside start");
                Utils.playSound(Prefs.START_MISS_ERR_SOUND); // Play error
                trialStartOver(); // Start over
            }
        }

    }

    /**
     * Virtual CLICK
     */
    public void vClickPrimary() {
        Point crsPos = getCursorPosition(); // Position of the curser

        // Log EVENT
        MouseEvent vMouseEvent = new MouseEvent(
                btn, MouseEvent.MOUSE_CLICKED, Utils.nowInMillis(),
                0, crsPos.x, crsPos.y, 0, false);
        Mologger.get().logEvent(mGeneralLogInfo, vMouseEvent);

        if (trialStarted) { // Clicking on the target => Trial finished
            // Save the position, distance, and time of press (for log)
            tgReleasePoint = tgPressPoint = crsPos;
            tgReleaseTime = tgPressTime = Utils.nowInMillis();
            tgReleaseDist = tgPressDist = MainFrame.px2mm(tarcle.distToCenter(crsPos));

            // Play error sound if outside the target (window in focus)
            if (!tarcle.isInside(crsPos)) {
                if (Objects.equals(MainFrame.get().getFocusOwner(), this)) { // Miss
                    Utils.playSound(Prefs.TARGET_MISS_ERR_SOUND);
                }

                if (stacle.isInside(crsPos)) { // Double click on Stacle
                    tgHit = -1;
                } else {
                    tgHit = 0;
                }

            } else {
                tgHit = 1;
            }

            // Trial is done!
            trialDone();

        } else { // Clicking on the start
            // Save the position, distance, and time of press (for log)
            stReleasePoint = stPressPoint = crsPos;
            stReleaseTime = stPressTime = Utils.nowInMillis();
            stReleaseDist = stPressDist = MainFrame.px2mm(stacle.distToCenter(crsPos));

            if (stacle.isInside(crsPos)) { // Inside
                System.out.println(TAG + "inside start");
                trialStarted = true;
                repaint();
            } else { // Outside
                System.out.println(TAG + "outside start");
                if (Objects.equals(MainFrame.get().getFocusOwner(), this)) {
                    Utils.playSound(Prefs.START_MISS_ERR_SOUND); // Play error
                }

                trialStartOver();
            }

        }
    }

    /**
     * Virtual cancel
     */
    public void vCancel() {
        Point crsPos = getCursorPosition(); // Curser position

        // Log EVENT (EXITED = CANCELLED!)
        MouseEvent vMouseEvent = new MouseEvent(
                btn, MouseEvent.MOUSE_EXITED, Utils.nowInMillis(),
                0, crsPos.x, crsPos.y, 0, false);
        Mologger.get().logEvent(mGeneralLogInfo, vMouseEvent);

        if (startPressed) { // Cancel on start

            Utils.playSound(Prefs.START_MISS_ERR_SOUND); // Play error

            // Save log info
            mInstantsLogInfo.startCancelInst = Utils.nowInMillis();
            mTrialLogInfo.startCancelPoint = crsPos;
            mTrialLogInfo.startCancelDist = MainFrame.px2mm(stacle.distToCenter(crsPos));

            startPressed = false;
            trialStartOver();
        }

        if (trialStarted) { // User is going for the Target

            if (targetPressed) { // Cancel on target

                Utils.playSound(Prefs.TARGET_MISS_ERR_SOUND);

                // Save log info
                mInstantsLogInfo.targetCancelInst = Utils.nowInMillis();
                mTrialLogInfo.targetCancelPoint = crsPos;
                mTrialLogInfo.targetCancelDist = MainFrame.px2mm(stacle.distToCenter(crsPos));

                targetPressed = false;
                trialRepeat();

            }

            // If cancelled in between, ignore it

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
    //region [Mouse actions]
    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) {
        if (Experimenter.get().isTechnique(Configs.TECH.MOUSE) &&
                e.getButton() == MouseEvent.BUTTON1) {
            vPressPrimary();
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (Experimenter.get().isTechnique(Configs.TECH.MOUSE) &&
                e.getButton() == MouseEvent.BUTTON1) {
            vReleasePrimary();
        }
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
        // Log EVENT
        Mologger.get().logEvent(mGeneralLogInfo, e);

        // Log Start exits, Target entries
        logExitEnter(getCursorPosition());

        // If homing on the mouse, log the homing time
        if (Experimenter.get().getHomingStart() > 0) {
            // Save homing time
            mTimesLogInfo.homingTime = (int) (Utils.nowInMillis() - Experimenter.get().getHomingStart());
            Mologger.get().logTime(mGeneralLogInfo, mTimesLogInfo);
            Experimenter.get().resetHoming();
            mTimesLogInfo.homingTime = 0;
        }

    }

    //endregion


    /**
     * Log the START exit and TARGET entries
     * @param crsPosition Point - cursor position
     */
    private void logExitEnter(Point crsPosition) {

        if (trialStarted) { // It only matters after the start is clicked

            // Log start exit
            if (!startExited && !stacle.isInside(crsPosition)) {
                mTrialLogInfo.startExitPoint = crsPosition;
                mInstantsLogInfo.startExitInst = Utils.nowInMillis();
                if (toLog) System.out.println(TAG + "Exit point: " + mTrialLogInfo.startExitPoint);
                if (toLog) System.out.println(TAG + "Exit inst: " + mInstantsLogInfo.startExitInst);
                startExited = true;
            }

            // Log target entries
            if (!targetEntered) {

                if (tarcle.isInside(crsPosition)) { // Went inside

                    if (mTrialLogInfo.nTargetEntries == 0) { // First time
                        mTrialLogInfo.targetFirstEntyPoint = crsPosition;
                        mInstantsLogInfo.targetFirstEntryInst = Utils.nowInMillis();

                    } else { // More times (always keep the last one)
                        mTrialLogInfo.targetLastEntyPoint = crsPosition;
                        mInstantsLogInfo.targetLastEntryInst = Utils.nowInMillis();
                    }

                    targetEntered = true;
                    mTrialLogInfo.nTargetEntries++;

                }

            } else { // Already entered => Look for exits
                if (!tarcle.isInside(crsPosition)) {
                    targetEntered = false;
                }
            }

        }
    }

    /**
     * Show the break dialog (between blocks)
     */
    public void showBreak() {
        inBreak = true;
        repaint();
        MainFrame.get().showDialog(new BreakDialog());

        // Back from the dialog
        System.out.println(TAG + "Back from the break");
        inBreak = false;
        nextSubblock();
    }

}
