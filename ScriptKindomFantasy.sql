CREATE TABLE def_mejora 
    ( 
     mej_id                      NUMBER        NOT NULL , 
     mej_nivel_velocidad_ballesta NUMBER (2)    DEFAULT 1 , 
     mej_nivel_dano_ballesta      NUMBER (2)    DEFAULT 1 , 
     mej_nivel_efecto_meteoro    NUMBER (2)    DEFAULT 1 , 
     mej_nivel_rango_meteoro      NUMBER (2)    DEFAULT 1 , 
     mej_nivel_efecto_hielo       NUMBER (2)    DEFAULT 1 , 
     mej_nivel_rango_hielo        NUMBER (2)    DEFAULT 1 ,
     mej_nivel_castillo           NUMBER (2)    DEFAULT 1 ,
     mej_nivel_elixir             NUMBER (2)    DEFAULT 1 ,
     mej_version                  NUMBER        DEFAULT 1
    ) 
;
ALTER TABLE def_mejora 
    ADD CONSTRAINT def_mejora_PK PRIMARY KEY ( mej_id ) ;
 
CREATE TABLE def_partida 
    ( 
     par_id              NUMBER      NOT NULL , 
     par_version         NUMBER      DEFAULT 1 , 
     par_nivel_actual    NUMBER (3)  DEFAULT 1 , 
     par_idply           NUMBER      NOT NULL , 
     par_puntos_actuales NUMBER      DEFAULT 0 , 
     par_idmej           NUMBER      NOT NULL , 
     par_fecha_guardado  DATE        DEFAULT SYSDATE 
    ) 
;
ALTER TABLE def_partida 
    ADD CONSTRAINT def_partid_PK PRIMARY KEY ( par_id ) ;
 
CREATE TABLE def_player 
    ( 
     ply_id             NUMBER              NOT NULL , 
     ply_foto_perfil    BLOB , 
     ply_nombre         VARCHAR2 (40 CHAR)  NOT NULL , 
     ply_fecha_registro DATE                DEFAULT SYSDATE NOT NULL , 
     ply_version        NUMBER              DEFAULT 1 , 
     ply_puntos_totales NUMBER              DEFAULT 0 , 
     ply_id_ballesta    NUMBER              NOT NULL 
    ) 
;
ALTER TABLE def_player 
    ADD CONSTRAINT def_player_PK PRIMARY KEY ( ply_id ) ;
ALTER TABLE def_player 
    ADD CONSTRAINT def_player__UNQ01 UNIQUE ( ply_nombre ) ;
 
ALTER TABLE def_partida 
    ADD CONSTRAINT def_partida_FK01 FOREIGN KEY 
    ( 
     par_idply
    ) 
    REFERENCES def_player 
    ( 
     ply_id
    ) 
;
ALTER TABLE def_partida 
    ADD CONSTRAINT def_partida_FK02 FOREIGN KEY 
    ( 
     par_idmej
    ) 
    REFERENCES def_mejora 
    ( 
     mej_id
    ) 
;
 
CREATE OR REPLACE TRIGGER FKNTM_def_partida 
BEFORE UPDATE OF par_idmej 
ON def_partida 
BEGIN 
  raise_application_error(-20225,'Non Transferable FK constraint on table def_partida is violated'); 
END; 
/
 
CREATE SEQUENCE def_mejora_SEQ01 
    START WITH 1 
    MINVALUE 1 
    NOCACHE ;
 
CREATE OR REPLACE TRIGGER der_mejora_TGR01 
BEFORE INSERT ON def_mejora 
FOR EACH ROW 
BEGIN 
    :NEW.mej_id := def_mejora_SEQ01.NEXTVAL; 
END;
/
 
CREATE SEQUENCE def_partida_SEQ01 
    START WITH 1 
    MINVALUE 1 
    NOCACHE ;
 
CREATE OR REPLACE TRIGGER def_partida_TGR01 
BEFORE INSERT ON def_partida 
FOR EACH ROW 
BEGIN 
    :NEW.par_id := def_partida_SEQ01.NEXTVAL; 
END;
/
 
CREATE SEQUENCE def_player_SEQ01 
    START WITH 1 
    MINVALUE 1 
    NOCACHE ;
 
CREATE OR REPLACE TRIGGER def_player_TGR01 
BEFORE INSERT ON def_player 
FOR EACH ROW 
BEGIN 
    :NEW.ply_id := def_player_SEQ01.NEXTVAL; 
END;
/