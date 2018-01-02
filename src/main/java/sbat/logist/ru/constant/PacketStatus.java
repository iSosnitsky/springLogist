package sbat.logist.ru.constant;

import lombok.Getter;

public enum PacketStatus {
    OK("OK"),
    ERROR("ERROR");

    @Getter
    private final String packetStatus;

    PacketStatus(String packetStatus) {
        this.packetStatus = packetStatus;
    }
}