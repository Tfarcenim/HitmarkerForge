package tfar.hitmarker;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
        if (attacker.getTrueSource() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)attacker.getTrueSource();
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),new S2CHitPacket(kill));
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.registerMessages(MODID);
    }

    private void sounds(RegistryEvent.Register<SoundEvent> e) {
        e.getRegistry().register(HIT.setRegistryName(new ResourceLocation(MODID, "hit")));
    }
}
