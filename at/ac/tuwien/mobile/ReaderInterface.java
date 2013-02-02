package mobile;

public interface ReaderInterface {

	
	//open(), close(), transeive(), getName(), isCardPresent()
	
	/**
	 * starts the reader, which is now waiting for cards to read
	 */
	public void open();
	
	/**
	 * closes every open connection from the reader and
	 * the applicatiion as well
	 */
	public void close();
	
	/**
	 * @return  true - if there is an open connection to a valid card
	 * 	    false - no or invalid card found
	 */
	public boolean isCardPresent();
	
	/**
	 * renamed "transeive()" function from instructions
	 * @param command byte array containing a apdu command
	 * @return response from card
	 */
	public byte[] sendAPDUCommand(byte[] command);
	
	/**
	 * @return name of the card reader
	 */
	public String getName();
	
	
	
}
