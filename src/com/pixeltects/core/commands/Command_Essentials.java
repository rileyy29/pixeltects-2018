package com.pixeltects.core.commands;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.utils.math.MathArgUtils;
import com.pixeltects.core.utils.messages.MessageOutput;
import net.minecraft.server.v1_14_R1.MinecraftServer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Command_Essentials implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("clearinventory")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp() || player.hasPermission("pixeltects.global.clearinventory.self") || player.hasPermission("pixeltects.build")) {
                    if (args.length == 0) {
                        player.getInventory().clear();
                        player.getInventory().setArmorContents(null);
                        player.sendMessage(ChatColor.GREEN + "Cleared your inventory.");
                    }
                    if (args.length >= 1) {
                        if (player.isOp() || player.hasPermission("pixeltects.global.clearinventory.others")) {
                            Player target = Bukkit.getPlayer(args[0]);
                            if (target == null) {
                                player.sendMessage(MessageOutput.TARGET_PLAYER_OFFLINE.getOutput());
                            } else {
                                target.getInventory().clear();
                                target.getInventory().setArmorContents(null);
                                target.sendMessage(ChatColor.GREEN + "Your inventory was cleared by another player.");
                                player.sendMessage(ChatColor.GREEN + "You have cleared " + target.getName() + "'s inventory.");
                            }
                        }else{
                            player.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                        }
                    }
                } else {
                    player.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                }
            } else {
                sender.sendMessage(MessageOutput.COMMAND_NOT_PLAYER.getOutput());
            }
    }
        if (cmd.getName().equalsIgnoreCase("gmc")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp() || player.hasPermission("pixeltects.global.gamemode.self") || player.hasPermission("pixeltects.build")) {
                    if (args.length == 0) {
                        player.setGameMode(GameMode.CREATIVE);
                        player.sendMessage(ChatColor.GREEN + "Changed your gamemode to creative.");
                    }
                    if (args.length >= 1) {
                        if (player.isOp() || player.hasPermission("pixeltects.global.gamemode.others")) {
                            Player target = Bukkit.getPlayer(args[0]);
                            if (target == null) {
                                player.sendMessage(MessageOutput.TARGET_PLAYER_OFFLINE.getOutput());
                            } else {
                                target.setGameMode(GameMode.CREATIVE);
                                target.sendMessage(ChatColor.GREEN + "Your gamemode was changed by another player.");
                                player.sendMessage(ChatColor.GREEN + "You have changed " + target.getName() + "'s gamemode to creative.");
                            }
                        } else {
                            player.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                        }
                    }
                } else {
                    player.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                }
            } else {
                sender.sendMessage(MessageOutput.COMMAND_NOT_PLAYER.getOutput());
            }
    }
        if (cmd.getName().equalsIgnoreCase("gms")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp() || player.hasPermission("pixeltects.global.gamemode.self") || player.hasPermission("pixeltects.build")) {
                    if (args.length == 0) {
                        player.setGameMode(GameMode.SURVIVAL);
                        player.sendMessage(ChatColor.GREEN + "Changed your gamemode to survival.");
                    }
                    if (args.length >= 1) {
                        if (player.isOp() || player.hasPermission("pixeltects.global.gamemode.others")) {
                            Player target = Bukkit.getPlayer(args[0]);
                            if (target == null) {
                                player.sendMessage(MessageOutput.TARGET_PLAYER_OFFLINE.getOutput());
                            } else {
                                target.setGameMode(GameMode.SURVIVAL);
                                target.sendMessage(ChatColor.GREEN + "Your gamemode was changed by another player.");
                                player.sendMessage(ChatColor.GREEN + "You have changed " + target.getName() + "'s gamemode to survival.");
                            }
                        } else {
                            player.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                        }
                    }
                } else {
                    player.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                }
            } else {
                sender.sendMessage(MessageOutput.COMMAND_NOT_PLAYER.getOutput());
            }
    }
        if (cmd.getName().equalsIgnoreCase("gma")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp() || player.hasPermission("pixeltects.global.gamemode.self") || player.hasPermission("pixeltects.build")) {
                    if (args.length == 0) {
                        player.setGameMode(GameMode.ADVENTURE);
                        player.sendMessage(ChatColor.GREEN + "Changed your gamemode to adventure.");
                    }
                    if (args.length >= 1) {
                        if (player.isOp() || player.hasPermission("pixeltects.global.gamemode.others")) {
                            Player target = Bukkit.getPlayer(args[0]);
                            if (target == null) {
                                player.sendMessage(MessageOutput.TARGET_PLAYER_OFFLINE.getOutput());
                            } else {
                                target.setGameMode(GameMode.ADVENTURE);
                                target.sendMessage(ChatColor.GREEN + "Your gamemode was changed by another player.");
                                player.sendMessage(ChatColor.GREEN + "You have changed " + target.getName() + "'s gamemode to adventure.");
                            }
                        } else {
                            player.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                        }
                    }
                } else {
                    player.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                }
            } else {
                sender.sendMessage(MessageOutput.COMMAND_NOT_PLAYER.getOutput());
            }
    }
        if (cmd.getName().equalsIgnoreCase("gmsp")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp() || player.hasPermission("pixeltects.global.gamemode.self") || player.hasPermission("pixeltects.build")) {
                    if (args.length == 0) {
                        player.setGameMode(GameMode.SPECTATOR);
                        player.sendMessage(ChatColor.GREEN + "Changed your gamemode to spectator.");
                    }
                    if (args.length >= 1) {
                        if (player.isOp() || player.hasPermission("pixeltects.global.gamemode.others")) {
                            Player target = Bukkit.getPlayer(args[0]);
                            if (target == null) {
                                player.sendMessage(MessageOutput.TARGET_PLAYER_OFFLINE.getOutput());
                            } else {
                                target.setGameMode(GameMode.SPECTATOR);
                                target.sendMessage(ChatColor.GREEN + "Your gamemode was changed by another player.");
                                player.sendMessage(ChatColor.GREEN + "You have changed " + target.getName() + "'s gamemode to spectator.");
                            }
                        } else {
                            player.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                        }
                    }
                } else {
                    player.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                }
            } else {
                sender.sendMessage(MessageOutput.COMMAND_NOT_PLAYER.getOutput());
            }
    }
        if (cmd.getName().equalsIgnoreCase("invsee")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp() || player.hasPermission("pixeltects.global.invsee")) {
                    if (args.length == 0)
                        player.sendMessage(ChatColor.RED + "/invsee <playerName>");
                    if (args.length >= 1) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target == null) {
                            player.sendMessage(MessageOutput.TARGET_PLAYER_OFFLINE.getOutput());
                        } else if (target.getName().equalsIgnoreCase(player.getName())) {
                            player.sendMessage(ChatColor.RED + "You cannot view your own inventory.");
                        } else {
                            player.openInventory(target.getInventory());
                            player.sendMessage(ChatColor.GREEN + "You have opened " + target.getName() + "'s inventory.");
                        }
                    }
                } else {
                    player.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                }
            } else {
                sender.sendMessage(MessageOutput.COMMAND_NOT_PLAYER.getOutput());
            }
        }
        if (cmd.getName().equalsIgnoreCase("tps")) {
            printTPS(sender);
        }
        if (cmd.getName().equalsIgnoreCase("time")) {
            boolean hasPermission = false;
            if (sender instanceof Player) {
                if (sender.isOp() || sender.hasPermission("pixeltects.global.time")) {
                    hasPermission = true;
                }
            }else{
                hasPermission = true;
            }
               if(hasPermission) {
                   if (args.length == 0) {
                       sender.sendMessage(ChatColor.RED + "Usage: /time set <time>");
                       sender.sendMessage(ChatColor.RED + "Usage: /time set <time> [world]");
                   }
                   if (args.length == 1) {
                       sender.sendMessage(ChatColor.RED + "Usage: /time set <time>");
                       sender.sendMessage(ChatColor.RED + "Usage: /time set <time> [world]");
                   }
                   if (args.length == 2) {
                       if (args[0].equalsIgnoreCase("set")) {
                           if (MathArgUtils.isLong(args[1])) {
                               if (sender instanceof Player) {
                                   Player player = (Player) sender;
                                   World world = player.getWorld();
                                   world.setTime(Long.parseLong(args[1]));
                                   player.sendMessage(ChatColor.GREEN + "The time has been changed to " + args[1] + " in your current world.");
                               } else if (sender instanceof BlockCommandSender) {
                                   Block block = ((BlockCommandSender) sender).getBlock();
                                   World world = block.getWorld();
                                   world.setTime(Long.parseLong(args[1]));
                                   sender.sendMessage(ChatColor.GREEN + "The time has been changed in your current world.");
                               } else {
                                   sender.sendMessage(ChatColor.RED + "This command is not available to Console. Try executing /time set <time> <world> instead.");
                               }
                           } else {
                               sender.sendMessage(ChatColor.RED + "That time value is invalid.");
                           }
                       } else {
                           sender.sendMessage(ChatColor.RED + "Usage: /time set <time>");
                           sender.sendMessage(ChatColor.RED + "Usage: /time set <time> [world]");
                       }
                       if (args.length >= 3)
                           if (args[0].equalsIgnoreCase("set")) {
                               if (MathArgUtils.isLong(args[1])) {
                                   String worldName = args[2];
                                   World world = Bukkit.getWorld(worldName);
                                   world.setTime(Long.parseLong(args[1]));
                                   if (sender instanceof Player)
                                       sender.sendMessage(ChatColor.GREEN + "The time has been changed to " + args[1] + " in the world '" + worldName + ".'");
                               } else {
                                   sender.sendMessage(ChatColor.RED + "That time value is invalid.");
                               }
                           } else {
                               sender.sendMessage(ChatColor.RED + "Usage: /time set <time>");
                               sender.sendMessage(ChatColor.RED + "Usage: /time set <time> [world]");
                           }
                   }
               }else {
                    sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
            }
        }
        if (cmd.getName().equalsIgnoreCase("tp")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp() || player.hasPermission("pixeltects.global.tp") || player.hasPermission("pixeltects.eme")
                        || player.hasPermission("pixeltects.cm")) {
                    if (args.length == 0) {
                        player.sendMessage(ChatColor.RED + "Usage: /tp <player>");
                        player.sendMessage(ChatColor.RED + "Usage: /tp [world] [x] [y] [z]");
                        if (player.isOp() || player.hasPermission("pixeltects.global.tpothers") || player.hasPermission("pixeltects.cm")) {
                            player.sendMessage(ChatColor.RED + "Usage: /tp <player1> <player2>");
                            player.sendMessage(ChatColor.RED + "Usage: /tp <player1> [world] [x] [y] [z]");
                        }
                    } else if (args.length == 1) {
                        String playerName = args[0];
                        Player target = Bukkit.getPlayer(playerName);
                        if (target != null) {
                            player.teleport(target);
                            player.sendMessage(ChatColor.AQUA + "Teleported to " + target.getName() + ".");
                        } else {
                            player.sendMessage(MessageOutput.TARGET_PLAYER_OFFLINE.getOutput());
                        }
                    } else if (args.length == 2) {
                        if (player.isOp() || player.hasPermission("pixeltects.global.tpothers") || player.hasPermission("pixeltects.cm")) {
                            String playerName = args[0];
                            String playerName2 = args[1];
                            Player target = Bukkit.getPlayer(playerName);
                            Player target2 = Bukkit.getPlayer(playerName2);
                            if (target != null) {
                                if (target2 != null) {
                                    target.teleport(target2);
                                    target.sendMessage(ChatColor.AQUA + "You have been teleported to " + target2.getName() + ".");
                                } else {
                                    player.sendMessage(ChatColor.RED + "Player 2 is offline.");
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "Player 1 is offline.");
                            }
                        } else {
                            player.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                        }
                    } else if (args.length == 5) {
                        if (player.isOp() || player.hasPermission("pixeltects.global.tpothers") || player.hasPermission("pixeltects.cm")) {
                            String playerName = args[0];
                            Player target = Bukkit.getPlayer(playerName);
                            if (target != null) {
                                String wName = args[1];
                                if (MathArgUtils.isDouble(args[2]) && MathArgUtils.isDouble(args[3]) &&
                                        MathArgUtils.isDouble(args[4])) {
                                    Location location = new Location(Bukkit.getWorld(wName), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]));
                                    target.teleport(location);
                                } else {
                                    player.sendMessage(ChatColor.RED + "You must enter valid parameters for the coordinates.");
                                }
                            } else {
                                player.sendMessage(MessageOutput.TARGET_PLAYER_OFFLINE.getOutput());
                            }
                        } else {
                            player.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                        }
                    } else if (args.length == 4) {
                        String wName = args[0];
                        if (MathArgUtils.isDouble(args[1]) && MathArgUtils.isDouble(args[2]) &&
                                MathArgUtils.isDouble(args[3])) {
                            Location location = new Location(Bukkit.getWorld(wName), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                            player.teleport(location);
                        } else {
                            player.sendMessage(ChatColor.RED + "You must enter valid parameters for the coordinates.");
                        }
                    }
                } else {
                    player.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                }
            } else {
                sender.sendMessage(MessageOutput.COMMAND_NOT_PLAYER.getOutput());
            }
        }
        if(cmd.getName().equalsIgnoreCase("hat")) {
            if(sender instanceof Player) {
                Player player = (Player)sender;
                if(player.isOp() || player.hasPermission("pixeltects.global.hat")) {
                    if(args.length == 0) {
                        ItemStack itemStack = player.getItemInHand();
                        if (itemStack != null) {
                            player.getInventory().setHelmet(itemStack);
                            sender.sendMessage(ChatColor.GREEN + "You have set your hat to the item you are holding.");
                        } else {
                            sender.sendMessage(ChatColor.RED + "You must be holding an item to set as your hat.");
                        }
                    }else if(args.length == 1) {
                        if(args[0].equalsIgnoreCase("clear") || args[0].equalsIgnoreCase("setasnull")
                                || args[0].equalsIgnoreCase("remove")) {
                            if(player.getInventory().getHelmet() != null) {
                                player.getInventory().setHelmet(null);
                                sender.sendMessage(ChatColor.GREEN + "You have cleared your hat.");
                            }else{
                                sender.sendMessage(ChatColor.RED + "You do not have your hat set to any item.");
                            }
                        }else{
                            if(player.hasPermission("pixeltects.global.hat.others")) {
                                Player target = Bukkit.getPlayer(args[0]);
                                if((target != null) && target.isOnline()) {
                                    ItemStack itemStack = player.getItemInHand();
                                    if (itemStack != null) {
                                        target.getInventory().setHelmet(itemStack);
                                        sender.sendMessage(ChatColor.GREEN + "You have set " + target.getName() + "'s hat to the item you are holding.");
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "You must be holding an item to set as a hat.");
                                    }
                                }else{
                                    sender.sendMessage(MessageOutput.TARGET_PLAYER_OFFLINE.getOutput());
                                }
                            }else{
                                sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                            }
                        }
                    }else if(args.length >= 2) {
                        if(args[0].equalsIgnoreCase("clear")) {
                            if(player.hasPermission("pixeltects.global.hat.others")) {
                                Player target = Bukkit.getPlayer(args[0]);
                                if ((target != null) && target.isOnline()) {
                                    if (target.getInventory().getHelmet() != null) {
                                        target.getInventory().setHelmet(null);
                                        sender.sendMessage(ChatColor.GREEN + "You have cleared " + target.getName() + "'s hat.");
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "That player does not have a hat set.");
                                    }
                                } else {
                                    sender.sendMessage(MessageOutput.TARGET_PLAYER_OFFLINE.getOutput());
                                }
                            }else{
                                sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                            }
                        }else{
                            sender.sendMessage(ChatColor.RED + "Correct usage: /hat");
                            sender.sendMessage(ChatColor.RED + "Correct usage: /hat clear");
                            sender.sendMessage(ChatColor.RED + "Correct usage: /hat <player>");
                            sender.sendMessage(ChatColor.RED + "Correct usage: /hat clear <player>");
                        }
                    }
                }else{
                    sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                }
            }else{
                sender.sendMessage(MessageOutput.COMMAND_NOT_PLAYER.getOutput());
            }
        }
        if (cmd.getName().equalsIgnoreCase("speed")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp() || player.hasPermission("pixeltects.global.speed") || player.hasPermission("pixeltects.cm")) {
                    if (args.length == 0) {
                        if (player.isFlying()) {
                            player.sendMessage(ChatColor.RED + "Current Fly Speed: " + (player.getFlySpeed() * 10.0F));
                        } else {
                            player.sendMessage(ChatColor.RED + "Current Walk Speed: " + (player.getFlySpeed() * 10.0F));
                        }
                        player.sendMessage(ChatColor.RED + "Usage: /speed <value> whilst flying/walking.");
                    }
                    if (args.length >= 1)
                        if (MathArgUtils.isInt(args[0])) {
                            float value = Integer.parseInt(args[0]);
                            if (value <= 10.0F) {
                                float speed = value / 10.0F;
                                if (args[0].equalsIgnoreCase("10"))
                                    speed = 0.9F;
                                if (args[0].equalsIgnoreCase("1"))
                                    speed = 0.25F;
                                if (speed < 0.9D) {
                                    if (player.isFlying()) {
                                        if (args[0].equalsIgnoreCase("1"))
                                            speed = 0.1F;
                                        player.setFlySpeed(speed);
                                        player.sendMessage(ChatColor.GREEN + "Set your fly speed to " + (speed * 10.0F) + ".");
                                    } else {
                                        player.setWalkSpeed(speed);
                                        player.sendMessage(ChatColor.GREEN + "Set your walk speed to " + (speed * 10.0F) + ".");
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.RED + "You must enter a valid parameter for the speed.");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "You must enter a valid parameter for the speed.");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "You must enter a valid parameter for the speed.");
                        }
                } else {
                    player.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
                }
            } else {
                sender.sendMessage(MessageOutput.COMMAND_NOT_PLAYER.getOutput());
            }
        }
        if(cmd.getName().equalsIgnoreCase("rescuetps")) {
            boolean hasPermission = false;
            if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
                if(sender instanceof Player) {
                    if(sender.getName().equalsIgnoreCase(Pixeltects.getPackageManager().RILEY_USERNAME)) {
                        hasPermission = true;
                    }
                }
                hasPermission = true;
            }

            if(hasPermission) {
                for(Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                    Bukkit.getScheduler().cancelTasks(plugin);
                }
                sender.sendMessage(ChatColor.RED + "Cancelled all active runnables on the server. The TPS should now increase.");
            }else{
                sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
            }

        }
        return true;
    }

    private static String formatTPS(double tps) {
        if (tps >= 20.0D) {
            return "*20.0";
        }
        return Math.round(tps) + "";
    }

    public static void printTPS(CommandSender sender) {
        if (sender.isOp() || sender.hasPermission("pixeltects.global.tps") || sender.hasPermission("pixeltects.technician")
                || sender.hasPermission("pixeltects.show") || sender.hasPermission("pixeltects.show.manager")) { //Useful for shows - to know if its lagging server
            double current = 20.0D;
            try {
                double[] recentTps = MinecraftServer.getServer().recentTps;
                StringBuilder sb = new StringBuilder(ChatColor.WHITE + "TPS from last 1s, 1m, 5m, 15m: " + ChatColor.AQUA);
                sb.append(ChatColor.BOLD + formatTPS(current) + ChatColor.AQUA + ", ");
                for (double tps : recentTps) {
                    sb.append(formatTPS(tps));
                    sb.append(", ");
                }
                sender.sendMessage(sb.substring(0, sb.length() - 2));

                if(sender instanceof ConsoleCommandSender || sender.getName().equalsIgnoreCase(Pixeltects.getPackageManager().RILEY_USERNAME)) {
                    //Advanced TPS display
                    for (double tps : recentTps) {
                        if(tps < 17) {
                            sender.sendMessage(ChatColor.RED + "TPS has fallen below a consistent value: " + tps + ".");
                            break;
                        }
                    }
                    Pixeltects pixeltects = Pixeltects.getPackageManager();
                    sender.sendMessage(ChatColor.GREEN + "Advanced TPS:");
                    sender.sendMessage(ChatColor.GOLD + "Show Module:");
                    sender.sendMessage(ChatColor.RED + " " + pixeltects.getShowManager().getRunningShows().size() + " shows are running.");
                    sender.sendMessage(ChatColor.RED + " " + pixeltects.getShowManager().getTotalEffectsRunning() + " show effects are running.");
                    sender.sendMessage(ChatColor.GOLD + "Profile Module:");
                    sender.sendMessage(ChatColor.RED + " " + pixeltects.getProfileManager().getProfiles().size() + " profiles are loaded.");
                    sender.sendMessage(" ");
                }

            }catch (NoSuchMethodError exception) {
                sender.sendMessage(ChatColor.RED + "Currently unable to fetch TPS data: Version mismatch.");
                return;
            }
        } else {
            sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
        }
    }


}
