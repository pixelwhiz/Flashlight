package dev.pixelwhiz.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerItemHeldEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import dev.pixelwhiz.Flashlight;

import java.util.UUID;

public class EventListener implements Listener {

    public Flashlight plugin;

    public EventListener(Flashlight plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        if (!this.plugin.flashLights.contains(player.getName())) {
            return;
        }

        this.plugin.flashLights.add(player.getUniqueId().toString());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        this.plugin.flashLights.remove(uuid.toString());
    }

}
