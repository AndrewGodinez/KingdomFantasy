/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.kingdomfantasy.util;

/**
 *
 * @author Usuario
 */
public class Vector2D {

    private Double x;
    private Double y;

    public Vector2D() {
        this(0.0, 0.0);
    }

    public Vector2D(Double x, Double y) {
        this.x = x;
        this.y = y;
    }
//Constructor para copiar vectores
    public Vector2D(Vector2D vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public void setValores(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public void sumarVectores(Vector2D vector) {
        this.x += vector.x;
        this.y += vector.y;
    }

    public void restarVectores(Vector2D vector) {
        this.x -= vector.x;
        this.y -= vector.y;
    }

    public void cambiarPosicion(Double nuevoX, Double nuevoY) {
        this.x += nuevoX;
        this.y += nuevoY;
    }
    
    public Double distancia(Vector2D vector) {
        Double restaX = vector.x - this.x;
        Double restaY = vector.y - this.y;
        return Math.sqrt(restaX * restaX + restaY * restaY);
    }

    public Vector2D copy() {
        return new Vector2D(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }  
}
