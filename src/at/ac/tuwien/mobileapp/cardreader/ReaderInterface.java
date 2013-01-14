package at.ac.tuwien.mobileapp.cardreader;

public interface ReaderInterface {

	
	//open(), close(), getATR, transeive(), getName(), isCardPresent()
	
	/**
	 * starts the reader, which is now waiting for cards to read
	 */
	public boolean open();
	
	/**
	 * closes every open connection from the reader and itself as well 
	 */
	public boolean close();
	
	/**
	 * @return  true - if there is an open connection to a valid card
	 *          false - no or invalid card found
	 */
	public boolean isCardPresent();
	
	/**
	 * renamed "transeive()" function from instructions
	 * @param command byte array containing a valid apdu command
	 * @return response from card to apdu sent
	 */
	public byte[] sendAPDUCommand(byte[] command);
	
	/**
	 * @return name of the card reader
	 */
	public String getName();
	
	/**
	 * @return tbd...
	 */
	public byte[] getATR();	

	
	
}
