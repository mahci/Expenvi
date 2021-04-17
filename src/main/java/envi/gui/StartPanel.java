package envi.gui;

import envi.tools.Config;
import envi.experiment.Experimenter;
import envi.tools.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartPanel extends JPanel {

    /**
     * Constructor
     */
    public StartPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(600, 500));
        setMaximumSize(new Dimension(600, 500));

        // Start button
        JButton startButton = new JButton("Start Experiment");
        startButton.setFont(new Font("Sans", Font.PLAIN, 14));
        startButton.setMaximumSize(new Dimension(250, 50));
        startButton.setAlignmentX(CENTER_ALIGNMENT);
        startButton.addActionListener(e -> Experimenter.get().startExperiment());

        // Hint label
        JLabel hintLabel = new JLabel("Press SPACE to start the experiment");
        hintLabel.setAlignmentX(CENTER_ALIGNMENT);
        hintLabel.setFont(new Font("Sans", Font.PLAIN, 14));

        // Config info
        JLabel radiiLabel = new JLabel("Target Radii: " + Config._targetRadiiMM + " mm");
        radiiLabel.setFont(new Font("Sans", Font.PLAIN, 12));
        radiiLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel distLabel = new JLabel("Target Distances: " + Config._distancesMM + " mm");
        distLabel.setFont(new Font("Sans", Font.PLAIN, 12));
        distLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel actLabel = new JLabel("Action: " +
                Utils.actionString(Config._action) + " | " +
                "Vibrate: " + Config._vibrate);
        actLabel.setFont(new Font("Sans", Font.PLAIN, 12));
        actLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Config button
        JButton configButton = new JButton("Change configuration");
        configButton.setFont(new Font("Sans", Font.PLAIN, 12));
        configButton.setMinimumSize(new Dimension(300, 35));
        configButton.setAlignmentX(CENTER_ALIGNMENT);
        configButton.addActionListener(e -> {
//            MainFrame.get().showDialog(new ConfigDialog());
            MainFrame.get().showDialog(new BreakDialog());
        });

        this.add(Box.createVerticalStrut(350)); // Top space
        this.add(hintLabel);
        this.add(Box.createVerticalStrut(20)); // Space
        this.add(startButton);
        this.add(Box.createVerticalStrut(100)); // Space
        this.add(radiiLabel);
        this.add(Box.createVerticalStrut(10)); // Space
        this.add(distLabel);
        this.add(Box.createVerticalStrut(10)); // Space
        this.add(actLabel);
        this.add(Box.createVerticalStrut(10)); // Space
        this.add(configButton);
    }

}
