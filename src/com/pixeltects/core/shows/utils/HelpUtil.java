package com.pixeltects.core.shows.utils;

import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;

import java.util.LinkedHashMap;

public class HelpUtil {

    public static LinkedHashMap<String, String> commands;

    public static void loadCommands() {
        commands = new LinkedHashMap<>();
        commands.put("wait [0]m,[0]s,0[ms]", "Wait for x mins, x secs, x milliseconds");
        commands.put("tickwait <ticksToWait> <command>", "Run /tickwait ingame");
        commands.put("ALL SPOTLIGHT COMMANDS (/spotlight)", "Run /spotlight ingame");
        commands.put("ALL FIREWORK COMMANDS (/buildfirework)", "Run /buildfirework ingame");
        commands.put("ALL PARTICLE-BLOCK COMMANDS (/particleblock)", "Run /particleblock ingame");
        commands.put("ALL FOUNTAIN COMMANDS (/fountain)", "Run /fountain ingame");
        commands.put("ALL LASER COMMANDS (/laser)", "Run /laser ingame");
        commands.put("textall <message>", "Send chat message to all players");
        commands.put("text <x> <y> <z> <world> <radius> <message>", "Send message to players in radius of coordinates");
        commands.put("actiontextall <message>", "Send actionbar message to all players");
        commands.put("actiontext <x> <y> <z> <world> <radius> <message>", "Send actionbar message to players in radius of coordinates");
        commands.put("log <message>", "Send message to all technicians+");
        commands.put("soundeffectall <soundeffect> <volume> <pitch>", "Send a sound effect to all players");
        commands.put("soundeffect <x> <y> <z> <world> <radius> <soundeffect> <volume> <pitch>", "Send a sound effect to all players");
        commands.put("repeat <timesToRepeat> <command>", "Run /repeat ingame");
        commands.put("setblock <x> <y> <z> <world> <material> [ticks]", "Run /setblock ingame");
        commands.put("rebuild paste <schematicFileName> <x> <y> <z> <world> [-copyAir]", "Run /rebuild ingame");
        commands.put("launch [x] [y] [z] [world] [xmotion] [ymotion] [zmotion] [material] [data] [particle] [endEarly (true/false)]", "Run /launch ingame");
    }

    public static void sendParticleList(CommandSender sender) {
        StringBuilder sb = new StringBuilder();
        for (Particle particle : Particle.values())
            sb.append(ChatColor.WHITE + particle.name().toUpperCase() + ChatColor.GRAY + ", ");
        sender.sendMessage(sb.toString());
    }

    public static void sendSoundList(CommandSender sender) {
        StringBuilder sb = new StringBuilder();
        for (Sound sound : Sound.values())
            sb.append(ChatColor.WHITE + sound.name().toUpperCase() + ChatColor.GRAY + ", ");
        sender.sendMessage(sb.toString());
    }

    public static void sendFireworkTypeList(CommandSender sender) {
        StringBuilder sb = new StringBuilder();
        for (FireworkEffect.Type type : FireworkEffect.Type.values())
            sb.append(ChatColor.WHITE + type.name().toUpperCase() + ChatColor.GRAY + ", ");
        sender.sendMessage(sb.toString());
    }
}
