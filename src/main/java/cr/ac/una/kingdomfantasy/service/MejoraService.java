package cr.ac.una.kingdomfantasy.service;

import cr.ac.una.kingdomfantasy.model.Mejora;
import cr.ac.una.kingdomfantasy.model.MejoraDto;
import cr.ac.una.kingdomfantasy.model.Partida;
import cr.ac.una.kingdomfantasy.model.PartidaDto;
import cr.ac.una.kingdomfantasy.util.EntityManagerHelper;
import cr.ac.una.kingdomfantasy.util.Respuesta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author semmo
 */
public class MejoraService {
  private EntityManager em = EntityManagerHelper.getInstance().getManager(); 
  private EntityTransaction et;
  
  public Respuesta getMejora(Long id){
  try{
 TypedQuery<Mejora>  qryMejora=em.createNamedQuery("Mejora.findById",Mejora.class);
 qryMejora.setParameter("id",id);
 MejoraDto mejoraDto = new MejoraDto(qryMejora.getSingleResult());
 return new Respuesta(true,"","","Mejora",mejoraDto);
  } catch (NoResultException ex) {
            return new Respuesta(false, "No existe una mejora con el id ingresado.", "getMejora NoResultException");
  } catch (NonUniqueResultException ex) {
            Logger.getLogger(MejoraService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar la mejora.", ex);
            return new Respuesta(false, "Ocurrio un error al consultar la mejora.", "getMejora NonUniqueResultException");
  } catch (Exception ex) {
            if (et.isActive()) { et.rollback(); }
            Logger.getLogger(MejoraService.class.getName()).log(Level.SEVERE, "Error obteniendo la mejora del jugador", ex);
            return new Respuesta(false, "Error obteniendo la mejora.", "getMejora" + ex.getMessage());
  }
  }
  
  public Respuesta guardarMejora(MejoraDto mejoraDto){
  try {
  et=em.getTransaction();
  et.begin();
  Mejora mejora;
  if(mejoraDto.getId()!=null && mejoraDto.getId()>0){
  mejora = em.find(Mejora.class, mejoraDto.getId());
  if(mejora==null){
  et.rollback();    
  return new Respuesta(false, "No se encontró una mejora a modificar.", "guardarMejora NoResultException");
  }
  mejora.actualizar(mejoraDto);
  mejora = em.merge(mejora);
  } else{
  mejora= new Mejora (mejoraDto);
  em.persist(mejora);
  }
  Respuesta respuesta = new Respuesta(true, "", "", "Mejora", new MejoraDto(mejora));
  respuesta.setResultado("MejoraEntity", mejora);
  et.commit(); 
  return respuesta;
  } catch (Exception ex) {
            if (et.isActive()) { et.rollback(); }
            Logger.getLogger(MejoraService.class.getName()).log(Level.SEVERE, "Error guardando la mejora", ex);
            return new Respuesta(false, "Error guardando la mejora.", "guardarMejora" + ex.getMessage());
  }
 }
  
  public Respuesta comprarMejora(MejoraDto mejoraDto, PartidaDto partidaDto) {
  try {
    et = em.getTransaction();
    et.begin();
    Mejora mejora = em.find(Mejora.class, mejoraDto.getId());
    Partida partida = em.find(Partida.class, partidaDto.getId());
    if (mejora == null) {
      et.rollback();
      return new Respuesta(false, "No se encontró la mejora.", "comprarMejora Mejora null");
    }
    if (partida == null) {
      et.rollback();
      return new Respuesta(false, "No se encontró la partida.", "comprarMejora Partida null");
    }
    mejora.actualizar(mejoraDto);
    partida.setPuntosActuales(partidaDto.getPuntosActuales());
    mejora = em.merge(mejora);
    partida = em.merge(partida);
    et.commit();
    Respuesta respuesta = new Respuesta(true, "", "", "Mejora", new MejoraDto(mejora));
    respuesta.setResultado("Partida", new PartidaDto(partida));
    return respuesta;
  } catch (Exception ex) {
    if (et != null && et.isActive()) {
      et.rollback();
    }
    Logger.getLogger(MejoraService.class.getName()).log(Level.SEVERE, "Error comprando mejora", ex);
    return new Respuesta(false, "Error comprando la mejora.", "comprarMejora " + ex.getMessage());
  }
 }
  
}
