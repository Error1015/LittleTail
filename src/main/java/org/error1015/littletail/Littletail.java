package org.error1015.littletail;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.HashSet;
import java.util.Set;

@Mod(Littletail.MODID)
@Mod.EventBusSubscriber(modid = Littletail.MODID)
public class Littletail {
    public static final String MODID = "littletail";
    // 创建Set集合过滤重复项
    public static final Set<String> CACHE_PLAYERS = new HashSet<>();
    public static final Set<String> CACHE_PLAYERS_UUID = new HashSet<>();

    public Littletail() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    public static void onServerChat(ServerChatEvent event) {
        Player player = event.getPlayer();
        String rawMessage = event.getRawText();
        var playerCatList = Config.getPlayerCatList();
        var playerUUIDList = Config.getPlayerUUIDList();

        // 如果信息开头是"/" 直接退出不做处理
        if (rawMessage.startsWith("/")) return;

        // 如果信息开头是"notail" 返回初始内容
        if (rawMessage.startsWith("notail")) {
            event.setMessage(Component.literal(rawMessage.substring(7)));
            return;
        }

        // 如果两个列表的内容都是空的就直接退出
        if (playerCatList.isEmpty() && playerUUIDList.isEmpty()) return;

        // 如果Set集合内的内容为空 就把两个列表的内容添加到Set集合内
        if (CACHE_PLAYERS.isEmpty()) CACHE_PLAYERS.addAll(playerCatList);
        if (CACHE_PLAYERS_UUID.isEmpty()) CACHE_PLAYERS_UUID.addAll(playerUUIDList);

        // 判断是否区分玩家名字大小写
        String playerName = getPlayerNameOnDifferentCase(player.getName().getString());
        String playerUUID = player.getUUID().toString();

        // 给玩家添加小尾巴 需要满足的条件有:
        // 全局启用 玩家名在列表内 玩家UUID在列表内 满足其中一项即可
        if (Config.isEnableToAllPlayer() || CACHE_PLAYERS.contains(playerName) || CACHE_PLAYERS_UUID.contains(playerUUID)) {
            Component finalMessage = event.getMessage().copy().append(Config.getTail());
            event.setMessage(finalMessage);
        }
    }

    /**
     * 判断玩家名字是否区分大小写 如果是 则返回原样 如果不是 则返回小写形式
     * @param playerName 需要转换的玩家名字
     * @return 操作完成后的玩家名
     */
    public static String getPlayerNameOnDifferentCase(String playerName) {
        if (Config.isCaseSensitive()) {
            return playerName;
        } else {
            return playerName.toLowerCase();
        }
    }
}