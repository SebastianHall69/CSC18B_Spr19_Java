/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sebastiansjalopydatabase;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author sebastian
 */
public class CarsDisplayController implements Initializable {
    
    @FXML
    private Button previousButton, nextButton, modelSearchButton, browseAllButton,
            insertNewButton, deleteCurrentButton;
    
    @FXML
    private TextField currentRecordTextField, totalRecordsTextField, 
            carIdTextField, carMakeTextField, carModelTextField, carYearTextField,
            carMileageTextField;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
