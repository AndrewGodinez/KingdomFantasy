package cr.ac.una.kingdomfantasy.model;

public enum CrossbowDesign { GREEN("ballesta_verde.png"), PURPLE("ballesta_morada.png");

    private final String resourceName;

    CrossbowDesign(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }
}
