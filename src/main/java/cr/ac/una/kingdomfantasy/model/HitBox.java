/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.kingdomfantasy.model;


/**
 *
 * @author Usuario
 */
public class HitBox {
    private Double ancho;
    private Double alto;
    private Double offSetX;
    private Double offSetY;

    public HitBox(Double ancho, Double alto, Double offSetX, Double offSetY) {
        this.ancho = ancho;
        this.alto = alto;
        this.offSetX = offSetX;
        this.offSetY = offSetY;
    }

    public Double getOffSetX() {
        return offSetX;
    }

    public void setOffSetX(Double offSetX) {
        this.offSetX = offSetX;
    }

    public Double getOffSetY() {
        return offSetY;
    }

    public void setOffSetY(Double offSetY) {
        this.offSetY = offSetY;
    }

    public double getAncho() {
        return ancho;
    }

    public void setAncho(Double ancho) {
        this.ancho = ancho;
    }

    public void setAlto(Double alto) {
        this.alto = alto;
    }

    public double getAlto() {
        return alto;
    }   
}
