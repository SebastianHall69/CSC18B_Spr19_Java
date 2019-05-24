/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sebastiansjalopydatabase;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author sebastian
 */
public class CarsDisplayController implements Initializable {
    
    @FXML
    private Button previousButton, nextButton, modelSearchButton, browseAllButton,
            insertNewButton, deleteCurrentButton, updateCurrentButton;
    
    @FXML
    private TextField currentRecordTextfield, totalRecordsTextfield, 
            carIdTextfield, carMakeTextfield, carModelTextfield, carYearTextfield,
            carMileageTextfield, modelSearchTextfield;
    
    @FXML
    private TableView recordsTableview;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Set starting mode for program
        if(CarQueries.getNumRecords() == 0)
        {
            noCarsMode();
        }
        else
        {
            carsMode();
        }
        
        //Create table columns
        TableColumn id = new TableColumn("Id"), make = new TableColumn("Make"), 
                model = new TableColumn("Model"), year = new TableColumn("Year"),
                mileage = new TableColumn("Mileage");
        
        //Set column width
        make.setMinWidth(125);
        model.setMinWidth(125);
        
        //Add columns to table
        recordsTableview.getColumns().setAll(id, make, model, year, mileage);
        
        //Associate columns with data
        id.setCellValueFactory(new PropertyValueFactory<Car, Integer>("id"));
        make.setCellValueFactory(new PropertyValueFactory<Car, String>("make"));
        model.setCellValueFactory(new PropertyValueFactory<Car, String>("model"));
        year.setCellValueFactory(new PropertyValueFactory<Car, String>("year"));
        mileage.setCellValueFactory(new PropertyValueFactory<Car, Float>("mileage"));
        
        //Update tableview data
        updateTableview();
        
