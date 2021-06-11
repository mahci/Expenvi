package envi.gui;

import envi.experiment.Experiment;
import envi.experiment.Experimenter;
import envi.experiment.Mologger;
import envi.experiment.Practice;
import envi.tools.Configs;
import envi.tools.Prefs;
import envi.tools.Strs;
import envi.tools.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class StartPanel extends JPanel {

    // Components
    JButton startButton = new JButton();
    JLabel hintLabel = new JLabel();
    JLabel techLabel = new JLabel();
    // -------------------------------------------------------------------------------

    //--- Actions to perform on click of the button
    private final Action startShowCase = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
//            ShowCaser.get().startShowCase();
            MainFrame.get().showPanel(new ShowcasePanel());
//            Experimenter.get().start(Experimenter.PHASE.SHOWCASE);
        }
    };

    private final Action startPractice = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
//            Experimenter.get().start(Experimenter.PHASE.PRACTICE);
//            Configs.updateDisplayValues();

            MainFrame.get().showPanel(
                    new ExperimentPanel(
                            new Practice(1, 3)));
//
//            Experimenter.get().startExperiment(false);
        }
    };

    private final Action startExperiment = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {

            // Show experiment panel
            MainFrame.get().showPanel(
                    new ExperimentPanel(
                    new Experiment(5, 2)));

        }
    };

    // ===============================================================================
    /**
     * Constructor
     */
    public StartPanel(Experimenter.PHASE phase, Configs.TECH technique) {
        showUI(phase);
    }

    /**
     * Show the UI of the panel
     * @param phase Which PHASE (SHOWCASE, ...)
     */
    private void showUI(Experimenter.PHASE phase) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Create the generic button
        startButton.setFont(new Font("Sans", Font.PLAIN, 14));
        startButton.setMaximumSize(new Dimension(400, 50));
        startButton.setAlignmentX(CENTER_ALIGNMENT);
        startButton.setFocusPainted(false);

        // Check state for different texts
        String labelText = "";
        switch (phase) {
        case SHOWCASE -> {
            labelText = Strs.STPN_SHOWCASE_HINT;
            startButton.getInputMap().put(
                    KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true),
                    "SPACE");
            startButton.getActionMap().put("SPACE", startShowCase);
        }
        case PRACTICE -> {
            labelText = Strs.STPN_PRACTICE_HINT;
            startButton.getInputMap().put(
                    KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true),
                    "SPACE");
            startButton.getActionMap().put("SPACE", startPractice);
        }
        case EXPERIMENT -> {
            labelText = Strs.STPN_EXPERIMENT_HINT;
            startButton.getInputMap().put(
                    KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true),
                    "SPACE");
            startButton.getActionMap().put("SPACE", startExperiment);
        }
        }

        // Hint label
        hintLabel.setText(labelText);
        hintLabel.setAlignmentX(CENTER_ALIGNMENT);
        hintLabel.setFont(Prefs.STAT_FONT.deriveFont(16.0F));

        // Adding the components
        this.add(Box.createVerticalStrut(350)); // Top space
        if (Experimenter.get().getPhase() != Experimenter.PHASE.SHOWCASE) {
            techLabel = new JLabel("<html>Technique: <B>" +
                    Experimenter.get().getTechnique() +
                    "</B></html>");
            techLabel.setFont(Prefs.STAT_FONT.deriveFont(16.0F));
            techLabel.setAlignmentX(CENTER_ALIGNMENT);
            techLabel.setHorizontalAlignment(SwingConstants.CENTER);
            this.add(techLabel);
            this.add(Box.createVerticalStrut(40)); // Space
        }
        this.add(hintLabel);
        this.add(Box.createVerticalStrut(20)); // Space
        this.add(startButton);

        this.add(Box.createVerticalStrut(100)); // Space
        if (Experimenter.get().getPhase() == Experimenter.PHASE.SHOWCASE) {
            techLabel = new JLabel("<html>Techniques: <B>" +
                    Experimenter.get().getTechOrderStr() +
                    "</B></html>");
            techLabel.setFont(Prefs.STAT_FONT.deriveFont(12.0F));
            techLabel.setAlignmentX(CENTER_ALIGNMENT);
            techLabel.setHorizontalAlignment(SwingConstants.CENTER);
            this.add(techLabel);
        }

        startButton.requestFocusInWindow();
    }

    /**
     * Main painting method
     * @param graphics Graphics
     */
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        startButton.requestFocus();
    }

}
