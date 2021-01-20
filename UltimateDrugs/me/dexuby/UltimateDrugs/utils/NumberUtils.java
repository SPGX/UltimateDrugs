// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.utils;

public class NumberUtils
{
    public static boolean isValidInteger(final String s) {
        try {
            Integer.parseInt(s);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
}
