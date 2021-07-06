package envi.tools;

import envi.experiment.Experimenter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UtilsTest {

    @org.junit.jupiter.api.Test
    void mm2px() {
    }

    @org.junit.jupiter.api.Test
    void px2mm() {
    }

    @org.junit.jupiter.api.Test
    void intValues() {
    }

    @org.junit.jupiter.api.Test
    void interactionString() {
    }

    @org.junit.jupiter.api.Test
    void dispToWin() {
    }

    @org.junit.jupiter.api.Test
    void lastPart() {
    }

    @org.junit.jupiter.api.Test
    void now() {
    }

    @org.junit.jupiter.api.Test
    void randInt() {
//        for (int i = 0; i < 10; i++) {
//            System.out.println(Utils.randInt(0, 2));
//        }
    }

    @org.junit.jupiter.api.Test
    void randPerm() {
    }

    @org.junit.jupiter.api.Test
    void playSound() {
//        Utils.playSound("err2.wav");
    }

    @org.junit.jupiter.api.Test
    void time() {
//        System.out.println(Utils.nowDateTime());
//        System.out.println(Experimenter.PHASE.EXPERIMENT.ordinal());
//        System.out.println(Utils.double3Dec(8.120));
//        List<String> list = new ArrayList<>();
//        list.add("First");
//        list.add("Second");
//        list.add("Third");
//        list.add("Fourth");
//        list.add("Fifth");
//        list.add("Sixth");
//        list.add("Seventh");
//        list.add("Eightth");
//
//        int num = 8; // number of the element to reshuffle
//
//        int index = num - 1;
//
//        if (num > 0 && num < list.size()) {
//            System.out.println(list.get(index));
//            String str = list.get(index);
//            int newInd = Utils.randInt(num + 1, list.size() + 1);
//            System.out.println((num + 1) + " to " + list.size());
//            list.add(list.size(), str);
//        } else if (num == list.size()){
//            System.out.println(list.get(index));
//            String str = list.get(index);
//            list.add(num, str);
//        }
//
//
//        System.out.println(list);
//        System.out.println(list.get(++index));

        Path parentPath = Paths.get("").toAbsolutePath().getParent();
        String path = parentPath.toAbsolutePath() + "/Test/";
        try {
            PrintWriter testPW = new PrintWriter(new FileWriter(path + "/test.txt", true));
            testPW.println("Testing...");
            testPW.flush();
            testPW.close();

            testPW = new PrintWriter(new FileWriter(path + "/test.txt", true));
            testPW.println("Again...");
            testPW.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
