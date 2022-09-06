package com.marsh.sqlmateapi.utils;

import com.alibaba.fastjson.JSONObject;
import com.marsh.sqlmateapi.service.dto.TeamJoinDto;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class CryptString {

    private Cipher encryptCipher = null;
    private Cipher decryptCipher = null;

    /**
     * Construct a new object which can be utilized to encrypt
     * and decrypt strings using the specified key
     * with a DES encryption algorithm.
     *
     * @param key The secret key used in the crypto operations.
     * @throws Exception If an error occurs.
     *
     */
    public CryptString(SecretKey key) throws Exception {
        encryptCipher = Cipher.getInstance("DES");
        decryptCipher = Cipher.getInstance("DES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }    

    /**
     * Encrypt a string using DES encryption, and return the encrypted
     * string as a base64 encoded string.
     * @param unencryptedString The string to encrypt.
     * @return String The DES encrypted and base 64 encoded string.
     * @throws Exception If an error occurs.
     */
    public String encryptBase64 (String unencryptedString) throws Exception {
        // Encode the string into bytes using utf-8
        byte[] unencryptedByteArray = unencryptedString.getBytes("UTF8");

        // Encrypt
        byte[] encryptedBytes = encryptCipher.doFinal(unencryptedByteArray);

        // Encode bytes to base64 to get a string
        byte [] encodedBytes = Base64.encodeBase64(encryptedBytes);

        return new String(encodedBytes);
    }

    /**
     * Decrypt a base64 encoded, DES encrypted string and return
     * the unencrypted string.
     * @param encryptedString The base64 encoded string to decrypt.
     * @return String The decrypted string.
     * @throws Exception If an error occurs.
     */
    public String decryptBase64 (String encryptedString) throws Exception {
        // Encode bytes to base64 to get a string
        byte [] decodedBytes = Base64.decodeBase64(encryptedString.getBytes());

        // Decrypt
        byte[] unencryptedByteArray = decryptCipher.doFinal(decodedBytes);

        // Decode using utf-8
        return new String(unencryptedByteArray, "UTF8");
    }    

    /**
     * Main unit test method.
     * @param args Command line arguments.
     *
     */
    public static void main(String args[]) {
        try {
            //Generate the secret key
            String password = "7e6659eb-b45b-4e28-957b-e346164112b8";
            DESKeySpec key = new DESKeySpec(password.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

            //Instantiate the encrypter/decrypter
            CryptString crypt = new CryptString(keyFactory.generateSecret(key));
            String unencryptedString =   JSONObject.toJSONString(TeamJoinDto.builder()
                    .teamId(1)
                    .userId(2)
                    .time(System.currentTimeMillis())
                    .build());;
            String encryptedString = crypt.encryptBase64(unencryptedString);
            // Encrypted String:8dKft9vkZ4I=
            System.out.println("Encrypted String:"+encryptedString);

            //Decrypt the string
            unencryptedString = crypt.decryptBase64(encryptedString);
            // UnEncrypted String:Message
            System.out.println("UnEncrypted String:"+unencryptedString);

        } catch (Exception e) {
            System.err.println("Error:"+e.toString());
        }
    }
}