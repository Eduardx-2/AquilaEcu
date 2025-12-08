/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package vistaForm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author brawl
 */
public class FXMLdbDataController implements Initializable {
    
    @FXML
    private TextField textUser;
    @FXML
    private TextField textPort;
    @FXML
    private TextField textDb;
    @FXML
    private TextField textPass;
    private String urBase;
    private String urPass;
    private String user;
    
    private final String URI_FINAL = System.getProperty("user.dir") +  "\\src\\vistaForm\\data_user.json";
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
    public void dataDbSql(ActionEvent event){
        JSONObject jsonB = new JSONObject();
        
        jsonB.put("User", textUser.getText());
        jsonB.put("Port", textPort.getText());
        jsonB.put("BaseName", textDb.getText());
        jsonB.put("Pass", textPass.getText());
        
        try{
         
            File file = new File(URI_FINAL);
            FileWriter wr = new FileWriter(file);
            wr.write(jsonB.toString());
            wr.flush();
            wr.close();
           
        }catch(IOException e){
            e.printStackTrace();
                 
        }
    }
    
    public void dataTextinput() throws IOException{
        
        Path path = Paths.get(URI_FINAL);
        
        if (!Files.exists(path) || Files.size(path) == 0){
            return;
        }
        String cont = new String(Files.readAllBytes(path));
        JSONObject obj = new JSONObject(cont);
        textUser.setText(obj.getString("User"));
        textPort.setText(obj.getString("Port"));
        textPass.setText(obj.getString("Pass"));
        textDb.setText(obj.getString("BaseName"));
        urBase = obj.getString("BaseName");
        urPass = obj.getString("Pass");
        user = obj.getString("User");
    }
    
    public void deleteDataText(ActionEvent event) throws IOException{
        
        File file = new File(URI_FINAL);
        FileWriter wr = new FileWriter(file); 
        wr.flush();
        wr.close();
    }
    
    public void createDataTable(ActionEvent event) throws ClassNotFoundException, SQLException{
        
        try {
           Connection connect = null;
           PreparedStatement qrw = null;

           Class.forName("com.mysql.cj.jdbc.Driver");
           connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/", user, urPass);



           Statement stmt = connect.createStatement();
           stmt.execute(String.format("CREATE DATABASE IF NOT EXISTS %s", urBase));
           stmt.close();
           connect.close();
           connect = DriverManager.getConnection(String.format("jdbc:mysql://localhost:3306/%s", urBase), user, urPass);
           qrw = connect.prepareStatement(
                 "CREATE TABLE IF NOT EXISTS dataInfo(" +
                 "id INT NOT NULL AUTO_INCREMENT, " +
                 "owners VARCHAR(50) NOT NULL, " +
                 "clase VARCHAR(30) NOT NULL, " +
                 "modelo VARCHAR(40) NOT NULL, " +
                 "servicio VARCHAR(50) NOT NULL, " +
                 "canton VARCHAR(50) NOT NULL, " +
                 "año varchar(60) NOT NULL, " +
                 "PRIMARY KEY(id)) ENGINE=InnoDB;"
            );

            qrw.execute();
            qrw.close();
            connect.close();
           
       }catch(java.sql.SQLException | java.lang.NullPointerException e){
            
           errorAlert("war",e.toString(),"WARNING ERROR DATA NULL");
       }
       

            
    }
    
    private void errorAlert(String tipoError,String text,String herror){
        Alert alertV = null;
        if (tipoError.equals("war")){
            alertV = new Alert(AlertType.WARNING);
            alertV.setTitle("WARNING!!");
        }else if (tipoError.equals("info")){
            alertV = new Alert(AlertType.INFORMATION);
            alertV.setTitle("WARNING!");
        }
        
        alertV.setHeaderText(herror);
        alertV.setContentText(text);
        DialogPane dialogPane = alertV.getDialogPane();
        dialogPane.getStylesheets().add(
            getClass().getResource("/vistaForm/fxmldbdata.css").toExternalForm()
        );

        alertV.show();
    }
}
