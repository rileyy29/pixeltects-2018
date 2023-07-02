package com.pixeltects.core.utils.messages;

import org.bukkit.ChatColor;

public enum MessageOutput {

    STAFF_INVENTORY_FULL(ChatColor.RED + "Your player inventory is full."),

    UNKNOWN_COMMAND(ChatColor.RED + "Pixeltects does not support this command."),

    MODULE_FAILED_LOGIN(ChatColor.RED + "Failed to load the player module, please contact a Cast Member. [1]"),
    MODULE_FAILED_JOIN(ChatColor.RED + "Failed to load the player module, please contact a Cast Member. [2]"),

    INVALID_WORLD(ChatColor.RED + "That world does not exist."),
    INVALID_MATERIAL(ChatColor.RED + "That material does not exist."),
    INVALID_PARTICLE(ChatColor.RED + "That particle does not exist."),
    INVALID_FIREWORK_TYPE(ChatColor.RED + "That type of firework does not exist."),

    FAILED_TO_CONNECT_TO_MODULE(ChatColor.RED + "[OP NOTICE] The %module% is currently offline."),
    TARGET_PLAYER_OFFLINE(ChatColor.RED + "That player is currently offline."),
    COMMAND_NO_PERMISSION(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is a mistake."),
    COMMAND_NOT_PLAYER(ChatColor.RED + "You must be a player to execute this command."),
    COMMAND_INVALID_PARAMETERS(ChatColor.RED + "You must enter a set of valid parameters."),
    COMMAND_CONSOLE_ONLY(ChatColor.RED + "Only the Console can execute this command."),

    COMMAND_SHOW_NOTFOUND(ChatColor.RED + "The show '%show%' does not exist. Remember, names are case-sensitive."),
    COMMAND_SHOW_NOTRUNNING(ChatColor.RED + "The show '%show%' is not running."),
    COMMAND_SHOW_STOPPED(ChatColor.RED + "You have stopped the show '%show%.'"),
    COMMAND_SHOW_STOPPEDALL(ChatColor.RED + "You have stopped ALL the shows that are currently running."),
    COMMAND_SHOW_START(ChatColor.GREEN + "You have started the show '%show%.'"),

    COMMAND_FOUNTAIN_NONACTIVE(ChatColor.RED + "There are currently no fountains active."),
    COMMAND_FOUNTAIN_ALREADYEXISTS(ChatColor.RED + "A fountain with that id already exists."),
    COMMAND_FOUNTAIN_ALREADYEXISTS_SPECIFIC(ChatColor.RED + "A fountain with the id '%fountain%' already exists."),
    COMMAND_FOUNTAIN_NOTRUNNING(ChatColor.RED + "The fountain '%fountain%' is not running."),
    COMMAND_FOUNTAIN_STOPPED(ChatColor.RED + "You have stopped the fountain '%fountain%.'"),
    COMMAND_FOUNTAIN_STOPPEDALL(ChatColor.RED + "You have stopped ALL the fountains that are currently running."),
    COMMAND_FOUNTAIN_CHANGEDMATERIAL(ChatColor.GREEN + "You have changed the material of: %fountain%."),

    COMMAND_LASER_NONACTIVE(ChatColor.RED + "There are currently no lasers active."),
    COMMAND_LASER_ALREADYEXISTS(ChatColor.RED + "A laser with that id already exists."),
    COMMAND_LASER_ALREADYEXISTS_SPECIFIC(ChatColor.RED + "A laser with the id '%laser%' already exists."),
    COMMAND_LASER_NOTRUNNING(ChatColor.RED + "The laser '%laser%' is not running."),
    COMMAND_LASER_STOPPED(ChatColor.RED + "You have stopped the laser '%laser%.'"),
    COMMAND_LASER_STOPPEDALL(ChatColor.RED + "You have stopped ALL the lasers that are currently running."),

    COMMAND_SPOTLIGHT_NONACTIVE(ChatColor.RED + "There are currently no spotlights active."),
    COMMAND_SPOTLIGHT_ALREADYEXISTS(ChatColor.RED + "A spotlight with that id already exists."),
    COMMAND_SPOTLIGHT_ALREADYEXISTS_SPECIFIC(ChatColor.RED + "A spotlight with the id '%spotlight%' already exists."),
    COMMAND_SPOTLIGHT_NOTRUNNING(ChatColor.RED + "The spotlight '%spotlight%' is not running."),
    COMMAND_SPOTLIGHT_STOPPED(ChatColor.RED + "You have stopped the spotlight '%spotlight%.'"),
    COMMAND_SPOTLIGHT_STOPPEDALL(ChatColor.RED + "You have stopped ALL the spotlights that are currently running."),

    COMMAND_PARTICLEBLOCK_NONACTIVE(ChatColor.RED + "There are currently no particle-blocks active."),
    COMMAND_PARTICLEBLOCK_ALREADYEXISTS(ChatColor.RED + "A particle-block with that id already exists."),
    COMMAND_PARTICLEBLOCK_NOTRUNNING(ChatColor.RED + "The particle-block '%block%' is not running."),
    COMMAND_PARTICLEBLOCK_STOPPED(ChatColor.RED + "You have stopped the particle-block '%block%.'"),
    COMMAND_PARTICLEBLOCK_STOPPEDALL(ChatColor.RED + "You have stopped ALL the particle-blocks that are currently running."),


    FLATRIDE_ALREADY_RUNNING(ChatColor.RED + "That flatride is currently running."),
    FLATRIDE_STARTED(ChatColor.GREEN + "The flatride '%name%' was started."),
    FLATRIDE_STOPPED_FORCE(ChatColor.RED + "The flatride '%name%' was force stopped."),
    FLATRIDE_STOPPED_FORCE_ALL(ChatColor.RED + "All flatrides were force stopped."),
    FLATRIDE_NOT_RUNNING(ChatColor.RED + "That flatride is not running."),
    FLATRIDE_SPAWN(ChatColor.AQUA + "A new flatride was spawned called '%name%.'"),
    FLATRIDE_DESPAWN(ChatColor.RED + "The flatride '%name%' was despawned."),
    FLATRIDE_ALREADY_EXISTS(ChatColor.RED + "A flatride with that name has already been spawned."),
    FLATRIDE_NO_EXIST(ChatColor.RED + "That flatride is not spawned."),
    FLATRIDE_NON_SPAWNED(ChatColor.RED + "There are no flatrides spawned."),
    FLATRIDE_NON_ACTIVE(ChatColor.RED + "There are no flatrides available to spawn."),
    FLATRIDE_SPAWNED_ALL(ChatColor.GREEN + "All flatrides were respawned."),
    FLATRIDE_DESPAWNED_ALL(ChatColor.RED + "All flatrides were despawned.");

   private String message;

   private MessageOutput(String message) {
       this.message = message;
   }

   public String getOutput() {
       return this.message;
   }



}
