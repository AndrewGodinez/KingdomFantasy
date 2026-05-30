module cr.ac.una.kingdomfantasy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.instrument;
    requires java.sql;
    requires jakarta.persistence;
    requires MaterialFX;

    opens cr.ac.una.kingdomfantasy to javafx.fxml;
    opens cr.ac.una.kingdomfantasy.controller to javafx.fxml;
    opens cr.ac.una.kingdomfantasy.model;                          
    opens cr.ac.una.kingdomfantasy.service;                       
    opens cr.ac.una.kingdomfantasy.util;                           
    
    exports cr.ac.una.kingdomfantasy;
    exports cr.ac.una.kingdomfantasy.controller;
    exports cr.ac.una.kingdomfantasy.model;                       
    exports cr.ac.una.kingdomfantasy.service;  
}
