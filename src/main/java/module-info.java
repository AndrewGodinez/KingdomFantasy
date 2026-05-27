module una.ac.cr.kingdomfantasy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires jakarta.persistence;
    requires MaterialFX;

    opens una.ac.cr.kingdomfantasy to javafx.fxml;
    opens una.ac.cr.kingdomfantasy.controller to javafx.fxml;
    
    exports una.ac.cr.kingdomfantasy;
    exports una.ac.cr.kingdomfantasy.controller;
}
