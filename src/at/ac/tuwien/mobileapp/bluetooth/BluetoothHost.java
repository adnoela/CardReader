package at.ac.tuwien.mobileapp.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.TextBox;
import javax.microedition.midlet.*;

/**
 * @author Sebastian Haas
 */
public class BluetoothHost extends MIDlet {

    private static final UUID RFCOMM_UUID = new UUID(0x0003);
    private LocalDevice localDevice;
    private Display display;
    private TextBox textbox;
    private InputStream inputStream;
    private OutputStream outputStream;

    public void startApp() {
        display = Display.getDisplay(this);
        textbox = new TextBox("USBTest", "", 8000, 0);
        display.setCurrent(textbox);

        try {
            localDevice = LocalDevice.getLocalDevice();
            localDevice.setDiscoverable(DiscoveryAgent.GIAC);
            
            String url = "btspp://localhost:" + RFCOMM_UUID + ";name=cardreader";
            StreamConnectionNotifier service = (StreamConnectionNotifier) Connector.open(url);

            trace("Setup completed. Waiting for incoming connection...");
            StreamConnection con = service.acceptAndOpen();
            trace("Connection established.");
            inputStream = con.openDataInputStream();
            outputStream = con.openOutputStream();

            byte[] b = new byte[100];
            inputStream.read(b);
            trace(b.toString());

            inputStream.close();
            outputStream.close();
            con.close();
            service.close();
        } catch (IOException ex) {
            trace(ex.getMessage());
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        try {
            trace("Cleaning up ressources...");
            inputStream.close();
            outputStream.close();
        } catch (IOException ex) {
            trace(ex.getMessage());
        }
    }

    public void trace(String s) {
        textbox.setString(textbox.getString().concat("\n" + s));
    }
}
