/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cr.ac.una.kingdomfantasy.model;

/**
 *
 * @author Usuario
 */
public interface Animable {
    public void cambiarAnimación(String nuevaAnimacion);
    public void actualizarFrame();
    public void reproducirAnimacion(TipoAnimacion tipoAnimacion);
}
