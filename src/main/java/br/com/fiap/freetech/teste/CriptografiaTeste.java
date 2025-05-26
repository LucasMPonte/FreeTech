package br.com.fiap.freetech.teste;

import br.com.fiap.freetech.utils.CriptografiaUtils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class CriptografiaTeste {
    public static void main(String[] args) {
        try{
            System.out.println(CriptografiaUtils.criptografar("1234567"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
