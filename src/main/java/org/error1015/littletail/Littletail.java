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
        // 如果信息开头是"/" 直接退出不做处理
        if (rawMessage.startsWith("/")) return;
        // 如果信息开头是"notail" 把字符串切割前面6位 最后的内容就是信息 "notail a" -> "a"
        if (rawMessage.startsWith("notail")) {
            event.setMessage(Component.literal(rawMessage.substring(7)));
            return;
        }
        var playerCatList = Config.getPlayerCatList();
        var playerUUIDList = Config.getPlayerUUIDList();
        // 如果两个列表的内容都是空的就直接退出
        if (playerCatList.isEmpty() && playerUUIDList.isEmpty()) return;
        // 如果CACHE_PLAYERS为空 添加PlayerCatList的内容
        if (CACHE_PLAYERS.isEmpty()) CACHE_PLAYERS.addAll(playerCatList);
        if (CACHE_PLAYERS_UUID.isEmpty()) CACHE_PLAYERS_UUID.addAll(playerUUIDList);
        // 如果集合中包含了玩家的名字或者是UUID 又或者默认是生效的 那么他发送的消息将添加上小尾巴
        if (CACHE_PLAYERS.contains(player.getName().getString())
                || CACHE_PLAYERS_UUID.contains(player.getUUID().toString())
                || Config.isEnableToAllPlayer()
        ) {
            Component finalMessage = event.getMessage().copy().append(Config.getTail());
            event.setMessage(finalMessage);
        }
    }
}