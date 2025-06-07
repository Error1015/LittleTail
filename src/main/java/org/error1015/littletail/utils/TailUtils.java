package org.error1015.littletail.utils;

import org.error1015.littletail.Config;

public class TailUtils {
    /**
     * 判断玩家名字是否区分大小写 如果是 则返回原样 如果不是 则返回小写形式
     *
     * @param playerName 需要转换的玩家名字
     * @return 操作完成后的玩家名
     */
    public static String getPlayerNameOnConfigValue(String playerName) {
        if (Config.isCaseSensitive) {
            return playerName;
        }
        return playerName.toLowerCase();
    }

    /**
     * 判断object是不是String类型
     */
    public static boolean isString(Object obj) {
        return obj instanceof String;
    }
}