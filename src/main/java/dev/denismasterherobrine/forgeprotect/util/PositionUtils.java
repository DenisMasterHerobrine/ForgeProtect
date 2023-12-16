package dev.denismasterherobrine.forgeprotect.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class PositionUtils {
    public static BlockPos getLookingAt(Player player, double range) {
        Vec3 pos = player.pick(range, 0.0F, false).getLocation();

        return new BlockPos(pos.x, pos.y, pos.z);
    }
}
