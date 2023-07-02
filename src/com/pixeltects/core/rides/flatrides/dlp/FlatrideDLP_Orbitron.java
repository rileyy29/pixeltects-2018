package com.pixeltects.core.rides.flatrides.dlp;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.rides.flatrides.Flatride;
import com.pixeltects.core.rides.flatrides.util.ArmorStandGroup;
import com.pixeltects.core.rides.flatrides.util.FlatrideUtils;
import com.pixeltects.core.rides.flatrides.util.ModelStand;
import com.pixeltects.core.rides.flatrides.util.FlatrideStandStatus;
import com.pixeltects.core.utils.armorstand.BodyPart;
import com.pixeltects.core.utils.math.MathUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class FlatrideDLP_Orbitron implements Flatride {

    private String name;
    private String id;
    private boolean interactEnabled;
    private double maxHeight;
    private double minHeight;
    private double yincr;

    private double modelSizePillarOffset = 0.15;
    private Location pillarOffset;
    private double pillarMoveUpwards = 0.75;

    private int rideRuntime = 1800; //Default: 1 minute 30 seconds - 1800

    private World world;
    private Location rideLocation;
    private double radius;
    private double speed;
    private int modelCount = 8; //Default: 8

    private double modelIncrementDegrees;
    private double phi;

    boolean canCurrentlyInteract = false;

    private ArmorStandGroup[] modelStands;
    private BukkitTask rideTask = null;

    public FlatrideDLP_Orbitron(String id) {
        this.id = id.toLowerCase();
        this.name = StringUtils.capitalize(id);
        this.rideRuntime = 1800;
        this.interactEnabled = true;
        this.rideLocation = new Location(Bukkit.getWorld("DLP"), 118.5,34,-106.5);
        this.world = rideLocation.getWorld();
        this.radius = 5;
        this.modelCount = 8;
        this.maxHeight = 39;
        this.modelIncrementDegrees = 360 / modelCount;
        this.phi = 2*Math.PI / modelCount;
        this.modelStands = new ArmorStandGroup[modelCount];
        this.speed = 2.90; //2.45

        this.minHeight = rideLocation.clone().add(0,1,0).getY();
        this.pillarOffset = rideLocation.clone().add(0,3,0);
        this.yincr = 0.035;
        this.canCurrentlyInteract = false;
    }

    @Override
    public void spawnRide() {
        Location stillLocation = pillarOffset.clone().subtract(0,1,0);
        Vector offset = new Vector(radius, 0, 0);
        for (int i = 0; i < modelCount; i++) {
            double degrees = ((modelIncrementDegrees) * i);
            Location mLocation = rideLocation.clone();
            Vector mOffset = FlatrideUtils.rotateY(offset.clone(), phi * i);
            mLocation.add(mOffset);
            mLocation.setYaw((float)degrees); //To face inwards: 90 + modelIncrementDegrees)*i);
            mLocation.setY(mLocation.getY());

            ArmorStand mainArmorStand = buildMainArmorStand(i, mLocation);
            ModelStand mainModel = new ModelStand(mainArmorStand, rideLocation.getY());
            mainModel.setPitch(0);
            mainModel.setStatus(FlatrideStandStatus.STATION);

            //MIDDLE
            Location middleLocation = stillLocation.clone();
            Location middleDirection = middleLocation.clone().subtract(mLocation.clone()).add(0,modelSizePillarOffset,0);
            float yaw = MathUtil.getLookAtYaw(middleDirection.toVector()) + 90;
            middleLocation.setYaw(yaw);
            ArmorStand middleArmorStand = buildMiddleArmorStand(i, middleLocation);
            middleArmorStand.teleport(middleLocation);
            double pitch = Math.atan2(Math.sqrt(middleDirection.getZ() * middleDirection.getZ() + middleDirection.getX() * middleDirection.getX()), middleDirection.getY()) + Math.PI;
            middleArmorStand.setHeadPose(middleArmorStand.getHeadPose().add(pitch + Math.toRadians(90),0,0));

            ModelStand middleModel = new ModelStand(middleArmorStand, stillLocation.getY());

            this.modelStands[i] = new ArmorStandGroup(mainModel, null, middleModel);
       }
    }

    @Override
    public void startRideSequence() {
        this.canCurrentlyInteract = false;
        runStartSequence();
           this.rideTask=new BukkitRunnable() {
                int tick = 0;
                double speedToSet = 0.0;
                double increment = 0.015; //Amount to speed up/slow down individually.

                double startYDiff = minHeight - rideLocation.getY();
                double startYIncr = 0.025;
                double timetoAscend = startYDiff / startYIncr;

                double ydiff = maxHeight - rideLocation.getY();
                double pitchincr = -5;
                double timeToDescend = ydiff / yincr;

                int timeToStart = (int) ((speed / increment) + timetoAscend + 1); //Calculate start time needed
                int timeToSlow =  (int) ((rideRuntime - timeToStart) + timeToDescend + timetoAscend + 1); //Calculate end time needed

                public void run() {

                    if (tick >= 0 && tick <= timeToStart) { //Starting
                        if (speedToSet < speed) {
                            speedToSet += increment;
                            for(ArmorStandGroup standGroup : modelStands) {
                                ModelStand modelStand = standGroup.getMainArmorStand();
                                if (modelStand.getYAxis() < minHeight) {
                                    modelStand.setYAxis(modelStand.getYAxis() + startYIncr);
                                    if(!modelStand.getStatus().equals(FlatrideStandStatus.DOWN)) {
                                        modelStand.setStatus(FlatrideStandStatus.DOWN);
                                    }
                                }

                                ModelStand middleStand = standGroup.getMiddlePole();
                                if(middleStand.getYAxis() < pillarOffset.getY()) {
                                    middleStand.setYAxis(middleStand.getYAxis() + startYIncr);
                                }
                            }
                        }
                    }
                    if(tick > timeToStart && tick < timeToSlow) { //Main
                        if(interactEnabled && !canCurrentlyInteract) {
                            canCurrentlyInteract = true;
                        }
                        runVelocities();
                    }

                    if (tick >= timeToSlow) { //Ending
                        if (speedToSet <= 0) {
                            runEndSequence();
                            rideTask = null;
                            cancel();
                        } else {
                            speedToSet -= increment;

                            canCurrentlyInteract = false;

                            for(ArmorStandGroup standGroup : modelStands) {
                                ModelStand modelStand = standGroup.getMainArmorStand();
                                ArmorStand armorStand = modelStand.getArmorStand();
                                if(armorStand.getLocation().getY() > minHeight) {
                                    modelStand.setYAxis(modelStand.getYAxis() - yincr);
                                    movePitch(modelStand, "down", true);

                                    if(modelStand.getYAxis() <= (maxHeight - pillarMoveUpwards)) {
                                        ModelStand middle = standGroup.getMiddlePole();
                                        if(middle.getYAxis() > pillarOffset.getY()) {
                                            middle.setYAxis(middle.getYAxis() - yincr);
                                        }
                                    }
                                }else{
                                    if(!modelStand.getStatus().equals(FlatrideStandStatus.DOWN)) {
                                        modelStand.setStatus(FlatrideStandStatus.DOWN);
                                    }
                                }
                            }

                            if(canStartGroupDescend()) {
                                for(ArmorStandGroup standGroup : modelStands) {
                                    ModelStand modelStand = standGroup.getMainArmorStand();
                                    ArmorStand armorStand = modelStand.getArmorStand();
                                    if(armorStand.getLocation().getY() > rideLocation.getY()) {
                                        modelStand.setYAxis(modelStand.getYAxis() - startYIncr);
                                        movePitch(modelStand, "down", true);

                                        if(!modelStand.getStatus().equals(FlatrideStandStatus.STATION)) {
                                            modelStand.setStatus(FlatrideStandStatus.STATION);
                                        }
                                    }

                                    ModelStand middleStand = standGroup.getMiddlePole();
                                    if(middleStand.getYAxis() > (pillarOffset.getY() - pillarMoveUpwards)) {
                                        middleStand.setYAxis(middleStand.getYAxis() - startYIncr);
                                    }
                                }
                            }
                        }
                    }

                    for(ArmorStandGroup standGroup : modelStands) {
                        //MAIN
                        ModelStand modelStand = standGroup.getMainArmorStand();
                        ArmorStand armorStand = modelStand.getArmorStand();

                        CraftArmorStand craftArmorStand = (CraftArmorStand) armorStand;

                        Location newLocation = FlatrideUtils.rotate(rideLocation, armorStand.getLocation(), speedToSet, radius);
                        double x2 = newLocation.getX();
                        double y2 = modelStand.getYAxis();
                        double z2 = newLocation.getZ();

                        craftArmorStand.getHandle().setLocation(x2, y2, z2, newLocation.getYaw(),0);
                        BodyPart.HEAD.setPitchPose(armorStand, newLocation, modelStand.getPitch());

                        //Middle
                        ModelStand middleStand = standGroup.getMiddlePole();
                        Location mLocation = new Location(rideLocation.getWorld(),x2,y2,z2);
                        mLocation.setYaw(newLocation.getYaw());
                        calculateMiddleToCar(middleStand,mLocation);

                    }


                    tick++;
                }
            }.runTaskTimerAsynchronously(Pixeltects.getPackageManager(), 0L, 1L);
    }

    public boolean canGoUp(ArmorStand stand) {
        if(stand.getPassenger() != null) {
            if(stand.getPassenger() instanceof Player) {
                Player player = (Player)stand.getPassenger();
                if((player.getItemInHand() != null) && player.getItemInHand().getType().equals(Material.STICK)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void calculateMiddleToCar(ModelStand modelStand, Location mLoc) {
        ArmorStand middleArmorStand = modelStand.getArmorStand();
        CraftArmorStand craftArmorStand = (CraftArmorStand)middleArmorStand;
        Location middleLocation = middleArmorStand.getLocation().clone();
        Location mLocation = mLoc.clone();

        double offsetY = modelSizePillarOffset;

        Location middleDirection = middleLocation.clone().subtract(mLocation.clone()).add(0,offsetY,0);
        float yaw = MathUtil.getLookAtYaw(middleDirection.toVector()) + 90;
        craftArmorStand.getHandle().setLocation(middleLocation.getX(), modelStand.getYAxis(), middleLocation.getZ(), yaw, 0);
        double pitch = Math.atan2(Math.sqrt(middleDirection.getZ() * middleDirection.getZ() + middleDirection.getX() * middleDirection.getX()), middleDirection.getY()) + Math.PI;

        EulerAngle newHeadPose = new EulerAngle(pitch + Math.toRadians(90),0,0);
        EulerAngle currHeadPose = middleArmorStand.getHeadPose();
        double dX = newHeadPose.getX() - currHeadPose.getX();
        double dY = newHeadPose.getY() - currHeadPose.getY();
        double dZ = newHeadPose.getZ() - currHeadPose.getZ();
        middleArmorStand.setHeadPose(middleArmorStand.getHeadPose().add(dX,dY,dZ));
    }

    public boolean canStartGroupDescend() {
        boolean can = true;
        for(ArmorStandGroup standGroup : modelStands) {
            if(standGroup.getMainArmorStand().getYAxis() > minHeight) {
                can = false;
                break;
            }
            if(standGroup.getMiddlePole().getYAxis() > pillarOffset.getY()) {
                can = false;
                break;
            }
        }
        return can;
    }

    public void move(ArmorStandGroup standGroup, String action) {
        ModelStand modelStand = standGroup.getMainArmorStand();
        if(action.equalsIgnoreCase("up")) {
            if(modelStand.getYAxis() < maxHeight) {
                modelStand.setStatus(FlatrideStandStatus.ASCENDING);
            }else{
                if(!modelStand.getStatus().equals(FlatrideStandStatus.UP)) {
                    modelStand.setStatus(FlatrideStandStatus.UP);
                }
            }
        }
        if(action.equalsIgnoreCase("down")) {
            if(modelStand.getArmorStand().getLocation().getY() > (minHeight + 1)) {
                modelStand.setStatus(FlatrideStandStatus.DESCENDING);
            }else{
                if(!modelStand.getStatus().equals(FlatrideStandStatus.DOWN)) {
                    modelStand.setStatus(FlatrideStandStatus.DOWN);
                }
            }
        }
    }

    public void runVelocities() {
            randomCarSequence();
            for (ArmorStandGroup armorStandGroup : modelStands) {
                ModelStand modelStand = armorStandGroup.getMainArmorStand();
                ArmorStand mainAS = modelStand.getArmorStand();
                if(canCurrentlyInteract && interactEnabled) {
                    if (FlatrideUtils.hasDonatorPassenger(mainAS)) { //MOVING WITH DONATOR
                        if (canGoUp(mainAS)) {
                            move(armorStandGroup, "up");
                        } else {
                            move(armorStandGroup, "down");
                        }
                    }
                }else{}

                if(modelStand.getStatus().equals(FlatrideStandStatus.ASCENDING)) {
                        if(modelStand.getYAxis() < maxHeight) {
                            modelStand.setYAxis(modelStand.getYAxis() + yincr);
                            movePitch(modelStand, "up", false);

                            if (modelStand.getYAxis() > (maxHeight - (pillarMoveUpwards - 0.15))) {
                                armorStandGroup.getMiddlePole().setYAxis(armorStandGroup.getMiddlePole().getYAxis() + yincr);
                            }
                        }else{
                            if(!modelStand.getStatus().equals(FlatrideStandStatus.UP)) {
                                modelStand.setStatus(FlatrideStandStatus.UP);
                            }
                        }
                    }else if(modelStand.getStatus().equals(FlatrideStandStatus.DESCENDING)) {
                        if(modelStand.getArmorStand().getLocation().getY() > (minHeight + 1)) {
                            modelStand.setYAxis(modelStand.getYAxis() - yincr);
                            movePitch(modelStand, "down", false);

                            if (modelStand.getYAxis() <= (maxHeight - (pillarMoveUpwards - 0.15))) {
                                ModelStand middle = armorStandGroup.getMiddlePole();
                                if (middle.getYAxis() > pillarOffset.getY()) {
                                    middle.setYAxis(middle.getYAxis() - yincr);
                                }
                            }
                        }else{
                            if(!modelStand.getStatus().equals(FlatrideStandStatus.DOWN)) {
                                modelStand.setStatus(FlatrideStandStatus.DOWN);
                            }
                        }
                    }
                }
    }

    public void randomCarSequence() {
        ArmorStandGroup armorStandGroup = FlatrideUtils.pickRandom(modelStands);
        if(interactEnabled) {
            if (!FlatrideUtils.hasDonatorPassenger(armorStandGroup.getMainArmorStand().getArmorStand())) {
                if (armorStandGroup.getMainArmorStand().getStatus().equals(FlatrideStandStatus.UP)) {
                    move(armorStandGroup, "down");
                } else {
                    if (armorStandGroup.getMainArmorStand().getStatus().equals(FlatrideStandStatus.DOWN)) {
                        move(armorStandGroup, "up");
                    }
                }
            }
        }else{
            if (armorStandGroup.getMainArmorStand().getYAxis() >= maxHeight) {
                move(armorStandGroup, "down");
            } else {
                if (armorStandGroup.getMainArmorStand().getYAxis() <= minHeight) {
                    move(armorStandGroup, "up");

                }
            }
        }
    }

    public void movePitch(ModelStand mainStand, String action, boolean end) {
        if(action.equalsIgnoreCase("up")) {
            double currPitch = mainStand.getPitch();
            if(currPitch > -15) {
                mainStand.setPitch(currPitch - 0.1); //Face inwards
            }
        }else{
            double currPitch = mainStand.getPitch();
            if(currPitch < 0) {
                if(end) {
                    mainStand.setPitch(currPitch + 0.25); //Reverse so will face up
                }else {
                    mainStand.setPitch(currPitch + 0.1); //Reverse so will face up
                }
            }
        }
    }

    public void runStartSequence() {
        FlatrideUtils.sendMessage(modelStands, " ", "all");
        FlatrideUtils.sendMessage(modelStands, ChatColor.AQUA + "" + ChatColor.BOLD + name, "all");
        FlatrideUtils.sendMessage(modelStands, ChatColor.AQUA + "Did you know? If you become a donator, you can interact with this ride!", "all");
        FlatrideUtils.sendMessage(modelStands, " ", "all");

    }

    public void runEndSequence() {
        FlatrideUtils.sendMessage(modelStands, ChatColor.RED + "Thanks for riding bro!", "all");
        FlatrideUtils.sendSoundEffect(modelStands, Sound.BLOCK_FIRE_EXTINGUISH, 1.0F, 1.0F);

    }

    public ArmorStand buildMainArmorStand(int counter, Location mLocation) {
        ArmorStand armorStand = world.spawn(mLocation, ArmorStand.class);
        armorStand.setAI(false);
        armorStand.setCollidable(false);
        armorStand.setRemoveWhenFarAway(false);
        armorStand.setInvulnerable(true);
        armorStand.setSilent(true);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setPersistent(true);
        armorStand.setNoDamageTicks(0);
        armorStand.setBasePlate(false);
        armorStand.setCustomName("ride:"+this.id + "_main_" + counter);
        armorStand.setCustomNameVisible(false);

        ItemStack modelHelmet = new ItemStack(Material.IRON_AXE, 1, (short)203);
        ItemMeta modelMeta = modelHelmet.getItemMeta();
        modelMeta.setUnbreakable(true);
        modelHelmet.setItemMeta(modelMeta);
        armorStand.setHelmet(modelHelmet);
        return armorStand;
    }

    public ArmorStand buildMiddleArmorStand(int counter, Location mLocation) {
        ArmorStand armorStand = world.spawn(mLocation, ArmorStand.class);
        armorStand.setAI(false);
        armorStand.setCollidable(false);
        armorStand.setRemoveWhenFarAway(false);
        armorStand.setInvulnerable(true);
        armorStand.setSilent(true);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setNoDamageTicks(0);
        armorStand.setBasePlate(false);
        armorStand.setPersistent(true);
        armorStand.setCustomName("ride:"+this.id + "_middle_" + counter);
        armorStand.setCustomNameVisible(false);

        ItemStack modelHelmet = new ItemStack(Material.IRON_AXE, 1, (short)204);
        ItemMeta modelMeta = modelHelmet.getItemMeta();
        modelMeta.setUnbreakable(true);
        modelHelmet.setItemMeta(modelMeta);
        armorStand.setHelmet(modelHelmet);
        return armorStand;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void despawnRide() {
        if(this.rideTask != null) {
            this.rideTask.cancel();
            this.rideTask = null;
        }

        for(ArmorStandGroup standGroup : modelStands) {
            standGroup.getMainArmorStand().remove();
            standGroup.getMiddlePole().remove();
        }
    }

    @Override
    public void cancelRideTask() {
        if(this.rideTask != null) {
            this.rideTask.cancel();
            this.rideTask = null;
        }
    }

    @Override
    public boolean spawnOnLoad() {
        return true;
    }

    @Override
    public BukkitTask getRideTask() {
        return this.rideTask;
    }

    @Override
    public ArmorStandGroup[] getArmorStandGroups() {
        return modelStands;
    }


}
