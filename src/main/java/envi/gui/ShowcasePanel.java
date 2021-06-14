package envi.gui;

import envi.action.Actions;
import envi.connection.MooseServer;
import envi.experiment.Experimenter;
import envi.experiment.FittsTrial;
import envi.experiment.FittsTuple;
import envi.tools.Configs;
import envi.tools.Prefs;
import envi.tools.Strs;
import envi.tools.Utils;
import io.reactivex.rxjava3.disposables.Disposable;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class ShowcasePanel extends JPanel implements MouseInputListener {

    private final String TAG = "[[ShowcasePanel]] ";
    private final boolean toLog = true;
    // -------------------------------------------------------------------------------

    // Circles to draw
    private Circle stacle;
    private Circle tarcle;

    // Texts to draw
    private String blockStatText = "Showcase";
    private String trialStatText = "";
    private String[] techStrs = new String[3];

    // Experiment vars
    private boolean startPressed = false;
    private boolean pressedInsideStart = false;
    private int techNum = 0;

    private Disposable disposable;

    // Keys
    private final String SPACE = "SPACE";
    private final String N1 = "1";
    private final String N2 = "2";
    private final String N3 = "3";

    // Next phase action
    private Action nextPhase = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Experimenter.get().end(Experimenter.PHASE.SHOWCASE);
            if (disposable != null) disposable.dispose();
            setVisible(false);
        }
    };

    // Set first, second, and third technique
    private Action tech1Action = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Experimenter.get().setTechInd(0);
            MooseServer.get().updateTechnique(Experimenter.get().getTechnique());
            techNum = 0;
            repaint();
        }
    };
    private Action tech2Action = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Experimenter.get().setTechInd(1);
            MooseServer.get().updateTechnique(Experimenter.get().getTechnique());
            techNum = 1;
            repaint();
        }
    };
    private Action tech3Action = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Experimenter.get().setTechInd(2);
            MooseServer.get().updateTechnique(Experimenter.get().getTechnique());
            techNum = 2;
            repaint();
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

        // Subscribe to the actions Publisher from MooseServer
        disposable = MooseServer.get().actionSubject.subscribe(action -> {
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

        setActions();
        setScene();

        requestFocusInWindow();

//        setFocusable(true);
    }

    private void setActions() {
        // With SPACE go to the next phase
        getInputMap().put(
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_SPACE,
                        0, true),
                SPACE);
        getActionMap().put(SPACE, nextPhase);

        // Put numbers to actions
        getInputMap().put(
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_1,
                        0, true),
                N1);
        getActionMap().put(N1, tech1Action);

        getInputMap().put(
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_2,
                        0, true),
                N2);
        getActionMap().put(N2, tech2Action);

        getInputMap().put(
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_3,
                        0, true),
                N3);
        getActionMap().put(N3, tech3Action);

        // Set the text
        techStrs[0] = "[1] " + Experimenter.get().getTech(0);
        techStrs[1] = "[2] " + Experimenter.get().getTech(1);
        techStrs[2] = "[3] " + Experimenter.get().getTech(2);

        // Update the technique on Moose
        MooseServer.get().sendMssg(Strs.MSSG_TECHNIQUE, Experimenter.get().getTech(0));

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
        Color startColor = startPressed ? Prefs.COLOR_START_SEL : Prefs.COLOR_START_DEF;

        // Start circle
        graphics2D.setColor(startColor);
        graphics2D.drawOval(stacle.tlX, stacle.tlY, stacle.getSide(), stacle.getSide());
        graphics2D.fillOval(stacle.tlX, stacle.tlY, stacle.getSide(), stacle.getSide());
        if(toLog) System.out.println(TAG + "stacle: " + stacle.tlX + " , " + stacle.tlY);
        graphics2D.setColor(Prefs.COLOR_TEXT);
        graphics2D.setFont(Prefs.S_FONT);
        graphics2D.drawString("S",
                stacle.cx - MainFrame.mm2px((Prefs.S_TEXT_X_OFFSET_mm)),
                stacle.cy + MainFrame.mm2px(Prefs.S_TEXT_Y_OFFSET_mm));

        //  Target circle
        graphics2D.setColor(Prefs.COLOR_TARGET_DEF);
        graphics2D.drawOval(tarcle.tlX, tarcle.tlY, tarcle.getSide(), tarcle.getSide());
        graphics2D.fillOval(tarcle.tlX, tarcle.tlY, tarcle.getSide(), tarcle.getSide());

        //--- Draw stat rectangles and stat texts
        graphics2D.setColor(Prefs.COLOR_TEXT);
        graphics2D.setFont(Prefs.STAT_FONT);

        int rect1W = MainFrame.mm2px(Prefs.STAT_RECT_WIDTH_mm) * 3/4;
        int rect2W = MainFrame.mm2px(Prefs.STAT_RECT_WIDTH_mm);
        int rectH = MainFrame.mm2px(Prefs.STAT_RECT_HEIGHT_mm);
        int rect2X = winW - (MainFrame.mm2px(Prefs.STAT_MARG_X_mm) +
                MainFrame.mm2px(Prefs.STAT_RECT_WIDTH_mm));
        int rect1X = winW - (MainFrame.mm2px(Prefs.STAT_MARG_X_mm) + rect1W + rect2W);
        int rect1Y = MainFrame.mm2px(Prefs.STAT_MARG_Y_mm);
