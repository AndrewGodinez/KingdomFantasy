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
public abstract class Entidad {
    private TipoEnJuego tipoEnJuego;
    private Vector2D posicion;
    private HitBox hitBox;
    private TipoAnimacion animacion;
    private Boolean activo;
    private static final Double DELTA_TIME = 0.016;
    
    public abstract void actualizar();  
    public abstract void dibujar();
    public abstract void manejarEvento(TipoEvento evento);
    public void alHaberColision(HitBox objetoColision){
        if(hitBox.hayColision(objetoColision)){
            // Agregar logica por colision
        }
    }   

    public TipoAnimacion getAnimacion() {
        return animacion;
    }

    public void setAnimacion(TipoAnimacion animacion) {
        this.animacion = animacion;
    }

    public TipoEnJuego getTipoEnJuego() {
        return tipoEnJuego;
    }

    public void setTipoEnJuego(TipoEnJuego tipoEnJuego) {
        this.tipoEnJuego = tipoEnJuego;
    }

    public Vector2D getPosicion() {
        return posicion;
    }

    public void setPosicion(Vector2D posicion) {
        this.posicion = posicion;
    }

    public HitBox getHitBox() {
        return hitBox;
    }

    public void setHitBox(HitBox hitBox) {
        this.hitBox = hitBox;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
