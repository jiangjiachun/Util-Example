package com.jjc.hex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 十六进制数据转换
 * @author jjc
 * 2020-12-15
 */
public class HexConversion {
    
    private static final Logger logger = LoggerFactory.getLogger(HexConversion.class);
    
    /**
     * @Title:bytes2HexString 
     * @Description: 16 byte array to a hexadecimal string 
     * @param b Byte array @return
     * Hexadecimal strings @throws
     */
    public static String bytes2HexString(byte[] b) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            result.append(String.format("%02X", b[i]));
        }
        return result.toString();
    }

    /**
     * @Title:hexString2Bytes 
     * @Description: 16 hexadecimal string byte array transfer 
     * @param src Hexadecimal
     * string @return Byte array @throws
     */
    public static byte[] hexString2Bytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }

    /**
     * @Title:string2HexUTF8 
     * @Description: Character string rotation hexadecimal character string UTF8 
     * @param strPart
     * String @return Hexadecimal strings @throws
     */
    public static String string2HexUTF8(String strPart) {
        return string2HexString(strPart, "UTF-8");
    }

    /**
     * @Title:string2HexUnicode 
     * @Description: Unicode character hexadecimal string string 16 rpm 
     * @param strPart
     * String @return Hexadecimal strings @throws
     */
    public static String string2HexUnicode(String strPart) {
        return string2HexString(strPart, "Unicode");
    }

    /**
     * @Title:string2HexGBK 
     * @Description: GBK character string of 16 hexadecimal string rotation 
     * @param strPart
     * String @return Hexadecimal strings @throws
     */
    public static String string2HexGBK(String strPart) {
        return string2HexString(strPart, "GBK");
    }

    /**
     * @Title:string2HexString 
     * @Description: String turn hex string 
     * @param strPart string 
     * @param tochartype hex coding
     * target @return Hexadecimal strings @throws
     */
    public static String string2HexString(String strPart, String tochartype) {
        try {
            return bytes2HexString(strPart.getBytes(tochartype));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @Title:hexUTF82String 
     * @Description: 16 hex UTF-8 strings to String 
     * @param src Hexadecimal string @return Byte
     * array @throws
     */
    public static String hexUTF82String(String src) {
        return hexString2String(src, "UTF-8", "UTF-8");
    }

    /**
     * @Title:hexGBK2String 
     * @Description: GBK hexadecimal string to String 
     * @param src Hexadecimal string @return Byte
     * array @throws
     */
    public static String hexGBK2String(String src) {
        return hexString2String(src, "GBK", "UTF-8");
    }

    /**
     * @Title:hexUnicode2String 
     * @Description: Hexadecimal Unicode strings to String 
     * @param src Hexadecimal
     * string @return Byte array @throws
     */
    public static String hexUnicode2String(String src) {
        return hexString2String(src, "Unicode", "UTF-8");
    }

    /**
     * @Title:hexString2String 
     * @Description: Hexadecimal string to String 
     * @param src Hexadecimal string 
     * @return Byte
     * array @throws
     */
    public static String hexString2String(String src, String oldchartype, String chartype) {
        byte[] bts = hexString2Bytes(src);
        try {
            if (oldchartype.equals(chartype))
                return new String(bts, oldchartype);
            else
                return new String(new String(bts, oldchartype).getBytes(), chartype);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    public static void main(String[] args) {
        String hexStr = "7c:ca:a1:bb:fa:d2:aa:be:d6:b6:af:bb:b7:cf:b5:cd:b3:7c:b5:e7:bb:b0:7c:ca:c2:bc:fe:a3:ba:31:46:5f:b5:e7:c1:a6:bb:fa:b7:bf:31:23:55:50:53:20:d5:fb:c1:f7:c6:f7:d4:cb:d0:d0:d7:b4:cc:ac:a3:ba:cd:a3:d6:b9:20:b7:a2:cb:cd:ca:a7:b0:dc:7c:33:7c:37:7c:32:30:32:30:2d:31:32:2d:31:34:20:30:36:3a:31:36:3a:30:32:7c:37:34:30:44:35:46:44:39:2d:30:36:36:46:2d:34:45:30:44:2d:42:44:45:30:2d:43:44:37:31:34:34:33:42:42:41:33:41:7c:53:30:2d:45:32:30:31:7c";
        String str = hexGBK2String(hexStr.replace(":", ""));
        logger.info("十六进制转换结果={}", str);
        
    }

}
