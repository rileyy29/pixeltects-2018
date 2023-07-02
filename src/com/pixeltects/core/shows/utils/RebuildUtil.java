package com.pixeltects.core.shows.utils;

import com.pixeltects.core.Pixeltects;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.FileInputStream;

public class RebuildUtil {

    public static boolean pasteSchematic(World world, File schematicFile, Vector origin, boolean copyAir) { //No feedback version
        try {
            WorldEditPlugin worldEditPlugin = Pixeltects.getWorldEditPlugin();
            ClipboardFormat clipboardFormat = ClipboardFormats.findByFile(schematicFile);
            ClipboardReader clipboardReader = clipboardFormat.getReader(new FileInputStream(schematicFile));
            Clipboard clipboard = clipboardReader.read();
            com.sk89q.worldedit.world.World bukkitWorld = BukkitAdapter.adapt(world);
            EditSession editSession = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(bukkitWorld, -1);
            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(BlockVector3.at(origin.getX(), origin.getY(), origin.getZ())).ignoreAirBlocks(copyAir).build();
            Operations.complete(operation);
            editSession.flushSession();
            return true;
        }catch(Exception exception) {
            System.out.print("[ShowManager] Error pasting schematic - RebuildUtil class fatal exception.");
            return false;
        }
    }

    public static boolean pasteSchematic(World world, File schematicFile, Vector origin, boolean copyAir, CommandSender sender) { //Feedback sent
        try {
            WorldEditPlugin worldEditPlugin = Pixeltects.getWorldEditPlugin();
            ClipboardFormat clipboardFormat = ClipboardFormats.findByFile(schematicFile);
            ClipboardReader clipboardReader = clipboardFormat.getReader(new FileInputStream(schematicFile));
            Clipboard clipboard = clipboardReader.read();
            com.sk89q.worldedit.world.World bukkitWorld = BukkitAdapter.adapt(world);
            EditSession editSession = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(bukkitWorld, -1);
            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(BlockVector3.at(origin.getX(), origin.getY(), origin.getZ())).ignoreAirBlocks(copyAir).build();
            Operations.complete(operation);
            editSession.flushSession();

            if(sender instanceof Player) {
                sender.sendMessage(ChatColor.GREEN + "[Rebuild] Successfully pasted the schematic.");
            }

            return true;
        }catch(Exception exception) {
            sender.sendMessage(ChatColor.RED + "[Rebuild] Failed to paste the schematic.");
            System.out.print("[ShowManager] Error pasting schematic - RebuildUtil class fatal exception.");
            return false;
        }
    }

}
