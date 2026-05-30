package cr.ac.una.kingdomfantasy.model;

import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Usuario
 */
public class MejoraDto {
    private Long id;
    private StringProperty nivelVelocidadBallesta;
    private StringProperty nivelDanoBallesta;
    private StringProperty nivelEfectoMeteoro;
    private StringProperty nivelRangoMeteoro;
    private StringProperty nivelEfectoHielo;
    private StringProperty nivelRangoHielo;
    private Long version;
    private List<Partida> partidaList;

    public MejoraDto() {
        this.nivelVelocidadBallesta = new SimpleStringProperty("");
        this.nivelDanoBallesta = new SimpleStringProperty("");
        this.nivelEfectoMeteoro = new SimpleStringProperty("");
        this.nivelRangoMeteoro = new SimpleStringProperty("");
        this.nivelEfectoHielo = new SimpleStringProperty("");
        this.nivelRangoHielo = new SimpleStringProperty("");
    }

    public MejoraDto(Mejora mejora) {
        this.id = mejora.getId();
        this.nivelVelocidadBallesta.set(mejora.getNivelVelocidadBallesta().toString());
        this.nivelDanoBallesta.set(mejora.getNivelDanoBallesta().toString());
        this.nivelEfectoMeteoro.set(mejora.getNivelEfectoMeteoro().toString());
        this.nivelRangoMeteoro.set(mejora.getNivelRangoMeteoro().toString());
        this.nivelEfectoHielo.set(mejora.getNivelEfectoHielo().toString());
        this.nivelRangoHielo.set(mejora.getNivelRangoHielo().toString());
        this.version = mejora.getVersion();
        this.partidaList = mejora.getPartidaList();
    }



    public StringProperty getNivelVelocidadBallestaProperty() {
        return nivelVelocidadBallesta;
    }

    public void setNivelVelocidadBallestaProperty(StringProperty nivelVelocidadBallesta) {
        this.nivelVelocidadBallesta = nivelVelocidadBallesta;
    }

    public StringProperty getNivelDanoBallestaProperty() {
        return nivelDanoBallesta;
    }

    public void setNivelDanoBallestaProperty(StringProperty nivelDaOBallesta) {
        this.nivelDanoBallesta = nivelDaOBallesta;
    }

    public StringProperty getNivelEfectoMeteoroProperty() {
        return nivelEfectoMeteoro;
    }

    public void setNivelEfectoMeteoroProperty(StringProperty nivelEfectoMeteoro) {
        this.nivelEfectoMeteoro = nivelEfectoMeteoro;
    }

    public StringProperty getNivelRangoMeteoroProperty() {
        return nivelRangoMeteoro;
    }

    public void setNivelRangoMeteoroProperty(StringProperty nivelRangoMeteoro) {
        this.nivelRangoMeteoro = nivelRangoMeteoro;
    }

    public StringProperty getNivelEfectoHieloProperty() {
        return nivelEfectoHielo;
    }

    public void setNivelEfectoHieloProperty(StringProperty nivelEfectoHielo) {
        this.nivelEfectoHielo = nivelEfectoHielo;
    }

    public StringProperty getNivelRangoHieloProperty() {
        return nivelRangoHielo;
    }


    public void setNivelRangoHieloProperty(StringProperty nivelRangoHielo) {
        this.nivelRangoHielo = nivelRangoHielo;
    }
    
   public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getNivelVelocidadBallesta() {
        if(this.nivelVelocidadBallesta.get() != null && !this.nivelVelocidadBallesta.get().isBlank()){
            return Integer.valueOf(this.nivelVelocidadBallesta.get());
        }
        else{
            return null;
        } 
    }

    public void setNivelVelocidadBallesta(Integer nivelVelocidadBallesta) {
        this.nivelVelocidadBallesta.set(nivelVelocidadBallesta.toString());
    }

    public Integer getNivelDanoBallesta() {
        if(this.nivelDanoBallesta.get() != null && !this.nivelDanoBallesta.get().isBlank()){
            return Integer.valueOf(this.nivelDanoBallesta.get());
        }
        else{
            return null;
        } 
    }

    public void setNivelDanoBallesta(Integer nivelDaOBallesta) {
        this.nivelDanoBallesta.set(nivelDaOBallesta.toString());
    }

    public Integer getNivelEfectoMeteoro() {
        if(this.nivelEfectoMeteoro.get() != null && !this.nivelEfectoMeteoro.get().isBlank()){
            return Integer.valueOf(this.nivelEfectoMeteoro.get());
        }
        else{
            return null;
        }
    }

    public void setNivelEfectoMeteoro(Integer nivelEfectoMeteoro) {
        this.nivelEfectoMeteoro.set(nivelEfectoMeteoro.toString());
    }

    public Integer getNivelRangoMeteoro() {
        if(this.nivelRangoMeteoro.get() != null && !this.nivelRangoMeteoro.get().isBlank()){
            return Integer.valueOf(this.nivelRangoMeteoro.get());
        }
        else{
            return null;
        }
    }

    public void setNivelRangoMeteoro(Integer nivelRangoMeteoro) {
        this.nivelRangoMeteoro.set(nivelRangoMeteoro.toString());
    }

    public Integer getNivelEfectoHielo() {
        if(this.nivelEfectoHielo.get() != null && !this.nivelEfectoHielo.get().isBlank()){
            return Integer.valueOf(this.nivelEfectoHielo.get());
        }
        else{
            return null;
        }
    }

    public void setNivelEfectoHielo(Integer nivelEfectoHielo) {
        this.nivelEfectoHielo.set(nivelEfectoHielo.toString());
    }

    public Integer getNivelRangoHielo() {
        if(this.nivelRangoHielo.get() != null && !this.nivelRangoHielo.get().isBlank()){
            return Integer.valueOf(this.nivelRangoHielo.get());
        }
        else{
            return null;
        }
    }

    public void setNivelRangoHielo(Integer nivelRangoHielo) {
        this.nivelRangoHielo.set(nivelRangoHielo.toString());
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
