package dev.pixelwhiz.utils;

import cn.nukkit.item.Item;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

public final class LightLevelCalculator {

    private static final Int2IntOpenHashMap lightLevels = new Int2IntOpenHashMap();

    public static void setLightLevel(int fullId, int lightLevel) {
        lightLevels.put(fullId, lightLevel);
    }

    public static int calc(Item item) {
        return lightLevels.getOrDefault(item.getFullId(), item.getBlock().getLightLevel());
    }

}
