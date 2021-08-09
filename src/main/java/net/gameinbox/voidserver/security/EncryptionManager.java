package net.gameinbox.voidserver.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;

public class EncryptionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionManager.class);

    public void init() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            
            keyPairGenerator.initialize(1024);
            KeyPair keyPair = keyPairGenerator.genKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            LOGGER.info("Generated new keypair");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
