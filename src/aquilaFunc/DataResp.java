/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aquilaFunc;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author brawl
 */
public class DataResp {
    public SimpleStringProperty owner;  
    public SimpleStringProperty modelo;
    public SimpleStringProperty clase;
    public SimpleStringProperty servicio; 
    public SimpleStringProperty pais;
    public SimpleStringProperty año;

    public DataResp(String owner, String modelo, String clase, String servicio, String pais, String año) {
        this.owner = new SimpleStringProperty(owner);
        this.modelo = new SimpleStringProperty(modelo);
        this.clase = new SimpleStringProperty(clase);
        this.servicio = new SimpleStringProperty(servicio);
        this.pais = new SimpleStringProperty(pais);
        this.año = new SimpleStringProperty(año);
    }

   
    
    public String getOwner() {
        return owner.get();
    }

    public String getModelo() {
        return modelo.get();
    }

    public String getClase() {
        return clase.get();
    }

    public String getServicio() {
        return servicio.get();
    }

    public String getPais() {
        return pais.get();
    }

    public String getAño() {
        return año.get();
    }

  
    
}
