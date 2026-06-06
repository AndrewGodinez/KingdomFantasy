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
import jakarta.persistence.Version;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "DEF_MEJORA")
@NamedQueries({
    @NamedQuery(name = "Mejora.findAll", query = "SELECT m FROM Mejora m"),
    @NamedQuery(name = "Mejora.findById", query = "SELECT m FROM Mejora m WHERE m.id =:id")
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
    @Column(name = "MEJ_NIVEL_DANO_BALLESTA")
    private Integer nivelDanoBallesta;
    @Column(name = "MEJ_NIVEL_EFECTO_METEORO")
    private Integer nivelEfectoMeteoro;
    @Column(name = "MEJ_NIVEL_RANGO_METEORO")
    private Integer nivelRangoMeteoro;
    @Column(name = "MEJ_NIVEL_EFECTO_HIELO")
    private Integer nivelEfectoHielo;
    @Column(name = "MEJ_NIVEL_RANGO_HIELO")
    private Integer nivelRangoHielo;
    @Column(name = "MEJ_NIVEL_CASTILLO")
    private Integer nivelCastillo;
    @Column(name = "MEJ_NIVEL_ELIXIR")
    private Integer nivelElixir;
    @Basic(optional = false)
    @Column(name = "MEJ_VERSION")
    @Version
    private Long version;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idmej", fetch = FetchType.LAZY)
    private List<Partida> partidaList;

    public Mejora() {
    }

    public Mejora(MejoraDto mejoraDto) {
        this.id = mejoraDto.getId();
        actualizar(mejoraDto);
    }

    public void actualizar(MejoraDto mejoraDto) {     
       this.nivelVelocidadBallesta = mejoraDto.getNivelVelocidadBallesta();
       this.nivelDanoBallesta = mejoraDto.getNivelDanoBallesta();
       this.nivelEfectoMeteoro = mejoraDto.getNivelEfectoMeteoro();
       this.nivelRangoMeteoro = mejoraDto.getNivelRangoMeteoro();
       this.nivelEfectoHielo = mejoraDto.getNivelEfectoHielo();
       this.nivelRangoHielo = mejoraDto.getNivelRangoHielo();
       this.nivelCastillo = mejoraDto.getNivelCastillo();
       this.nivelElixir = mejoraDto.getNivelElixir();
       this.version = mejoraDto.getVersion();
       this.partidaList = mejoraDto.getPartidaList();
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

    public Integer getNivelDanoBallesta() {
        return nivelDanoBallesta;
    }

    public void setNivelDanoBallesta(Integer nivelDanoBallesta) {
        this.nivelDanoBallesta = nivelDanoBallesta;
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
    
    public Integer getNivelCastillo() {
        return nivelCastillo;
    }

    public void setNivelCastillo(Integer nivelCastillo) {
        this.nivelCastillo = nivelCastillo;
    }

    public Integer getNivelElixir() {
        return nivelElixir;
    }

    public void setNivelElixir(Integer nivelElixir) {
        this.nivelElixir = nivelElixir;
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
