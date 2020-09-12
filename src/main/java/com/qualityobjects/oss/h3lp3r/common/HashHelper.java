package com.qualityobjects.oss.h3lp3r.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


public class HashHelper {
	
	private static final Logger LOG = LoggerFactory.getLogger(HashHelper.class);

	/**
	 * Genera el hash del string pasado por parámetro con el algoritmo SHA-256.
	 * 
	 * @param text
	 * @return código hash en hexadecimal o null si hay una excepción.
	 */
	public static String hashSHA256(CharSequence text) throws NoSuchAlgorithmException {
		return hash("SHA-256", text);
	}

	/**
	 * Genera el hash del string pasado por parámetro con el algoritmo MD5
	 * 
	 * @param text
	 * @return código hash en hexadecimal o null si hay una excepción.
	 */
	public static String hashMD5(String text) throws NoSuchAlgorithmException {
		return hash("MD5", text);
	}


	/**
	 * Genera el hash del string pasado por parámetro con el algoritmo pasado por parámetro
	 * 
	 * @param text
	 * @return código hash en hexadecimal o null si hay una excepción.
	 */
	public static String hash(String alg, CharSequence text) throws NoSuchAlgorithmException {
		if (text == null) {
			return null;
		}
		MessageDigest digest = MessageDigest.getInstance(alg);
		return bytesToHex(digest.digest(text.toString().getBytes(StandardCharsets.UTF_8)));
	}

	/**
	 * Convierte un arry binario como byte[] en una representación hexadecimal en un String.
	 * 
	 * @param hash
	 * @return
	 */
	public static String bytesToHex(byte[] hash) {
	    StringBuffer hexString = new StringBuffer();
	    for (int i = 0; i < hash.length; i++) {
	    String hex = Integer.toHexString(0xff & hash[i]);
	    if(hex.length() == 1) hexString.append('0');
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}
}
