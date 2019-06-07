/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Queue;
import java.util.Stack;
import java.util.EmptyStackException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;


/**
 *
 * @author sebastian
 */
public class CalculatorFXMLController implements Initializable {
    //Private textfields in fxml
    @FXML
    private TextField inputTextfield, resultTextfield;
    
    //Private fxml choicebox
    @FXML
    private ChoiceBox resultsChoicebox;
    
    //Private buttons in fxml
    @FXML
    private Button oneButton, twoButton, threeButton, fourButton, fiveButton,
            sixButton, sevenButton, eightButton, nineButton, zeroButton,
            addButton, subtractButton, multiplyButton, divideButton, modButton, 
            decimalButton, backButton, clearButton, leftParenButton, 
            rightParenButton, powerButton, sqrtButton, negativeButton, equalButton,
            saveResultButton;
    
    //Private fields
    ObservableList<Result> savedResults;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Add event handlers for all buttons that append text to inpput
        Button [] buttons = {oneButton, twoButton, threeButton, fourButton,
            fiveButton,sixButton, sevenButton, eightButton, nineButton, zeroButton,
            addButton, subtractButton, multiplyButton, divideButton, modButton,
            decimalButton, leftParenButton, rightParenButton, powerButton};
        for(Button b : buttons)
        {
            b.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {insertInput(b.getText());});
        }
        
        //Add event handler for sqrt button
        sqrtButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {insertInput(sqrtButton.getText() + "(");});
        
        //Add event handler for negative button
        negativeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {insertInput("~");});
        
        //Add event handler for clear button
        clearButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent) -> {inputTextfield.clear();});
        
        //Add event handler for back button
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            //If at least 1 character behind caret delete it 
            int caretPos = inputTextfield.getCaretPosition();
            if(caretPos > 0){
                inputTextfield.setText(new StringBuilder(inputTextfield.getText()).deleteCharAt(caretPos - 1).toString());
                inputTextfield.positionCaret(caretPos - 1);
            }
        });
        
        //Add event handler for equal button
        equalButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            resultTextfield.setText(format(calculate()));
            inputTextfield.clear();
        } );
        
        //Add event handler for save result button
        saveResultButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::saveResult);
        
        //Add event handler for action on input textfield
        inputTextfield.addEventHandler(ActionEvent.ACTION, this::setAnswer);
        
        //Add event hanlder for mouse click on result textfield
        resultTextfield.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            insertInput(resultTextfield.getText());
            inputTextfield.requestFocus();
            inputTextfield.end();
        });
        
        //Add event handler for choicebox
        resultsChoicebox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                //Get selected result
                try
                {
                    int index = Integer.parseInt(newValue.toString());
                    String result = savedResults.get(index).getData();
                    //Insert it into the input textfield
                    try
                    {
                        inputTextfield.requestFocus();
                        inputTextfield.end();
                        insertInput(format(new BigDecimal(result)));
                    }
                    catch(NumberFormatException nfEx)
                    {
                        insertInput("NaN");
                    }
                }
                catch(ArrayIndexOutOfBoundsException ex)
                {
                    //Nothing
                }
                
                
            }
        });
        
        //Load results
        savedResults = FXCollections.observableArrayList();
        loadResults();
        
        //Add tooltips
        Tooltip justTheTip1 = new Tooltip("Insert One Of Your Saved Result");
        Tooltip justTheTip2 = new Tooltip("Remainder");
        Tooltip justTheTip3 = new Tooltip("Negative");
        Tooltip justTheTip4 = new Tooltip("Exponent");
        Tooltip justTheTip5 = new Tooltip("Square Root");
        Tooltip justTheTip6 = new Tooltip("Backspace");
        Tooltip justTheTip7 = new Tooltip("Clear All");
        resultsChoicebox.setTooltip(justTheTip1);
        modButton.setTooltip(justTheTip2);
        negativeButton.setTooltip(justTheTip3);
        powerButton.setTooltip(justTheTip4);
        sqrtButton.setTooltip(justTheTip5);
        backButton.setTooltip(justTheTip6);
        clearButton.setTooltip(justTheTip7);
        
        
    }
    
    public void loadResults()
    {
        //Set saved results in choicebox
        try(Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/CalcResults", "Sebastian", "Password");
            PreparedStatement query = conn.prepareStatement("SELECT binary_data FROM results");
            ResultSet rs = query.executeQuery();)
        {
            while(rs.next())
            {
                byte [] arr = rs.getBytes(1);
                try(ByteArrayInputStream bis = new ByteArrayInputStream(arr);
                    ObjectInputStream in = new ObjectInputStream(bis);)
                {
                    Result r = (Result) in.readObject();
                    savedResults.add(r);
                }
                catch(IOException | ClassNotFoundException ex)
                {
                    System.err.printf("ERROR: %s%n", ex.getMessage());
                }
            }
        }
        catch(SQLException sqlEx)
        {
            System.err.printf("ERROR: %s%n", sqlEx.getMessage());
        }
        
        setSaved();
    }
    
    public void saveResult(MouseEvent event) 
    {   
        //Create and display modal save dialog
        Stage stage = new Stage();
        Label title = new Label("Enter a Result Name");
        TextField saveName = new TextField();
        Button saveButton = new Button("Save Results");
        VBox parent = new VBox();
        parent.setSpacing(8);
        parent.setPadding(new Insets(14));
        parent.getChildren().add(title);
        parent.getChildren().add(saveName);
        parent.getChildren().add(saveButton);
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Save Result");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        
        saveButton.setOnMouseClicked((MouseEvent mEvent) -> {
            //If save name is empty
            if(saveName.getText().isEmpty())
            {
                saveName.setText("Enter A Name");
                saveName.requestFocus();
                return;//Exit function
            }
            else if(resultTextfield.getText().isEmpty())
            {
                saveName.setText("No Result To Save");
                saveName.requestFocus();
                return;
            }
            
            //Create result and serialize, store as byte array
            Result r = new Result(saveName.getText(), resultTextfield.getText());
            byte [] arr = null;
            try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bos);)
            {
                out.writeObject(r);
                out.flush();
                arr = bos.toByteArray();
            }
            catch(IOException ioEx)
            {
                System.err.printf("ERROR: %s%n", ioEx.getMessage());
            }
            
            //Save result in db
            try(Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/CalcResults", "Sebastian", "Password");
                PreparedStatement query = conn.prepareStatement("INSERT INTO results (binary_data) VALUES (?)");)
            {
                query.setBytes(1, arr);
                query.executeUpdate();
            }
            catch(SQLException sqlEx)
            {
                System.err.printf("ERROR: %s%n", sqlEx.getMessage());
            }
            
            //Add result to current saved list and reload saved results
            savedResults.add(r);
            setSaved();
            
            //Close window
            stage.close();
            
        } );
    }
    
    public void setSaved()
    {
        //Create list of string titles to store in choicebox
        ObservableList<String> resultTitles = FXCollections.observableArrayList();
        savedResults.forEach((r)-> { resultTitles.add(r.getName() + ": " + r.getData()); });
        
        //Set list of saved result titles in choicebox
        resultsChoicebox.setItems(resultTitles);
    }
    
    public BigDecimal calculate()
    {
        //Declare and initialize variables
        String infix = inputTextfield.getText();//Infix string provided by user
        Queue<String> postfix;//Postfix operator/operand queue
        Stack<String> stack = new Stack();//Stack for evaluating postfix expression
        
        //Convert infix to postfix
        postfix = ShuntingYard.infixToPostfix(infix);
        
        //Calculate value from postfix
        while(postfix.size() > 0)
        {
            //Add the element to the stack
            stack.push(postfix.remove());
            
            //Try to cast as a float to see if it is an operand
            try
            {
                Float.parseFloat(stack.peek());
            }
            catch(NumberFormatException nfEx)
            {
                try{
                    //Declare variables
                    String operator = stack.pop();//Operator between values
                    BigDecimal rVal = new BigDecimal(stack.pop());//Right hand values
                    BigDecimal lVal = new BigDecimal("-1000000000");//Default, obvious is not working
                    if(!operator.equals("√") && !operator.equals("~")){
                        lVal = new BigDecimal(stack.pop());//Left hand values
                    }
                    BigDecimal newVal;//Value of combined lVal and rVal
                    MathContext context = new MathContext(10, RoundingMode.HALF_EVEN);

                    //Switch to determine operation
                    switch(operator)
                    {
                        case "+": newVal = lVal.add(rVal);break;//Add values
                        case "−":;
                        case "-": newVal = lVal.subtract(rVal);break;//Subtract right from left
                        case "*": newVal = lVal.multiply(rVal);break;//Multiply values
                        case "/": newVal = lVal.divide(rVal, context);break;//Divide right from left
                        case "^": newVal = lVal.pow(rVal.intValue());break;//Exponent, integer only
                        case "%": newVal = lVal.remainder(rVal);break;//Modulus
                        case "√": newVal = new BigDecimal(Math.pow(rVal.doubleValue(), 0.5));break;
                        case "~": newVal = rVal.multiply(new BigDecimal(-1));break;
                        default: newVal = new BigDecimal(-1);//Default of negative one, I guess
                    }

                    //Push operand back onto stack
                    stack.push(newVal.toString());
                }
                catch(EmptyStackException esEx)
                {
                    resultTextfield.setText("Malformed Expression");
                    return null;
                }
            }
        }
        //Return final answer
        return new BigDecimal(stack.pop());
    }
    
    public String format(BigDecimal val) 
    {
        try{
            if(val.toString().length() > 14)
            {
                NumberFormat format = new DecimalFormat("0.0E0");
                format.setRoundingMode(RoundingMode.HALF_EVEN);
                format.setMaximumIntegerDigits(12);
                return format.format(val);
            }
            else {
                return val.toString();
            }
        }
        catch(NullPointerException npEx)
        {
            return "Malformed Expression";
        }
    }
    
    public void insertInput(String input)
    {
        int caretPos = inputTextfield.getCaretPosition();
        inputTextfield.setText(inputTextfield.getText().substring(0, caretPos) +
                input + inputTextfield.getText().substring(caretPos, inputTextfield.getText().length()));
        inputTextfield.positionCaret(caretPos + input.length());
    }
    
    public void setAnswer(ActionEvent event)
    {
        resultTextfield.setText(format(calculate()));
        inputTextfield.clear();
    }
}