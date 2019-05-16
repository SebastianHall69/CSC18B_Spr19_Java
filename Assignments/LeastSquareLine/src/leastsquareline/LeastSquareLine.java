package leastsquareline;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

public class LeastSquareLine 
{
    
    public static void main(String[] args)
    {
        //Declare and initialize variables
        boolean badFile;//Flag for proper file being selected
        PrintStream cout = new PrintStream(System.out);//Shorter output
        PrintStream error = new PrintStream(System.err);//Error output
        String filename = null;//Filename to be read in
        Scanner cin = new Scanner(System.in);//Std input
        Scanner file = null;//File scanner for float values
        short count = 0;//Number of ordered pairs
        float sumX = 0.0F;//Sum of the x of all ordered pairs
        float sumY = 0.0F;//Sum of the y of all ordered pairs
        float sumXsq = 0.0F;//Sum of all x^2
        float sumXY = 0.0F;//Sum of all x*y
        float xAvg = 0.0F;//Average of the sum of all x
        float yAvg = 0.0F;//Average of the sum of all y
        float slope = 0.0F;//Calculated slope
        float intercept = 0.0F;//Calulated intercept
        
        //Get filename from user and create file Scanner
        do
        {
            try
            {
                //Get file name from user
                badFile = false;//Reset flag
                cout.printf("Enter the filename: ");//Get file name
                filename = cin.nextLine();
                
                //Create Scanner to scan file for input
                FileInputStream inStream = new FileInputStream(filename);
                file = new Scanner(inStream);
            }
            catch(FileNotFoundException fnfEx) 
            {
                //Output error message and set flag
                error.printf("%s%n", fnfEx.getMessage());
                badFile = true;
            }
        } while(badFile);
        
        //Loop through file and sum values
        try
        {
            while(file.hasNext())
            {   
                //Read the x,y pair from file
                float x = file.nextFloat();
                float y = file.nextFloat();
                
                //Sum values
                ++count;//Increment count
                sumX += x;//Add x to the sum of all x's
                sumY += y;//Add y to the sum of all y's
                sumXsq += Math.pow(x, 2);//Add x^2 to the sum of all x^2's
                sumXY += x * y;//Add x*y to the sum of all x*y's

            }
        }
        catch(InputMismatchException imEx) 
        {
            //Output error message and quit program
            error.printf("Something that wasn't a float made it into the stream%n");
            System.exit(1);
        }
        catch(NoSuchElementException nseEx) 
        {
            //Output error message and quit program
            error.printf("No more elements in file, but still reading from it%n");
            System.exit(1);
        }
        
        //Calculate the x avg, y avg, slope, and intercept
        xAvg = sumX / count;//X average
        yAvg = sumY / count;//Y average
        slope = (sumXY - sumX * yAvg) / (sumXsq - sumX * xAvg);
        intercept = yAvg - slope * xAvg;
        
        //Output results
        cout.printf("Equation of the least squares line: Y = %.5fX + %.5f%n", slope, intercept);
    }
}
