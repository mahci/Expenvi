package envi.gui;

import envi.action.Actions;
import envi.connection.MooseServer;
import envi.experiment.Experimenter;
import envi.experiment.FittsTrial;
import envi.experiment.FittsTuple;
import envi.tools.Configs;
import envi.tools.Prefs;
import envi.tools.Utils;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

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
    private String[] techStrs = new String[3];

    // Experiment vars
    private boolean startPressed = false;
    private boolean pressedInsideStart = false;
    private int techNum = 0;


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
//        if(toLog) System.out.println(TAG + "stacle: " + stacle.tlX + " , " + stacle.tlY);
        graphics2D.setColor(Prefs.COLOR_TEXT);
        graphics2D.setFont(Prefs.S_FONT);
        graphics2D.drawString("S",
                stacle.cx - MainFrame.get().mm2px((Prefs.S_TEXT_X_OFFSET_mm)),
                stacle.cy + MainFrame.get().mm2px(Prefs.S_TEXT_Y_OFFSET_mm));

        //  Target circle
        graphics2D.setColor(Prefs.COLOR_TARGET_DEF);
        graphics2D.drawOval(tarcle.tlX, tarcle.tlY, tarcle.getSide(), tarcle.getSide());
        graphics2D.fillOval(tarcle.tlX, tarcle.tlY, tarcle.getSide(), tarcle.getSide());

        //--- Draw stat rectangles and stat texts
        graphics2D.setColor(Prefs.COLOR_TEXT);
        graphics2D.setFont(Prefs.STAT_FONT);

        int rect1W = MainFrame.get().mm2px(Prefs.STAT_RECT_WIDTH_mm) * 3/4;
        int rect2W = MainFrame.get().mm2px(Prefs.STAT_RECT_WIDTH_mm);
        int rectH = MainFrame.get().mm2px(Prefs.STAT_RECT_HEIGHT_mm);
        int rect2X = winW - (MainFrame.get().mm2px(Prefs.STAT_MARG_X_mm) +
                MainFrame.get().mm2px(Prefs.STAT_RECT_WIDTH_mm));
        int rect1X = winW - (MainFrame.get().mm2px(Prefs.STAT_MARG_X_mm) + rect1W + rect2W);
        int rect1Y = MainFrame.get().mm2px(Prefs.STAT_MARG_Y_mm);
//        graphics2D.drawRect(rect1X, rect1Y, rect1W, rectH);
        graphics2D.drawRect(rect2X, rect1Y, rect2W, rectH);

        int text1X = rect1X + MainFrame.get().mm2px(Prefs.STAT_TEXT_X_PAD_mm);
        int text1Y = rect1Y + MainFrame.get().mm2px(Prefs.STAT_TEXT_Y_PAD_mm);
        int text2X = rect2X + MainFrame.get().mm2px(Prefs.STAT_TEXT_X_PAD_mm);
//        graphics2D.drawString(trialStatText, text1X, text1Y);
        graphics2D.drawString(blockStatText, text2X, text1Y);

        //--- Draw technique text
        int techTextX = MainFrame.get().mm2px(Prefs.STAT_MARG_X_mm);
        int techTextY = MainFrame.get().mm2px(Prefs.STAT_MARG_Y_mm)
                + MainFrame.get().mm2px(Prefs.STAT_TEXT_Y_PAD_mm);
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
        stacle = new Circle(
                MainFrame.get().dispToWin(trial.getStaclePosition()),
                MainFrame.get().mm2px(Configs._stacleRadMM)
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
                Utils.playSound(Prefs.TARGET_MISS_ERR_SOUND);
            }

            trialDone();
        } else { // Released from the start
            if (pressedInsideStart && stacle.isInside(crsPos.x, crsPos.y)) {
                trialStarted();
            } else {
                // Play error sound
                Utils.playSound(Prefs.START_MISS_ERR_SOUND);

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
        if (Experimenter.get().getTechnique()
                .equals(Configs.TECH.MOUSE)) vPressPrimary();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (Experimenter.get().getTechnique()
                .equals(Configs.TECH.MOUSE)) vReleasePrimary();
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
