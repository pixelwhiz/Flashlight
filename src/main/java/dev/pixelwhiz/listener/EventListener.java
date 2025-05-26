package dev.pixelwhiz.listener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerItemHeldEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.scheduler.TaskHandler;
import dev.pixelwhiz.Flashlight;
import dev.pixelwhiz.task.FlashlightTask;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventListener implements Listener {

    public Flashlight plugin;

    public Map<Player, TaskHandler> tasks = new ConcurrentHashMap<>();

    public EventListener(Flashlight plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        TaskHandler handler = tasks.get(player);

        if (handler != null && plugin.flashLights.contains(player.getName())) {
            Runnable task = this.tasks.get(player).getTask();
            ((FlashlightTask) task).requestLightLevelUpdate();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.tasks.put(player, Server.getInstance().getScheduler().scheduleRepeatingTask(
                new FlashlightTask(player),
                (int)(this.plugin.getConfig().getDouble("update-delay", 0.25) * 20)
        ));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        TaskHandler handler = tasks.remove(player);

        if (handler != null) {
            handler.cancel();
        }
    }

}
