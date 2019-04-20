/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regexhelper;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;//For fxml text fields
import javafx.beans.value.ChangeListener;//To add change listener for string
import javafx.beans.value.ObservableValue;//For overriding abstract method changed
import java.util.regex.*;//For Pattern, Matcher, and PatternSyntaxException

/**
 *
 * @author sebastian
 */
public class FXMLDocumentController implements Initializable {
    //FXML controllers
    @FXML
    private TextField regexTextField;
    @FXML
    private TextField inputTextField;
    @FXML
    private TextField replaceTextField;
    @FXML
    private TextField resultTextField;
    
    //Variables
    String result = "";//Final result string
    String pattern = "";//Regex pattern from user
    String input = "";//Input from user to be matched against
    String replace = "";//String to replace pattern match
    Pattern p;//Regex pattern to be matched
    Matcher m;//Regex matcher
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Regex field handler
        regexTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                pattern = newValue;
                update();
            }
        });
        
        //String to match field handler
        inputTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                input = newValue;
                update();
            }
        });
        
        //String to replace field handler
        replaceTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                replace = newValue;
                update();
            }
        });
    }
    
    public void update() {
        try{
            p = Pattern.compile(pattern);
            m = p.matcher(input);
            result = m.replaceAll(replace);
        }
        catch(PatternSyntaxException ex) {
            result = ex.getDescription();
        }
        resultTextField.setText(result);
    }
    
    
}
