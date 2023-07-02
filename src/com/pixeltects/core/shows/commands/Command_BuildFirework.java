package com.pixeltects.core.shows.commands;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.shows.effects.firework.FireworkGenerator;
import com.pixeltects.core.shows.utils.HelpUtil;
import com.pixeltects.core.utils.math.MathArgUtils;
import com.pixeltects.core.utils.messages.MessageOutput;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Command_BuildFirework implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("buildfirework")) {
            if(sender.isOp() || sender.hasPermission("pixeltects.show.buildfirework")
            || sender.hasPermission("pixeltects.show")) {
                if(args.length >= 14) {
                    if(MathArgUtils.isDouble(args[1])
                            && MathArgUtils.isDouble(args[2])
                            && MathArgUtils.isDouble(args[3])
                            && MathArgUtils.isFloat(args[4])
                            && MathArgUtils.isFloat(args[5])
                            && MathArgUtils.isFloat(args[6])
                            && MathArgUtils.isInt(args[8])
                            && MathArgUtils.isInt(args[13])) {

                        World world = Bukkit.getWorld(args[0]);
                        if(world != null) {
                            double x = Double.parseDouble(args[1]);
                            double y = Double.parseDouble(args[2]);
                            double z = Double.parseDouble(args[3]);

                            float xmot = Float.parseFloat(args[4]);
                            float ymot = Float.parseFloat(args[5]);
                            float zmot = Float.parseFloat(args[6]);

                            int power = Integer.parseInt(args[8]);
                            int amount = Integer.parseInt(args[13]);

                            String type = args[7];
                            FireworkEffect.Type fireworkType = FireworkEffect.Type.valueOf(type.toUpperCase());
                            if(fireworkType != null) {

                                Boolean flicker = Boolean.valueOf(args[9]);
                                Boolean trail = Boolean.valueOf(args[10]);

                                if(flicker != null && trail != null) {
                                    String primaryColors = args[11];
                                    String fadeColors = args[12];

                                    Color colors1;
                                    Color colors2;
                                    String[] color1 = primaryColors.split(",");
                                    colors1 = Color.fromRGB(Integer.parseInt(color1[0]),
                                            Integer.parseInt(color1[1]), Integer.parseInt(color1[2]));

                                    String[] color2 = fadeColors.split(",");
                                    colors2 = Color.fromRGB(Integer.parseInt(color2[0]),
                                                Integer.parseInt(color2[1]), Integer.parseInt(color2[2]));

                                    int ticks = -1;

                                    if(args.length == 15) {
                                        if(MathArgUtils.isInt(args[14])) {
                                            ticks = Integer.parseInt(args[14]);
                                        }else{
                                            sender.sendMessage(MessageOutput.COMMAND_INVALID_PARAMETERS.getOutput());
                                        }
                                    }

                                    FireworkGenerator fireworkGenerator = new FireworkGenerator(
                                            new Location(world,x,y,z),
                                            amount,power,colors1,colors2,flicker,trail,type,xmot,ymot,zmot,ticks);

                                    Pixeltects.getPackageManager().getShowManager().getRunningFireworks().put("test", fireworkGenerator);
                                    fireworkGenerator.spawnFirework();
                                }else{
                                    sender.sendMessage(MessageOutput.COMMAND_INVALID_PARAMETERS.getOutput());
                                }
                            }else{
                                sender.sendMessage(MessageOutput.INVALID_FIREWORK_TYPE.getOutput());
                                HelpUtil.sendFireworkTypeList(sender);
                            }
                        }else{
                         sender.sendMessage(MessageOutput.INVALID_WORLD.getOutput());
                        }
                    }else {
                        sender.sendMessage(MessageOutput.COMMAND_INVALID_PARAMETERS.getOutput());
                    }
                }else{
                    sender.sendMessage(ChatColor.RED + "Correct usage: "
                            + "/buildfirework <world> <x> <y> <z> <xmotion> <ymotion> <zmotion> "
                            + "<type> <power> <flicker (true/false)> <trail (true/false)> "
                            + "<r,g,b> <r,g,b> "
                            + "<amount> [ticks]");
                }
            }else{
                sender.sendMessage(MessageOutput.COMMAND_NO_PERMISSION.getOutput());
            }
        }
        return false;
    }

}
