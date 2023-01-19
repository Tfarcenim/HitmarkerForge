package tfar.hitmarker.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import tfar.hitmarker.HitmarkerClient;

import java.util.function.Supplier;

public class S2CHitPacket {

  private boolean kill;

  public S2CHitPacket(boolean kill) {
    this.kill = kill;
  }

  public S2CHitPacket(FriendlyByteBuf buf) {
    kill = buf.readBoolean();
  }

  public void encode(FriendlyByteBuf buffer) {
    buffer.writeBoolean(kill);
  }

  public void handle(Supplier<NetworkEvent.Context> ctx) {
    HitmarkerClient.receiveHit(kill);
    ctx.get().setPacketHandled(true);
  }
}