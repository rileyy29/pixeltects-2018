package com.pixeltects.core.shows.effects.gwts;

public class GlowWithTheShow {

  /*  public void changeColor(int r, int g, int b, int ticks) {
        if(this.firework != null) {

            new BukkitRunnable() {
                int currentR = fwPrimaryColor.getRed();
                int currentG = fwPrimaryColor.getGreen();
                int currentB = fwPrimaryColor.getBlue();

                public void run() {
                    if(r > currentR) {
                        currentR++;
                    }else if(r < currentR) {
                        currentR--;
                    }else{
                        currentR = r;
                    }

                    if(g > currentG) {
                        currentG++;
                    }else if(g < currentG) {
                        currentG--;
                    }else{
                        currentG = g;
                    }

                    if(b > currentB) {
                        currentB++;
                    }else if(b < currentB) {
                        currentB--;
                    }else{
                        currentB = b;
                    }

                    int newR = currentR;
                    int newG = currentG;
                    int newB = currentB;


                    for(Player player : Bukkit.getOnlinePlayers()) {
                        ItemStack itemStack = new ItemStack(Material.LEATHER_HELMET);
                        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
                        leatherArmorMeta.setColor(Color.fromRGB(newR,newG,newB));
                        itemStack.setItemMeta(leatherArmorMeta);
                        player.getInventory().setHelmet(itemStack);
                        player.updateInventory();
                    }

                    if(newR == r && newG == g && newB == b) {
                        cancel();
                    }
                }
            }.runTaskTimer(Pixeltects.getPackageManager(), 0L, ticks);


        }
    }*/

}
