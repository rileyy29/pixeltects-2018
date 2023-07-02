package com.pixeltects.core.player.rank;

import lombok.Getter;
import org.bukkit.ChatColor;

public enum Rank {

    //Format: ENUMID(id, tag, primary color, second color, ladder)

    DEFAULT("default", "Guest", ChatColor.RESET, ChatColor.RESET, 0),
    SG("special-guest", "Special Guest", ChatColor.RESET, ChatColor.RESET, 1),
    EME("eme", "Earning My Ears", ChatColor.RESET, ChatColor.RESET, 2),
    GR("guest-relations", "Cast Member", ChatColor.RESET, ChatColor.RESET, 3),
    IMAGINEER("imagineer", "Cast Member", ChatColor.RESET, ChatColor.RESET, 4),
    TECHNICIAN("technician", "Cast Member", ChatColor.RESET, ChatColor.RESET, 5),
    DEVELOPER("developer", "Cast Member", ChatColor.RESET, ChatColor.RESET, 6),
    CAST_LEADER("cast-leader", "Cast Leader", ChatColor.RESET, ChatColor.RESET, 7),
    DIRECTOR("director", "Director", ChatColor.RESET, ChatColor.RESET, 8);

    @Getter
    private String groupID;
    @Getter
    private String tag;
    @Getter
    private ChatColor primaryColor;
    @Getter
    private ChatColor secondaryColor;
    @Getter
    private int rankLadder;

    Rank(String groupID, String tag, ChatColor primaryColor, ChatColor secondaryColor, int rankLadder) {
        this.groupID = groupID;
        this.tag = tag;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.rankLadder = rankLadder;
    }

    public static Rank byName(String name) {
        if(name == null) {
            return DEFAULT;
        }
        for(Rank rank : values()) {
            if(rank.name().equals(name)) {
                return rank;
            }
        }
        return DEFAULT;
    }

    public static Rank byGroupID(String id) {
        if(id == null) {
            return DEFAULT;
        }
        for(Rank rank : values()) {
            if(rank.getGroupID().equals(id)) {
                return rank;
            }
        }
        return DEFAULT;
    }

    public boolean canSetRanks(Rank hasRank) {
        if(hasRank.has(CAST_LEADER)) {
            return true;
        }
        return false;
    }

    public boolean has(Rank rank) {
        return (compareTo(rank) >= 0);
    }

    public String getColoredTag() {
        if (this == DEFAULT) {
            return DEFAULT.getPrimaryColor().toString();
        }else{
            return getPrimaryColor().toString() + ChatColor.BOLD + getTag() + getPrimaryColor() + " " + getSecondaryColor();
        }
    }

    public boolean hasTag() {
        return (this != DEFAULT);
    }

}
