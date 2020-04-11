package com.uolinc.marvelapp.util

import android.util.Log
import com.uolinc.marvelapp.util.Keys.apiKey
import com.uolinc.marvelapp.util.Keys.privateKey

import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Classe responsável para setar e configurar os dados de acordo com a documentação da API
 */
object Tools {

    /**
     * Pega string de milisegundos
     *
     * @return milisegundos
     */
    val ts: String
        get() = System.currentTimeMillis().toString()

    /**
     * Pega hash Md5
     *
     * @param tS string de milisegundos
     * @return hash Md5
     */
    fun getHash(tS: String): String = md5(tS + privateKey + apiKey)

    /**
     * Gera hash Md5
     *
     * @param s Dados para gerar hash Md5
     * @return hash Md5
     */
    private fun md5(s: String): String {
        return try {
            val md = MessageDigest.getInstance("MD5")
            val messageDigest = md.digest(s.toByteArray())
            val number = BigInteger(1, messageDigest)
            number.toString(16)
        } catch (e: NoSuchAlgorithmException) {
            Log.e("MD5", e.localizedMessage!!)
            ""
        }
    }
}