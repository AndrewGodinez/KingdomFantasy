/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.kingdomfantasy.model;

/**
 *
 * @author Usuario
 */
public class Castillo extends Entidad implements TieneVida {
    
    private Integer vidaActual;
    private Integer vidaMaxima;
    private Integer manaActual;
    private Integer manaMaximo;
  
    @Override
    public void actualizar(Double deltaTime) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Integer getManaActual() {
        return manaActual;
    }

    public void setManaActual(Integer manaActual) {
        this.manaActual = manaActual;
    }

    public Integer getManaMaximo() {
        return manaMaximo;
    }

    public void setManaMaximo(Integer manaMaximo) {
        this.manaMaximo = manaMaximo;
    }

    @Override
    public void manejarEvento(TipoEvento evento) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Integer obtenerVidaActual() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setearVidaMaxima(Integer vidaMaxima) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void recibirDaño(Integer danoRecivido) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean estaMuerto() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void morir() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
