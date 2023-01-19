package tfar.hitmarker;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class HitmarkerClient {

    public static int remainingTicks = 0;
    public static boolean kill = false;

    public static void clientSetup(FMLClientSetupEvent e) {
        MinecraftForge.EVENT_BUS.addListener(HitmarkerClient::tick);
        MinecraftForge.EVENT_BUS.addListener(HitmarkerClient::crosshair);
    }

    private static void tick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.START && remainingTicks > 0) {
            remainingTicks--;
        }
    }

    private static final ResourceLocation HIT_TEXTURE = new ResourceLocation(HitMarker.MODID, "textures/hit.png");

    private static void crosshair(RenderGameOverlayEvent.Post e) {
        if (e.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            if (HitmarkerClient.remainingTicks > 0) {
                int scaledWidth = Minecraft.getInstance().getMainWindow().getScaledWidth();
                int scaledHeight = Minecraft.getInstance().getMainWindow().getScaledHeight();
                Minecraft.getInstance().getTextureManager().bindTexture(HIT_TEXTURE);
                if (kill) {
                    RenderSystem.color4f(0,1,1,1);
                }
                AbstractGui.blit(e.getMatrixStack(), (scaledWidth - 11) / 2, (scaledHeight - 11) / 2, 0.0F, 0.0F, 11, 11, 11, 11);
                Minecraft.getInstance().getTextureManager().bindTexture(IngameGui.GUI_ICONS_LOCATION);
            }
        }
    }

    public static void receiveHit(boolean kill) {
        remainingTicks = 20;
        HitmarkerClient.kill = kill;
        Minecraft.getInstance().player.playSound(HitMarker.HIT, .4f, 1);
    }
}
