package org.error1015.littletail;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.error1015.littletail.Littletail.*;

@Mod.EventBusSubscriber(modid = Littletail.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue isEnableToAllPlayer = BUILDER.define("对所有玩家启用", true);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> playerCatList = BUILDER.defineList("玩家名字白名单", Collections.emptyList(), o -> o instanceof String);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> PlayerUUIDList = BUILDER.defineList("玩家UUID白名单", Collections.emptyList(), o -> o instanceof String);
    private static final ForgeConfigSpec.ConfigValue<? extends String> tail = BUILDER.define("尾巴", "喵呜~", o -> o instanceof String);
    private static final ForgeConfigSpec.BooleanValue isCaseSensitive = BUILDER.define("名称匹配大小写敏感", false);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklistName = BUILDER.comment().defineList("玩家名字黑名单", Collections.emptyList(), o -> o instanceof String);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklistUUID = BUILDER.comment().defineList("玩家名字白名单", Collections.emptyList(), o -> o instanceof String);
    private static final ForgeConfigSpec.ConfigValue<String> notEnablePrefix = BUILDER.define("不添加小尾巴消息前缀", "notail");
    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static List<? extends String> getPlayerCatList() {
        // 返回转换小写的玩家名字列表
        if (isCaseSensitive()) return playerCatList.get().stream().map(String::toLowerCase).toList();
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

    public static boolean isCaseSensitive() {
        return isCaseSensitive.get();
    }

    public static List<? extends String> getBlackListName() {
        if (isCaseSensitive()) return blacklistName.get().stream().map(String::toLowerCase).toList();
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
        reload();
    }

    private static void reload() {
        // 清理缓存
        CACHE_PLAYERS.clear();
        CACHE_PLAYERS_UUID.clear();
        CACHE_BLACKLIST_NAME.clear();
        CACHE_BLACKLIST_UUID.clear();

        // 加载缓存
        reload(getPlayerCatList(), CACHE_PLAYERS);
        reload(getPlayerUUIDList(), CACHE_PLAYERS_UUID);
        reload(getBlackListName(), CACHE_BLACKLIST_NAME);
        reload(getBlackListUUID(), CACHE_BLACKLIST_UUID);
    }

    /**
     * 如果列表内的内容不为空 就添加到缓存中
     * @param list 列表
     * @param set 缓存
     */
    public static void reload(List<? extends String> list, Set<String> set) {
        if (!list.isEmpty()) {
            set.addAll(list);
        }
    }
}