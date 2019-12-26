package util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Aneureka
 * @createdAt 2019-12-25 16:44
 * @description
 **/
public class CodecUtil {

    public static int bytesToInteger(byte[] bytes) {
        try {
            return Integer.parseInt(new String(bytes));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("failed to parse to integer");
        }
    }

    public static int bytesToInteger(List<Byte> bytes) {
        return bytesToInteger(byteListToArray(bytes));
    }

    public static byte[] integerToBytes(int num) {
        return String.valueOf(num).getBytes();
    }

    public static byte[] byteListToArray(List<Byte> byteList) {
        byte[] bytes = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            bytes[i] = byteList.get(i);
        }
        return bytes;
    }

    public static String bytesToString(List<Byte> byteList) {
        return bytesToString(byteList, StandardCharsets.UTF_8);
    }

    public static String bytesToString(List<Byte> byteList, Charset charset) {
        byte[] bytes = byteListToArray(byteList);
        return new String(bytes, charset);
    }
}
