package cr.ac.una.kingdomfantasy.model;

public class Castle extends LivingEntity {

    public Castle(double x, double y, double width, double height, double maxHealth) {
        super(x, y, width, height, width, height, 0, 0, new CombatStats(maxHealth, 0, 0, 0, 1, 0, 0, 0.95));
    }

    @Override
    public void update(double deltaSeconds) {
        super.update(deltaSeconds);
        if (isAlive()) {
            setState(EntityState.IDLE);
        }
    }
}
