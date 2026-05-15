package com.guatex.igconfiguraciones.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 *
 * @author ESTEFANIECM
 */
public class CifradoPassword {

    private  final String ALGORITHM = "AES";
    private  final String SECRET_KEY = "0123456789abcdef"; 

    // Encriptar el texto
    public  String encrypt(String textoEncriptar) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedBytes = cipher.doFinal(textoEncriptar.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Desencriptar el texto
    public String decrypt(String textoEncriptado) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decodedBytes = Base64.getDecoder().decode(textoEncriptado);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }
}
