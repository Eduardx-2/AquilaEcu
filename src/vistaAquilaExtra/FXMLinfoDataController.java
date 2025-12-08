/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package vistaAquilaExtra;



import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author brawl
 */
public class FXMLinfoDataController implements Initializable {
    
    @FXML
    private TableView<DataTable> dataTablex;
    @FXML
    private TableColumn<DataTable,String> columnCanton;    
    @FXML
    private TableColumn<DataTable,String> columnMatr;  
    @FXML
    private TableColumn<DataTable,String> columnrev;
    @FXML 
    private TableColumn<DataTable,String> columnUltm;
    @FXML
    private TableColumn<DataTable,String> columnHasta;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
     
   
    public void setTableData(JSONArray dataJson){
        JSONObject objData = dataJson.getJSONObject(1);
        System.out.println(objData);
        columnCanton.setCellValueFactory(new PropertyValueFactory<>("canton"));
        columnMatr.setCellValueFactory(new PropertyValueFactory<>("matriculaD"));
        columnrev.setCellValueFactory(new PropertyValueFactory<>("ultmrev"));
        columnUltm.setCellValueFactory(new PropertyValueFactory<>("ultmH"));
        columnHasta.setCellValueFactory(new PropertyValueFactory<>("matriculaH"));
       
        ObservableList<DataTable> dataAgg = FXCollections.observableArrayList(new DataTable(objData.getString("registrationCanton"),objData.getString("lastRegistrationDate"),objData.getString("registrationExpiryDate"),objData.getString("inspectionDate"),objData.getString("registrationExpiryDate")));
        dataTablex.refresh();
        dataTablex.setItems(dataAgg);
    }
    
}
