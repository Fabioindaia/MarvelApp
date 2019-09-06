package com.uolinc.marvelapp.util;

import android.util.Log;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe responsável para setar e configurar os dados de acordo com a documentação da API
 */
public class Tools {
    private static final String PRIVATE_KEY = "8bf03c0132a99e81383dab2298061c82e86c0d94";
    private static final String PUBLIC_KEY = "f4c8f084c2f5a73f09ae3c2ecff2be9f";

    /**
     * Pega a chave pública
     *
     * @return retorna chave pública
     */
    public static String getApiKey(){
        return PUBLIC_KEY;
    }

    /**
     * Pega string de milisegundos
     *
     * @return milisegundos
     */
    public static String getTs(){
        return Long.toString(System.currentTimeMillis());
    }

    /**
     * Pega hash Md5
     *
     * @param tS string de milisegundos
     * @return hash Md5
     */
    public static String getHash(String tS){
        return md5(tS + PRIVATE_KEY + PUBLIC_KEY);
    }

    /**
     * Gera hash Md5
     *
     * @param s Dados para gerar hash Md5
     * @return hash Md5
     */
    private static String md5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(s.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            return number.toString(16);
        } catch (NoSuchAlgorithmException e) {
            Log.e("MD5", e.getLocalizedMessage());
            return null;
        }
    }
}