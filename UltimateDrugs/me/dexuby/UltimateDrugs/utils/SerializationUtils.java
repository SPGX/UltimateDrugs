package me.dexuby.UltimateDrugs.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class SerializationUtils {
  public static List<String> serializeLocationSet(Set<Location> paramSet) {
    ArrayList<String> arrayList = new ArrayList();
    try {
      for (Location location : paramSet) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);
        bukkitObjectOutputStream.writeObject(location);
        bukkitObjectOutputStream.close();
        arrayList.add(Base64Coder.encodeLines(byteArrayOutputStream.toByteArray()));
      } 
    } catch (IOException iOException) {
      iOException.printStackTrace();
      return arrayList;
    } 
    return arrayList;
  }
  
  public static Set<Location> deserializeLocationSet(List<String> paramList) {
    HashSet<Location> hashSet = new HashSet();
    try {
      for (String str : paramList) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(str));
        BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream);
        Location location = (Location)bukkitObjectInputStream.readObject();
        bukkitObjectInputStream.close();
        hashSet.add(location);
      } 
    } catch (IOException|ClassNotFoundException iOException) {
      iOException.printStackTrace();
      return hashSet;
    } 
    return hashSet;
  }
}
