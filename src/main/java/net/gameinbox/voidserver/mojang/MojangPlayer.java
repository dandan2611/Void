package net.gameinbox.voidserver.mojang;

import java.util.HashMap;

public class MojangPlayer {

    private final String identifier;
    private final String name;

    private final HashMap<String, Property> properties;

    public MojangPlayer(String identifier, String name, HashMap<String, Property> properties) {
        this.identifier = identifier;
        this.name = name;
        this.properties = properties;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Property> getProperties() {
        return properties;
    }

    public static class Property {

        private final String name;
        private final String value;
        private final String signature;

        public Property(String name, String value, String signature) {
            this.name = name;
            this.value = value;
            this.signature = signature;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public String getSignature() {
            return signature;
        }

    }

}
