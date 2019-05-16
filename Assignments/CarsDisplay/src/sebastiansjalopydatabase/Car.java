package sebastiansjalopydatabase;

/**
 *
 * @author Sebastian Hall
 */
public class Car {
    /*
     * Private Member Declaration
     */
    private int id;
    private String make;
    private String model;
    private int year;
    private int mileage;
    
    /*
     * Constructors
     */
    //Default constructor
    Car()
    {
        id = -1;
        make = "default_make";
        model = "default_model";
        year = -1;
        mileage = -1;
    }
    
    //Constructor with all arguments for creating from db
    Car(int id, String make, String model, int year, int mileage)
    {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
    }
    
    //Constructor without id for inserting into db from gui form
    Car(String make, String model, int year, int mileage)
    {
        this.id = -1;
        this.make = make;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
    }
    
    //Copy constructor
    Car(Car obj)
    {
        this.id = obj.id;
        this.make = obj.make;
        this.model = obj.model;
        this.year = obj.year;
        this.mileage = obj.mileage;
    }
    
    /*
     * Setters
     */
    //Set id
    public void setId(int id)
    {
        this.id = id;
    }
    
    //Set make
    public void setMake(String make)
    {
        this.make = make;
    }
    
    //Set model
    public void setModel(String model)
    {
        this.model = model;
    }
    
    //Set year
    public void setYear(int year)
    {
        this.year = year;
    }
    
    //Set mileage
    public void setMileage(int mileage)
    {
        this.mileage = mileage;
    }
    
    /*
     * Getters
     */
    //Get id
    public int getId()
    {
        return this.id;
    }
    
    //Get make
    public String getMake()
    {
        return this.make;
    }
    
    //Get model
    public String getModel()
    {
        return this.model;
    }
    
    //Get year
    public int getYear()
    {
        return this.year;
    }
    
    //Get mileage
    public int getMileage()
    {
        return this.mileage;
    }
    
    /*
     * Display
     */
    //To String
    @Override
    public String toString()
    {
        return "Id: " + this.id + "\nMake: " + this.make + "\nModel: " + this.model 
                + "\nYear: " + this.year + "\nMileage: " + this.mileage + "\n";
    }
}