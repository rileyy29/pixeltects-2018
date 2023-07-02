package com.pixeltects.core.shows.effects.armorstand;

import com.pixeltects.core.Pixeltects;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;

import java.io.File;
import java.io.IOException;

public class AAManager {

    public static String BASE_PATH = "/animatronics/";

    @Getter
    private static BukkitTask bukkitTask;

    public static void startTask() {
        if(bukkitTask != null) {
            bukkitTask.cancel();
        }

        //Space for redis rank listener

        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Pixeltects.getPackageManager(), () -> ArmorStandAnimator.updateAll(), 0L, 1L);

    }

    public static void playAnimation(CommandSender sender, String id, ArmorStand armorStand) {

    }

    public static void stopAnimation(String id) {

    }

    public static void stopAllAnimations() {

    }

    public static void createAnimation(CommandSender sender, String id) {
        File file = new File(Pixeltects.getPackageManager().getDataFolder() + BASE_PATH, id + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();

                if(file.exists()) {
                    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                    configuration.set("frame", "0");
                    configuration.save(file);
                }
            }catch(IOException exception) { }
        }
    }

    public static void removeAnimation(String id) {
        File file = new File(Pixeltects.getPackageManager().getDataFolder() + BASE_PATH, id + ".animatronic");
        if (file.exists()) {
            file.delete();
        }
    }

    public static void addFrame(CommandSender sender, String id, EulerAngle eulerAngle, double ticks) {
        File file = new File(Pixeltects.getPackageManager().getDataFolder() + BASE_PATH, id + ".yml");
        if (file.exists()) {
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            int nextFrame = configuration.getInt("frame");

            configuration.set("frame", (nextFrame + 1));
            configuration.set("frames." + nextFrame + ".x", eulerAngle.getX());
            configuration.set("frames." + nextFrame + ".y", eulerAngle.getY());
            configuration.set("frames." + nextFrame + ".z", eulerAngle.getZ());
            configuration.set("frames." + nextFrame + ".ticks", ticks);

            try {
                configuration.save(file);
            }catch(IOException exception) { }
        }
    }

    public static void removeFrame() {

    }




}
