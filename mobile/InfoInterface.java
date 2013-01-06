package mobile;

import javax.microedition.lcdui.AlertType;

/**
 * Callback interface for Nfc Classes to interact with the UI / controller.
 */
public interface InfoInterface {
    /**
     * Callback when reading / writing to a tag was
     * not successful.
     * @param text Text to show in a message box if desired.
     */
    public void cardError(final String text);

    /**
     * Callback when writing to a tag was successful.
     * @param text Text to show in a message box if desired.
     */
    public void cardSuccess(final String text);
    
    /**
     * Utility function to show a Java ME alert, as used for informing the user
     * about events in this app.
     * @param title title text to use for the message box.
     * @param text text to show as the main message in the box.
     * @param type one of the available alert types, defining the icon, sound
     * and display length.
     */
    public void displayAlert(final String title, final String text, final AlertType type);
    
    /**
     * Log information about a tag in textual form.
     * @param text Text that contains information about the tag.
     */
    public void logCardInfo(final String text);

    public void showState(String state);
}
