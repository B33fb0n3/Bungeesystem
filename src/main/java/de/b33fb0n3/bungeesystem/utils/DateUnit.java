package de.b33fb0n3.bungeesystem.utils;

/** Plugin made by B33fb0n3YT
30.03.2019
F*CKING SKIDDER!
Licensed by B33fb0n3
© All rights reserved
*/

public enum DateUnit {

    MIN(60),
    HOUR(60*60),
    DAY(24*60*60),
    WEEK(7*24*60*60),
    MON(30*24*60*60),
    YEAR(365*24*60*60);

    private long toSec;

    private DateUnit(long toSec) {
        this.toSec = toSec;
    }

    public long getToSec() {
        return toSec;
    }
}
