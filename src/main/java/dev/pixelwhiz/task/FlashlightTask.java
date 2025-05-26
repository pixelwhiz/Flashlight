package dev.pixelwhiz.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLiquid;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.scheduler.Task;
import dev.pixelwhiz.Flashlight;
import dev.pixelwhiz.utils.LightLevelCalculator;

public class FlashlightTask extends Task {

    public Flashlight plugin;

    private int lightLevel = 0;
    private Position pos = null;

    private boolean requireLightLevelUpdate = false;

    public FlashlightTask(Flashlight plugin) {
        this.plugin = plugin;
        this.requestLightLevelUpdate();
    }

    @Override
    public void onRun(int currentTick) {
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {

            if (this.requireLightLevelUpdate) {
                this.setLightLevel(Math.max(
                        LightLevelCalculator.calc(player.getInventory().getItemInHand()),
                        LightLevelCalculator.calc(player.getOffhandInventory().getItem(0))
                ));

                this.requireLightLevelUpdate = true;
            }

            if (this.lightLevel <= 0) {
                return;
            }

            Position pos = player.getPosition();
            Position newPos = Position.fromObject(pos.add(0.5, 1, 0.5).floor(), pos.getLevel());
            if (this.pos == null || !this.pos.equals(newPos)) {
                this.restoreBlock();
                this.pos = newPos;
                this.overrideBlock();
            }
        }

    }

    private void restoreBlock() {
        if (this.pos == null) {
            return;
        }

        sendBlockLayers(this.pos.getLocation(), this.pos.getLevel().getBlock(this.pos), Block.get(Block.AIR));
    }

    private static void sendBlockLayers(Location pos, Block normalLayer, Block liquidLayer) {
        BlockVector3 blockPos = new BlockVector3(pos.getFloorX(), pos.getFloorY(), pos.getFloorZ());

        UpdateBlockPacket normalPacket = new UpdateBlockPacket();
        normalPacket.x = blockPos.getX();
        normalPacket.y = blockPos.getY();
        normalPacket.z = blockPos.getZ();
        normalPacket.blockRuntimeId = normalLayer.getRuntimeId();
        normalPacket.flags = UpdateBlockPacket.FLAG_ALL_PRIORITY;
        normalPacket.dataLayer = 0;

        UpdateBlockPacket liquidPacket = new UpdateBlockPacket();
        liquidPacket.x = blockPos.getX();
        liquidPacket.y = blockPos.getY();
        liquidPacket.z = blockPos.getZ();
        liquidPacket.blockRuntimeId = liquidLayer.getRuntimeId();
        liquidPacket.flags = UpdateBlockPacket.FLAG_ALL_PRIORITY;
        liquidPacket.dataLayer = 1;

        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            player.dataPacket(normalPacket);
            player.dataPacket(liquidPacket);
        }
    }

    public Block getLightLevelBlock(int lightLevel) {
        switch (lightLevel) {
            case 0:
                return Block.get(Block.LIGHT_BLOCK_0);
            case 1:
                return Block.get(Block.LIGHT_BLOCK_1);
            case 2:
                return Block.get(Block.LIGHT_BLOCK_2);
            case 3:
                return Block.get(Block.LIGHT_BLOCK_3);
            case 4:
                return Block.get(Block.LIGHT_BLOCK_4);
            case 5:
                return Block.get(Block.LIGHT_BLOCK_5);
            case 6:
                return Block.get(Block.LIGHT_BLOCK_6);
            case 7:
                return Block.get(Block.LIGHT_BLOCK_7);
            case 8:
                return Block.get(Block.LIGHT_BLOCK_8);
            case 9:
                return Block.get(Block.LIGHT_BLOCK_9);
            case 10:
                return Block.get(Block.LIGHT_BLOCK_10);
            case 11:
                return Block.get(Block.LIGHT_BLOCK_11);
            case 12:
                return Block.get(Block.LIGHT_BLOCK_12);
            case 13:
                return Block.get(Block.LIGHT_BLOCK_13);
            case 14:
                return Block.get(Block.LIGHT_BLOCK_14);
            case 15:
                return Block.get(Block.LIGHT_BLOCK_15);
        }

        return Block.get(Block.LIGHT_BLOCK_0);
    }

    private void overrideBlock() {
        if (this.pos == null) {
            return;
        }

        Block normalLayer = this.pos.level.getBlock(this.pos);
        Block liquidLayer = this.getLightLevelBlock(this.lightLevel);
        if (normalLayer instanceof BlockLiquid) {
            Block temp = normalLayer;
            normalLayer = liquidLayer;
            liquidLayer = temp;
        }

        sendBlockLayers(this.pos.getLocation(), normalLayer, liquidLayer);
    }

    private void requestLightLevelUpdate() {
        this.requireLightLevelUpdate = true;
    }

    private void setLightLevel(int lightLevel) {
        int newLightLevel = Math.max(0, lightLevel & 0xf - 1);
        if (this.lightLevel == newLightLevel) {
            return;
        }

        this.lightLevel = newLightLevel;
    }

}
