package envi.tools;

import envi.gui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

public class Config {

    private static final String TAG = "[[Config]] ";
    private static final boolean toLog = false;

    // Config file path
    public static final String CONFIG_FILE_PATH = "config.txt";

    // Colors
    public static Color _normalTextColor        = Color.BLUE;
    public static Color _errorTextColor         = Color.RED;
    public static Color _titleTextColor         = Color.DARK_GRAY;
    public static Color _starcleDefColor        = Color.decode("#3dcf38");
    public static Color _starcleClickedColor    = Color.decode("#3d6e3b");
    public static Color _starcleTextColor       = Color.black;
    public static Color _tarcleDefColor         = Color.decode("#e05c2f");

    // Display
    public static final int BENQ_DPI = 90;
    public static final int MacCinDisp_DPI = 109;

    public static int _dpi = MacCinDisp_DPI;

    public static int _nScr = 1; // Set programmatically
    public static int _scrId = 1; // Used in Main
    public static Rectangle _scrDims; // Screen dimenstions (px)

    public static int _winWidthMargin = 100;   // Left/right margin (px)
    public static int _winHeightMargin = 50;   // Top bottom margin (px)

    public static int _dispAreaH, _dispAreaW; // px

    // Text -----------------------------------------------
    public static int TEXT_X = 100; // From the right edge
    public static int TEXT_Y = 50; // From the top
    public static int ERROR_Y = 50; // X is calculated dynamically (from middle of the screen)
    public static String FONT_STYLE = "Sans-serif";
    public static int EXP_INFO_FONT_SIZE = 14;
    public static Font EXP_INFO_FONT = new Font(FONT_STYLE, Font.PLAIN, EXP_INFO_FONT_SIZE);
    public static Font S_FONT = new Font(FONT_STYLE, Font.PLAIN, EXP_INFO_FONT_SIZE);

    // Messages --------------------------------------------
    public static final String MSSG_MOOSE       = "MOOSE";
    public static final String MSSG_CONFIRM     = "CONFIRM";
    public static final String MSSG_PID         = "PID";
    public static final String MSSG_BEG_EXP     = "BEGEXP";
    public static final String MSSG_END_EXP     = "ENDEXP";
    public static final String MSSG_BEG_BLK     = "BEGBLK";
    public static final String MSSG_END_BLK     = "ENDBLK";
    public static final String MSSG_END_TRL     = "ENDTRL";
    public static final String MSSG_BEGIN_LOG   = "BEGLOG";
    public static final String MSSG_END_LOG     = "ENDLOG";
    public static final String MSSG_ACK         = "ACK";
    // -----------------------------------------------------

    // Network ---------------------------------------------
    public static int _netPort = 8000;
    public static final String NET_DISCONNECT   = "DISCONNECT";
    // -----------------------------------------------------

    // Experiment ==================================================================
    public static int _stacleRadMM = 8; // Stacle radius (mm)
    public static int _stacleRad; // Stacle radius (px) Set programmatically
    // Target radii (mm)
    public static List<Integer> _targetRadiiMM = new ArrayList<Integer>() {{
       add(5);
       add(7);
       add(9);
    }};
    // Distances (mm)
    public static List<Integer> _distancesMM = new ArrayList<Integer>() {{
        add(20);
        add(40);
        add(60);
        add(80);
    }};

    public static int _nBlocksInExperiment = 2; // Number of blocks in an experiment
    // The gesture for clicks
    public static enum INTERACTION {
        SWIPE_LCLICK,
        TAP_LCLICK,
        MOUSE_LCLICK
    }
    public static INTERACTION _interaction = INTERACTION.MOUSE_LCLICK;
    public static boolean _vibrate = false; // Vibrate?

    // --- Show Case
//    public static int _practiceTime = 10; // Practice time (min)
    public static int _minTarRadMM = 5; // Minimum traget radius (mm)
    public static int _dispHRatioToMaxRad = 6; // Maximum target radius = dispH / this (for random)
    public static enum PROCESS_STATE {
        SHOW_CASE,
        WARM_UP,
        EXPERIMENT
    }

    // --- ERRORS and TEXTs
    public static final String ERR_NOT_INSIDE   =
            "PLEASE CLICK INSIDE THE START CIRCLE";
    public static final String DIMSSG_BLOCK_FINISH =
            "Block finished! You can now take a break. Press OK when ready.";

    // Logs


