package cr.ac.una.kingdomfantasy.service;

import cr.ac.una.kingdomfantasy.model.Player;
import cr.ac.una.kingdomfantasy.model.PlayerDto;
import cr.ac.una.kingdomfantasy.util.EntityManagerHelper;
import cr.ac.una.kingdomfantasy.util.Respuesta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author semmo
 */
public class PlayerService {
  private EntityManager em = EntityManagerHelper.getInstance().getManager(); 
  private EntityTransaction et;
  
  public Respuesta getPlayerByName (String nombre){
  try{
 TypedQuery<Player>  qryPlayer=em.createNamedQuery("Player.findByNombre",Player.class);
 qryPlayer.setParameter("nombre",nombre);
 PlayerDto playerDto = new PlayerDto(qryPlayer.getSingleResult());
 return new Respuesta(true,"","","Player",playerDto);
  } catch (NoResultException ex) {
            return new Respuesta(false, "No existe un jugador con el nombre ingresado.", "getPlayer NoResultException");
  } catch (NonUniqueResultException ex) {
            Logger.getLogger(PlayerService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar el jugador.", ex);
            return new Respuesta(false, "Ocurrio un error al consultar el jugador.", "getPlayer NonUniqueResultException");
  } catch (Exception ex) {
            if (et.isActive()) { et.rollback(); }
            Logger.getLogger(PlayerService.class.getName()).log(Level.SEVERE, "Error obteniendo al jugador llamado [" + nombre + "]", ex);
            return new Respuesta(false, "Error obteniendo el jugador.", "getPlayer " + ex.getMessage());
  }
  }
  
  public Respuesta guardarPlayer(PlayerDto playerDto){
  try {
  et=em.getTransaction();
  et.begin();
  Player player;
  if(playerDto.getId()!=null && playerDto.getId()>0){
  player = em.find(Player.class, playerDto.getId());
  if(player==null){
  et.rollback();    
  return new Respuesta(false, "No se encontró un jugador a modificar.", "guardarPlayer NoResultException");
  }
  player.actualizar(playerDto);
  player= em.merge(player);
  } else{
  player= new Player(playerDto);
  em.persist(player);
  }
  et.commit();
  return new Respuesta(true,"","","Jugador",new PlayerDto(player));
  } catch (Exception ex) {
            if (et.isActive()) { et.rollback(); }
            Logger.getLogger(PlayerService.class.getName()).log(Level.SEVERE, "Error guardando el jugador", ex);
            return new Respuesta(false, "Error guardando el jugador.", "guardaJugador" + ex.getMessage());
  }
  }
  
  public Respuesta getAllPlayersRanking(){
  try {
        TypedQuery<Player> qryPlayer = em.createNamedQuery("Player.findAllByRanking", Player.class);
        List<Player> players = qryPlayer.getResultList();           
        List<PlayerDto> playersDto = new ArrayList<>();              
        for (Player p : players) {
            playersDto.add(new PlayerDto(p));
        }
        return new Respuesta(true, "", "", "Players", playersDto);
    } catch (Exception ex) {
        if (et.isActive()) { et.rollback(); }
        Logger.getLogger(PlayerService.class.getName()).log(Level.SEVERE, "Error obteniendo ranking", ex);
        return new Respuesta(false, "Error obteniendo el ranking.", "getAllPlayersRanking " + ex.getMessage());
    }
  }

}
