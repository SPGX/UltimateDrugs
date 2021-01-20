package me.dexuby.UltimateDrugs.utils;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BukkitConverters;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.brigadier.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class ReflectionUtils {
   private static int versionId = calculateVersionId();
   private static Map<String, Class<?>> classCache = new HashMap<String, Class<?>>();
   private static Map<String, Method> methodCache = new HashMap<String, Method>();
   private static Map<String, Constructor<?>> constructorCache = new HashMap<String, Constructor<?>>();
   private static Map<String, Field> fieldCache = new HashMap<String, Field>();

   private static Field getAndPrepareField(Class var0, String var1) {
      try {
         Field field = var0.getDeclaredField(var1);
         field.setAccessible(true);
         return field;
      } catch (Exception exception) {
         exception.printStackTrace();
         return null;
      }
   }

   public static ItemStack getTexturedSkull(String var0) {
      ItemStack itemstack = new ItemStack(Material.PLAYER_HEAD);
      SkullMeta skullmeta = (SkullMeta)itemstack.getItemMeta();

      try {
         if (versionId >= 250) {
            Method method = methodCache.get("CraftMetaSkull-setProfile");
            method.setAccessible(true);
            method.invoke(skullmeta, getProfile(var0));
         } else {
            Field field = ((Class)Objects.requireNonNull(classCache.get("CraftMetaSkull"))).getDeclaredField("profile");
            field.setAccessible(true);
            field.set(skullmeta, getProfile(var0));
         }
      } catch (Exception exception) {
         exception.printStackTrace();
      }

      itemstack.setItemMeta(skullmeta);
      return itemstack;
   }

   public static void setSkullTexture(ItemStack var0, String var1) {
      SkullMeta skullmeta = (SkullMeta)var0.getItemMeta();

      try {
         if (versionId >= 250) {
            Method method = methodCache.get("CraftMetaSkull-setProfile");
            method.setAccessible(true);
            method.invoke(skullmeta, getProfile(var1));
         } else {
            Field field = ((Class)Objects.requireNonNull(classCache.get("CraftMetaSkull"))).getDeclaredField("profile");
            field.setAccessible(true);
            field.set(skullmeta, getProfile(var1));
         }
      } catch (Exception var4) {
         ;
      }

      var0.setItemMeta(skullmeta);
   }

   public static void setSkullBlockTexture(Block var0, String var1) {
      if (var0.getType() == Material.PLAYER_HEAD || var0.getType() == Material.PLAYER_WALL_HEAD) {
         try {
            GameProfile gameprofile = getProfile(var1);
            Object object = ((Method)methodCache.get("CraftWorld-getHandle")).invoke(var0.getWorld());
            Object object1 = ((Method)methodCache.get("WorldServer-getTileEntity")).invoke(object, (constructorCache.get("BlockPosition")).newInstance(var0.getX(), var0.getY(), var0.getZ()));
            if (object1 == null) {
               return;
            }

            ((Method)methodCache.get("TileEntitySkull-setGameProfile")).invoke(object1, gameprofile);
            var0.getWorld().refreshChunk(var0.getChunk().getX(), var0.getChunk().getZ());
         } catch (Exception exception) {
            exception.printStackTrace();
         }

      }
   }

   private static GameProfile getProfile(String var0) {
      GameProfile gameprofile = new GameProfile(UUID.nameUUIDFromBytes(var0.getBytes()), (String)null);
      Property property = new Property("textures", var0);
      gameprofile.getProperties().put("textures", property);
      return gameprofile;
   }

   public static boolean removeRecipeByKey(String string) {
       try {
           Object object = methodCache.get("CraftServer-getServer").invoke((Object)Bukkit.getServer(), new Object[0]);
           Object object2 = methodCache.get("MinecraftServer-getCraftingManager").invoke(object, new Object[0]);
           Object var3_4 = null;
           Object map = fieldCache.get("CraftingManager-recipes").get(object2);
           for (Map.Entry entry : ((Map<String, Class<?>>) map).entrySet()) {
               String string2 = (String)methodCache.get("MinecraftKey-getKey").invoke(entry.getKey(), new Object[0]);
               if (!string2.equals(string)) continue;
               var3_4 = entry.getKey();
               break;
           }
           if (var3_4 != null) {
               ((Map<String, Class<?>>) map).remove(var3_4);
           }
           return true;
       }
       catch (Exception exception) {
           return false;
       }
   }

   public static ItemStack setItemStackStringTag(ItemStack var0, String var1, String var2) {
      try {
         Object object = ((Method)methodCache.get("CraftItemStack-asNMSCopy")).invoke((Object)null, var0);
         boolean flag = ((Boolean)((Method)methodCache.get("ItemStack-hasTag")).invoke(object)).booleanValue();
         if (!flag) {
            ((Method)methodCache.get("ItemStack-setTag")).invoke(object, (classCache.get("NBTTagCompound")).newInstance());
         }

         Object object1 = ((Method)methodCache.get("ItemStack-getTag")).invoke(object);
         ((Method)methodCache.get("NBTTagCompound-setString")).invoke(object1, var1, var2);
         ((Method)methodCache.get("ItemStack-setTag")).invoke(object, object1);
         return (ItemStack)((Method)methodCache.get("CraftItemStack-asBukkitCopy")).invoke((Object)null, object);
      } catch (Exception var6) {
         return null;
      }
   }

   @SafeVarargs
   public static ItemStack setItemStackStringTags(ItemStack var0, SimpleEntry<String, String>... var1) {
      try {
         Object object = ((Method)methodCache.get("CraftItemStack-asNMSCopy")).invoke((Object)null, var0);
         boolean flag = ((Boolean)((Method)methodCache.get("ItemStack-hasTag")).invoke(object)).booleanValue();
         if (!flag) {
            ((Method)methodCache.get("ItemStack-setTag")).invoke(object, (classCache.get("NBTTagCompound")).newInstance());
         }

         Object object1 = ((Method)methodCache.get("ItemStack-getTag")).invoke(object);

         for(SimpleEntry simpleentry : var1) {
            ((Method)methodCache.get("NBTTagCompound-setString")).invoke(object1, simpleentry.getKey(), simpleentry.getValue());
         }

         ((Method)methodCache.get("ItemStack-setTag")).invoke(object, object1);
         return (ItemStack)((Method)methodCache.get("CraftItemStack-asBukkitCopy")).invoke((Object)null, object);
      } catch (Exception var9) {
         return null;
      }
   }

   public static String getItemStackStringTag(ItemStack var0, String var1) {
      try {
         Object object = ((Method)methodCache.get("CraftItemStack-asNMSCopy")).invoke((Object)null, var0);
         boolean flag = ((Boolean)((Method)methodCache.get("ItemStack-hasTag")).invoke(object)).booleanValue();
         if (flag) {
            Object object1 = ((Method)methodCache.get("ItemStack-getTag")).invoke(object);
            return (String)((Method)methodCache.get("NBTTagCompound-getString")).invoke(object1, var1);
         } else {
            return null;
         }
      } catch (Exception var5) {
         return null;
      }
   }

   public static List<Entity> getEntitiesFromSelector(CommandSender var0, String var1) {
      ArrayList arraylist = new ArrayList();

      try {
         Object object = ((Method)methodCache.get("ArgumentEntity-multipleEntities")).invoke((Object)null);
         Object object1 = ((Method)methodCache.get("ArgumentEntity-parse")).invoke(object, new StringReader(var1));
         Object object2 = ((Method)methodCache.get("VanillaCommandWrapper-getListener")).invoke(classCache.get("VanillaCommandWrapper"), var0);

         for(Object object3 : (List)((Method)methodCache.get("EntitySelector-getEntities")).invoke(object1, object2)) {
            try {
               arraylist.add((Entity)((Method)methodCache.get("Entity-getBukkitEntity")).invoke(object3));
            } catch (Exception var10) {
               ;
            }
         }
      } catch (Exception var11) {
         ;
      }

      return arraylist;
   }

   public static void prepareRespawnPacketContainer(PacketContainer var0, World var1) {
      try {
         Object object = BukkitConverters.getWorldConverter().getGeneric(var1);
         Object object1 = ((Field)fieldCache.get("World-dimensionKey")).get(object);
         Object object2 = ((Field)fieldCache.get("World-typeKey")).get(object);
         Class oclass = classCache.get("ResourceKey");
         Class oclass1 = classCache.get("DimensionManager");
         if (versionId >= 262) {
            Object object3 = ((Method)methodCache.get("CraftWorld-getHandle")).invoke(var1);
            Object object4 = ((Method)methodCache.get("World-getDimensionManager")).invoke((classCache.get("World")).cast(object3));
            var0.getSpecificModifier(oclass1).write(0, object4);
            var0.getSpecificModifier(oclass).write(0, object1);
         } else {
            var0.getSpecificModifier(oclass).write(0, object2);
            var0.getSpecificModifier(oclass).write(1, object1);
         }
      } catch (Exception exception) {
         exception.printStackTrace();
      }

   }

   private static int calculateVersionId() {
      String s = Bukkit.getServer().getBukkitVersion().split("-")[0];
      String[] astring = s.split("\\.");
      int i = 0;
      int j = 100;

      for(String s1 : astring) {
         i += Integer.parseInt(s1) * j;
         j = (int)((double)j * 0.1D);
      }

      return i;
   }

   public static int getVersionId() {
      return versionId;
   }

   public static int getVersionNumber() {
      return Integer.parseInt(Bukkit.getServer().getBukkitVersion().split("-")[0].replace(".", "").substring(0, 3));
   }

   private static String getVersion() {
      return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
   }

   static {
      String s = getVersion();

      try {
         classCache.put("ItemStack", Class.forName(String.format("net.minecraft.server.%s.ItemStack", s)));
         classCache.put("CraftMetaSkull", Class.forName(String.format("org.bukkit.craftbukkit.%s.inventory.CraftMetaSkull", s)));
         classCache.put("CraftWorld", Class.forName(String.format("org.bukkit.craftbukkit.%s.CraftWorld", s)));
         classCache.put("CraftServer", Class.forName(String.format("org.bukkit.craftbukkit.%s.CraftServer", s)));
         classCache.put("CraftItemStack", Class.forName(String.format("org.bukkit.craftbukkit.%s.inventory.CraftItemStack", s)));
         classCache.put("NBTTagCompound", Class.forName(String.format("net.minecraft.server.%s.NBTTagCompound", s)));
         classCache.put("MinecraftServer", Class.forName(String.format("net.minecraft.server.%s.MinecraftServer", s)));
         classCache.put("MinecraftKey", Class.forName(String.format("net.minecraft.server.%s.MinecraftKey", s)));
         classCache.put("CraftingManager", Class.forName(String.format("net.minecraft.server.%s.CraftingManager", s)));
         classCache.put("WorldServer", Class.forName(String.format("net.minecraft.server.%s.WorldServer", s)));
         classCache.put("BlockPosition", Class.forName(String.format("net.minecraft.server.%s.BlockPosition", s)));
         classCache.put("TileEntitySkull", Class.forName(String.format("net.minecraft.server.%s.TileEntitySkull", s)));
         classCache.put("GameProfile", Class.forName("com.mojang.authlib.GameProfile"));
         classCache.put("Entity", Class.forName(String.format("net.minecraft.server.%s.Entity", s)));
         classCache.put("ArgumentEntity", Class.forName(String.format("net.minecraft.server.%s.ArgumentEntity", s)));
         classCache.put("EntitySelector", Class.forName(String.format("net.minecraft.server.%s.EntitySelector", s)));
         classCache.put("CommandListenerWrapper", Class.forName(String.format("net.minecraft.server.%s.CommandListenerWrapper", s)));
         classCache.put("VanillaCommandWrapper", Class.forName(String.format("org.bukkit.craftbukkit.%s.command.VanillaCommandWrapper", s)));
         if (versionId >= 260) {
            classCache.put("ResourceKey", Class.forName(String.format("net.minecraft.server.%s.ResourceKey", s)));
            classCache.put("World", Class.forName(String.format("net.minecraft.server.%s.World", s)));
            classCache.put("DimensionManager", Class.forName(String.format("net.minecraft.server.%s.DimensionManager", s)));
         }

         methodCache.put("ItemStack-hasTag", (classCache.get("ItemStack")).getMethod("hasTag"));
         methodCache.put("ItemStack-getTag", (classCache.get("ItemStack")).getMethod("getTag"));
         methodCache.put("ItemStack-setTag", (classCache.get("ItemStack")).getMethod("setTag", classCache.get("NBTTagCompound")));
         methodCache.put("CraftItemStack-asNMSCopy", (classCache.get("CraftItemStack")).getMethod("asNMSCopy", ItemStack.class));
         methodCache.put("CraftItemStack-asBukkitCopy", (classCache.get("CraftItemStack")).getMethod("asBukkitCopy", classCache.get("ItemStack")));
         methodCache.put("NBTTagCompound-getString", (classCache.get("NBTTagCompound")).getMethod("getString", String.class));
         methodCache.put("NBTTagCompound-setString", (classCache.get("NBTTagCompound")).getMethod("setString", String.class, String.class));
         methodCache.put("CraftWorld-getHandle", (classCache.get("CraftWorld")).getMethod("getHandle"));
         methodCache.put("CraftServer-getServer", (classCache.get("CraftServer")).getMethod("getServer"));
         methodCache.put("MinecraftServer-getCraftingManager", (classCache.get("MinecraftServer")).getMethod("getCraftingManager"));
         methodCache.put("MinecraftKey-getKey", (classCache.get("MinecraftKey")).getMethod("getKey"));
         methodCache.put("WorldServer-getTileEntity", (classCache.get("WorldServer")).getMethod("getTileEntity", classCache.get("BlockPosition")));
         methodCache.put("TileEntitySkull-setGameProfile", (classCache.get("TileEntitySkull")).getMethod("setGameProfile", classCache.get("GameProfile")));
         if (versionId >= 250) {
            methodCache.put("CraftMetaSkull-setProfile", (classCache.get("CraftMetaSkull")).getDeclaredMethod("setProfile", classCache.get("GameProfile")));
         }

         if (versionId >= 240) {
            methodCache.put("ArgumentEntity-multipleEntities", (classCache.get("ArgumentEntity")).getDeclaredMethod("multipleEntities"));
         } else {
            methodCache.put("ArgumentEntity-multipleEntities", (classCache.get("ArgumentEntity")).getDeclaredMethod("b"));
         }

         methodCache.put("ArgumentEntity-parse", (classCache.get("ArgumentEntity")).getDeclaredMethod("parse", StringReader.class));
         if (versionId >= 240) {
            methodCache.put("EntitySelector-getEntities", (classCache.get("EntitySelector")).getDeclaredMethod("getEntities", classCache.get("CommandListenerWrapper")));
         } else {
            methodCache.put("EntitySelector-getEntities", (classCache.get("EntitySelector")).getDeclaredMethod("b", classCache.get("CommandListenerWrapper")));
         }

         methodCache.put("VanillaCommandWrapper-getListener", (classCache.get("VanillaCommandWrapper")).getDeclaredMethod("getListener", CommandSender.class));
         methodCache.put("Entity-getBukkitEntity", (classCache.get("Entity")).getDeclaredMethod("getBukkitEntity"));
         if (versionId >= 262) {
            methodCache.put("World-getDimensionManager", (classCache.get("World")).getDeclaredMethod("getDimensionManager"));
         }

         constructorCache.put("BlockPosition", (classCache.get("BlockPosition")).getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE));
         if (versionId >= 260) {
            fieldCache.put("World-dimensionKey", getAndPrepareField(classCache.get("World"), "dimensionKey"));
            fieldCache.put("World-typeKey", getAndPrepareField(classCache.get("World"), "typeKey"));
         }
      } catch (Exception exception) {
         exception.printStackTrace();
      }

   }
}
