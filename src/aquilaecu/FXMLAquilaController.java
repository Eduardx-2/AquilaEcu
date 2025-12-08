/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package aquilaecu;

import aquilaFunc.DataResp;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vistaForm.FXMLdbDataController;
import aquilaFunc.Func_app;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import vistaAquilaExtra.FXMLinfoDataController;

/**
 *
 * @author brawl
 */
public class FXMLAquilaController implements Initializable {
    
    /**
     * Data info json
     */
    private JSONArray arraysj = null;
    
    @FXML
    private TextField inputSearch;
    @FXML
    private Button btnSearch;
    @FXML
    private ImageView imageViewer;
    @FXML
    private Button btnImage;
    
    
    /**
     dataColumn- columnas name
     **/
    @FXML
    private TableColumn<DataResp,String> columnD;
    @FXML
    private TableColumn<DataResp,String> columnM;
    @FXML
    private TableColumn<DataResp,String> columnC;
    @FXML
    private TableColumn<DataResp,String> columnS;
    @FXML
    private TableColumn<DataResp,String> columnP;
    @FXML
    private TableColumn<DataResp,String> columnA;
    @FXML
    private TableView<DataResp> dataTable;
    
    private final String URI_FINAL = System.getProperty("user.dir") +  "\\src\\vistaForm\\data_user.json";
    
    private void errorAlert(String tipoError,String text){
        Alert alertV = null;
        if (tipoError.equals("war")){
            alertV = new Alert(AlertType.WARNING);
            alertV.setTitle("WARNING!!");
        }
       
        alertV.setContentText(text);
        DialogPane dialogPane = alertV.getDialogPane();
        dialogPane.getStylesheets().add(
            getClass().getResource("/aquilaecu/style.css").toExternalForm()
        );

        alertV.show();
    }
    
    @FXML
    private void buttonInsert(ActionEvent event) throws IOException, SQLException, ClassNotFoundException{
        try{
            String cont = lecterConfig();
            JSONObject obj = new JSONObject(cont);
            Connection connect = null;
        
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = (Connection) DriverManager.getConnection(
                    String.format("jdbc:mysql://localhost:%s/%s", obj.getString("Port"),obj.get("BaseName")),obj.getString("User"),obj.getString("Pass"));
            PreparedStatement ps = connect.prepareStatement("INSERT INTO dataInfo(owners,clase,modelo,servicio,canton,año) VALUES(?,?,?,?,?,?)");
            
            ps.setString(1, arraysj.getJSONObject(0).getString("name"));
            ps.setString(2, arraysj.getJSONObject(1).getString("model"));
            ps.setString(3, arraysj.getJSONObject(1).getString("vehicleClass"));
            ps.setString(4, arraysj.getJSONObject(1).getString("serviceType"));
            ps.setString(5, "Porto");
            ps.setString(6, arraysj.getJSONObject(1).getString("purchaseDate"));
            ps.execute();
           
            
        }catch(org.json.JSONException e){
            errorAlert("war","WARNING DATOS NULOS!!");
        }
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        

        String jsonSt = Func_app.data_apiowner(inputSearch.getText()).trim();
        jsonSt = jsonSt.replaceAll("[\\n\\r\\t]", "");
        arraysj = new JSONArray(jsonSt);
        JSONObject ownerdata = arraysj.getJSONObject(0);
        JSONObject dataCar = arraysj.getJSONObject(1);
        columnD.setCellValueFactory(new PropertyValueFactory<>("owner"));
        columnM.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        columnC.setCellValueFactory(new PropertyValueFactory<>("clase"));
        columnS.setCellValueFactory(new PropertyValueFactory<>("servicio"));
        columnP.setCellValueFactory(new PropertyValueFactory<>("pais"));
        columnA.setCellValueFactory(new PropertyValueFactory<>("año"));
     
        ObservableList<DataResp> datas = FXCollections.observableArrayList(new DataResp(ownerdata.getString("name"),dataCar.getString("model"),
                dataCar.getString("vehicleClass"),dataCar.getString("serviceType"),dataCar.getString("countryOfManufacture"),dataCar.getString("purchaseDate")));
        dataTable.refresh();
        dataTable.setItems(datas);
        
    }
    
    @FXML
    private void handleActivateBtn(ActionEvent event){
        
        FileChooser file = new FileChooser();
        file.setTitle("Select Image");
        file.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Imagenes", "*.png", "*.jpg", "*.jpeg"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        File fileImg = file.showOpenDialog(stage);
       
        if (fileImg != null) {
            
            Image img = new Image(fileImg.toURI().toString());
            imageViewer.setImage(img);
           
        }
    }
    
    @FXML
    private void buttonEventWindow(ActionEvent event){
        try{
            FXMLLoader loadfx = new FXMLLoader(getClass().getResource("/vistaForm/FXMLdbData.fxml"));
            Parent root = loadfx.load();
            FXMLdbDataController controll = loadfx.getController();
            
            controll.dataTextinput();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            
            stage.initModality(Modality.APPLICATION_MODAL);
            
            stage.setScene(scene);
            stage.showAndWait();
            
        }catch(IOException except){
            System.out.println(except);
            
        }
    }
    
    @FXML
    private void buttonInfoExtra(ActionEvent event){
         try{
            FXMLLoader loadfxs = new FXMLLoader(getClass().getResource("/vistaAquilaExtra/FXMLinfoData.fxml"));
            Parent root = loadfxs.load();
            FXMLinfoDataController controll = loadfxs.getController();
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setOnShown(e -> {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Consulta Extra Información");
                alert.setHeaderText("Usted está por realizar una consulta de información extra sobre el vehiculo.");
                alert.setContentText("Opciónes");
                ButtonType conf = new ButtonType("Ok",ButtonData.OK_DONE);
                ButtonType exit = new ButtonType("Cancel",ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(conf,exit);
                
                DialogPane pxns = alert.getDialogPane();
                pxns.getStylesheets().add(
                        getClass().getResource("/aquilaecu/style.css").toExternalForm()
                );
                Optional<ButtonType> btnClick = alert.showAndWait();
 
                if (!btnClick.isPresent()) {
                    stage.hide();
                    return;
                }
                
                if (btnClick.get().getButtonData() == ButtonBar.ButtonData.OK_DONE){
                    controll.setTableData(arraysj);
                    
                }else if (btnClick.get().getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE){
                    stage.hide();
                }
                
            });
            stage.showAndWait();
            
        }catch(IOException except){
            System.out.println(except);
            
        }
        
    }
    
    private String lecterConfig() throws IOException{
        Path path = Paths.get(URI_FINAL);
        if (!Files.exists(path) || Files.size(path) == 0){
            return "";
        }
        String cont = new String(Files.readAllBytes(path));
        return cont;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    
    
}
