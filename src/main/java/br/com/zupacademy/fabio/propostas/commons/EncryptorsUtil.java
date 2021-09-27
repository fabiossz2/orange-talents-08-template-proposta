package br.com.zupacademy.fabio.propostas.commons;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

public class EncryptorsUtil {

    private static final String SALT = KeyGenerators.string().generateKey();
    private static TextEncryptor textEncryptor = Encryptors.queryableText("documento", SALT);

    public static String encripta(String text) {
        return textEncryptor.encrypt(text);
    }

    public static String decripta(String text) {
        return textEncryptor.decrypt(text);
    }
}
