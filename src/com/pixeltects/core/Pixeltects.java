package com.pixeltects.core;

import com.google.common.collect.Lists;
import com.pixeltects.core.commands.Command_DebugStick;
import com.pixeltects.core.commands.Command_Essentials;
import com.pixeltects.core.commands.Command_Outline;
import com.pixeltects.core.commands.entities.Command_KillEntity;
import com.pixeltects.core.commands.entities.Command_MovePlayers;
import com.pixeltects.core.commands.entities.Command_RideEntity;
import com.pixeltects.core.commands.network.Command_Join;
import com.pixeltects.core.commands.network.Command_Playtime;
import com.pixeltects.core.commands.network.Command_Seen;
import com.pixeltects.core.commands.network.Command_UpdateCachedServers;
import com.pixeltects.core.cosmetic.CosmeticManager;
import com.pixeltects.core.cosmetic.commands.Command_BalloonTest;
import com.pixeltects.core.cosmetic.commands.Command_Vomit;
import com.pixeltects.core.database.mongo.DatabaseManager;
import com.pixeltects.core.database.redis.RedisManager;
import com.pixeltects.core.events.GetServerMessageEvent;
import com.pixeltects.core.listeners.PhysicalListeners;
import com.pixeltects.core.listeners.PlayerListeners;
import com.pixeltects.core.player.command.Command_PermissionManager;
import com.pixeltects.core.player.manager.ProfileManager;
import com.pixeltects.core.player.permissions.PermissionManager;
import com.pixeltects.core.player.tracker.PlayerTracker;
import com.pixeltects.core.queues.commands.Command_QueueTest;
import com.pixeltects.core.rides.RideManager;
import com.pixeltects.core.rides.commands.Command_Flatride;
import com.pixeltects.core.rides.listeners.RideListeners;
import com.pixeltects.core.security.AntiListeners;
import com.pixeltects.core.shows.commands.*;
import com.pixeltects.core.shows.ShowManager;
import com.pixeltects.core.shows.effects.armorstand.AAManager;
import com.pixeltects.core.shows.utils.HelpUtil;
import com.pixeltects.core.utils.bungeecord.ServerFetchRequest;
import com.pixeltects.core.utils.executor.BukkitAsyncExecutor;
import com.pixeltects.core.utils.messages.ConsoleFilter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Pixeltects extends JavaPlugin {

    @Getter
    private static Pixeltects packageManager;

    @Getter
    private static BukkitAsyncExecutor asyncExecutor;

    @Getter
    private static WorldEditPlugin worldEditPlugin;

    @Getter
    private ProfileManager profileManager;

    @Getter
    private PermissionManager permissionManager;

    @Getter
    private DatabaseManager databaseManager;

    @Getter
    private RedisManager redisManager;

    @Getter
    private ShowManager showManager;

    @Getter
    private RideManager rideManager;

    @Getter
    private CosmeticManager cosmeticManager;

    @Getter
    private Date timeLoaded;

    @Getter
    private PlayerTracker playerTracker;

    @Getter
    private List<String> networkedServers = Lists.newArrayList();

    public String RILEY_USERNAME = "[REDACTED]";

    public void onLoad() {
        this.packageManager = this;
    }

    public void onEnable() {
        
        if(this.getServer().getPluginManager().getPlugin("WorldEdit") == null) {
            System.out.print("[PIXELTECTS] Plugin failed to load due to missing dependency. Ref: WorldEdit");
            return;
        }

        this.packageManager = this;
        this.worldEditPlugin = (WorldEditPlugin)this.getServer().getPluginManager().getPlugin("WorldEdit");
        this.asyncExecutor = BukkitAsyncExecutor.create(this);
        this.databaseManager = new DatabaseManager(this); //Will load and attempt connection of DB
        this.redisManager = new RedisManager(this);
        this.showManager = new ShowManager(this);
        this.rideManager = new RideManager(this);
        this.cosmeticManager = new CosmeticManager(this);

        this.profileManager = new ProfileManager();
        this.profileManager.init();
        this.profileManager.startTask();

        this.permissionManager = new PermissionManager(this);

        this.playerTracker = new PlayerTracker();

        ConsoleFilter consoleFilter = new ConsoleFilter();
        this.getServer().getLogger().setFilter(consoleFilter); //Attempt at preventing spam

        HelpUtil.loadCommands();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new GetServerMessageEvent());

        //World Downloader Prevention - Protocol: Ask for permission
       // getServer().getMessenger().registerIncomingPluginChannel(this, "WDL|INIT", new AntiListeners());
        //getServer().getMessenger().registerOutgoingPluginChannel(this, "WDL|CONTROL");

        registerListeners();
        registerCommands();

        timeLoaded = new Date();

        ServerFetchRequest.getAllServers(); //Update the networked servers map after loading incoming plugin channels

        AAManager.startTask();
        //Notify Riley
        OfflinePlayer player = Bukkit.getPlayer(RILEY_USERNAME);
        if((player != null) && player.isOnline()) {
            ((Player) player).sendMessage(ChatColor.AQUA + "[PIXELTECTS] Plugin enabled at: " + new SimpleDateFormat("HH:mm:ss z").format(timeLoaded) +
                    " on " + new SimpleDateFormat("EEE, MMM d").format(timeLoaded) + ".");
        }
    }

    public void onDisable() {
        this.databaseManager.closeConnection();
        this.redisManager.getJedisPool().destroy();
        this.permissionManager.clearPermissionMaps();
        this.showManager.stopAllShows(); //Prevent any shows overloading the shutdown process
        this.showManager.stopAllFountains();
        this.showManager.stopAllSpotlights();
        this.showManager.stopAllLasers();
        this.showManager.stopAllParticleBlocks();

        this.cosmeticManager.disable();
        //Notify Riley
        OfflinePlayer player = Bukkit.getPlayer(RILEY_USERNAME);
        if((player != null) && player.isOnline()) {
            Date timeUnloaded = new Date();
            ((Player)player).sendMessage(ChatColor.AQUA + "[PIXELTECTS] Plugin disabled at: " + new SimpleDateFormat("HH:mm:ss z").format(timeUnloaded) +
                    " on " + new SimpleDateFormat("EEE, MMM d").format(timeUnloaded) + ".");
        }

        this.packageManager = null;

        //getServer().getMessenger().unregisterIncomingPluginChannel(this, "WDL|INIT");
        //getServer().getMessenger().unregisterOutgoingPluginChannel(this, "WDL|CONTROL");

    }

    private void registerCommands() {
        getCommand("outline").setExecutor(new Command_Outline());
        getCommand("debugstick").setExecutor(new Command_DebugStick());

        getCommand("vomit").setExecutor(new Command_Vomit());
        getCommand("animate").setExecutor(new Command_Animate());

        getCommand("perms").setExecutor(new Command_PermissionManager());

        getCommand("show").setExecutor(new Command_Show());
        getCommand("repeat").setExecutor(new Command_Repeat());
        getCommand("setblock").setExecutor(new Command_SetBlock());
        getCommand("rebuild").setExecutor(new Command_Rebuild());
        getCommand("tickwait").setExecutor(new Command_TickWait());
        getCommand("fountain").setExecutor(new Command_Fountain());
        getCommand("launch").setExecutor(new Command_Launch());
        getCommand("laser").setExecutor(new Command_Laser());
        getCommand("spotlight").setExecutor(new Command_Spotlight());
        getCommand("particleblock").setExecutor(new Command_ParticleBlock());
        getCommand("buildfirework").setExecutor(new Command_BuildFirework());

        getCommand("killentity").setExecutor(new Command_KillEntity());
        getCommand("moveplayers").setExecutor(new Command_MovePlayers());
        getCommand("setpassenger").setExecutor(new Command_RideEntity());

        //Testing commands
        getCommand("flatride").setExecutor(new Command_Flatride());

        getCommand("tps").setExecutor(new Command_Essentials());
        getCommand("rescuetps").setExecutor(new Command_Essentials());
        getCommand("hat").setExecutor(new Command_Essentials());
        getCommand("time").setExecutor(new Command_Essentials());
        getCommand("gmc").setExecutor(new Command_Essentials());
        getCommand("tp").setExecutor(new Command_Essentials());
        getCommand("speed").setExecutor(new Command_Essentials());
        getCommand("gma").setExecutor(new Command_Essentials());
        getCommand("gms").setExecutor(new Command_Essentials());
        getCommand("gmsp").setExecutor(new Command_Essentials());
        getCommand("clearinventory").setExecutor(new Command_Essentials());
        getCommand("invsee").setExecutor(new Command_Essentials());
        getCommand("seen").setExecutor(new Command_Seen());
        getCommand("join").setExecutor(new Command_Join());
        getCommand("playtime").setExecutor(new Command_Playtime());
        getCommand("updatecachedservers").setExecutor(new Command_UpdateCachedServers());

    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PhysicalListeners(),this);
        pluginManager.registerEvents(new PlayerListeners(),this);
        pluginManager.registerEvents(new RideListeners(),this);
        pluginManager.registerEvents(new AntiListeners(),this);

    }


}
