package mobile;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

public class ReaderMidlet extends MIDlet {

    /** To determine the first app start. */
    private boolean initialized = false;
	private ReaderForm readerUi;
	private Display display;

    /**
     * Constructor of the MIDlet. Initializes the UI.
     */
    public ReaderMidlet() {
    	display = Display.getDisplay(this);
    }

    /**
     * Called when the midlet is entering the active state.
     * On the very first start-up, this registers listening for NDEF tags.
     */
    public void startApp() {
        if (!initialized) {
            readerUi = new ReaderForm(this);
            readerUi.init();
        }
        display.setCurrent(readerUi);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        readerUi.shutdown();
    }
    
    public void exitApp() {
        destroyApp(false);
        notifyDestroyed();
    }
}
