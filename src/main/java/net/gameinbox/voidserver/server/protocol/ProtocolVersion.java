package net.gameinbox.voidserver.server.protocol;

public enum ProtocolVersion {

    V1_17_1(756);

    final int protocol;

    ProtocolVersion(int protocol) {
        this.protocol = protocol;
    }

    public int getProtocol() {
        return protocol;
    }

    public static ProtocolVersion getByProtocol(int protocol) {
        ProtocolVersion protocolVersion = null;
        for(ProtocolVersion pv : values())
            if(protocol == pv.protocol)
                protocolVersion = pv;
        return protocolVersion;
    }

}
