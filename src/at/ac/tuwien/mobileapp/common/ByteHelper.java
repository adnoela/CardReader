package at.ac.tuwien.mobileapp.common;

/**
 *
 * @author Sebastian Haas
 */
public class ByteHelper {

    public static String arrayToHexString(byte[] data, boolean showSpaces) {
        String sb = new String();

        for (int i = 0; i < data.length; i++) {
            String bs = Integer.toHexString(data[i] & 0xFF);
            if (bs.length() == 1) {
                sb += 0;
            }
            if (showSpaces) {
                sb += bs + " ";
            } else {
                sb += bs;
            }
        }
        return sb.toString();
    }

    public static byte[] intToByteArray(int value) {
        return new byte[]{
                    (byte) (value >> 24),
                    (byte) (value >> 16),
                    (byte) (value >> 8),
                    (byte) value};
    }

    public static int intFromByteArray(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }
}
