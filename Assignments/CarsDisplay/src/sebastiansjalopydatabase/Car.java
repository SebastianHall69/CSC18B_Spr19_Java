package sebastiansjalopydatabase;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Sebastian Hall
 */
public class Car {
    /*
     * Private Member Declaration
     */
    private SimpleIntegerProperty id;
    private SimpleStringProperty make;
    private SimpleStringProperty model;
    private SimpleStringProperty year;
    private SimpleFloatProperty mileage;
    
    /*
     * Constructors
     */
    //Default constructor
    Car()
    {
        this.id = new SimpleIntegerProperty(-1);
        this.make = new SimpleStringProperty("default_make");
        this.model = new SimpleStringProperty("default_model");
        this.year = new SimpleStringProperty("0000");
        this.mileage = new SimpleFloatProperty(-1.0f);
    }
    
    //Constructor with all arguments for creating from db
    Car(int id, String make, String model, String year, float mileage)
    {
        this.id = new SimpleIntegerProperty(id);
        this.make = new SimpleStringProperty(make);
        this.model = new SimpleStringProperty(model);
        this.year = new SimpleStringProperty(year);
        this.mileage = new SimpleFloatProperty(mileage);
    }
    
    //Constructor without id for inserting into db from gui form
    Car(String make, String model, String year, float mileage)
    {
        this.id = new SimpleIntegerProperty(-1);
        this.make = new SimpleStringProperty(make);
        this.model = new SimpleStringProperty(model);
        this.year = new SimpleStringProperty(year);
        this.mileage = new SimpleFloatProperty(mileage);
    }
    
    //Copy constructor
    Car(Car obj)
    {
        this.id = new SimpleIntegerProperty(obj.id.get());
        this.make = new SimpleStringProperty(obj.make.get());
        this.model = new SimpleStringProperty(obj.model.get());
        this.year = new SimpleStringProperty(obj.model.get());
        this.mileage = new SimpleFloatProperty(obj.mileage.get());
    }
    
    /*
     * Setters
     */
    //Set id
    public void setId(int id)
    {
        this.id.set(id);
    }
    
    //Set make
    public void setMake(String make)
    {
        this.make.set(make);
    }
    
    //Set model
    public void setModel(String model)
    {
        this.model.set(model);
    }
    
    //Set year
    public void setYear(String year)
    {
        this.year.set(year);
    }
    
    //Set mileage
    public void setMileage(float mileage)
    {
        this.mileage.set(mileage);
    }
    
    /*
     * Getters
     */
    //Get id
    public int getId()
    {
        return this.id.get();
    }
    
    //Get make
    public String getMake()
    {
        return this.make.get();
    }
    
    //Get model
    public String getModel()
    {
        return this.model.get();
    }
    
    //Get year
    public String getYear()
    {
        return this.year.get();
    }
    
    //Get mileage
    public float getMileage()
    {
        return this.mileage.get();
    }
    
    /*
     * Display
     */
    //To String
    @Override
    public String toString()
    {
        return "Id: " + this.id.get() + "\nMake: " + this.make.get() + 
                "\nModel: " + this.model.get() + "\nYear: " + this.year.get() + 
                "\nMileage: " + this.mileage.get() + "\n";
    }
}