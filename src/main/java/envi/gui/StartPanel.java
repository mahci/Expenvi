package envi.gui;

import envi.experiment.Experimenter;
import envi.experiment.Practicer;
import envi.tools.Config;
import envi.tools.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class StartPanel extends JPanel {

    private String showCaseHint = "Welcome to the Moose! Press SPACE to start";
    private String showCaseBtnText = "Show Me How It Works";

    private String warmUpHint = "Let's warm-up a bit. Press SPACE to go";
    private String warmUpBtnText = "Let's Go!";

    private String experimentHint = "Now the real experiment! Press SPACE to start";
    private String experimentBtnText = "Start Experiment";

    private Action startShowCase = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            MainFrame.get().showPanel(new PracticePanel());
        }
    };

    private Action startWarmUp = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Experimenter.get().startExperiment(false);
        }
    };

    private Action startExperiment = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Experimenter.get().startExperiment(true);
        }
    };

    JButton startButton = new JButton();
    JLabel hintLabel = new JLabel();

    /**
     * Constructor
     */
    public StartPanel(Config.PROCESS_STATE state) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(600, 500));
        setMaximumSize(new Dimension(600, 500));

        // Create the generic button
        startButton.setFont(new Font("Sans", Font.PLAIN, 14));
        startButton.setMaximumSize(new Dimension(250, 50));
        startButton.setAlignmentX(CENTER_ALIGNMENT);

        // Check state for different texts
        String labelText = "";
        String btnText = "";
        switch(state) {
        case SHOW_CASE:
            labelText = showCaseHint;
            btnText = showCaseBtnText;
            startButton.setText(btnText);
            startButton.getInputMap().put(
                    KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true),
                    "SPACE");
            startButton.getActionMap().put("SPACE", startShowCase);
            break;
        case WARM_UP:
            labelText = warmUpHint;
            btnText = warmUpBtnText;
            startButton.setText(btnText);
            startButton.getInputMap().put(
                    KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true),
                    "SPACE");
            startButton.getActionMap().put("SPACE", startWarmUp);
            break;
        case EXPERIMENT:
            labelText = experimentHint;
            btnText = experimentBtnText;
            startButton.setText(btnText);
            startButton.getInputMap().put(
                    KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true),
                    "SPACE");
            startButton.getActionMap().put("SPACE", startExperiment);
            break;
        }

        // Hint label
        hintLabel.setText(labelText);
        hintLabel.setAlignmentX(CENTER_ALIGNMENT);
        hintLabel.setFont(new Font("Sans", Font.PLAIN, 14));

        // Config info
//        JLabel radiiLabel = new JLabel("Target Radii: " + Config._targetRadiiMM + " mm");
//        radiiLabel.setFont(new Font("Sans", Font.PLAIN, 12));
//        radiiLabel.setAlignmentX(CENTER_ALIGNMENT);
//
//        JLabel distLabel = new JLabel("Target Distances: " + Config._distancesMM + " mm");
//        distLabel.setFont(new Font("Sans", Font.PLAIN, 12));
//        distLabel.setAlignmentX(CENTER_ALIGNMENT);
//
//        JLabel actLabel = new JLabel("Action: " +
//                Utils.actionString(Config._action) + " | " +
//                "Vibrate: " + Config._vibrate);
//        actLabel.setFont(new Font("Sans", Font.PLAIN, 12));
//        actLabel.setAlignmentX(CENTER_ALIGNMENT);
//
//        // Config button
//        JButton configButton = new JButton("Change configuration");
//        configButton.setFont(new Font("Sans", Font.PLAIN, 12));
//        configButton.setMinimumSize(new Dimension(300, 35));
//        configButton.setAlignmentX(CENTER_ALIGNMENT);
//        configButton.addActionListener(e -> {
//            MainFrame.get().showDialog(new ConfigDialog());
//        });

        this.add(Box.createVerticalStrut(350)); // Top space
        this.add(hintLabel);
        this.add(Box.createVerticalStrut(20)); // Space
        this.add(startButton);
        this.add(Box.createVerticalStrut(100)); // Space
//        this.add(radiiLabel);
//        this.add(Box.createVerticalStrut(10)); // Space
//        this.add(distLabel);
//        this.add(Box.createVerticalStrut(10)); // Space
//        this.add(actLabel);
//        this.add(Box.createVerticalStrut(10)); // Space
//        this.add(configButton);

        startButton.requestFocusInWindow();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        startButton.requestFocus();
    }

}
