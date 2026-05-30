package cr.ac.una.kingdomfantasy.model;

import java.time.LocalDate;

/**
 *
 * @author Usuario
 */
public class PartidaDto {
    private Long id;
    private Integer nivelActual;
    private Long puntosActuales;
    private LocalDate fechaGuardado;
    private Long version;
    private Mejora idmej;
    private Player idply;

    public PartidaDto() {
        this.nivelActual = 0;
        this.puntosActuales = 0L;
        this.fechaGuardado = null;
    }

    public PartidaDto(Partida partida) {
        this.id = partida.getId();
        this.nivelActual = partida.getNivelActual();
        this.puntosActuales = partida.getPuntosActuales();
        this.fechaGuardado = partida.getFechaGuardado();
        this.version = partida.getVersion();
        this.idmej = partida.getIdmej();
        this.idply = partida.getIdply();
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
        if (!(object instanceof PartidaDto)) {
            return false;
        }
        PartidaDto other = (PartidaDto) object;
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
