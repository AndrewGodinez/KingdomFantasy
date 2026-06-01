/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cr.ac.una.kingdomfantasy.model;

/**
 *
 * @author Usuario
 */
public interface TieneVida {
    public Integer obtenerVidaActual();
    public void setearVidaMaxima(Integer vidaMaxima);
    public void recibirDaño(Integer danoRecivido);
    public Boolean estaMuerto();
}
