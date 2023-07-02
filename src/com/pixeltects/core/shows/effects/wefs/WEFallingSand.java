package com.pixeltects.core.shows.effects.wefs;

import com.pixeltects.core.Pixeltects;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WEFallingSand {

    public static String BASE_PATH = "/wefs/";

    public static void convertWESelection(Player player, String fileName, String fbName, String asName){
        WorldEditPlugin worldEditPlugin = Pixeltects.getWorldEditPlugin();
        File file = new File(Pixeltects.getPackageManager().getDataFolder() + BASE_PATH, fileName + ".fallingblocks");
        if(file.exists()) {
            player.sendMessage(ChatColor.RED + "A file with that name already exists.");
            return;
        }
        try {
            Region selection = worldEditPlugin.getSession(player).getSelection(BukkitAdapter.adapt(player.getWorld()));
            if(selection == null) {
                player.sendMessage(ChatColor.RED + "Please make a selection using WorldEdit.");
                return;
            }

            BlockVector3 maxPoint = selection.getMaximumPoint();
            BlockVector3 minPoint = selection.getMinimumPoint();

            ArrayList<String> commands = new ArrayList<>();

            for (int x = minPoint.getBlockX(); x <= maxPoint.getBlockX(); x++) {
                for (int y = minPoint.getBlockY(); y <= maxPoint.getBlockY(); y++) {
                    for (int z = minPoint.getBlockZ(); z <= maxPoint.getBlockZ(); z++) {
                        Block block = player.getWorld().getBlockAt(new Location(player.getWorld(), x,y,z));
                        if((block.getType() != null) && !block.getType().equals(Material.AIR)) {
                            //Might have to minus Y by 1. //The time set will last several days, 3 ingame years. So every restart, reload all falling blocks??
                            commands.add("summon armor_stand " + x + " " + y + " " + z + " " +
                                    "{CustomName:\"{\"text\":" + asName + "\"}\",NoGravity:1,Marker:1b,Invisible:1," +
                                    "Passengers:[{id:\"falling_block\",CustomName:\"{\"text\":" + fbName + "\"}\",Time:-2147483648,DropItem:0,BlockState:" +
                                    "{Name:minecraft:" + block.getType().toString().toLowerCase() + ", " +
                                    "Data:" + block.getData() + "}}]}");

                            /*Location loc = new Location(player.getWorld(), x + 0.5,y,z + 0.5);
                            ArmorStand armorStand = (ArmorStand)player.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
                            armorStand.setGravity(false);
                            armorStand.setBasePlate(false);
                            armorStand.setVisible(false);
                            armorStand.setMarker(true);
                            armorStand.setCustomName(asName);
                            armorStand.setCustomNameVisible(false);
                            armorStand.setInvulnerable(true);

                            FallingBlock fallingBlock = spawnFallingBlock(loc, block.getType(), block.getData());
                            fallingBlock.setDropItem(false);
                            fallingBlock.setInvulnerable(true);
                            fallingBlock.setCustomName(fbName);
                            fallingBlock.setCustomNameVisible(false);

                            armorStand.setPassenger(fallingBlock);*/
                        }
                    }
                }
            }

            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            BufferedWriter writer = new BufferedWriter(fileWriter);

            for(String command : commands) {
                writer.write(command);
                writer.newLine();
            }

            writer.close();

        } catch (IncompleteRegionException | IOException exception) {
            player.sendMessage(ChatColor.RED + "Please make a selection using WorldEdit.");
            return;
        }

    }

    private static FallingBlock spawnFallingBlock(Location location, Material material, int data) {
        FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(location, material, (byte)data);
        fallingBlock.setDropItem(false);
        fallingBlock.setMetadata("wefs-block",new FixedMetadataValue(Pixeltects.getPackageManager(), true));
        return fallingBlock;
    }

}
