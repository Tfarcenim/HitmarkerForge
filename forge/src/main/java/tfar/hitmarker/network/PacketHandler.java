package tfar.hitmarker.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import tfar.hitmarker.HitMarker;
import tfar.hitmarker.HitMarkerFo;

public class PacketHandler {
  public static SimpleChannel INSTANCE;

  public static void registerMessages(String channelName) {
    int id = 0;

    INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(HitMarker.MODID, channelName), () -> "1.0", s -> true, s -> true);

    INSTANCE.registerMessage(id++, S2CHitPacket.class,
            S2CHitPacket::encode,
            S2CHitPacket::new,
            S2CHitPacket::handle);
  }
}
