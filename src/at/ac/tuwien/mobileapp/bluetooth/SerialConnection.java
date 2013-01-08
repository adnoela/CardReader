package at.ac.tuwien.mobileapp.bluetooth;

import java.io.IOException;

/**
 *
 * @author Sebastian Haas
 */
public interface SerialConnection {

    public int read(byte[] b) throws IOException;

    public void write(byte[] b) throws IOException;
}
