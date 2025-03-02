package org.error1015.littletail;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.Collections;
import java.util.List;

import static org.error1015.littletail.events.TailHandler.*;

@Mod.EventBusSubscriber(modid = LittleTail.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.BooleanValue isEnableToAllPlayerConfig = BUILDER.define("对所有玩家启用", true);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> whitePlayerNameConfig = BUILDER.defineList("玩家名字白名单", Collections.emptyList(), o -> o instanceof String);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> whitePlayerUUIDConfig = BUILDER.defineList("玩家UUID白名单", Collections.emptyList(), o -> o instanceof String);
    private static final ForgeConfigSpec.ConfigValue<? extends String> tailConfig = BUILDER.define("尾巴", "喵~", o -> o instanceof String);
    private static final ForgeConfigSpec.BooleanValue isCaseSensitiveConfig = BUILDER.define("名称匹配大小写敏感", false);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklistNameConfig = BUILDER.comment().defineList("玩家名字黑名单", Collections.emptyList(), o -> o instanceof String);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklistUUIDConfig = BUILDER.comment().defineList("玩家UUID黑名单", Collections.emptyList(), o -> o instanceof String);
    private static final ForgeConfigSpec.ConfigValue<String> notEnablePrefixConfig = BUILDER.define("不添加小尾巴消息前缀", "notail");
    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static List<? extends String> whitePlayerName;
    public static List<? extends String> whitePlayerUUID;
    public static List<? extends String> blackPlayerName;
    public static List<? extends String> blackPlayerUUID;
    public static String tail;
    public static boolean isEnableToAllPlayer;
    public static boolean isCaseSensitive;
    public static String notEnablePrefix;

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event) {
        whitePlayerUUID = whitePlayerUUIDConfig.get();
        blackPlayerUUID = blacklistUUIDConfig.get();
        tail = tailConfig.get();
        isEnableToAllPlayer = isEnableToAllPlayerConfig.get();
        isCaseSensitive = isCaseSensitiveConfig.get();
        notEnablePrefix = notEnablePrefixConfig.get();
        // 需要判断玩家名字是否大小写敏感 需要在后面执行
        if (isCaseSensitive) {
            whitePlayerName = whitePlayerNameConfig.get();
            blackPlayerName = blacklistNameConfig.get();
        } else {
            whitePlayerName = whitePlayerNameConfig.get().stream().map(String::toLowerCase).toList();
            blackPlayerName = blacklistNameConfig.get().stream().map(String::toLowerCase).toList();
        }
    }

    @SubscribeEvent
    public static void onConfigEvent(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == Config.SPEC) {
            CACHE_PLAYERS.clear();
            CACHE_PLAYERS_UUID.clear();
            CACHE_BLACKLIST_NAME.clear();
            CACHE_BLACKLIST_UUID.clear();
        }

        CACHE_PLAYERS.addAll(whitePlayerName);
        CACHE_PLAYERS_UUID.addAll(whitePlayerUUID);
        CACHE_BLACKLIST_NAME.addAll(blackPlayerName);
        CACHE_BLACKLIST_UUID.addAll(blackPlayerUUID);
    }
}