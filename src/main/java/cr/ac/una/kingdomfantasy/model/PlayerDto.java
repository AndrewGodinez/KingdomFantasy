package cr.ac.una.kingdomfantasy.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Usuario
 */
public class PlayerDto {
    private Long id;
    private byte[] fotoPerfil;
    private StringProperty nombre;
    private LocalDate fechaRegistro;
    private Long version;
    private StringProperty puntosTotales;
    private Integer idBallesta;
    private List<Partida> partidaList;

   
    
   public PlayerDto(){
     this.fotoPerfil = null;
     this.nombre = new SimpleStringProperty("");
     this.fechaRegistro = null;
     this.puntosTotales = new SimpleStringProperty("");
     this.idBallesta = 0;
    }
    
    public PlayerDto(Player player){
    this.id = player.getId();
    this.fotoPerfil = player.getFotoPerfil();
    this.nombre.set(player.getNombre());
    this.fechaRegistro = player.getFechaRegistro();
    this.version = player.getVersion();
    this.puntosTotales.set(player.getPuntosTotales().toString());
    this.idBallesta = player.getIdBallesta();
    this.partidaList = player.getPartidaList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getPuntosTotales() {
        if(this.puntosTotales.get() != null && !this.puntosTotales.get().isBlank()){
            return Long.valueOf(this.puntosTotales.get());
        }
        else{
            return null;
        } 
    }

    public void setPuntosTotales(Long puntosTotales) {
        this.puntosTotales.set(puntosTotales.toString());
    }

    public Integer getIdBallesta() {
        return idBallesta;
    }

    public void setIdBallesta(Integer idBallesta) {
        this.idBallesta = idBallesta;
    }
    
    public List<Partida> getPartidaList() {
        return partidaList;
    }

    public void setPartidaList(List<Partida> partidaList) {
        this.partidaList = partidaList;
    }
    
        public StringProperty getNombreProperty() {
        return nombre;
    }

    public void setNombreProperty(StringProperty Nombre) {
        this.nombre = Nombre;
    }

    public StringProperty getPuntosTotalesProperty() {
        return puntosTotales;
    }

    public void setPuntosTotalesProperty(StringProperty puntosTotales) {
        this.puntosTotales = puntosTotales;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PlayerDto other = (PlayerDto) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "PlayerDto{" + "id=" + id + ", Nombre=" + nombre + ", fechaRegistro=" + fechaRegistro + ", puntosTotales=" + puntosTotales + '}';
    }
    
}
