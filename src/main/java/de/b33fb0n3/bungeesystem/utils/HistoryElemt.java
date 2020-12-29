package de.b33fb0n3.bungeesystem.utils;

import java.util.UUID;

/**
 * Plugin made by B33fb0n3YT
 * 29.12.2020
 * F*CKING SKIDDER!
 * Licensed by B33fb0n3YT
 * © All rights reserved
 */

public class HistoryElemt {

    private UUID targetUUID;
    private UUID vonUUID;
    private String type;
    private String grund;
    private long erstellt;
    private long bis;
    private int perma;
    private int ban;

    public HistoryElemt(UUID targetUUID, UUID vonUUID, String type, String grund, long erstellt, long bis, int perma, int ban) {
        this.targetUUID = targetUUID;
        this.vonUUID = vonUUID;
        this.type = type;
        this.grund = grund;
        this.erstellt = erstellt;
        this.bis = bis;
        this.perma = perma;
        this.ban = ban;
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }

    public UUID getVonUUID() {
        return vonUUID;
    }

    public String getType() {
        return type;
    }

    public String getGrund() {
        return grund;
    }

    public long getErstellt() {
        return erstellt;
    }

    public long getBis() {
        return bis;
    }

    public int getPerma() {
        return perma;
    }

    public int getBan() {
        return ban;
    }
}
