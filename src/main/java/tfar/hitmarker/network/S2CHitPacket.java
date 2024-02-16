package tfar.hitmarker.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import tfar.hitmarker.HitmarkerClient;

import java.util.function.Supplier;

public class S2CHitPacket {

  private final boolean kill;

  public S2CHitPacket(boolean kill) {
    this.kill = kill;
  }

  public S2CHitPacket(PacketBuffer buf) {
    kill = buf.readBoolean();
  }

  public void encode(PacketBuffer buffer) {
    buffer.writeBoolean(kill);
  }

  public void handle(Supplier<NetworkEvent.Context> ctx) {
    HitmarkerClient.receiveHit(kill);
    ctx.get().setPacketHandled(true);
  }
}