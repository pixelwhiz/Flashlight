package dev.pixelwhiz;

import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import dev.pixelwhiz.listener.EventListener;
import dev.pixelwhiz.utils.LightLevelCalculator;

import java.util.Map;

public class Flashlight extends PluginBase {

    @Override
    public void onEnable() {

        Config config = new Config();
        this.saveDefaultConfig();
        Server.getInstance().getPluginManager().registerEvents(new EventListener(this), this);

        Map<String, Object> itemLightLevels = config.getSection("item-light-levels");
        if (itemLightLevels != null) {
            for (Map.Entry<String, Object> entry : itemLightLevels.entrySet()) {
                String itemString = entry.getKey();
                int lightLevel;
                try {
                    lightLevel = Integer.parseInt(entry.getValue().toString());
                } catch (NumberFormatException e) {
                    this.getLogger().warning("Invalid light level for item: " + itemString);
                    continue;
                }

                int itemId = Item.get(itemString).getFullId();
                if (itemId == 0) {
                    this.getLogger().warning("Invalid item name: "+ itemString);
                    continue;
                }

                Item item = Item.get(itemString);
                if (item == null) {
                    this.getLogger().warning("Item not found for String: " + itemString);
                    continue;
                }

                LightLevelCalculator.setLightLevel(item.getFullId(), Math.max(0, Math.min(15, lightLevel)));

            }
        }

    }

}