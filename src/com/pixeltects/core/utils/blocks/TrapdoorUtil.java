package com.pixeltects.core.utils.blocks;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.Door;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;

public class TrapdoorUtil {
    public static boolean setOpen(Block block) { //Used for event listener
        BlockState s = block.getState();
        if (((s.getData() instanceof Door)) && (((Door)s.getData()).isTopHalf())) {
            s = block.getRelative(BlockFace.DOWN).getState();
        }
        Openable d = (Openable)s.getData();
        d.setOpen(!d.isOpen());
        s.setData((MaterialData)d);
        s.update();
        return d.isOpen();
    }

}
