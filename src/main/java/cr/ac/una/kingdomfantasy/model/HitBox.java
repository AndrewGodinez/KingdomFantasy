/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.kingdomfantasy.model;

import cr.ac.una.kingdomfantasy.util.Vector2D;

/**
 *
 * @author Usuario
 */
public class HitBox {
    private Vector2D posicion;
    private Double ancho;
    private Double alto;

    public HitBox(Vector2D position, Double ancho, Double alto) {
        this.posicion = position;
        this.ancho = ancho;
        this.alto = alto;
    }

    public Vector2D getPosicion() {
        return posicion;
    }

    public double getWidth() {
        return ancho;
    }

    public double getHeight() {
        return alto;
    }
    
    public Boolean hayColision(HitBox hitBox) {

    return posicion.getX() < hitBox.posicion.getX() + hitBox.ancho &&
           posicion.getX() + ancho > hitBox.posicion.getX() &&
           posicion.getY() < hitBox.posicion.getY() + hitBox.alto &&
           posicion.getY() + alto > hitBox.posicion.getY();
    }
}
