package envi.gui;

import javax.swing.*;
import java.awt.*;

public class ConfigDialog extends JDialog {

    /**
     * Constructor
     */
    public ConfigDialog() {
        setTitle("Configuration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Limit of radius is Win_Height - 2*Vertical_Margin
//        int maxRadMM = Utils.px2mm(Configs._scrDims.height - (2 * Configs._winHeightMargin));
//        // Limit of distance is Win_Width - (2*Horizontal_Margin + STACLE_R + Max_Radii)
//        int maxdistMM = Utils.px2mm(Configs._scrDims.height - (2 * Configs._winHeightMargin));
//
//        // Components ------------------------------------
//        JLabel radiiLabel = new JLabel("Radii (mm):");
//
//        JTextField radiiField = new JTextField(15);
//        radiiField.setText(
//                Configs._targetRadiiMM
//                .toString().replaceAll("[\\[\\]]", ""));
//
//        JLabel radiiInfo = new JLabel("Max = " + maxRadMM + " mm");
//        radiiInfo.setFont(new Font("Sans-Serif", Font.PLAIN, 10));
//
//        JLabel distLabel = new JLabel("Distances (mm):");
//
//        JTextField distField = new JTextField(15);
//        distField.setText(
//                Configs._distancesMM
//                .toString().replaceAll("[\\[\\]]", ""));
//
//        JLabel distInfo = new JLabel("Max = " + maxdistMM + " mm");
//        distInfo.setFont(new Font("Sans-Serif", Font.PLAIN, 10));
//
//        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
//
//        JLabel actLabel = new JLabel("Action for click:");
//        JRadioButton swipeRBtn = new JRadioButton("SWIPE");
//        JRadioButton tapRBtn = new JRadioButton("TAP");
//        tapRBtn.setSelected(true);
//
//        ButtonGroup actGroup = new ButtonGroup();
//        actGroup.add(swipeRBtn);
//        actGroup.add(tapRBtn);
//
//        JCheckBox vibChBox = new JCheckBox("Vibrate");
//
//        JLabel stacleRadLabel = new JLabel("Start-circle radius (mm) =");
//        JSpinner stacleRadSpinner = new JSpinner();
//        stacleRadSpinner.setPreferredSize(new Dimension(50, 20));
//        stacleRadSpinner.setValue(Configs._stacleRadMM);
//
//        JLabel numBlocksLabel = new JLabel("Number of blocks =");
//        JSpinner numBlocksSpinner = new JSpinner();
//        numBlocksSpinner.setPreferredSize(new Dimension(50, 20));
//        numBlocksSpinner.setValue(Configs._nBlocksInExperiment);
//
//        JButton setConfigButton = new JButton("SET CONFIGURATION");
//        setConfigButton.setPreferredSize(new Dimension(100, 40));
//        setConfigButton.addActionListener(e -> {
//            // Save all the configs -------
//            Configs._targetRadiiMM = new ArrayList<>(Utils.intValues(radiiField.getText(), ","));
//            Configs._distancesMM = new ArrayList<>(Utils.intValues(distField.getText(), ","));
//
//            if (swipeRBtn.isSelected()) Configs._technique = Configs.TECH.SWIPE_LCLICK;
//            if (tapRBtn.isSelected()) Configs._technique = Configs.TECH.TAP_LCLICK;
//            Configs._vibrate = vibChBox.isSelected();
//
//            Configs._stacleRadMM = (int) stacleRadSpinner.getValue();
//            Configs._stacleRad = Utils.mm2px(Configs._stacleRadMM);
//            Configs._nBlocksInExperiment = (int) numBlocksSpinner.getValue();
//
//            // Close the dialog
//            setVisible(false);
//            dispose();
//
//        });
//
//        // Fill the Panel --------------------------------
//        JPanel panel = new JPanel();
//        GridBagLayout layout = new GridBagLayout();
//        panel.setLayout(layout);
//
//        int pnlMargin = 20;
//        int vSepMargin = 10;
//        int hSepMargin = 5;
//        int leftX = 0;
//
//        int yRow = 0;
//        addComponent(panel, radiiLabel,
//                leftX, yRow,
//                1, GridBagConstraints.HORIZONTAL,
//                pnlMargin, hSepMargin, pnlMargin, vSepMargin);
//        addComponent(panel, radiiField,
//                leftX + 1, yRow,
//                2, GridBagConstraints.HORIZONTAL,
//                0, hSepMargin, pnlMargin, vSepMargin);
//        addComponent(panel, radiiInfo,
//                leftX + 3, yRow,
//                1, GridBagConstraints.HORIZONTAL,
//                0, pnlMargin, pnlMargin, vSepMargin);
//
//        yRow++;
//        addComponent(panel, distLabel,
//                leftX, yRow,
//                1, GridBagConstraints.HORIZONTAL,
//                pnlMargin, hSepMargin, 0, vSepMargin);
//        addComponent(panel, distField,
//                leftX + 1, yRow,
//                2, GridBagConstraints.HORIZONTAL,
//                0, hSepMargin, 0, vSepMargin);
//        addComponent(panel, distInfo,
//                leftX + 3, yRow,
//                2, GridBagConstraints.HORIZONTAL,
//                0, pnlMargin, 0, vSepMargin);
//
//        yRow++;
//        addComponent(panel, sep,
//                leftX, yRow,
//                4, GridBagConstraints.HORIZONTAL,
//                pnlMargin, pnlMargin, 5, vSepMargin);
//
//        yRow++;
//        addComponent(panel, actLabel,
//                leftX, yRow,
//                1, GridBagConstraints.HORIZONTAL,
//                pnlMargin, 0, 0, vSepMargin);
//        addComponent(panel, tapRBtn,
//                leftX + 1, yRow,
//                1, GridBagConstraints.WEST,
//                0, 0, 0, vSepMargin);
//        addComponent(panel, swipeRBtn,
//                leftX + 2, yRow,
//                1, GridBagConstraints.WEST,
//                10, 0, 0, vSepMargin);
//
//        yRow++;
//        addComponent(panel, vibChBox,
//                leftX + 1, yRow,
//                1, GridBagConstraints.HORIZONTAL,
//                10, 0, 0, pnlMargin);
//
//        yRow++;
//        addComponent(panel, sep,
//                leftX, yRow,
//                4, GridBagConstraints.HORIZONTAL,
//                pnlMargin, pnlMargin, 0, 0);
//
//        yRow++;
//        addComponent(panel, stacleRadLabel,
//                leftX, yRow,
//                1, GridBagConstraints.HORIZONTAL,
//                pnlMargin, hSepMargin, vSepMargin, vSepMargin);
//        addComponent(panel, stacleRadSpinner,
//                leftX + 1, yRow,
//                1, GridBagConstraints.WEST,
//                0, hSepMargin, vSepMargin, vSepMargin);
//
//        yRow++;
//        addComponent(panel, numBlocksLabel,
//                leftX, yRow,
//                1, GridBagConstraints.HORIZONTAL,
//                pnlMargin, hSepMargin, 0, vSepMargin);
//        addComponent(panel, numBlocksSpinner,
//                leftX + 1, yRow,
//                1, GridBagConstraints.WEST,
//                0, hSepMargin, 0, vSepMargin);
//
//        yRow++;
//        addComponent(panel, setConfigButton,
//                leftX, yRow,
//                4, GridBagConstraints.HORIZONTAL,
//                pnlMargin, pnlMargin, vSepMargin, vSepMargin);
//
//        // Add the panel to the frame
//        add(panel);

    }

    /**
     * Add components to a panel
     * @param panel JPanel
     * @param cmp Component
     * @param gbX GridX
     * @param gbY GridY
     * @param gbW GridWidth
     * @param gbFill Fill (GridBagConstraints.HORIZENTAL, ...)
     * @param leftMrg Left margin
     * @param rightMrg Right margin
     * @param topMrg Top margin
     * @param botMrg Botton margin
     */
    private void addComponent(JPanel panel, Component cmp,
                              int gbX, int gbY,
                              int gbW, int gbFill,
                              int leftMrg, int rightMrg, int topMrg, int botMrg) {

        GridBagConstraints gbConstraints = new GridBagConstraints();

        gbConstraints.gridx = gbX;
        gbConstraints.gridy = gbY;
        gbConstraints.gridwidth = gbW;
        gbConstraints.fill = gbFill;
        gbConstraints.insets = new Insets(topMrg, leftMrg, botMrg, rightMrg);

        panel.add(cmp, gbConstraints);
    }

}
