package com.pixeltects.core.shows.tools;

import com.pixeltects.core.Pixeltects;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class FountainBuilder {

    public static FallingBlock spawnFountainBlock(Location location, Vector velocity, String type, int data) {
        FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(location, Material.getMaterial(type.toUpperCase()), (byte)data);
        fallingBlock.setDropItem(false);
        fallingBlock.setMetadata("show-block",new FixedMetadataValue(Pixeltects.getPackageManager(), true));
        fallingBlock.setVelocity(velocity);
        return fallingBlock;
    }


}
