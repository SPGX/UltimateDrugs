package me.dexuby.UltimateDrugs.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;

public class TextUtils {
  public static String replaceVariables(String paramString, Map<String, String> paramMap) {
    String str = paramString;
    for (String str1 : paramMap.keySet()) {
      String str2 = paramMap.get(str1);
      str = str.replace(str1, str2);
    } 
    return str;
  }
  
  public static List<String> replaceVariables(List<String> paramList, Map<String, String> paramMap) {
    ArrayList<String> arrayList = new ArrayList();
    for (String str1 : paramList) {
      String str2 = str1;
      for (String str3 : paramMap.keySet()) {
        String str4 = paramMap.get(str3);
        str2 = str2.replace(str3, str4);
      } 
      arrayList.add(str2);
    } 
    return arrayList;
  }
  
  public static List<String> translateAlternateColorCodesFromList(char paramChar, List<String> paramList) {
    ArrayList<String> arrayList = new ArrayList();
    for (String str : paramList)
      arrayList.add(ChatColor.translateAlternateColorCodes(paramChar, str)); 
    return arrayList;
  }
  
  public static String convertSecondsToFormattedTime(long paramLong) {
    int i = Math.round((float)(paramLong / 3600L));
    paramLong -= (i * 3600);
    int j = Math.round((float)(paramLong / 60L));
    paramLong -= (j * 60);
    StringBuilder stringBuilder = new StringBuilder();
    if (i < 10)
      stringBuilder.append("0"); 
    stringBuilder
      .append(i)
      .append("h ");
    if (j < 10)
      stringBuilder.append("0"); 
    stringBuilder
      .append(j)
      .append("m ");
    if (paramLong < 10L)
      stringBuilder.append("0"); 
    stringBuilder
      .append(paramLong)
      .append("s");
    return stringBuilder.toString();
  }
}
