/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vistaAquilaExtra;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author brawl
 */
public class DataTable {
    public SimpleStringProperty canton;  
    public SimpleStringProperty matriculaD;
    public SimpleStringProperty ultmrev;
    public SimpleStringProperty ultmH; 
    public SimpleStringProperty matriculaH;

    public DataTable(String canton, String matriculaD, String ultmrev, String ultmH, String matriculaH) {
        this.canton = new SimpleStringProperty(canton);
        this.matriculaD =  new SimpleStringProperty(matriculaD);
        this.ultmrev =  new SimpleStringProperty(ultmrev);
        this.ultmH =  new SimpleStringProperty(ultmH);
        this.matriculaH =  new SimpleStringProperty(matriculaH);
        
       
    }

    public String getCanton() {
        return canton.get();
    }

    public String getMatriculaD() {
        return matriculaD.get();
    }

    public String getUltmrev() {
        return ultmrev.get();
    }

    public String getUltmH() {
        return ultmH.get();
    }

    public String getMatriculaH() {
        return matriculaH.get();
    }
    
    
    
    
}