    // Methods ================================================================
    public static void readConfigFromFile() {
        try {
            Scanner fileScan = new Scanner(new File(CONFIG_FILE_PATH));

            //===== Read display values
            fileScan.nextLine(); // skip title

            _scrId = Integer.parseInt(Utils.lastPart(fileScan.nextLine()));
            _dpi = Integer.parseInt(Utils.lastPart(fileScan.nextLine()));
            _winWidthMargin = Integer.parseInt(Utils.lastPart(fileScan.nextLine()));
            _winHeightMargin = Integer.parseInt(Utils.lastPart(fileScan.nextLine()));

            // Additional info
            _dispAreaW = MainFrame.get().getWidth() - 2 * _winWidthMargin;
            _dispAreaH = MainFrame.get().getHeight() - 2 * _winHeightMargin;
            if (toLog) System.out.println(TAG + "winW = " + MainFrame.get().getWidth());
            if (toLog) System.out.println(TAG + "_dispAreaH = " + _dispAreaH);
            int maxRadMM = Utils.px2mm(_dispAreaH / 2);
            if (toLog) System.out.println(TAG + "maxRadMM = " + maxRadMM);
            //===== Read network values
            fileScan.nextLine(); // skip title

            _netPort = Integer.parseInt(Utils.lastPart(fileScan.nextLine()));

            //===== Read color values
            fileScan.nextLine(); // skip title

            _normalTextColor = Color.decode(Utils.lastPart(fileScan.nextLine()));
            _errorTextColor = Color.decode(Utils.lastPart(fileScan.nextLine()));
            _titleTextColor = Color.decode(Utils.lastPart(fileScan.nextLine()));
            _starcleDefColor = Color.decode(Utils.lastPart(fileScan.nextLine()));
            _starcleClickedColor = Color.decode(Utils.lastPart(fileScan.nextLine()));
            _starcleTextColor = Color.decode(Utils.lastPart(fileScan.nextLine()));
            _tarcleDefColor = Color.decode(Utils.lastPart(fileScan.nextLine()));

            //===== Read show case values
            fileScan.nextLine(); // skip title

            _minTarRadMM = Integer.parseInt(Utils.lastPart(fileScan.nextLine()));
            _dispHRatioToMaxRad = Integer.parseInt(Utils.lastPart(fileScan.nextLine()));

            //===== Read experiment values
            fileScan.nextLine(); // skip title

            // Stacle radius
            int radius = Integer.parseInt(Utils.lastPart(fileScan.nextLine()));
            if (radius >= maxRadMM) {
                MainFrame.get().showMessageDialog(
                        "Start radius can't be more than " + maxRadMM + " mm");
            } else {
                _stacleRadMM = radius;
            }
            _nBlocksInExperiment = Integer.parseInt(Utils.lastPart(fileScan.nextLine()));

            // Target radii
            Scanner scanner = new Scanner(Utils.lastPart(fileScan.nextLine()));
            while (scanner.hasNextInt()) {
                radius = scanner.nextInt();
                // Radius should not be less than half the height display area
                if (radius >= maxRadMM) {
                    MainFrame.get().showMessageDialog(
                            "Target radius can't be more than " + maxRadMM + " mm");
                } else {
                    _targetRadiiMM.add(radius);
                }
            }

            // Target distances
            int maxDist = Utils.px2mm(_dispAreaW) - Collections.max(_targetRadiiMM) - _stacleRadMM;
            scanner = new Scanner(Utils.lastPart(fileScan.nextLine()));
            while (scanner.hasNextInt()) {
                int dist = scanner.nextInt();
                if (dist >= maxDist) {
                    MainFrame.get().showMessageDialog(
                            "Distance can't be more than " + maxDist + " mm");
                } else {
                    _distancesMM.add(dist);
                }

            }
            // Next...
            _interaction = INTERACTION.valueOf(Utils.lastPart(fileScan.nextLine()));
            _vibrate = Boolean.parseBoolean(Utils.lastPart(fileScan.nextLine()));


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Print the config
        if (toLog) {
            System.out.println(TAG + "_minTarRadMM = " + _minTarRadMM);
            System.out.println(TAG + "_dispHRatioToMaxRad = " + _dispHRatioToMaxRad);
            System.out.println(TAG + "_stacleRadMM = " + _stacleRadMM);
            System.out.println(TAG + "_nBlocksInExperiment = " + _nBlocksInExperiment);
            System.out.println(TAG + "_targetRadiiMM = " + _targetRadiiMM);
            System.out.println(TAG + "_distancesMM = " + _distancesMM);
            System.out.println(TAG + "_interaction = " + _interaction);
            System.out.println(TAG + "_vibrate = " + _vibrate);
            System.out.println(TAG + "_scrId = " + _scrId);
            System.out.println(TAG + "_dpi = " + _dpi);
            System.out.println(TAG + "_winWMargin = " + _winWidthMargin);
            System.out.println(TAG + "_winHMargin = " + _winHeightMargin);
            System.out.println(TAG + "_netPort = " + _netPort);
        }

    }


}