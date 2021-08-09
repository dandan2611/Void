package net.gameinbox.voidserver.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;

public class EncryptionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionManager.class);

    private PublicKey publicKey;
    private PrivateKey privateKey;

    public void init() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

            keyPairGenerator.initialize(1024);
            KeyPair keyPair = keyPairGenerator.genKeyPair();

            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();

            LOGGER.info("Generated new keypair");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

}
