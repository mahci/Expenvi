package envi.gui;

import envi.connection.MooseServer;
import envi.experiment.Experiment;
import envi.experiment.Experimenter;
import envi.experiment.Practice;
import envi.tools.Configs;
import envi.tools.Prefs;
import envi.tools.Strs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class StartPanel extends JPanel {

    // Components
    JButton startButton = new JButton();
    JLabel hintLabel = new JLabel();
    JLabel techLabel = new JLabel();

    // Variables
    private Configs.TECH technique;
    private Experimenter.PHASE phase;

    // -------------------------------------------------------------------------------

    //--- Actions to perform on click of the button
    private final Action startShowCase = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
//            ShowCaser.get().startShowCase();
            MooseServer.get().syncPhase(Experimenter.PHASE.SHOWCASE); // Sync the phase
            MainFrame.get().showPanel(new ShowcasePanel()); // Show panel
//            Experimenter.get().start(Experimenter.PHASE.SHOWCASE);
        }
    };

    private final Action startPractice = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
//            Experimenter.get().start(Experimenter.PHASE.PRACTICE);
//            Configs.updateDisplayValues();

            MooseServer.get().syncPhase(Experimenter.PHASE.PRACTICE); // Sync the phase
            MooseServer.get().syncTechnique(Experimenter.get().getTechnique()); // Sync the technique

            MainFrame.get().showPanel(
                    new ExperimentPanel(new Practice(1, 3), technique, phase));
//


//            Experimenter.get().startExperiment(false);
        }
    };

    private final Action startExperiment = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {

            MooseServer.get().syncPhase(Experimenter.PHASE.EXPERIMENT); // Sync the phase
            MooseServer.get().syncTechnique(Experimenter.get().getTechnique()); // Sync the technique

            // Show experiment panel
            MainFrame.get().showPanel(
                    new ExperimentPanel(new Experiment(5, 2), technique, phase));

        }
    };

    // ===============================================================================
    /**
     * Constructor
     */
    public StartPanel(Experimenter.PHASE phase, Configs.TECH technique) {
        this.technique = technique;
        this.phase = phase;

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
