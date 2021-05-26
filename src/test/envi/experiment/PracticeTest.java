package envi.experiment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PracticeTest {

    @Test
    void testPractice() {
        Practice pr = new Practice(1, 3);
        Assertions.assertTrue(pr.hasNext(0));
    }

}