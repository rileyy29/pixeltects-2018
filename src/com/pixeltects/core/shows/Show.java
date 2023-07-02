package com.pixeltects.core.shows;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.player.PlayerProfile;
import com.pixeltects.core.utils.TPSTracker;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.*;

public class Show extends BukkitRunnable {

    public Queue<String> commandQueue = new LinkedList<>();
    public String showName;

    public double beenWaiting = 0.0D;
    public int balanceRate = 0;
    public int waitingDifference = 0;

    public String whoStarted;

    public Show(String nameOfShow, File file, String startedBy) { //At this point, the file has been checked for existence.
        this.showName = nameOfShow;
        this.whoStarted = startedBy;

        Pixeltects pixeltects = Pixeltects.getPackageManager();
        pixeltects.getShowManager().getRunningShows().add(this);
        pixeltects.getShowManager().getNameToShowMap().put(nameOfShow.toLowerCase(), this);

        ArrayList<String> linesInFile = readFile(file); //Moved reading the file into this class for easier updating and management
        for(String line : linesInFile) {
            commandQueue.add(line);
        }

        if(!startedBy.equalsIgnoreCase("Console")) { //Prevent spamming incase show is on loop
            System.out.print("[ShowManager] Loaded show '" + nameOfShow + "' into server cache. Ran by player: " + startedBy + ".");

            for(Player toNotify : Bukkit.getOnlinePlayers()) {
                if(toNotify.hasPermission("pixeltects.show.notify")) {
                    toNotify.sendMessage(ChatColor.AQUA + "[Show] '" + nameOfShow + "' was started by " + startedBy + ".");
                }
            }
        }
    }

