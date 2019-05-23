package sebastiansjalopydatabase;

import java.sql.ResultSet;//Holds results of query
import java.sql.Connection;//Connection to db
import java.sql.DriverManager;//Helps connect to db
import java.sql.SQLException;//For when my sql statements blow up
import java.sql.PreparedStatement;//Prepared statements


public class CarQueries
{
    //Declare static final methods
    private static final String DB_URL = "jdbc:derby://localhost:1527/Cars";//Url
    private static final String USERNAME = "Sebastian";//Username
    private static final String PASSWORD = "Password";//Password
    private static final String SELECT_QUERY = "SELECT carid, carmake, carmodel, caryear, carmileage FROM Cars";
    private static final String UPDATE_QUERY = "UPDATE Cars SET carmake=?, carmodel=?, caryear=?, carmileage=? WHERE carid=?";
    private static final String INSERT_QUERY = "INSERT INTO Cars (carmake, carmodel, caryear, carmileage) VALUES (?,?,?,?)";
    private static final String DELETE_QUERY = "DELETE FROM Cars WHERE carid=?";
    private static final String COUNT_QUERY = "SELECT COUNT(*) FROM Cars";
    private static final String MODEL_SEARCH_QUERY = "";
    
    public static Car selectRow(int rowNum)
    {
        //Declare variables
        int id = -1;//Default car id
        String make = "Default Make";//Default car make
        String model = "Default Model";//Default car model
        String year = "0000";//Default year
        float mileage = -1.0f;//Default mileage
        
        //Try and get car from specified row number
        try(Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            PreparedStatement query = conn.prepareStatement(SELECT_QUERY, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = query.executeQuery())
        {   
            //If row is found set car data
            if(rs.absolute(rowNum))
            {
                id = rs.getInt(1);
                make = rs.getString(2);
                model = rs.getString(3);
                year = rs.getString(4);
                mileage = rs.getFloat(5);
            }
        }
        catch(SQLException sqlEx)
        {
            System.err.printf("%s%n",sqlEx.getMessage());
        }
        
        //Return the car
        return new Car(id, make, model, year, mileage);
    }
    
    public static boolean delete(int id)
    {
        //Delete car from id
        try(Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            PreparedStatement query = conn.prepareStatement(DELETE_QUERY);)
        {
            //Set elements in prepared statement
            query.setInt(1, id);

            //Execute statement and return result
            query.executeUpdate();
            return true;
        }
        catch(SQLException sqlEx)
        {
            //Return false for sql failure
            System.err.printf("%s%n",sqlEx.getMessage());
            return false;
        }
    }
    
    public static boolean update(Car car) 
    {
        //Declare and initialize variables
        String make = car.getMake();//Car make
        String model = car.getModel();//Car model
        String year = car.getYear();//Car year
        float mileage = car.getMileage();//Car mileage
        int id = car.getId();//Car unique id
                
        try(Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            PreparedStatement query = conn.prepareStatement(UPDATE_QUERY);)
        {
            //Prepare statement
            query.setString(1, make);
            query.setString(2, model);
            query.setString(3, year);
            query.setFloat(4, mileage);
            query.setInt(5, id);
            
            //Execute statement and return boolean result
            query.executeUpdate();
            return true;
        }
        catch(SQLException sqlEx)
        {
            //Return false for sql failure
            System.err.printf("%s%n", sqlEx.getMessage());
            return false;
        }
    }
    
    public static boolean insert(Car car)
    {
        //Declare and initialize variables
        String make = car.getMake();//Car make
        String model = car.getModel();//Car model
        String year = car.getYear();//Car year
        float mileage = car.getMileage();//Car mileage
        
        try(Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            PreparedStatement query = conn.prepareStatement(INSERT_QUERY);)
        {
            //Prepare statement   
            query.setString(1, make);
            query.setString(2, model);
            query.setString(3, year);
            query.setFloat(4, mileage);
            
            //Execute and return result
            query.executeUpdate();
            return true;
        }
        catch(SQLException sqlEx)
        {
            //Return false for sql failure
            System.err.printf("%s%n", sqlEx.getMessage());
            return false;
        }
    }
    
    public static int getNumRecords() {
        //Declare variables
        int numRecords = 0;//Number of records in db
        
        //Try to query database and get number of records
        try(
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            PreparedStatement query = conn.prepareStatement(COUNT_QUERY);
            ResultSet rs = query.executeQuery();
                )
        {
            //Get number of records from query result
            rs.next();//Move cursor to first
            numRecords = rs.getInt(1);//Get number of records
        }
        catch(SQLException sqlEx)
        {
            System.err.printf("ERROR: %s", sqlEx.getMessage());
        }
        
        //Return the number of records
        return numRecords;
    }
}