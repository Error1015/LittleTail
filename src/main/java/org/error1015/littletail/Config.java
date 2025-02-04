package org.error1015.littletail;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber(modid = Littletail.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue isEnableToAllPlayer = BUILDER.define("isEnableToAllPlayer", false);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> playerCatList = BUILDER.comment("if player name in it, this player's chat will append tail.", "如果玩家名字在里面,这个玩家的发言将会附加小尾巴")
            .defineList("catPlayerNameList", Collections.emptyList(), o -> o instanceof String);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> PlayerUUIDList = BUILDER.comment("if player's uuid in it, this player's chat will append tail.", "如果玩家的UUID在里面,这个玩家的发言将会附加小尾巴").defineList("catPlayerUUIDList", Collections.emptyList(), o -> o instanceof String);
    private static final ForgeConfigSpec.ConfigValue<? extends String> tail = BUILDER.comment("tail pattern", "小尾巴样式")
            .define("tail", "喵~", o -> o instanceof String);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static List<? extends String> getPlayerCatList() {
        return playerCatList.get();
    }

    public static List<? extends String> getPlayerUUIDList() {
        return PlayerUUIDList.get();
    }

    public static String getTail() {
        return tail.get();
    }

    public static boolean isEnableToAllPlayer() {
        return isEnableToAllPlayer.get();
    }
}