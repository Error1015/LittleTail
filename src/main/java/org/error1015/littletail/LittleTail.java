package org.error1015.littletail;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(LittleTail.MODID)
@Mod.EventBusSubscriber(modid = LittleTail.MODID)
public class LittleTail {
    public static final String MODID = "littletail";

    public LittleTail() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}