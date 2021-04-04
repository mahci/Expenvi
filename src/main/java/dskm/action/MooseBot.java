package dskm.action;

import dskm.Constants;
import dskm.gui.ExperimentPanel;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.awt.*;
import java.awt.event.InputEvent;

public class MooseBot {

    private String TAG = "MooseBot";

    private static MooseBot self; // for singleton

    private Robot bot;

    /**
     * Constructor
     */
    public MooseBot() throws AWTException {
        bot = new Robot();
    }

    /**
     * Start observing actions from the PublishSubject and performing them
     * @param actionSubject PublishSubject of actions (from Constants)
     */
    public void startBot(PublishSubject<String> actionSubject) {
        // Subscribe to the actionSubject to get the actions
        actionSubject.subscribe(action -> {
//            System.out.println(TAG + " <- " + action);
            switch (action) {
                case Actions.ACT_CLICK:
                    click();
                    break;
                case Actions.ACT_PRESS_PRI:
                    press(InputEvent.BUTTON1_DOWN_MASK);
                    break;
                case Actions.ACT_RELEASE_PRI:
                    release(InputEvent.BUTTON1_DOWN_MASK);
                    break;
                case Actions.ACT_PRESS_SEC:
                    press(InputEvent.BUTTON2_DOWN_MASK);
                    break;
                case Actions.ACT_RELEASE_SEC:
                    release(InputEvent.BUTTON2_DOWN_MASK);
                    break;
            }
        });
    }

    /***
     * Singleton
     * @return Singleton instance
     * @throws AWTException
     */
    public static MooseBot get() {
        try {
            if (self == null) self = new MooseBot();
        } catch (AWTException e) {
            System.out.println(e);
        }

        return self;
    }

    /**
     * Simulate clicking
     * @throws AWTException
     */
    public void click() throws AWTException {
//        System.out.println("Bot click");
        Point cursorPos = MouseInfo.getPointerInfo().getLocation();
        bot.mouseMove(cursorPos.x, cursorPos.y);
        bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    /**
     * Simulate mouse button press
     * @param msBtnNum Number of mouse button (InputEvent constants)
     * @throws AWTException
     */
    public void press(int msBtnNum) throws AWTException {
//        System.out.println("Bot press");
//        Point cursorPos = MouseInfo.getPointerInfo().getLocation();
//        bot.mouseMove(cursorPos.x, cursorPos.y);
//        bot.mousePress(msBtnNum);
        if (msBtnNum == InputEvent.BUTTON1_DOWN_MASK) {
        }
    }

    /**
     * Simulate mouse button release
     * @param msBtnNum Number of mouse button (InputEvent constants)
     * @throws AWTException
     */
    public void release(int msBtnNum) throws AWTException {
//        System.out.println(TAG + " release");
        Point cursorPos = MouseInfo.getPointerInfo().getLocation();
        bot.mouseMove(cursorPos.x, cursorPos.y);
        bot.mouseRelease(msBtnNum);
    }

}
