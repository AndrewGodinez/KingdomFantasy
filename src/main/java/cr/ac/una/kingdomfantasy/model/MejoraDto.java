/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.kingdomfantasy.model;

import java.util.List;

/**
 *
 * @author Usuario
 */
public class MejoraDto {
    private Long id;
    private Integer nivelVelocidadBallesta;
    private Integer nivelDaOBallesta;
    private Integer nivelEfectoMeteoro;
    private Integer nivelRangoMeteoro;
    private Integer nivelEfectoHielo;
    private Integer nivelRangoHielo;
    private Long version;
    private List<Partida> partidaList;

    public MejoraDto() {
        this.nivelVelocidadBallesta = 0;
        this.nivelDaOBallesta = 0;
        this.nivelEfectoMeteoro = 0;
        this.nivelRangoMeteoro = 0;
        this.nivelEfectoHielo = 0;
        this.nivelRangoHielo = 0;
    }

    public MejoraDto(Mejora mejora) {
        this.id = mejora.getId();
        this.nivelVelocidadBallesta = mejora.getNivelVelocidadBallesta();
        this.nivelDaOBallesta = mejora.getNivelDaOBallesta();
        this.nivelEfectoMeteoro = mejora.getNivelEfectoMeteoro();
        this.nivelRangoMeteoro = mejora.getNivelRangoMeteoro();
        this.nivelEfectoHielo = mejora.getNivelEfectoHielo();
        this.nivelRangoHielo = mejora.getNivelRangoHielo();
        this.version = mejora.getVersion();
        this.partidaList = mejora.getPartidaList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNivelVelocidadBallesta() {
        return nivelVelocidadBallesta;
    }

    public void setNivelVelocidadBallesta(Integer nivelVelocidadBallesta) {
        this.nivelVelocidadBallesta = nivelVelocidadBallesta;
    }

    public Integer getNivelDaOBallesta() {
        return nivelDaOBallesta;
    }

    public void setNivelDaOBallesta(Integer nivelDaOBallesta) {
        this.nivelDaOBallesta = nivelDaOBallesta;
    }

    public Integer getNivelEfectoMeteoro() {
        return nivelEfectoMeteoro;
    }

    public void setNivelEfectoMeteoro(Integer nivelEfectoMeteoro) {
        this.nivelEfectoMeteoro = nivelEfectoMeteoro;
    }

    public Integer getNivelRangoMeteoro() {
        return nivelRangoMeteoro;
    }

    public void setNivelRangoMeteoro(Integer nivelRangoMeteoro) {
        this.nivelRangoMeteoro = nivelRangoMeteoro;
    }

    public Integer getNivelEfectoHielo() {
        return nivelEfectoHielo;
    }

    public void setNivelEfectoHielo(Integer nivelEfectoHielo) {
        this.nivelEfectoHielo = nivelEfectoHielo;
    }

    public Integer getNivelRangoHielo() {
        return nivelRangoHielo;
    }

    public void setNivelRangoHielo(Integer nivelRangoHielo) {
        this.nivelRangoHielo = nivelRangoHielo;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<Partida> getPartidaList() {
        return partidaList;
    }

    public void setPartidaList(List<Partida> partidaList) {
        this.partidaList = partidaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MejoraDto)) {
            return false;
        }
        MejoraDto other = (MejoraDto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cr.ac.una.kingdomfantasy.model.Mejora[ mejId=" + id + " ]";
    }
}
