package mobile;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

public class ReaderForm extends Form implements CommandListener, InfoInterface, Runnable {
	 
	/** Command to exit the app. */
    private Command exitCommand;
	
	private ReaderMidlet readerMidlet;
	private CardReader cardReader;

	public ReaderForm(ReaderMidlet midlet) {
		super("SmartCard Reader");
		this.readerMidlet = midlet;
	}
	
	public void init() {
        // Initialize the class in an own thread
        Thread t = new Thread(this);
        t.start();
    }
    
    public void run() {
        privateInit();
    }
    
    private void privateInit() {

        this.setCommandListener(this);
        
        exitCommand = new Command("Exit", Command.EXIT, 1);
        this.addCommand(exitCommand);
        
        cardReader = new CardReader(this);
        
        createMainUi();
        
    }
    
    private void createMainUi() {
        showState("waiting for pc...");
    }
    
    public void showState(String state){
    	this.append("\n\nCurrent state:\n");
    	this.append(state);
    }
    
    public void shutdown() {
    	if (cardReader != null) {
        	cardReader.close();
    	}        
    }

	public void commandAction(Command command, Displayable displayable) {
		if (command == exitCommand) {
            // Exit the application
            readerMidlet.exitApp();
        }
	}

	public void cardSuccess(String text) {
		displayAlert(this.getTitle(), text, AlertType.CONFIRMATION);
	}
	
	public void cardError(String text) {
		displayAlert(this.getTitle(), text, AlertType.ERROR);
	}
	
	public void displayAlert(String title, String text, AlertType type) {
		Alert al = new Alert(title, text, null, type);
        Display.getDisplay(readerMidlet).setCurrent(al, this);
	}

	public void logCardInfo(String text) {
		showState(text);
	}
	
}
