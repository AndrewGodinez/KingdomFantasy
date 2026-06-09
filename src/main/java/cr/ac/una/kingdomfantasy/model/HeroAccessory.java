package cr.ac.una.kingdomfantasy.model;

public enum HeroAccessory { BEARD("Beard"), BOOTS("Boots"), CHESTPLATE("Chestplate"), GLOVES("Gloves"), HAIR("Hair"), HAT("Hat"), LEGGINGS("Leggings"), SWORD("Sword"), SWORD_ALT("Sword_2");

    private final String resourceSuffix;

    HeroAccessory(String resourceSuffix) {
        this.resourceSuffix = resourceSuffix;
    }

    public String getResourceSuffix() {
        return resourceSuffix;
    }

    public String getIdleResourceName() {
        return "Character_Idle_" + resourceSuffix + ".png";
    }

    public String getMoveResourceName() {
        return "Character_Move_" + resourceSuffix + ".png";
    }
}
