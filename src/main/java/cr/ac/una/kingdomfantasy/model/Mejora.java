/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.kingdomfantasy.model;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "DEF_MEJORA")
@NamedQueries({
    @NamedQuery(name = "Mejora.findAll", query = "SELECT m FROM Mejora m"),
    @NamedQuery(name = "Mejora.findByMejId", query = "SELECT m FROM Mejora m WHERE m.mejId = :mejId")
    })
public class Mejora implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "DEF_MEJORA_MEJ_ID_GENERATOR", sequenceName = "una.DEF_MEJORA_SEQ01", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEF_MEJORA_MEJ_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "MEJ_ID")
    private Long id;
    @Column(name = "MEJ_NIVEL_VELOCIDAD_BALLESTA")
    private Integer nivelVelocidadBallesta;
    @Column(name = "MEJ_NIVEL_DA\ufffdO_BALLESTA")
    private Integer nivelDaOBallesta;
    @Column(name = "MEJ_NIVEL_EFECTO_METEORO")
    private Integer nivelEfectoMeteoro;
    @Column(name = "MEJ_NIVEL_RANGO_METEORO")
    private Integer nivelRangoMeteoro;
    @Column(name = "MEJ_NIVEL_EFECTO_HIELO")
    private Integer nivelEfectoHielo;
    @Column(name = "MEJ_NIVEL_RANGO_HIELO")
    private Integer nivelRangoHielo;
    @Basic(optional = false)
    @Column(name = "MEJ_VERSION")
    private Long version;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parIdmej", fetch = FetchType.LAZY)
    private List<Partida> partidaList;

    public Mejora() {
    }

    public Mejora(Long mejId) {
        this.id = mejId;
    }

    public Mejora(Long mejId, Long mejVersion) {
        this.id = mejId;
        this.version = mejVersion;
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
        if (!(object instanceof Mejora)) {
            return false;
        }
        Mejora other = (Mejora) object;
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
