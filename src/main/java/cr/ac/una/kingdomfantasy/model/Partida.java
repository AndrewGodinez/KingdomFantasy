package cr.ac.una.kingdomfantasy.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "DEF_PARTIDA")
@NamedQueries({
    @NamedQuery(name = "Partida.findAll", query = "SELECT p FROM Partida p"),
    @NamedQuery(name = "Partida.findByIdply", query = "SELECT p FROM Partida p WHERE p.idply.id = :idply"),
    @NamedQuery(name="Partida.findRanking", query = "SELECT p FROM Partida p JOIN FETCH p.idply ORDER BY p.nivelActual DESC, p.idply.puntosTotales DESC")
    })
public class Partida implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "DEF_PARTIDA_PAR_ID_GENERATOR", sequenceName = "una.DEF_PARTIDA_SEQ01", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEF_PARTIDA_PAR_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "PAR_ID")
    private Long id;
    @Column(name = "PAR_NIVEL_ACTUAL")
    private Integer nivelActual;
    @Column(name = "PAR_PUNTOS_ACTUALES")
    private Long puntosActuales;
    @Column(name = "PAR_FECHA_GUARDADO")
    private LocalDate fechaGuardado;
    @Basic(optional = false)
    @Column(name = "PAR_VERSION")
    @Version
    private Long version;
    @JoinColumn(name = "PAR_IDMEJ", referencedColumnName = "MEJ_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Mejora idmej;
    @JoinColumn(name = "PAR_IDPLY", referencedColumnName = "PLY_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Player idply;

    public Partida() {
    }

    public Partida(PartidaDto partidaDto) {
        this.id = partidaDto.getId();
        actualizar(partidaDto);
    }

    public void actualizar(PartidaDto partidaDto) {      
        this.nivelActual = partidaDto.getNivelActual();
        this.puntosActuales = partidaDto.getPuntosActuales();
        this.fechaGuardado = partidaDto.getFechaGuardado();
        this.idmej = partidaDto.getIdmej();
        this.idply = partidaDto.getIdply();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNivelActual() {
        return nivelActual;
    }

    public void setNivelActual(Integer nivelActual) {
        this.nivelActual = nivelActual;
    }

    public Long getPuntosActuales() {
        return puntosActuales;
    }

    public void setPuntosActuales(Long puntosActuales) {
        this.puntosActuales = puntosActuales;
    }

    public LocalDate getFechaGuardado() {
        return fechaGuardado;
    }

    public void setFechaGuardado(LocalDate fechaGuardado) {
        this.fechaGuardado = fechaGuardado;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Mejora getIdmej() {
        return idmej;
    }

    public void setIdmej(Mejora idmej) {
        this.idmej = idmej;
    }

    public Player getIdply() {
        return idply;
    }

    public void setIdply(Player idply) {
        this.idply = idply;
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
        if (!(object instanceof Partida)) {
            return false;
        }
        Partida other = (Partida) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cr.ac.una.kingdomfantasy.model.Partida[ parId=" + id + " ]";
    }
    
}
