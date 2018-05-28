package com.endava.service_system.utils;

import com.endava.service_system.model.entities.BankKey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Component
public class EncryptionUtils {

    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;
    private KeyFactory keyFactory;
    private KeyPairGenerator keyPairGenerator;
    private Cipher encrypter;
    private Cipher decrypter;
    private ObjectMapper objectMapper;

    @Autowired
    public void setKeyFactory(KeyFactory keyFactory) {
        this.keyFactory = keyFactory;
    }

    @Autowired
    public void setKeyPairGenerator(KeyPairGenerator keyPairGenerator) {
        this.keyPairGenerator = keyPairGenerator;
    }

    @Autowired
    public void setEncrypter(@Qualifier("encrypter") Cipher encrypter) {
        this.encrypter = encrypter;
    }

    @Autowired
    public void setDecrypter(@Qualifier("decrypter") Cipher decrypter) {
        this.decrypter = decrypter;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper){
        this.objectMapper=objectMapper;
    }

    public void init(byte[] bankModulus, byte[] privateKey) throws InvalidKeySpecException, InvalidKeyException {
        if(bankModulus!=null)
        initEncrypter(bankModulus);
        if(privateKey!=null) {
            initDecrypter(privateKey);
        }
    }

    public void init(BankKey bankKey) throws InvalidKeySpecException, InvalidKeyException {
        init(bankKey.getBankModulus(),bankKey.getJavaPrivateKey());
    }

    public byte[] encryptBytes(byte[] data) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        encrypter.init(Cipher.ENCRYPT_MODE,publicKey);
        return encrypter.doFinal(data);
    }

    public byte[] decryptBytes(byte[] data) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        decrypter.init(Cipher.PRIVATE_KEY,privateKey);
        byte[] newArr=new byte[data.length-1];
        for(int i=0;i<newArr.length;i++){
            newArr[i]=data[i+1];
        }
        byte[] decoded=decrypter.doFinal(newArr);
        return decoded;
    }

    /**
     * This string should be encoded with my public key then encoded with base64 ;
     * This string is a json
     */
    public Object decryptData(String encodedString,Class classObject) throws BadPaddingException, IllegalBlockSizeException, IOException, InvalidKeyException {
        byte[] dataEncoded=Base64.getDecoder().decode(new String(encodedString.getBytes(),"UTF-8"));
        byte[] decoded=decryptBytes(dataEncoded);
        String realJson=new String(decoded);
        return objectMapper.readValue(realJson,classObject);
    }

    public String encryptData(Object data) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        String json=objectMapper.writeValueAsString(data);
        byte[] encoded=encryptBytes(json.getBytes());
        return Base64.getEncoder().encodeToString(encoded);
    }

    public String getModulus(RSAPublicKey publicKey){
        return Base64.getEncoder().encodeToString(publicKey.getModulus().toByteArray());
    }

    private void initDecrypter(byte[] privateKeyBytes) throws InvalidKeySpecException, InvalidKeyException {
        PKCS8EncodedKeySpec rsaPrivKey = new PKCS8EncodedKeySpec(privateKeyBytes);
        privateKey=(RSAPrivateKey) keyFactory.generatePrivate(rsaPrivKey);
    }

    private void initEncrypter(byte[] modulusBase64) throws InvalidKeySpecException, InvalidKeyException {
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(1,modulusBase64), new BigInteger(Base64.getDecoder().decode("AQAB")));
        publicKey= (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
    }

    public KeyPair generateKeyPair(){
        return keyPairGenerator.generateKeyPair();
    }
}
