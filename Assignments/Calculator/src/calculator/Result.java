package calculator;

import java.io.Serializable;

public class Result implements Serializable
{
    //Declare fields
    private String name;//Saved name of the result
    private String data;//Numeric data saved
    
    //Constructor
    Result(String name, String data)
    {
        this.name = name;
        this.data = data;
    }
    
    //Setters
    public void setName(String name)
    {
        this.name = name;
    }
    
    public void setData(String data)
    {
        this.data = data;
    }
    
    //Getters
    public String getName()
    {
        return this.name;
    }
    
    public String getData()
    {
        return this.data;
    }
}