//        graphics2D.drawRect(rect1X, rect1Y, rect1W, rectH);
        graphics2D.drawRect(rect2X, rect1Y, rect2W, rectH);

        int text1X = rect1X + MainFrame.mm2px(Prefs.STAT_TEXT_X_PAD_mm);
        int text1Y = rect1Y + MainFrame.mm2px(Prefs.STAT_TEXT_Y_PAD_mm);
        int text2X = rect2X + MainFrame.mm2px(Prefs.STAT_TEXT_X_PAD_mm);
//        graphics2D.drawString(trialStatText, text1X, text1Y);
        graphics2D.drawString(blockStatText, text2X, text1Y);

        //--- Draw technique text
        int techTextX = MainFrame.mm2px(Prefs.STAT_MARG_X_mm);
        int techTextY = MainFrame.mm2px(Prefs.STAT_MARG_Y_mm)
                + MainFrame.mm2px(Prefs.STAT_TEXT_Y_PAD_mm);
        graphics2D.drawString(techStrs[0], techTextX, techTextY);
        graphics2D.drawString(techStrs[1], techTextX + 100, techTextY);
        graphics2D.drawString(techStrs[2], techTextX + 200, techTextY);

        graphics2D.drawLine(techTextX + techNum * 100, techTextY + 10 ,
                techTextX + techNum * 100 + 80, techTextY + 10);

        requestFocus();
    }

    /**
     * Set a random scene for painting
     */
    private void setScene() {

        // Generate a trial based on a random combination
        FittsTrial trial = new FittsTrial(FittsTuple.randFittsTuple());

        // Create the circles
        Point staclePosition = MainFrame.get().dispToWin(trial.getStaclePosition());
        Point tarclePosition = MainFrame.get().dispToWin(trial.getTarclePosition());
        stacle = new Circle(staclePosition, trial.getStRad());
        tarcle = new Circle(tarclePosition, trial.getTarRad());

        if(toLog) System.out.println(TAG + staclePosition);
        if(toLog) System.out.println(TAG + tarclePosition);
        if(toLog) System.out.println(TAG + trial.getStaclePosition());
        if(toLog) System.out.println(TAG + trial.getTarclePosition());
        if(toLog) System.out.println(TAG + trial.getStRad());
        if(toLog) System.out.println(TAG + trial.getTarRad());
        if(toLog) System.out.println(TAG + stacle);
        if(toLog) System.out.println(TAG + tarcle);
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

    /**
     * Clicked outside the start
     */
    private void trialRepeat() {
        startPressed = false; // Reset pressed
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
        if (!startPressed && stacle.isInside(crsPos)) {
            pressedInsideStart = true;
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
            // Play error sound if outside the target
            if (!tarcle.isInside(crsPos)) {
                if (toLog) System.out.println(TAG + "Outside Target");
                if (Objects.equals(MainFrame.get().getFocusOwner(), this)) {
                    Utils.playSound(Prefs.TARGET_MISS_ERR_SOUND);
                }
            }

            trialDone();
        } else { // Released from the start
            if (pressedInsideStart && stacle.isInside(crsPos.x, crsPos.y)) {
                trialStarted();
            } else {
                if (toLog) System.out.println(TAG + "Outside Start");
                // Play error sound
                if (Objects.equals(MainFrame.get().getFocusOwner(), this)) {
                    Utils.playSound(Prefs.START_MISS_ERR_SOUND);
                }

                trialRepeat();
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
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseDragged(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) { }

    //endregion

}
