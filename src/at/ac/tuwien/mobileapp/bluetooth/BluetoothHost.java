package at.ac.tuwien.mobileapp.bluetooth;

import at.ac.tuwien.mobileapp.cardreader.InfoInterface;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/**
 * @author Sebastian Haas
 */
public class BluetoothHost implements SerialConnection {

    private static final UUID RFCOMM_UUID = new UUID(0x0003);
    private LocalDevice localDevice;
    private InfoInterface infoInterface;
    private InputStream inputStream;
    private OutputStream outputStream;
    private StreamConnectionNotifier service;
    private StreamConnection con;

    public BluetoothHost(String serviceName, InfoInterface infoInterface) {
        this.infoInterface = infoInterface;

        try {
            localDevice = LocalDevice.getLocalDevice();
            localDevice.setDiscoverable(DiscoveryAgent.GIAC);

            String url = "btspp://localhost:" + RFCOMM_UUID + ";name=" + serviceName;
            service = (StreamConnectionNotifier) Connector.open(url);

            infoInterface.showState("Setup completed. Waiting for incoming connection...");
            con = service.acceptAndOpen();
            infoInterface.showState("Connection established.");
            inputStream = con.openDataInputStream();
            outputStream = con.openOutputStream();
        } catch (IOException ex) {
            infoInterface.showState(ex.getMessage());
        }
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public void close() {
        try {
            infoInterface.showState("Cleaning up ressources...");
            inputStream.close();
            outputStream.close();
            con.close();
            service.close();
        } catch (IOException ex) {
            infoInterface.showState(ex.getMessage());
        }
    }

    public int read(byte[] b) throws IOException {
        if (inputStream != null) {
            return inputStream.read(b);
        } else {
            throw new IOException("Input stream closed or not yet ready.");
        }
    }

    public void write(byte[] b) throws IOException {
        if (outputStream != null) {
            outputStream.write(b);
            outputStream.flush();
        } else {
         throw new IOException("Output stream closed or not yet ready.");
        }
    }
}
