package com.pixeltects.core.security.type;

import com.pixeltects.core.utils.url.URLStringUtils;

import java.util.UUID;

public class UUIDSpoofCheck {

    public static boolean isUUIDSpoofed(String username, UUID uuid) {
        String checkedID = URLStringUtils.getUrlAsString("https://api.mojang.com/users/profiles/minecraft/" + username);
        String actualID = uuid.toString().replace("-", "");

        if(!checkedID.contains(actualID)) {
            return true;
        }
        return false;
    }

}
