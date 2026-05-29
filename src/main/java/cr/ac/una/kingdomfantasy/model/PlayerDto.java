/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.kingdomfantasy.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Usuario
 */
public class PlayerDto {
    private Long id;
    private Byte fotoPerfil;
    private String Nombre;
    private LocalDate fechaRegistro;
    private Long version;
    private Long puntosTotales;
    private Integer idBallesta;
    private List<Partida> partidaList;

   
    
    PlayerDto(){
     this.fotoPerfil = null;
     this.Nombre = "";
     this.fechaRegistro = null;
     this.puntosTotales = null;
     this.idBallesta = 0;
    }
    
    PlayerDto(Player player){
    this.id = player.getId();
    this.fotoPerfil = player.getFotoPerfil();
    this.Nombre = player.getNombre();
    this.fechaRegistro = player.getFechaRegistro();
    this.version = player.getVersion();
    this.puntosTotales = player.getPuntosTotales();
    this.idBallesta = player.getIdBallesta();
    this.partidaList = player.getPartidaList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(Byte fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
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
        return puntosTotales;
    }

    public void setPuntosTotales(Long puntosTotales) {
        this.puntosTotales = puntosTotales;
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
        return "PlayerDto{" + "id=" + id + ", Nombre=" + Nombre + ", fechaRegistro=" + fechaRegistro + ", puntosTotales=" + puntosTotales + '}';
    }
    
}