        //Add event handler for next button
        nextButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent event) {
                //Declare and initialize variables
                int cur = Integer.parseInt(currentRecordTextfield.getText());//Get current number
                int max = Integer.parseInt(totalRecordsTextfield.getText());//Get max number
                
                //Adjust current record number based on last record number
                cur = (cur >=max) ? max : ++cur;
                
                //Change record
                changeRecord(cur);
            }
        });
        
        //Add event handler for previous button
        previousButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent event) {
                //Declare and initialize variables
                int cur = Integer.parseInt(currentRecordTextfield.getText());
                int min = 1;//First car record in db
                
                //Adjust current record number based on first record number
                cur = (cur <= min) ? min : --cur;
                
                //Change record
                changeRecord(cur);
            }
        });
        
        //Add event handler for insert new entry button
        insertNewButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent event)
            {
                //Create car from form data
                Car newCar = getCarData();
                
                //If no car present then leave
                if(newCar == null)
                {
                    return;//Leave and do nothing
                }
                
                //Insert new car
                if(CarQueries.insert(newCar))
                {
                    //Update totalRecords and tableview
                    updateTotalRecords();
                    updateTableview();
                    
                    //Clear out textfields
                    clearFields();
                    
                    //Check if carsMode needed
                    if(CarQueries.getNumRecords() > 0)
                    {
                        carsMode();
                    }
                }
                else
                {
                    System.err.printf("%n%n%nERROR: FAILURE TO INSERT NEW CAR%%n%n%n");
                }
            }
        });
        
        //Add event handler for delete current entry button
        deleteCurrentButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent event)
            {
                //Declare variables
                int id = -1;
                
                //Try to get valid id from the form
                try
                {
                    id = Integer.parseInt(carIdTextfield.getText());
                }
                catch(NumberFormatException nfEx)
                {
                    System.err.printf("ERROR: %s",nfEx.getMessage());
                    carIdTextfield.setText("No Car To Delete Yet");
                    return;
                }
                
                //Delete car from form
                if(CarQueries.delete(id))
                {
                    //Update total records and tableview
                    updateTotalRecords();
                    updateTableview();
                    
                    //Check if no cars mode is needed
                    if(CarQueries.getNumRecords() == 0)
                    {
                        noCarsMode();
                        clearFields();
                    }
                    else
                    {
                        if(Integer.parseInt(currentRecordTextfield.getText()) > Integer.parseInt(totalRecordsTextfield.getText()))
                        {
                            changeRecord(Integer.parseInt(totalRecordsTextfield.getText()));
                        }
                        else
                        {
                            changeRecord(Integer.parseInt(currentRecordTextfield.getText()));
                        }
                    }
                }
            }
        });
        
        //Add event handler for update current entry button
        updateCurrentButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent event)
            {
                //Get data from textfields
                Car car = getCarData();
                
                //If car has all necessary data
                if(car != null)
                {
                    try
                    {
                        int id = Integer.parseInt(carIdTextfield.getText());
                        if(id < 0) throw new NumberFormatException();
                        car.setId(id);
                        CarQueries.update(car);
                        updateTableview();
                    }
                    catch(NumberFormatException nfEx)
                    {
                        carIdTextfield.setText("Invalid car id for update");
                    }
                    
                }
                else
                {
                    System.err.println("ERROR CAR IS NULL");
                }
            }
        });
        
        //Add event handler for browse all button
        browseAllButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent event)
            {
                //Check if currently visible or not
                if(recordsTableview.isVisible())
                {
                    browseAllButton.setText("Browse All Records");
                    recordsTableview.setVisible(false);
                }
                else
                {
                    browseAllButton.setText("Hide All Records");
                    recordsTableview.setVisible(true);
                    updateTableview();
                }
            }
        });
        
        //Add event handler for model search button
        modelSearchButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent> (){
            @Override
            public void handle(MouseEvent event)
            {
                //Declare variables
                String model = modelSearchTextfield.getText();
                
                //Exit function if model is empty
                if(model.equals("")) return;
                
                //Search for items and set into table, set visible
                recordsTableview.setItems(CarQueries.modelSearch(model));
                recordsTableview.setVisible(true);
                browseAllButton.setText("Hide Records");
                
                //Clear model search textfield
                modelSearchTextfield.setText("");
            }
        });
        
        //Add event handler for tableview row selection
        recordsTableview.getSelectionModel().selectedItemProperty().addListener((new ChangeListener<Car>() {
                @Override
                public void changed(ObservableValue<? extends Car> observable, Car oldValue, Car newValue) {
                    //Change record to selected row
                    changeRecord(1 + recordsTableview.getSelectionModel().getSelectedIndex());
                }  
            }));
        
        //Add event handler for current record textfield
        currentRecordTextfield.textProperty().addListener(new ChangeListener<String> () {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                try
                {
                    int rowNum = Integer.parseInt(newValue);
                    int max = Integer.parseInt(totalRecordsTextfield.getText());
                    rowNum = (rowNum > max) ? max : (rowNum < 1) ? 1 : rowNum;
                    changeRecord(rowNum);
                }
                catch(NumberFormatException nfEx)
                {
                    //Nothing
                }
            }
        });
    }
    
    public void noCarsMode() {
        //Disable buttons
        previousButton.setDisable(true);
        nextButton.setDisable(true);
        modelSearchButton.setDisable(true);
        browseAllButton.setDisable(true);
        deleteCurrentButton.setDisable(true);
        updateCurrentButton.setDisable(true);
        
        //Disable textfields
        currentRecordTextfield.setDisable(true);
        modelSearchTextfield.setDisable(true);
        
        //Set default values in current record/ total record textfields
        currentRecordTextfield.setText("N/A");
        totalRecordsTextfield.setText("N/A");
    }
    
    public void carsMode() {
        //Enable buttons
        previousButton.setDisable(false);
        nextButton.setDisable(false);
        modelSearchButton.setDisable(false);
        browseAllButton.setDisable(false);
        deleteCurrentButton.setDisable(false);
        updateCurrentButton.setDisable(false);
        
        //Enable textfields
        currentRecordTextfield.setDisable(false);
        modelSearchTextfield.setDisable(false);
        
        //Set values for record text fields
        currentRecordTextfield.setText("1");
        totalRecordsTextfield.setText(Integer.toString(CarQueries.getNumRecords()));
        
        //Set car data for first record
        setCarData(1);
    }
    
    public void setCarData(int rowNum)
    {
        //Load data up into appropriate textfields
        Car car = CarQueries.selectRow(rowNum);
        carIdTextfield.setText(Integer.toString(car.getId()));
        carMakeTextfield.setText(car.getMake());
        carModelTextfield.setText(car.getModel());
        carYearTextfield.setText(car.getYear());
        carMileageTextfield.setText(Float.toString(car.getMileage()));
    }
    
    public Car getCarData()
    {
        //Declare and initialize variables
        String make, model, year;//Car data
        make = model = year = "";//Default values
        float  mileage = -1.0f;//Car data, default value
        boolean isValid = true;//Flag if car data is valid or not
        Car car = null;//Car to be returned
        
        //Get car data from textfields
        try
        {
            make = carMakeTextfield.getText();
            model = carModelTextfield.getText();
            year = carYearTextfield.getText();
            mileage = Float.parseFloat(carMileageTextfield.getText());
        }
        catch(NumberFormatException nfEx)
        {
            carMileageTextfield.setText("Enter Numbers Only");
            isValid = false;
        }
        
        //Check for all valid car info
        if(make.equals(""))
        {
            carMakeTextfield.setText("Required");
            isValid = false;
        }
        if(model.equals(""))
        {
            carModelTextfield.setText("Required");
            isValid = false;
        }
        if(year.equals(""))
        {
            carYearTextfield.setText("Required");
            isValid = false;
        }
        if(mileage == -1.0f)
        {
            carMileageTextfield.setText("Required");
            isValid = false;
        }
        
        //Build car if valid info, if not just keep car null
        if(isValid)
        {
            car = new Car(make, model, year, mileage);
        }
        
        //Return the car
        return car;
    }
    
    public void updateTotalRecords() {
        totalRecordsTextfield.setText(Integer.toString(CarQueries.getNumRecords()));
    }
    
    public void clearFields()
    {
        //Clear out the enterable textfields
        carIdTextfield.clear();
        carMakeTextfield.clear();
        carModelTextfield.clear();
        carYearTextfield.clear();
        carMileageTextfield.clear();
    }
    
    public void changeRecord(int rowNum)
    {
        currentRecordTextfield.setText(Integer.toString(rowNum));
        setCarData(rowNum);
    }
    
    public void updateTableview()
    {
        //Set observable array and columns in tableview
        recordsTableview.setItems(CarQueries.getRecords());
    }
}

