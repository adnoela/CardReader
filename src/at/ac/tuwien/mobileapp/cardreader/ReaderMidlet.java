package at.ac.tuwien.mobileapp.cardreader;

import at.ac.tuwien.mobileapp.bluetooth.BluetoothHost;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

public class ReaderMidlet extends MIDlet {

    /**
     * To determine the first application start.
     */
    private final String ENC = "UTF8";
    private boolean initialized = false;
    private ReaderForm readerUi;
    private Display display;
    private BluetoothHost bluetooth;
    private CardReader reader;

    /**
     * Constructor of the MIDlet. Initializes the UI.
     */
    public ReaderMidlet() {
    }

    /**
     * Called when the midlet is entering the active state. On the very first
     * start-up, this registers listening for NDEF tags.
     */
    public void startApp() {
        display = Display.getDisplay(this);
        if (!initialized) {
            readerUi = new ReaderForm(this);
            readerUi.init();
        }
        display.setCurrent(readerUi);

        // start up bluetooth host and wait for incoming connections
        bluetooth = new BluetoothHost("cardreader", readerUi);
        
        // start card reader
        reader = new CardReader(readerUi);

        while (true) {
            try {
                byte[] b = new byte[4];
                bluetooth.read(b);
                String str = new String(b, ENC);
                
                // distinguish commands
                if (str.startsWith("HELL")) {
                    cmdHello();
                } else if (str.startsWith("CDPR")) {
                    cmdIsCardPresent();
                } else if (str.startsWith("EXIT")) {
                    cmdExit();
                } else {
                    readerUi.showState(str);
                    // nerdy hex-output:
                    // readerUi.showState(BluetoothHost.bytesToHex(b));
                }
            } catch (IOException ex) {
                readerUi.showState(ex.getMessage());
            }
        }
    }

    // handles HELL event
    private void cmdHello() throws UnsupportedEncodingException, IOException {
        bluetooth.write("ACK HELL".getBytes(ENC));
        readerUi.showState("Processed HELL event");
    }

    // handles CDPR event
    private void cmdIsCardPresent() throws UnsupportedEncodingException, IOException {
        bluetooth.write("ACK CDPR".getBytes(ENC));
        if(reader.isCardPresent()) {
            bluetooth.write("TRUE".getBytes(ENC));
        } else{
            bluetooth.write("FALS".getBytes(ENC));
        }
        readerUi.showState("Processed CDPR event");
    }

    // handles EXIT event
    private void cmdExit() throws UnsupportedEncodingException, IOException {
        bluetooth.write("ACK EXIT".getBytes("UTF8"));
        readerUi.showState("Processed EXIT event");
        bluetooth.close();
        exitApp();
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
