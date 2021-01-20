// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.packetwrappers;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.entity.Player;
import com.google.common.base.Objects;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class AbstractPacket
{
    protected PacketContainer handle;
    
    protected AbstractPacket(final PacketContainer handle, final PacketType packetType) {
        if (handle == null) {
            throw new IllegalArgumentException("Packet handle was null");
        }
        if (!Objects.equal((Object)handle.getType(), (Object)packetType)) {
            throw new IllegalArgumentException("Packet type not matching");
        }
        this.handle = handle;
    }
    
    public PacketContainer getHandle() {
        return this.handle;
    }
    
    public void sendPacket(final Player player) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, this.getHandle());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void sendPacket(final Player... array) {
        try {
            for (int length = array.length, i = 0; i < length; ++i) {
                ProtocolLibrary.getProtocolManager().sendServerPacket(array[i], this.getHandle());
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void receivePacket(final Player player) {
        try {
            ProtocolLibrary.getProtocolManager().recieveClientPacket(player, this.getHandle());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
