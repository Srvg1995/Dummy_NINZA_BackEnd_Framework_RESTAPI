package com.ninza.hrm.api.genericutility;

import java.util.Base64;

/*private key: Ac03tEam@j!tu_#1*/

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
//These below codes are copied from a mail sent by Tushar(Actually these codes displayed in the video&sir told need not to try/use this)

public class Encryption {

	public String encrypt(String input,String secretKey) throws Exception{

		SecretKeySpec secretKeySpec=new SecretKeySpec(secretKey.getBytes(),"AES");

		IvParameterSpec ivParameterSpec = new IvParameterSpec("4234567890123456".getBytes());

		Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");

		cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec,ivParameterSpec);

		byte[] encryted=cipher.doFinal(input.getBytes());

		return Base64.getEncoder().encodeToString(encryted);



	}

	public String decrypt(String input,String secretKey) throws Exception{

		SecretKeySpec secretKeySpec=new SecretKeySpec(secretKey.getBytes(),"AES");

		IvParameterSpec ivParameterSpec = new IvParameterSpec("4234567890123456".getBytes());

		Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");

		cipher.init(Cipher.DECRYPT_MODE,secretKeySpec,ivParameterSpec);

		byte[] decrypted=cipher.doFinal(Base64.getDecoder().decode(input));

		return new String(decrypted);

	}



}

