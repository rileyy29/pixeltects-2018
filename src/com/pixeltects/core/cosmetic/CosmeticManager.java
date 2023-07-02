package com.pixeltects.core.cosmetic;

import com.pixeltects.core.Pixeltects;
import com.pixeltects.core.cosmetic.fun.Vomit;
import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;

public class CosmeticManager {

    private Pixeltects packageManager;

    @Getter
    private HashMap<UUID, Vomit> currentlyVomiting;

    public CosmeticManager(Pixeltects mainClass) {
        this.packageManager = mainClass;
        System.out.print("[PixeltectsCore] 'CosmeticManager' class initialized");

        this.currentlyVomiting = new HashMap<>();
        System.out.print("[CosmeticManager] Created array and hash to manage data.");
    }

    public void disable() {
        for(Vomit vomit : currentlyVomiting.values()) {
            vomit.cancel();
        }
        currentlyVomiting.clear();
    }


}
