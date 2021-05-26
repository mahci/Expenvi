package envi.tools;

import envi.connection.MooseServer;
import envi.gui.MainFrame;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Config {

    private static final String TAG = "[[Config]] ";
    private static final boolean toLog = false;
    // -------------------------------------------------------------------------------
    // Network
    public static int _netPort = 8000;

    // Config file path
    public static final String CONFIG_FILE_PATH = "config.txt";

    // Colors
    public static Color _normalTextColor        = Color.BLACK;
    public static Color _errorTextColor         = Color.RED;
    public static Color _titleTextColor         = Color.DARK_GRAY;

    public static Color _stacleDefColor         = Color.decode("#3dcf38");
    public static Color _stacleClickedColor     = Color.decode("#3d6e3b");
    public static Color _stacleTextColor        = Color.black;

    public static Color _tarcleDefColor         = Color.decode("#e05c2f");


    // Display
    public static final int BENQ_DPI = 90;
    public static final int MacCinDisp_DPI = 109;
    public static final int _dpi = BENQ_DPI;

    public static int _nScr = 1; // Set programmatically
    public static int _scrId = 1; // Used in Main
    public static Rectangle _scrDims; // Screen dimenstions (px)

    public static int WIN_W_MARGIN = 70; // Width margin (mm)
    public static int WIN_H_MARGIN = 30; // Width margin (mm)
    public static int _winWidthMargin = Utils.mm2px(WIN_W_MARGIN);   // Left/right margin (px)
    public static int _winHeightMargin = Utils.mm2px(WIN_H_MARGIN);   // Top bottom margin (px)

    public static int _dispAreaH, _dispAreaW; // px

    // Text -----------------------------------------------
    public static int TEXT_X = 55; // (mm) From the right edge
    public static int TEXT_Y = 10; // (mm) From the top
    public static int ERROR_Y = 50; // (X is calculated dynamically from middle of the screen)


    // Experiment ==================================================================
    public static int _stacleRadMM = 8; // Stacle radius (mm)
    public static int _stacleRad; // Stacle radius (px) Set programmatically
    // Target radii (mm)
    public static List<Integer> _targetRadiiMM = new ArrayList<Integer>();
    // Distances (mm)
    public static List<Integer> _distancesMM = new ArrayList<Integer>();

    public static int _nBlocksInExperiment = 2; // Number of blocks in an experiment
    // The gesture for clicks
    public enum TECHNIQUE {
        SWIPE_LCLICK,
        TAP_LCLICK,
        MOUSE_LCLICK
    }
    public static TECHNIQUE _technique = TECHNIQUE.MOUSE_LCLICK;
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



    // ===============================================================================
    //region [Methods]

    /**
     * Read and set the config from file
     */
    public static void setFromFile() {
        try {
            Scanner fileScan = new Scanner(new File(CONFIG_FILE_PATH));

            //===== Read display values

            //===== Read network values
//            fileScan.nextLine(); // skip title
//
//            _netPort = Integer.parseInt(Utils.lastPart(fileScan.nextLine()));

            //===== Read color values
//            fileScan.nextLine(); // skip title
//
//            _normalTextColor = Color.decode(Utils.lastPart(fileScan.nextLine()));
//            _errorTextColor = Color.decode(Utils.lastPart(fileScan.nextLine()));
//            _titleTextColor = Color.decode(Utils.lastPart(fileScan.nextLine()));
//            _stacleDefColor = Color.decode(Utils.lastPart(fileScan.nextLine()));
//            _stacleClickedColor = Color.decode(Utils.lastPart(fileScan.nextLine()));
//            _stacleTextColor = Color.decode(Utils.lastPart(fileScan.nextLine()));
//            _tarcleDefColor = Color.decode(Utils.lastPart(fileScan.nextLine()));
//            if (toLog) System.out.println(TAG + "Colors set!");
            //===== Read show case values
            fileScan.nextLine(); // skip title

            _minTarRadMM = Integer.parseInt(Utils.lastPart(fileScan.nextLine()));
            _dispHRatioToMaxRad = Integer.parseInt(Utils.lastPart(fileScan.nextLine()));
            if (toLog) System.out.println(TAG + "Show case config set!");
            //===== Read experiment values
            fileScan.nextLine(); // skip title

            // Stacle radius
            int stRad = Integer.parseInt(Utils.lastPart(fileScan.nextLine()));
            _stacleRadMM = stRad;

            if (toLog) System.out.println(TAG + "Start R (mm) = " + _stacleRadMM);
            _nBlocksInExperiment = Integer.parseInt(Utils.lastPart(fileScan.nextLine()));

            // Target radii
            _targetRadiiMM = Arrays.stream(Utils.lastPart(fileScan.nextLine())
                    .split(","))
                    .map(String::trim)
                    .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

            // Target distances
            if (toLog) System.out.println(TAG + "Target Rs = " + _targetRadiiMM);

            _distancesMM = Arrays.stream(Utils.lastPart(fileScan.nextLine())
                    .split(","))
                    .map(String::trim)
                    .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());


            // Technique
            try {
                _technique = TECHNIQUE.valueOf(Utils.lastPart(fileScan.nextLine()));
            } catch (IllegalArgumentException iaException) {
                _technique = TECHNIQUE.TAP_LCLICK;
            }

            _vibrate = Boolean.parseBoolean(Utils.lastPart(fileScan.nextLine()));

            // Send the technique to the Moose
            MooseServer.get().sendMssg(Strs.MSSG_TECHNIQUE + "-" + _technique);

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
            System.out.println(TAG + "_TECHNIQUE = " + _technique);
            System.out.println(TAG + "_vibrate = " + _vibrate);
            System.out.println(TAG + "_scrId = " + _scrId);
            System.out.println(TAG + "_dpi = " + _dpi);
            System.out.println(TAG + "_winWMargin = " + _winWidthMargin);
            System.out.println(TAG + "_winHMargin = " + _winHeightMargin);
            System.out.println(TAG + "_netPort = " + _netPort);
        }

    }

    public static void updateDisplayValues() {
        _dispAreaW = MainFrame.get().getWidth() - 2 * _winWidthMargin;
        _dispAreaH = MainFrame.get().getHeight() - 2 * _winHeightMargin;
        if (toLog) System.out.println(TAG + "winW = " + Utils.px2mm(MainFrame.get().getWidth()));
        if (toLog) System.out.println(TAG + "_dispAreaW = " + _dispAreaW);
        if (toLog) System.out.println(TAG + "_dispAreaH = " + _dispAreaH);
        int maxRadMM = Utils.px2mm(_dispAreaH / 4);

        _stacleRad = Utils.mm2px(_stacleRadMM);

        int maxDist = Utils.px2mm(_dispAreaW) - Collections.max(_targetRadiiMM) - _stacleRadMM;
    }

    //endregion
}
