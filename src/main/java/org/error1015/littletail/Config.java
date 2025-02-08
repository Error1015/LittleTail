package org.error1015.littletail;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.Collections;
import java.util.List;

import static org.error1015.littletail.Littletail.*;

@Mod.EventBusSubscriber(modid = Littletail.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue isEnableToAllPlayer = BUILDER.define("对所有玩家启用", true);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> playerCatList = BUILDER.defineList("玩家名字白名单", Collections.emptyList(), o -> o instanceof String);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> playerUUIDList = BUILDER.defineList("玩家UUID白名单", Collections.emptyList(), o -> o instanceof String && ((String) o).matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"));
    private static final ForgeConfigSpec.ConfigValue<? extends String> tail = BUILDER.define("尾巴", "喵~", o -> o instanceof String);
    private static final ForgeConfigSpec.BooleanValue isCaseSensitive = BUILDER.define("名称匹配大小写敏感", false);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklistName = BUILDER.comment().defineList("玩家名字黑名单", Collections.emptyList(), o -> o instanceof String);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklistUUID = BUILDER.comment().defineList("玩家UUID黑名单", Collections.emptyList(), o -> o instanceof String);
    private static final ForgeConfigSpec.ConfigValue<String> notEnablePrefix = BUILDER.define("不添加小尾巴消息前缀", "notail");
    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static List<? extends String> getPlayerCatList() {
        // 返回转换小写的玩家名字列表
        if (!isCaseSensitive()) return playerCatList.get().stream().map(String::toLowerCase).toList();
        return playerCatList.get();
    }

    public static List<? extends String> getPlayerUUIDList() {
        return playerUUIDList.get();
    }

    public static String getTail() {
        return tail.get();
    }

    public static boolean isEnableToAllPlayer() {
        return isEnableToAllPlayer.get();
    }

    public static boolean isCaseSensitive() {
        return isCaseSensitive.get();
    }

    public static List<? extends String> getBlackListName() {
        if (!isCaseSensitive()) return blacklistName.get().stream().map(String::toLowerCase).toList();
        return blacklistName.get();
    }

    public static List<? extends String> getBlackListUUID() {
        return blacklistUUID.get();
    }

    public static String getNotEnablePrefix() {
        return notEnablePrefix.get();
    }

    @SubscribeEvent
    public static void onConfigEvent(ModConfigEvent.Reloading event) {
        // 清理缓存
        if (event.getConfig().getSpec() == Config.SPEC) {
            CACHE_PLAYERS.clear();
            CACHE_PLAYERS_UUID.clear();
            CACHE_BLACKLIST_NAME.clear();
            CACHE_BLACKLIST_UUID.clear();
        }

        // 加载缓存
        CACHE_PLAYERS.addAll(getPlayerCatList());
        CACHE_PLAYERS_UUID.addAll(getPlayerUUIDList());
        CACHE_BLACKLIST_NAME.addAll(getBlackListName());
        CACHE_BLACKLIST_UUID.addAll(getBlackListUUID());
    }
}