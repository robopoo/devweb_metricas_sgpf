package br.com.sgpf.metrica.core.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class AES {

	private static Cipher cipher;

	private static KeyGenerator kgen;

	static {

		try {
			cipher = Cipher.getInstance("AES");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (NoSuchPaddingException e1) {
			e1.printStackTrace();
		}

		try {
			kgen = KeyGenerator.getInstance("AES");
			kgen.init(128);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

	}

	private static SecretKey getSecretKey() {
		String pass = "pdcases-positivo";
		char[] pw = new char[pass.length()];
		for (int k = 0; k < pass.length(); ++k) {
			pw[k] = pass.charAt(k);
		}
		SecretKeyFactory factory;
		try {
			factory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			KeySpec spec = new PBEKeySpec(pw, "salt".getBytes(), 1024, 256);
			return factory.generateSecret(spec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static SecretKeySpec getSecretKeySpec() {
		SecretKey skey = getSecretKey();
		byte[] raw = skey.getEncoded();
		return new SecretKeySpec(raw, "AES");
	}

	/**
	 * Cifra a senha
	 * 
	 * @param password
	 * @return
	 */
	public static String encrypt(String password) {

		SecretKeySpec skeySpec = getSecretKeySpec();

		try {
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] encrypted = cipher.doFinal(password.getBytes());
			return convert(encrypted);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * Decifra a senha
	 * 
	 * @param password
	 * @return
	 */
	public static String decrypt(String password) {

		SecretKeySpec skeySpec = getSecretKeySpec();

		try {
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] encrypted = cipher.doFinal(convert(password));
			return new String(encrypted);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * Define o formato de exibicao
	 * 
	 * @param input
	 * @return
	 */
	private static String convert(byte[] input) {
		return Hex.encodeHexString(input);
	}

	/**
	 * Converte o formato de exibicao para decifrar a senha
	 * 
	 * @param input
	 * @return
	 */
	private static byte[] convert(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			e.printStackTrace();
		}
		return null;
	}

}