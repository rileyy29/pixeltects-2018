package com.pixeltects.core.rides.flatrides.util;

import com.pixeltects.core.rides.flatrides.util.FlatrideStandStatus;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.ArmorStand;

public class ModelStand {

    @Getter
    private ArmorStand armorStand;
    @Getter @Setter
    private double yAxis;
    @Getter @Setter
    private double yaw;
    @Getter @Setter
    private double pitch = 0;
    @Getter @Setter
    private FlatrideStandStatus status;

    public ModelStand(ArmorStand armorStand, double yAxis) {
        this.armorStand = armorStand;
        this.yAxis = yAxis;
    }

    public void remove() {
        if(this.armorStand != null) {
            this.armorStand.remove();
        }
    }

}
