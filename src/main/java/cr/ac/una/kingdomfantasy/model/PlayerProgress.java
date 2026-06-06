package cr.ac.una.kingdomfantasy.model;

public class PlayerProgress {

    private final String name;
    private int totalPoints;
    private int gold;
    private int currentLevel = 1;
    private CrossbowDesign crossbowDesign = CrossbowDesign.GREEN;
    private UpgradeProfile upgradeProfile = new UpgradeProfile();
    private String avatarPath;

    public PlayerProgress(String name) {
        this.name = sanitizeName(name);
    }

    public String getName() {
        return name;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = Math.max(0, totalPoints);
    }

    public void addPoints(int points) {
        totalPoints = Math.max(0, totalPoints + Math.max(0, points));
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = Math.max(0, gold);
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = Math.max(1, Math.min(100, currentLevel));
    }

    public CrossbowDesign getCrossbowDesign() {
        return crossbowDesign;
    }

    public void setCrossbowDesign(CrossbowDesign crossbowDesign) {
        if (crossbowDesign != null) {
            this.crossbowDesign = crossbowDesign;
        }
    }

    public UpgradeProfile getUpgradeProfile() {
        return upgradeProfile;
    }

    public void setUpgradeProfile(UpgradeProfile upgradeProfile) {
        if (upgradeProfile != null) {
            this.upgradeProfile = upgradeProfile;
        }
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    private String sanitizeName(String value) {
        String safe = value == null ? "" : value.trim();
        return safe.isBlank() ? "Invitado" : safe;
    }
}
