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
    private static final String PASSWORD = "Kallel13$";//Password
    private static final String SELECT_QUERY = "SELECT * FROM Cars;";
    private static final String UPDATE_QUERY = "UPDATE Cars SET car_make=?, car_model=?, car_year=?, car_mileage=? WHERE car_id=?;";
    private static final String INSERT_QUERY = "INSERT INTO Cars (car_make, car_model, car_year, car_mileage) VALUES (?,?,?,?);";
    private static final String DELETE_QUERY = "DELETE FROM Cars WHERE car_id=?;";
    private static ResultSet rs;//Query results
    
    public static ResultSet select()
    {
        try
        {
            //Connect to db and execute query
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            PreparedStatement query = conn.prepareStatement(SELECT_QUERY);
            rs = query.executeQuery();
            
            //Return result set
            return rs;
        }
        catch(SQLException sqlEx)
        {
            System.err.println("SOMETHING NULL IS HAPPENING");
            System.err.printf("%s%n",sqlEx.getMessage());
            return null;
        }
    }
    
    public static boolean delete(Car car)
    {
        //Declare and initialize variables
        int id = car.getId();//Car id
        Connection conn;//Connection to db
        PreparedStatement query;//Prepared statement
        
        try
        {
        //Connect to db
        conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        
        //Prepare statement
        query = conn.prepareStatement(DELETE_QUERY);
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
        int year = car.getYear();//Car year
        int mileage = car.getMileage();//Car mileage
        Connection conn;//Connection to db
        PreparedStatement query;//Prepared statement
        
        try
        {
            //Connect to db
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            
            //Prepare statement
            query = conn.prepareStatement(UPDATE_QUERY);
            query.setString(1, make);
            query.setString(2, model);
            query.setInt(3, year);
            query.setInt(4, mileage);
            
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
        int year = car.getYear();//Car year
        int mileage = car.getMileage();//Car mileage
        Connection conn;//Connection to db
        PreparedStatement query;//Prepared statement
        
        try
        {
            //Connect to db
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            
            //Prepare statement
            query = conn.prepareStatement(INSERT_QUERY);
            query.setString(1, make);
            query.setString(2, model);
            query.setInt(3, year);
            query.setInt(4, mileage);
            
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
}
