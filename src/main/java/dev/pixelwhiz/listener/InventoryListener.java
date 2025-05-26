package dev.pixelwhiz.listener;

import cn.nukkit.Player;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import dev.pixelwhiz.task.FlashlightTask;

public class InventoryListener implements cn.nukkit.inventory.InventoryListener {

    public Player player;
    public FlashlightTask task;

    public InventoryListener(FlashlightTask plugin) {
        this.task = plugin;
    }

    @Override
    public void onInventoryChanged(Inventory inventory, Item oldItem, int slot) {
        this.task.requestLightLevelUpdate();
    }
}
