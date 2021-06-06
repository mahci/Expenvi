package envi.tools;

import envi.connection.MooseServer;

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

    // Display
    public static final int BENQ_DPI = 90;
    public static final int MacCinDisp_DPI = 109;
    public static final int _dpi = BENQ_DPI;

    public static int _nScr = 1; // Set programmatically
    public static int _scrId = 1; // Used in Main
    public static Rectangle _scrDims; // Screen dimenstions (px)

    // Experiment ==================================================================
    public static int _stacleRadMM = 8; // Stacle radius (mm)
    public static int _stacleRad; // Stacle radius (px) Set programmatically

    public static List<Integer> _widthsMM = new ArrayList<Integer>(); // Target radii (mm)
    public static List<Integer> _distancesMM = new ArrayList<Integer>(); // Distances (mm)

    public static int _nBlocksInExperiment = 2; // Number of blocks in an experiment

    // The technique for clicks
    public enum TECHNIQUE {
        SWIPE_LCLICK,
        TAP_LCLICK,
        MOUSE_LCLICK
    }
    public static TECHNIQUE _technique = TECHNIQUE.MOUSE_LCLICK;
    public static boolean _vibrate = false; // Vibrate?

    public static List<int[]> _techOrderList = new ArrayList<>();

    // Participant number
    public static int _participNum = 1;

    // Showcase ====================================================================
    public static int _minTarRadMM = 5; // Minimum traget radius (mm)
    public static int _dispHRatioToMaxRad = 6; // Maximum target radius = dispH / this (for random)
    public static enum PROCESS_STATE {
        SHOW_CASE,
        WARM_UP,
        EXPERIMENT
    }

    // ===============================================================================
    //region [Methods]

    static {
        // Setting the techniques orders
        _techOrderList.add(new int[] {1, 2, 0});
        _techOrderList.add(new int[] {0, 1, 2});
        _techOrderList.add(new int[] {2, 0, 1});
        _techOrderList.add(new int[] {1, 0, 2});
        _techOrderList.add(new int[] {2, 1, 0});
        _techOrderList.add(new int[] {0, 2, 1});
    }

    /**
     * Read and set the config from file
     */
    public static void setFromFile() {
        try {
            Scanner fileScan = new Scanner(new File(CONFIG_FILE_PATH));

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
            _widthsMM = Arrays.stream(Utils.lastPart(fileScan.nextLine())
                    .split(","))
                    .map(String::trim)
                    .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

            // Target distances
            if (toLog) System.out.println(TAG + "Target Rs = " + _widthsMM);

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
            System.out.println(TAG + "_targetRadiiMM = " + _widthsMM);
            System.out.println(TAG + "_distancesMM = " + _distancesMM);
            System.out.println(TAG + "_TECHNIQUE = " + _technique);
            System.out.println(TAG + "_vibrate = " + _vibrate);
            System.out.println(TAG + "_scrId = " + _scrId);
            System.out.println(TAG + "_dpi = " + _dpi);
            System.out.println(TAG + "_netPort = " + _netPort);
        }

    }

//    public static void updateDisplayValues() {

//        int maxRadMM = Utils.px2mm(_dispAreaH / 4);

//        _stacleRad = Utils.mm2px(_stacleRadMM);

//        int maxDist = Utils.px2mm(_dispAreaW) - Collections.max(_targetRadiiMM) - _stacleRadMM;
//    }

    //endregion
}
