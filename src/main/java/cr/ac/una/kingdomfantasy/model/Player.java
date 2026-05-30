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
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "DEF_PLAYER")
@NamedQueries({
    @NamedQuery(name = "Player.findAll", query = "SELECT p FROM Player p"),
    @NamedQuery(name = "Player.findById", query = "SELECT p FROM Player p WHERE p.id = :id"),
    @NamedQuery(name = "Player.findByNombre",query = "SELECT p FROM Player p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Player.findAllByRanking",query = "SELECT p FROM Player p ORDER BY p.puntosTotales DESC")
   })
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "DEF_JUGADOR_PLY_ID_GENERATOR", sequenceName = "una.DEF_PLAYER_SEQ01", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEF_JUGADOR_PLY_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "PLY_ID")
    private Long id;
    @Lob
    @Column(name = "PLY_FOTO_PERFIL")
    private byte[] fotoPerfil;
    @Basic(optional = false)
    @Column(name = "PLY_NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "PLY_FECHA_REGISTRO")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDate fechaRegistro;
    @Basic(optional = false)
    @Column(name = "PLY_VERSION")
    @Version
    private Long version;
    @Column(name = "PLY_PUNTOS_TOTALES")
    private Long puntosTotales;
    @Basic(optional = false)
    @Column(name = "PLY_ID_BALLESTA")
    private Integer idBallesta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idply", fetch = FetchType.LAZY)
    private List<Partida> partidaList;

    public Player() {
    }

    public Player(PlayerDto playerDto) { 
        this.id = playerDto.getId();
        actualizar(playerDto);
    }

    public void actualizar(PlayerDto playerDto) {
        this.nombre = playerDto.getNombre();
        this.fechaRegistro = playerDto.getFechaRegistro();
        this.version = playerDto.getVersion();
        this.idBallesta = playerDto.getIdBallesta();
        this.fechaRegistro = playerDto.getFechaRegistro();
        this.puntosTotales = playerDto.getPuntosTotales();
        this.partidaList = playerDto.getPartidaList();
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
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Player)) {
            return false;
        }
        Player other = (Player) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cr.ac.una.kingdomfantasy.model.Player[ plyId=" + id + " ]";
    }
    
}
