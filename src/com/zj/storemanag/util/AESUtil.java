package com.zj.storemanag.util;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;



public class AESUtil
{
    private static final String Encoding = "GB2312";
    private static final String AES = "AES";
    
    public static String decrypt(String strSrcString)
            throws Exception
        {
    		String key = "edobnet";
            if(null == strSrcString || strSrcString.trim().length() < 1)
            {
                return null;
            }
            
            key = generateKey(key);
            byte[] raw = key.getBytes(Encoding);
            SecretKeySpec skeySpec = new SecretKeySpec(raw,AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE,skeySpec);
            byte[] encrypted1 = Base64.decode(strSrcString);// new BASE64Decoder().decodeBuffer(strSrcString);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString;
        } 

        public static String encrypt(String strSrcString)
        {
        	try
        	{
        		String key = "edobnet";
    	        if(null == strSrcString || strSrcString.trim().length() < 1)
    	        {
//    	            return null;
    	        	strSrcString = "";
    	        }
    	        key = generateKey(key);
    	        byte[] raw = key.getBytes(Encoding);
    	        SecretKeySpec skeySpec = new SecretKeySpec(raw,AES);
    	        Cipher cipher = Cipher.getInstance(AES);
    	        cipher.init(Cipher.ENCRYPT_MODE,skeySpec);
    	        byte[] encrypted = cipher.doFinal(strSrcString.getBytes());
    	        return Base64.encode(encrypted);
        	}
        	catch(Exception ex){
        		return "";
        	}
           
        } 

//    public static String decrypt(String strSrcString,String strPassword)
//        throws Exception
//    {
//        if(null == strSrcString || strSrcString.trim().length() < 1)
//        {
//            return null;
//        }
//        strPassword = generateKey(strPassword);
//        byte[] raw = strPassword.getBytes(Encoding);
//        SecretKeySpec skeySpec = new SecretKeySpec(raw,AES);
//        Cipher cipher = Cipher.getInstance(AES);
//        cipher.init(Cipher.DECRYPT_MODE,skeySpec);
//        byte[] encrypted1 = Base64.decode(strSrcString);// new BASE64Decoder().decodeBuffer(strSrcString);
//        byte[] original = cipher.doFinal(encrypted1);
//        String originalString = new String(original);
//        return originalString;
//    } 
//
//    public static String encrypt(String strSrcString,String strPassword)
//    {
//    	try
//    	{
//	        if(null == strSrcString || strSrcString.trim().length() < 1)
//	        {
//	            return null;
//	        }
//	        strPassword = generateKey(strPassword);
//	        byte[] raw = strPassword.getBytes(Encoding);
//	        SecretKeySpec skeySpec = new SecretKeySpec(raw,AES);
//	        Cipher cipher = Cipher.getInstance(AES);
//	        cipher.init(Cipher.ENCRYPT_MODE,skeySpec);
//	        byte[] encrypted = cipher.doFinal(strSrcString.getBytes());
//	        return Base64.encode(encrypted);
//    	}
//    	catch(Exception ex){
//    		return "";
//    	}
//       
//    } 

    private static String generateKey(String str)
        throws NoSuchAlgorithmException,UnsupportedEncodingException
    {
        if(null == str)
        {
            str = "defaultpassword";
        }
        else if(str.length() < 1)
        {
            str = "emptypassword";
        }
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        md.update(str.getBytes(Encoding));
        String strret = Base64.encode(md.digest());//(new sun.misc.BASE64Encoder()).encode(md.digest());
        while(strret.length() < 16)
        {
            strret += "%";
        }
        if(strret.length() > 16)
        {
            int nbegin = (strret.length() - 16) / 2;
            strret = strret.substring(nbegin,nbegin + 16);
        }
        return strret;
    } 
    /*
     * 解压
     */
    public static String DeCodeCompress(String data){
    	return ZipUtil.decompress(Base64.decode(data));
    }

//     public static void main(String[] args)
//  throws Exception
//{ 
//
////  String strMessage = "中文输入法";
////  String strPassword = "a";
////  String desc = encrypt(strMessage,strPassword);
////  System.out.println(desc);
////  System.out.println(decrypt(desc,strPassword)); 
////
////  System.out.println(generateKey(""));
////  System.out.println(generateKey(""));
////  System.out.println(generateKey("a"));
////  System.out.println(generateKey("a"));
////  System.out.println(generateKey("A"));
////  System.out.println(generateKey("asdasdsadsadasdsadasdsad"));
////  System.out.println(generateKey("asdasdsadsadasdsadasdsad"));
//} 

}
