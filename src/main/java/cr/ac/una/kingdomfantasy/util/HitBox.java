package cr.ac.una.kingdomfantasy.util;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class HitBox {

    private double entityX;
    private double entityY;
    private double width;
    private double height;
    private double offsetX;
    private double offsetY;
    private Rectangle2D bounds;
    private boolean debugVisible = false;

    public HitBox(double entityX, double entityY,double width, double height, double offsetX, double offsetY) {
        this.entityX = entityX;
        this.entityY = entityY;
        this.width   = width;
        this.height  = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        recalculate();
    }

    public HitBox(double entityX, double entityY, double width, double height) {
        this(entityX, entityY, width, height, 0, 0);
    }

    public void updatePosition(double newEntityX, double newEntityY) {
        this.entityX = newEntityX;
        this.entityY = newEntityY;
        recalculate();
    }

    public void moveBy(double deltaX, double deltaY) {
        updatePosition(entityX + deltaX, entityY + deltaY);
    }

    public void setGeometry(double width, double height, double offsetX, double offsetY) {
        this.width = Math.max(0, width);
        this.height = Math.max(0, height);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        recalculate();
    }

    public void setSize(double width, double height) {
        this.width = Math.max(0, width);
        this.height = Math.max(0, height);
        recalculate();
    }

    public void setOffset(double offsetX, double offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        recalculate();
    }

    public HitBox copy() {
        HitBox copy = new HitBox(entityX, entityY, width, height, offsetX, offsetY);
        copy.setDebugVisible(debugVisible);
        return copy;
    }

    public HitBox translated(double deltaX, double deltaY) {
        HitBox copy = copy();
        copy.moveBy(deltaX, deltaY);
        return copy;
    }

    public Rectangle2D expanded(double amount) {
        return new Rectangle2D( bounds.getMinX() - amount, bounds.getMinY() - amount, bounds.getWidth() + amount * 2, bounds.getHeight() + amount * 2);
    }

    private void recalculate() {
        double x = entityX + offsetX;
        double y = entityY + offsetY;
        bounds   = new Rectangle2D(x, y, width, height);
    }

    public boolean intersects(HitBox other) {
        if (other == null) return false;
        return bounds.intersects(other.bounds);
    }
  
    public boolean intersects(Rectangle2D rect) {
        if (rect == null) return false;
        return bounds.intersects(rect);
    }

    public boolean intersectsExpanded(HitBox other, double padding) {
        if (other == null) return false;
        return expanded(padding).intersects(other.bounds);
    }

    public double overlapX(HitBox other) {
        if (!intersects(other)) return 0;
        return Math.min(getMaxX(), other.getMaxX()) - Math.max(getX(), other.getX());
    }

    public double overlapY(HitBox other) {
        if (!intersects(other)) return 0;
        return Math.min(getMaxY(), other.getMaxY()) - Math.max(getY(), other.getY());
    }

    public boolean intersectsCircle(double circleX, double circleY, double radius) {
        double nearestX = clamp(circleX, bounds.getMinX(), bounds.getMaxX());
        double nearestY = clamp(circleY, bounds.getMinY(), bounds.getMaxY());
        double dx = circleX - nearestX;
        double dy = circleY - nearestY;
        double distanceSquared = dx * dx + dy * dy;
        return distanceSquared <= (radius * radius);
    }

    public boolean containsPoint(double pointX, double pointY) {
        return bounds.contains(pointX, pointY);
    }

    public double distanceTo(HitBox other) {
        double dx = getCenterX() - other.getCenterX();
        double dy = getCenterY() - other.getCenterY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double distanceToPoint(double px, double py) {
        double dx = getCenterX() - px;
        double dy = getCenterY() - py;
        return Math.sqrt(dx * dx + dy * dy);
    }


    public void debugDraw(GraphicsContext gc, Color color) {
        if (!debugVisible) return;
        gc.setStroke(color);
        gc.setLineWidth(1.5);
        gc.strokeRect(bounds.getMinX(), bounds.getMinY(), width, height);
        gc.setFill(color);
        gc.fillOval(getCenterX() - 2, getCenterY() - 2, 4, 4);
    }

    public Rectangle2D getBounds() { return bounds; }

    public double getX(){ 
        return bounds.getMinX();
    }

    public double getY(){ 
        return bounds.getMinY();
    }

    public double getMaxX(){
        return bounds.getMaxX(); 
    }

    public double getMaxY(){
        return bounds.getMaxY(); 
    }

    public double getCenterX(){
        return bounds.getMinX() + width  / 2.0;
    }

    public double getCenterY(){
        return bounds.getMinY() + height / 2.0; 
    }

    public double getWidth(){
        return width; 
    }
    
    public double getHeight(){
        return height; 
    }
    
    public double getOffsetX(){
        return offsetX; 
    }
    
    public double getOffsetY(){ 
        return offsetY; 
    }
    
    public double getEntityX(){ 
        return entityX; 
    }
    
    public double getEntityY(){ 
        return entityY;
    }

    public boolean isDebugVisible(){ 
        return debugVisible; 
    }
    
    public void setDebugVisible(boolean debugVisible){
        this.debugVisible = debugVisible; 
    }


    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    @Override
    public String toString() {
        return String.format("HitBox[x=%.1f, y=%.1f, w=%.1f, h=%.1f]",
            bounds.getMinX(), bounds.getMinY(), width, height);
    }
}
