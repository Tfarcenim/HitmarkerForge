package tfar.hitmarker;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class HitmarkerClient {

    public static int remainingTicks = 0;
    public static boolean kill = false;

    public static void clientSetup(FMLClientSetupEvent e) {
        MinecraftForge.EVENT_BUS.addListener(HitmarkerClient::tick);
        OverlayRegistry.registerOverlayAbove(ForgeIngameGui.CROSSHAIR_ELEMENT, HitMarker.MODID, overlay);
    }

    private static void tick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.START && remainingTicks > 0) {
            remainingTicks--;
        }
    }

    private static final ResourceLocation HIT_TEXTURE = new ResourceLocation(HitMarker.MODID, "textures/hit.png");

    static IIngameOverlay overlay = HitmarkerClient::crosshair;

    private static void crosshair(ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
        if (Minecraft.getInstance().options.hideGui && HitmarkerClient.remainingTicks > 0) {
            bind(HIT_TEXTURE);
            if (kill) {
                RenderSystem.setShaderColor(0, 1, 1, 1);
            }
            GuiComponent.blit(poseStack, (width - 11) / 2, (height - 11) / 2, 0.0F, 0.0F, 11, 11, 11, 11);
            bind(Gui.GUI_ICONS_LOCATION);
        }
    }

    static void bind(ResourceLocation tex) {
        RenderSystem.setShaderTexture(0, tex);
    }

    public static void receiveHit(boolean kill) {
        remainingTicks = 20;
        HitmarkerClient.kill = kill;
        Minecraft.getInstance().player.playSound(HitMarker.HIT, .4f, 1);
    }
}
