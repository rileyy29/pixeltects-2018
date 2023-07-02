package com.pixeltects.core.rides.flatrides.util;

import lombok.Getter;
import lombok.Setter;

public class ArmorStandGroup {

    @Getter @Setter
    private ModelStand mainArmorStand; //In terms of ariel carousel: 1st seat
    @Getter @Setter
    private ModelStand secondaryArmorStand; //In terms of ariel carousel: 2nd seat
    @Getter @Setter
    private ModelStand middlePole; //In terms of ariel carousel: connecting to middle

    public ArmorStandGroup(ModelStand main, ModelStand secondary, ModelStand middle) {
        this.mainArmorStand = main;
        this.secondaryArmorStand = secondary;
        this.middlePole = middle;
    }

}
