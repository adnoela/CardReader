package at.ac.tuwien.mobileapp.cardreader;

import java.io.IOException;

import javax.microedition.contactless.ContactlessException;
import javax.microedition.contactless.DiscoveryManager;
import javax.microedition.contactless.TargetListener;
import javax.microedition.contactless.TargetProperties;
import javax.microedition.contactless.TargetType;
import javax.microedition.contactless.TransactionListener;
import javax.microedition.contactless.sc.ISO14443Connection;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.AlertType;

public class CardReader implements ReaderInterface, TargetListener, TransactionListener {

    private InfoInterface callback;
    private DiscoveryManager dm;
    private ISO14443Connection isocon = null;

    public CardReader(InfoInterface callback) {
        this.callback = callback;
        //activateReader();
    }

    public boolean open() {
        try {
            if (dm == null) {
                DiscoveryManager dm = DiscoveryManager.getInstance();
                dm.addTargetListener(this, TargetType.ISO14443_CARD);
                dm.addTransactionListener(this);
                callback.showState("active");
                return true;
            } else {
                return false;
            }
        } catch (ContactlessException ce) {
            callback.displayAlert("Error registering for ISO14443 CARDs targets", "NDEF Tag type not supported", AlertType.ERROR);
            return false;
        }
    }

    public void close() {
        deleteNfcInstances(true);
    }

    private void deleteNfcInstances(boolean alsoRemoveDiscoveryManager) {
        if (isocon != null) {
            try {
                isocon.close();
                isocon = null;
            } catch (IOException ex) {
                callback.displayAlert("IOException during close", ex.toString(), AlertType.ERROR);
            }
        }
        if (alsoRemoveDiscoveryManager && dm != null) {
            callback.showState("closing cardreader...");
            dm.removeTargetListener(this, TargetType.ISO14443_CARD);
            dm.removeTransactionListener(this);
            dm = null;
        }
    }

    public void targetDetected(TargetProperties[] properties) {
        // Check Properties
        if (properties.length == 0) {
            callback.displayAlert("Target detected", "No target properties available", AlertType.WARNING);
            return;
        }
        callback.showState("Card detected!");
        // Close old Connections
        deleteNfcInstances(false);
        // Get new Connection
        //TargetProperties target = properties[0];
        //isocon = getConnection(target);
        isocon = getConnection(properties);
    }

    /**
     * gets iso14443 connection
     *
     * @param prop
     * @return connection if found, otherwise null
     */
    private ISO14443Connection getConnection(TargetProperties[] prop) {
        callback.showState("connecting to card");
        for (int i = 0; i < prop.length; i++) {
            if (prop[i].hasTargetType(TargetType.ISO14443_CARD)) {
                Class[] classes = prop[i].getConnectionNames();
                for (int j = 0; j < classes.length; j++) {
                    try {
                        if (classes[j].equals(Class.forName("javax.microedition.contactless.sc.ISO14443Connection")));
                        String url = prop[i].getUrl(classes[j]);
                        return (ISO14443Connection) Connector.open(url);
                    } catch (ClassNotFoundException e) {
                        callback.displayAlert("ClassNotFoundException", e.getMessage(), AlertType.WARNING);
                    } catch (IOException e) {
                        callback.displayAlert("IOException", e.getMessage(), AlertType.WARNING);
                    }
                }
            }
        }
        return null;
    }

    public boolean isCardPresent() {
        if (isocon != null) {
            return true;
        } else {
            callback.cardError("No active ISO14443 Connection found.");
            return false;
        }
    }

    public byte[] sendAPDUCommand(byte[] command) {
        if (isCardPresent()) {
            try {
                callback.showState("exchanging data...");
                byte[] response = isocon.exchangeData(command);
                return response;
            } catch (IOException e) {
                callback.displayAlert("IOException during APDU Command", e.getMessage(), AlertType.ERROR);
            } catch (ContactlessException e) {
                callback.displayAlert("ContactlessException during APDU Command", e.getMessage(), AlertType.ERROR);
            }
        }
        return null;
    }

    public void externalReaderDetected(byte arg0) {
        // do nothing
    }

    public byte[] getATR() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getName() {
        return System.getProperty("microedition.platform");
    }
}
