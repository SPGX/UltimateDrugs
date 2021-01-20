package me.dexuby.UltimateDrugs.handlers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import java.util.function.Predicate;
import me.dexuby.UltimateDrugs.Main;
import me.dexuby.UltimateDrugs.drugs.ActiveDrugEffect;
import me.dexuby.UltimateDrugs.managers.DrugManager;

public class ProtocolLibHandler {
   private final DrugManager drugManager;

   public ProtocolLibHandler(Main var1) {
      this.drugManager = var1.getDrugManager();
      ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(var1, ListenerPriority.NORMAL, new PacketType[]{Server.NAMED_SOUND_EFFECT, Server.CUSTOM_SOUND_EFFECT, Server.ENTITY_SOUND}) {
         public void onPacketSending(PacketEvent var1) {
            ActiveDrugEffect activedrugeffect = (ActiveDrugEffect)ProtocolLibHandler.this.drugManager.getActiveDrugEffects(var1.getPlayer()).stream().filter((var0) -> {
               return System.currentTimeMillis() < var0.getExpirationTimestamp() && (var0.getDrug().getGlobalSoundVolumeModifier() != 0.0D || var0.getDrug().getGlobalSoundPitchModifier() != 0.0D);
            }).findFirst().orElse(null);
            if (activedrugeffect != null) {
               if (activedrugeffect.getDrug().getGlobalSoundVolumeModifier() != 0.0D) {
                  double d0 = activedrugeffect.getDrug().getGlobalSoundVolumeModifier();
                  float f = ((Float)var1.getPacket().getFloat().read(0)).floatValue() + (float)d0;
                  var1.getPacket().getFloat().write(0, Float.valueOf(f));
               }

               if (activedrugeffect.getDrug().getGlobalSoundPitchModifier() != 0.0D) {
                  double d1 = activedrugeffect.getDrug().getGlobalSoundPitchModifier();
                  float f1 = ((Float)var1.getPacket().getFloat().read(1)).floatValue() + (float)d1;
                  var1.getPacket().getFloat().write(1, Float.valueOf(f1));
               }
            }

         }
      });
   }
}
