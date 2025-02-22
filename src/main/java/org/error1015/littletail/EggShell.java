package org.error1015.littletail;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;

public class EggShell {
    /**
     * 检查Mod作者 如果检测到了抛出异常
     */
    static void checkAuthors(String author) {
        for (IModInfo mod : ModList.get().getMods()) {
            String authors = mod.getConfig().getConfigElement("authors").isPresent() ? mod.getConfig().getConfigElement("authors").get().toString() : "Unknown";
            if (authors.equals(author)) {
                throw new RuntimeException("被" + author + "的史蹦飞了: " + mod.getDisplayName() + "\n如果真的需要使用此Mod,请关闭聊天小尾巴的彩蛋模式");
            }
        }
    }
}