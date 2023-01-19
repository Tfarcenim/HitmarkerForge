package tfar.hitmarker;

import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.PacketDistributor;
import tfar.hitmarker.network.PacketHandler;
import tfar.hitmarker.network.S2CHitPacket;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(HitMarker.MODID)
public class HitMarker {

    public static final String MODID = "hitmarker";
    public static final SoundEvent HIT = new SoundEvent(new ResourceLocation(MODID, "hit"));

    public HitMarker() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addGenericListener(SoundEvent.class,this::sounds);
        bus.addListener(this::setup);
        if (FMLEnvironment.dist.isClient()) {
            bus.addListener(HitmarkerClient::clientSetup);
        }
        MinecraftForge.EVENT_BUS.addListener(this::hit);
        MinecraftForge.EVENT_BUS.addListener(this::death);
    }

    private void hit(LivingDamageEvent e) {
        sendToPlayer(false,e.getSource());
    }

    private void death(LivingDeathEvent e) {
        sendToPlayer(true,e.getSource());
    }

    private void sendToPlayer(boolean kill,DamageSource attacker) {
        if (attacker.getEntity() instanceof ServerPlayer player) {
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),new S2CHitPacket(kill));
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.registerMessages(MODID);
    }

    private void sounds(RegistryEvent.Register<SoundEvent> e) {
        e.getRegistry().register(HIT.setRegistryName(HIT.getLocation()));
    }
}
