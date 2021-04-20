package envi.gui;

import envi.experiment.Practicer;
import envi.tools.Config;
import envi.tools.Utils;

import javax.swing.*;
import java.awt.*;

public class StartPanel extends JPanel {

    private String showCaseHint = "Welcome to the Moose! Press SPACE to start";
    private String showCaseBtnText = "Show Me How It Works";

    private String warmUpHint = "Let's warm-up a bit. Press SPACE to go";
    private String warmUpBtnText = "Let's Go!";

    private String experimentHint = "Now the real experiment. Press SPACE to start";
    private String experimentBtnText = "Start Experiment";

    /**
     * Constructor
     */
    public StartPanel(Config.PROCESS_STATE state) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(600, 500));
        setMaximumSize(new Dimension(600, 500));

        // Create the generic button
        JButton startButton = new JButton();
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
            startButton.addActionListener(e ->
                    MainFrame.get().showPanel(new PracticePanel()));
            break;
        case WARM_UP:
            labelText = warmUpHint;
            btnText = warmUpBtnText;

            break;
        case EXPERIMENT:
            break;
        }

        // Start button

//        startButton.addActionListener(e -> Experimenter.get().startExperiment());


        // Hint label
        JLabel hintLabel = new JLabel(labelText);
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
    }

}
