package org.error1015.littletail;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod(Littletail.MODID)
@Mod.EventBusSubscriber(modid = Littletail.MODID)
public class Littletail {
    public static final String MODID = "littletail";
    // 创建Set集合过滤重复项
    public static final Set<String> CACHE_PLAYERS = new HashSet<>();
    public static final Set<String> CACHE_PLAYERS_UUID = new HashSet<>();
    public static final Set<String> CACHE_BLACKLIST_NAME = new HashSet<>();
    public static final Set<String> CACHE_BLACKLIST_UUID = new HashSet<>();

    public Littletail() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    public static void onServerChat(ServerChatEvent event) {
        Player player = event.getPlayer();
        String rawMessage = event.getRawText();
        var playerCatList = Config.getPlayerCatList();
        var playerUUIDList = Config.getPlayerUUIDList();
        var blackListName = Config.getBlackListName();
        var blackListUUID = Config.getBlackListUUID();
        String playerName = getPlayerNameOnConfigValue(player.getName().getString());
        String playerUUID = player.getUUID().toString();
        String prefix = Config.getNotEnablePrefix();

        // 如果信息开头是"/" 直接退出不做处理
        if (rawMessage.startsWith("/")) return;

        // 如果信息开头符合前缀 返回初始内容
        Component original = event.getMessage();
        String messageString = original.getString();
        if (messageString.startsWith(prefix)) {
            int trimLength = prefix.length();
            if (messageString.length() > trimLength && messageString.charAt(trimLength) == ' ')
                trimLength += 1;
            Component newComponent = Component.literal(messageString.substring(trimLength)).withStyle(original.getStyle());
            event.setMessage(newComponent);
            return;
        }

        // 添加内容到缓存中
        addToTemp(playerCatList, playerUUIDList, blackListName, blackListUUID);

        // 如果玩家在黑名单内 就直接退出不做处理
        if (CACHE_BLACKLIST_NAME.contains(playerName) || CACHE_BLACKLIST_UUID.contains(playerUUID)) return;

        // 给玩家添加小尾巴 需要满足的条件有:
        // 全局启用 玩家名在列表内 玩家UUID在列表内 满足其中一项即可
        if (Config.isEnableToAllPlayer() || CACHE_PLAYERS.contains(playerName) || CACHE_PLAYERS_UUID.contains(playerUUID)) {
            Component finalMessage = event.getMessage().copy().append(Config.getTail());
            event.setMessage(finalMessage);
        }
    }

    /**
     * 如果Set集合内的内容为空 就把两个列表的内容添加到Set集合内
     */
    private static void addToTemp(List<? extends String> playerCatList, List<? extends String> playerUUIDList, List<? extends String> blackListName, List<? extends String> blackListUUID) {
        if (CACHE_PLAYERS.isEmpty()) CACHE_PLAYERS.addAll(playerCatList);
        if (CACHE_PLAYERS_UUID.isEmpty()) CACHE_PLAYERS_UUID.addAll(playerUUIDList);
        if (CACHE_BLACKLIST_NAME.isEmpty()) CACHE_BLACKLIST_NAME.addAll(blackListName);
        if (CACHE_BLACKLIST_UUID.isEmpty()) CACHE_BLACKLIST_UUID.addAll(blackListUUID);
    }

    /**
     * 判断玩家名字是否区分大小写 如果是 则返回原样 如果不是 则返回小写形式
     * @param playerName 需要转换的玩家名字
     * @return 操作完成后的玩家名
     */
    public static String getPlayerNameOnConfigValue(String playerName) {
        if (Config.isCaseSensitive()) {
            return playerName;
        } else {
            return playerName.toLowerCase();
        }
    }
}