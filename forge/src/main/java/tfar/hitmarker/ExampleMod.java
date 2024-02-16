package tfar.hitmarker;

import net.minecraftforge.fml.common.Mod;

@Mod(HitMarker.MODID)
public class ExampleMod {
    
    public ExampleMod() {
    
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.
        HitMarker.LOG.info("Hello Forge world!");
        HitMarker.init();
        
    }
}