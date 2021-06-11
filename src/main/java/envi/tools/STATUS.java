package envi.tools;

import envi.gui.MainFrame;

public enum STATUS {
    ERR_PARTICIP_DIR,
    ERR_PHASE_DIR,
    ERR_BLOCK_FILES,
    CANCELLED,
    SUCCESS;

    public void check() {
        switch (this) {
        case ERR_PARTICIP_DIR -> MainFrame.get()
            .showMessageDialog("Problem in logging the participant!");
        case ERR_PHASE_DIR -> MainFrame.get()
            .showMessageDialog("Problem in logging this phase!");
        }
    }

}
