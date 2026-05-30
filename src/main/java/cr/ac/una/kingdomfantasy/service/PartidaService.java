package cr.ac.una.kingdomfantasy.service;

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
public class PartidaService {
  private EntityManager em = EntityManagerHelper.getInstance().getManager(); 
  private EntityTransaction et;
  
  public Respuesta getPartida(Long idply){
    try{
 TypedQuery<Partida>  qryPartida=em.createNamedQuery("Partida.findByIdply",Partida.class);
 qryPartida.setParameter("idply",idply);
 PartidaDto partidaDto = new PartidaDto(qryPartida.getSingleResult());
 return new Respuesta(true,"","","Partida",partidaDto);
  } catch (NoResultException ex) {
            return new Respuesta(false, "No existe una partida con el jugador ingresadp.", "getPartida NoResultException");
  } catch (NonUniqueResultException ex) {
            Logger.getLogger(PartidaService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar la partida.", ex);
            return new Respuesta(false, "Ocurrio un error al consultar la partida.", "getPartida NonUniqueResultException");
  } catch (Exception ex) {
            if (et.isActive()) { et.rollback(); }
            Logger.getLogger(PartidaService.class.getName()).log(Level.SEVERE, "Error obteniendo la partida jugador", ex);
            return new Respuesta(false, "Error obteniendo la partida.", "getPartida" + ex.getMessage());
  }
 }
  
  public Respuesta guardarPartida(PartidaDto partidaDto){
  try {
  et=em.getTransaction();
  et.begin();
  Partida partida;
  if(partidaDto.getId()!=null && partidaDto.getId()>0){
  partida = em.find(Partida.class, partidaDto.getId());
  if(partida==null){
  et.rollback();    
  return new Respuesta(false, "No se encontró una partida a modificar.", "guardarPartida NoResultException");
  }
  partida.actualizar(partidaDto);
  partida = em.merge(partida);
  } else{
  partida= new Partida (partidaDto);
  em.persist(partida);
  }
  et.commit();
  return new Respuesta(true,"","","Partida",new PartidaDto(partida));
  } catch (Exception ex) {
            if (et.isActive()) { et.rollback(); }
            Logger.getLogger(PartidaService.class.getName()).log(Level.SEVERE, "Error guardando la partida", ex);
            return new Respuesta(false, "Error guardando la partida.", "guardarPartida" + ex.getMessage());
  }
 }
  
}
