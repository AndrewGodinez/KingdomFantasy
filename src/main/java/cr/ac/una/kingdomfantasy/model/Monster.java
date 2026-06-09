package cr.ac.una.kingdomfantasy.model;

public class Monster extends LivingEntity {

    public static final double SPRITE_WIDTH = 128;
    public static final double SPRITE_HEIGHT = 128;

    private final MonsterType type;
    private final int level;
    private boolean scoreClaimed;
    private boolean deathAnimationComplete;

    public Monster(MonsterType type, int level, double x, double y) {
        super(x, y, SPRITE_WIDTH, SPRITE_HEIGHT, type.getHitBoxWidth(), type.getHitBoxHeight(), type.getHitBoxOffsetX(), type.getHitBoxOffsetY(), type.createStatsForLevel(level));
        this.type = type;
        this.level = Math.max(1, Math.min(100, level));
        setFacing(Direction.LEFT);
    }

    @Override
    public void update(double deltaSeconds) {
        super.update(deltaSeconds);
        if (isAlive() && !isFrozen() && getState() == EntityState.HURT) {
            setState(EntityState.IDLE);
        }
    }

    public void advanceToward(GameEntity target, double deltaSeconds) {
        update(deltaSeconds);
        if (target == null || isDead() || isFrozen() || isTargetInRange(target)) {
            return;
        }
        Vector2D direction = Vector2D.fromPoints(getCenterX(), getCenterY(), target.getCenterX(), target.getCenterY()).normalize();
        moveBy(direction.multiply(getStats().getSpeed() * Math.max(0, deltaSeconds)));
        setState(EntityState.MOVING);
    }

    public boolean attackCastle(Castle castle) {
        if (castle == null || !isTargetInRange(castle)) {
            return false;
        }
        if (!canAttack()) {
            if (isAlive() && !isFrozen() && getState() != EntityState.ATTACKING) {
                setState(EntityState.IDLE);
            }
            return false;
        }
        castle.takeDamage(castleDamageAgainst(castle));
        resetAttackCooldown();
        setState(EntityState.ATTACKING);
        return true;
    }

   
    private double castleDamageAgainst(Castle castle) {
        double fraction;
        switch (type) {
            case GOLLUX:
                fraction = 0.035; 
                break;
            case BADGER:
                fraction = 0.028;
                break;
            case DINO_REX:
            default:
                fraction = 0.025;
                break;
        }
        return castle.getStats().getMaxHealth() * fraction +(0.5*level);
    }

    public boolean attackLiving(LivingEntity target) {
        if (target == null || target.isDead() || !isTargetInRange(target)) {
            return false;
        }
        if (!canAttack()) {
            if (isAlive() && !isFrozen() && getState() != EntityState.ATTACKING) {
                setState(EntityState.IDLE);
            }
            return false;
        }
        target.takeDamage(damageAgainst(target));
        resetAttackCooldown();
        setState(EntityState.ATTACKING);
        return true;
    }

    public Projectile createRangedProjectile(GameEntity target) {
        if (type.getAttackStyle() != AttackStyle.RANGED || target == null || !isTargetInRange(target)) {
            return null;
        }
        if (!canAttack()) {
            if (isAlive() && !isFrozen() && getState() != EntityState.ATTACKING) {
                setState(EntityState.IDLE);
            }
            return null;
        }
        Vector2D start = new Vector2D(getCenterX(), getCenterY() - 10);
        Vector2D direction = Vector2D
                .fromPoints(start.getX(), start.getY(), target.getCenterX(), target.getCenterY())
                .normalize();
        resetAttackCooldown();
        setState(EntityState.ATTACKING);

        String imageAsset;
        double projWidth, projHeight, hbW, hbH, hbOffX, hbOffY;
        if (type == MonsterType.CAT) {
            imageAsset = "cat_bullet.png";
            projWidth = 160;  projHeight = 40;
            hbW = 100;  hbH = 24;  hbOffX = -50;  hbOffY = -12;
        } else {
            imageAsset = "pengu_bullet.png";
            projWidth = 140;  projHeight = 23;
            hbW = 90;   hbH = 14;  hbOffX = -45;  hbOffY = -7;
        }

        return new Projectile(ProjectileOwner.MONSTER,start.getX(),start.getY(), projWidth,projHeight, hbW, hbH, hbOffX, hbOffY, direction.multiply(getStats().getProjectileSpeed()), getStats().getDamage(),getStats().getAttackRange() + 80, 8, imageAsset);
    }

    public boolean isTargetInRange(GameEntity target) {
        if (target == null) {
            return false;
        }
        if (type.getAttackStyle() == AttackStyle.MELEE
                && getHitBox().intersectsExpanded(target.getHitBox(), getStats().getAttackRange())) {
            return true;
        }
        return distanceTo(target) <= getStats().getAttackRange();
    }

    private double damageAgainst(LivingEntity target) {
        if (target instanceof Hero) {
            return target.getStats().getMaxHealth() / 6.0;
        }
        return getStats().getDamage();
    }

    public void knockBackFrom(Vector2D source, double force) {
        if (source == null || force <= 0 || isDead()) {
            return;
        }
        Vector2D direction = Vector2D.fromPoints(source.getX(), source.getY(), getCenterX(), getCenterY()).normalize();
        moveBy(direction.multiply(force * (1.0 - getStats().getKnockbackResistance())));
    }

    public int claimScoreValue() {
        if (!isDead() || scoreClaimed) {
            return 0;
        }
        scoreClaimed = true;
        return getStats().getScoreValue();
    }

    public MonsterType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public boolean isScoreClaimed() {
        return scoreClaimed;
    }

    public boolean isDeathAnimationComplete() {
        return deathAnimationComplete;
    }

    public void markDeathAnimationComplete() {
        deathAnimationComplete = true;
    }
}
