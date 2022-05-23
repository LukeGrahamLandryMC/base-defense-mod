package ca.lukegrahamlandry.basedefense.events;

import ca.lukegrahamlandry.basedefense.Config;
import ca.lukegrahamlandry.basedefense.ModMain;
import ca.lukegrahamlandry.basedefense.material.MaterialGenerationHandler;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ModMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents {
    private static int timer = 0;
    @SubscribeEvent
    public static void tick(TickEvent.WorldTickEvent event){
        if (event.phase == TickEvent.Phase.START && !event.world.isClientSide() && event.world.dimension().equals(Level.OVERWORLD)){
            if (timer >= Config.getGenerationTimer()){
                MaterialGenerationHandler.get(event.world).distributeMaterials(event.world.getServer());
                timer = 0;
            }
            timer++;
        }
    }
}
