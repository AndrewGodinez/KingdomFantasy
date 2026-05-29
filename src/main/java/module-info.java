module cr.ac.una.kingdomfantasy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires jakarta.persistence;
    requires MaterialFX;

    opens cr.ac.una.kingdomfantasy to javafx.fxml;
    opens cr.ac.una.kingdomfantasy.controller to javafx.fxml;
    
    exports cr.ac.una.kingdomfantasy;
    exports cr.ac.una.kingdomfantasy.controller;
}
