package cr.ac.una.kingdomfantasy.model;

public class Projectile extends GameEntity {

    private static final double SPRITE_HALF_W = 60;
    private static final double TIP_OFFSET    = 30;   
    private static final double HB_HALF       = 12.0; 

    private final ProjectileOwner owner;
    private final Vector2D velocity;
    private final double damage;
    private final double maxDistance;
    private final double knockbackForce;
    private final String imageAsset;
    private final boolean isCrossbowArrow;
    private double traveledDistance;
    private boolean active = true;

    public Projectile(ProjectileOwner owner, double x, double y, double width, double height,
            double hitBoxWidth, double hitBoxHeight, double hitBoxOffsetX, double hitBoxOffsetY,
            Vector2D velocity, double damage, double maxDistance, double knockbackForce, String imageAsset) {
        super(x, y, width, height, hitBoxWidth, hitBoxHeight, hitBoxOffsetX, hitBoxOffsetY);
        this.owner           = owner;
        this.velocity        = velocity == null ? Vector2D.ZERO : velocity;
        this.damage          = Math.max(0, damage);
        this.maxDistance     = Math.max(1, maxDistance);
        this.knockbackForce  = Math.max(0, knockbackForce);
        this.imageAsset      = imageAsset;
        this.isCrossbowArrow = false;
    }

    public Projectile(ProjectileOwner owner, double x, double y, double width, double height,
            Vector2D velocity, double damage, double maxDistance, double knockbackForce) {
        super(x, y, width, height,
              HB_HALF * 2, HB_HALF * 2,          
              SPRITE_HALF_W - HB_HALF, -HB_HALF); 
        this.owner           = owner;
        this.velocity        = velocity == null ? Vector2D.ZERO : velocity;
        this.damage          = Math.max(0, damage);
        this.maxDistance     = Math.max(1, maxDistance);
        this.knockbackForce  = Math.max(0, knockbackForce);
        this.imageAsset      = null;
        this.isCrossbowArrow = (owner == ProjectileOwner.CROSSBOW);
    }

    @Override
    public void update(double deltaSeconds) {
        if (!active) return;

        Vector2D delta = velocity.multiply(Math.max(0, deltaSeconds));
        moveBy(delta);
        traveledDistance += delta.length();
        if (traveledDistance >= maxDistance) {
            active = false;
        }

        if (isCrossbowArrow && velocity.length() > 0) {
            Vector2D dir = velocity.normalize();
            double spriteCenterX = getX() + SPRITE_HALF_W;
            double spriteCenterY = getY();      
            double tipX = spriteCenterX + dir.getX() * TIP_OFFSET;
            double tipY = spriteCenterY + dir.getY() * TIP_OFFSET;
            configureHitBox(HB_HALF * 2, HB_HALF * 2,
                    tipX - HB_HALF - getX(),
                    tipY - HB_HALF - getY());
        }
    }

    public boolean hits(GameEntity target) {
        return active && target != null && getHitBox().intersects(target.getHitBox());
    }

    public boolean applyTo(LivingEntity target) {
        return applyTo(target, damage);
    }

    public boolean applyTo(LivingEntity target, double damageAmount) {
        if (!hits(target) || target.isDead()) return false;
        Vector2D knockback = velocity.normalize().multiply(knockbackForce);
        target.takeDamage(damageAmount, knockback);
        active = false;
        return true;
    }

    public ProjectileOwner getOwner()            { return owner; }
    public Vector2D        getVelocity()          { return velocity; }
    public double          getDamage()            { return damage; }
    public double          getMaxDistance()       { return maxDistance; }
    public double          getTraveledDistance()  { return traveledDistance; }
    public boolean         isActive()             { return active; }
    public void            deactivate()           { active = false; }
    public String          getImageAsset()        { return imageAsset; }
}
