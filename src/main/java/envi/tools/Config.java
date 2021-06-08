package envi.tools;

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
    public enum TECH {
        SWIPE,
        TAP,
        MOUSE
    }
//    public static TECH _technique = TECH.MOUSE;
    public static boolean _vibrate = false; // Vibrate?

    public static List<TECH[]> _techOrderList = new ArrayList<>();
    public static TECH[] _techOrder;

    // Participant number
    public static int _participNum = 1;

    // Showcase ====================================================================
    public static int _minTarRadMM = 5; // Minimum traget radius (mm)
    public static int _dispHRatioToMaxRad = 6; // Maximum target radius = dispH / this (for random)

    // ===============================================================================
    //region [Methods]

    static {
        // Setting the techniques orders
        _techOrderList.add(new TECH[] {TECH.TAP, TECH.MOUSE, TECH.SWIPE});
        _techOrderList.add(new TECH[] {TECH.SWIPE, TECH.TAP, TECH.MOUSE});
        _techOrderList.add(new TECH[] {TECH.MOUSE, TECH.SWIPE, TECH.TAP});
        _techOrderList.add(new TECH[] {TECH.TAP, TECH.SWIPE, TECH.MOUSE});
        _techOrderList.add(new TECH[] {TECH.MOUSE, TECH.TAP, TECH.SWIPE});
        _techOrderList.add(new TECH[] {TECH.SWIPE, TECH.MOUSE, TECH.TAP});

        // Get the order for the participant
        _techOrder = _techOrderList.get(_participNum % 6);
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
            System.out.println(TAG + "_targetRadiiMM = " + _widthsMM);
            System.out.println(TAG + "_distancesMM = " + _distancesMM);
//            System.out.println(TAG + "_TECHNIQUE = " + _technique);
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
