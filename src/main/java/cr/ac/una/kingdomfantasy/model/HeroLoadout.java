package cr.ac.una.kingdomfantasy.model;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class HeroLoadout {

    private final EnumSet<HeroAccessory> accessories;

    public HeroLoadout(Set<HeroAccessory> accessories) {
        if (accessories == null || accessories.isEmpty()) {
            this.accessories = EnumSet.noneOf(HeroAccessory.class);
        } else {
            this.accessories = EnumSet.copyOf(accessories);
        }
    }

    public static HeroLoadout defaultKnightNoHelmetNoBeard() {
        return new HeroLoadout(EnumSet.of(
                HeroAccessory.HAIR,
                HeroAccessory.BOOTS,
                HeroAccessory.CHESTPLATE,
                HeroAccessory.GLOVES,
                HeroAccessory.LEGGINGS,
                HeroAccessory.SWORD_ALT));
    }

    public boolean has(HeroAccessory accessory) {
        return accessories.contains(accessory);
    }

    public Set<HeroAccessory> getAccessories() {
        return Collections.unmodifiableSet(accessories);
    }

    public HeroLoadout with(HeroAccessory accessory) {
        EnumSet<HeroAccessory> copy = EnumSet.copyOf(accessories);
        copy.add(accessory);
        return new HeroLoadout(copy);
    }

    public HeroLoadout without(HeroAccessory accessory) {
        EnumSet<HeroAccessory> copy = accessories.isEmpty()
                ? EnumSet.noneOf(HeroAccessory.class)
                : EnumSet.copyOf(accessories);
        copy.remove(accessory);
        return new HeroLoadout(copy);
    }
}
