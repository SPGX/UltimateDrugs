// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.utils;

public class TimeUtils
{
    public static String convertSecondsToFormattedTime(long lng) {
        final int round = Math.round((float)(lng / 3600L));
        lng -= round * 3600;
        final int round2 = Math.round((float)(lng / 60L));
        lng -= round2 * 60;
        final StringBuilder sb = new StringBuilder();
        if (round < 10) {
            sb.append("0");
        }
        sb.append(round).append("h ");
        if (round2 < 10) {
            sb.append("0");
        }
        sb.append(round2).append("m ");
        if (lng < 10L) {
            sb.append("0");
        }
        sb.append(lng).append("s");
        return sb.toString();
    }
}