    public void run() {
        if(this.commandQueue != null && !this.commandQueue.isEmpty()) {
            ArrayList<String> lines = new ArrayList();
            for (String command : this.commandQueue) {
                if (command.toLowerCase().contains("wait")) {
                    lines.add(command);
                    break;
                }
                lines.add(command);
            }

            for(String command : lines) {
                String toExecute = this.commandQueue.element();
                String[] args = toExecute.split(" ");

                if(args[0].equalsIgnoreCase("wait")) { //Format: wait 1m2s3ms
                    int timeToWait = calculateTicks(args[1]);
                    double lag = TPSTracker.getTPS() / 20.0D;
                    this.beenWaiting += lag;
                    if (this.balanceRate == 0) {
                        if (Math.ceil(this.beenWaiting) >= timeToWait) {
                            this.beenWaiting = 0.0D;
                            proccessNextCommand();
                            this.balanceRate += 1;
                            this.waitingDifference = 0;
                        }
                    } else if (this.balanceRate > 0) {
                        if (Math.floor(this.beenWaiting) >= timeToWait) {
                            this.beenWaiting = 0.0D;
                            proccessNextCommand();
                            this.balanceRate -= 1;
                            this.waitingDifference = 0;
                        }
                    } else if ((this.balanceRate < 0) &&
                            (Math.ceil(this.beenWaiting) >= timeToWait)) {
                        this.beenWaiting = 0.0D;
                        proccessNextCommand();
                        this.balanceRate += 1;
                        this.waitingDifference = 0;
                    }

                }else if(args[0].equalsIgnoreCase("textall")) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        sb.append(args[i] + " ");
                    }

                    for(Player players : Bukkit.getOnlinePlayers()) {
                        players.sendMessage(ChatColor.translateAlternateColorCodes('&', sb.toString()));
                    }
                    proccessNextCommand();
                    continue;
                }else if(args[0].equalsIgnoreCase("text")) {

                    int x = Integer.parseInt(args[1]);
                    int y = Integer.parseInt(args[2]);
                    int z = Integer.parseInt(args[3]);
                    World world = Bukkit.getWorld(args[4]);

                    if(world != null) {
                        int radius = Integer.parseInt(args[5]);
                        Location location = new Location(world,x,y,z);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 6; i < args.length; i++) {
                            sb.append(args[i] + " ");
                        }

                        for(Player players : Bukkit.getOnlinePlayers()) {
                            if (players.getLocation().distance(location) <= radius) {
                                players.sendMessage(ChatColor.translateAlternateColorCodes('&', sb.toString()));
                            }
                        }
                    }else{
                        runEmergencyStop("&cThe world you set for 'text' is invalid.");
                        break;
                    }

                    proccessNextCommand();
                    continue;
                }else if(args[0].equalsIgnoreCase("log")) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        sb.append(args[i] + " ");
                    }

                    for(Player players : Bukkit.getOnlinePlayers()) {
                        if(players.isOp() || players.hasPermission("pixeltects.show")
                                || players.hasPermission("pixeltects.show.notify")) {
                            players.sendMessage(ChatColor.AQUA + "[" + this.showName + "] " + ChatColor.WHITE +
                                    ChatColor.translateAlternateColorCodes('&', sb.toString()));
                        }
                    }

                    proccessNextCommand();
                    continue;
                }else if(args[0].equalsIgnoreCase("actiontextall")) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        sb.append(args[i] + " ");
                    }

                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&', sb.toString())));
                    }

                    proccessNextCommand();
                    continue;
                }else if(args[0].equalsIgnoreCase("actiontext")) {
                    int x = Integer.parseInt(args[1]);
                    int y = Integer.parseInt(args[2]);
                    int z = Integer.parseInt(args[3]);
                    World world = Bukkit.getWorld(args[4]);

                    if(world != null) {
                        int radius = Integer.parseInt(args[5]);
                        Location location = new Location(world,x,y,z);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 6; i < args.length; i++) {
                            sb.append(args[i] + " ");
                        }

                        for(Player players : Bukkit.getOnlinePlayers()) {
                            if (players.getLocation().distance(location) <= radius) {
                                players.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&', sb.toString())));
                            }
                        }
                    }else{
                        runEmergencyStop("&cThe world you set for 'actiontext' is invalid.");
                        break;
                    }

                    proccessNextCommand();
                    continue;
                }else if(args[0].equalsIgnoreCase("soundeffectall")) {
                    String soundEffect = args[1].toUpperCase();
                    int volume = Integer.parseInt(args[2]);
                    float pitch = Float.parseFloat(args[3]);

                    Sound soundToPlay = Sound.valueOf(soundEffect);
                    if(soundToPlay != null) {
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            players.playSound(players.getLocation(),soundToPlay,volume,pitch);
                        }
                    }else{
                        runEmergencyStop("&cThe sound for 'soundeffectall' is invalid.");
                        break;
                    }

                    proccessNextCommand();
                    continue;
                }else if(args[0].equalsIgnoreCase("soundeffect")) {
                    int x = Integer.parseInt(args[1]);
                    int y = Integer.parseInt(args[2]);
                    int z = Integer.parseInt(args[3]);
                    World world = Bukkit.getWorld(args[4]);

                    if(world != null) {
                        int radius = Integer.parseInt(args[5]);
                        Location location = new Location(world,x,y,z);

                        String soundEffect = args[6].toUpperCase();
                        int volume = Integer.parseInt(args[7]);
                        float pitch = Float.parseFloat(args[8]);

                        Sound soundToPlay = Sound.valueOf(soundEffect);
                        if(soundToPlay != null) {
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                if (players.getLocation().distance(location) <= radius) {
                                    players.playSound(players.getLocation(), soundToPlay, volume, pitch);
                                }
                            }
                        }else{
                            runEmergencyStop("&cThe sound for 'soundeffectall' is invalid.");
                            break;
                        }
                    }else{
                        runEmergencyStop("&cThe world you set for 'actiontext' is invalid.");
                        break;
                    }

                    proccessNextCommand();
                    continue;
                }else{
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), toExecute);
                    proccessNextCommand();
                }
            }
        }
    }

    public void runEmergencyStop(String reason) {
        alertPlayers(reason);
        ShowManager showManager = Pixeltects.getPackageManager().getShowManager();
        this.cancel();
        showManager.getRunningShows().remove(this);
        showManager.getNameToShowMap().remove(this.showName.toLowerCase());
    }

    public void alertPlayers(String reason) {
        for(Player players : Bukkit.getOnlinePlayers()) {
            if(players.isOp() || players.hasPermission("pixeltects.show")
                    || players.hasPermission("pixeltects.show.notify")) {
                players.sendMessage(ChatColor.AQUA + "[" + this.showName + "] " + ChatColor.WHITE +
                        ChatColor.translateAlternateColorCodes('&', reason));
            }
        }
    }

    public void proccessNextCommand() {
        this.commandQueue.remove();
        if(this.commandQueue.iterator().hasNext()) {
            this.commandQueue.iterator().next();
        }else{
            Pixeltects.getPackageManager().getShowManager().getRunningShows().remove(this);
            Pixeltects.getPackageManager().getShowManager().getNameToShowMap().remove(this.showName.toLowerCase());
            cancel();
        }
    }

    public ArrayList<String> readFile(File f) {
        ArrayList<String> lines = new ArrayList();
        try {
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine())
            {
                String t = sc.nextLine();
                lines.add(t);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.print("Show file does not exist.");
        }
        return lines;
    }

    public int calculateTicks(String index) {
        String[] split = index.replace(" ", "").split(",");
        String mins = split[0];
        String secs = split[1];
        String ms = split[2];

        int min = Integer.parseInt(mins.replace("m", ""));
        int seconds = Integer.parseInt(secs.replace("s", ""));
        int mili = Integer.parseInt(ms.replace("ms", ""));

        int newMins = min * 60;

        int tickMins = newMins * 20;
        int tickSecs = seconds * 20;

        int ticks = tickMins + tickSecs + mili;
        return ticks;
    }
}
