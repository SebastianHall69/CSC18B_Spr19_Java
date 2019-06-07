package calculator;

public class Operator {
    //Declare fields
    private final String type;
    private final Precedence prec;
    private final Associativity assoc;
    
    //Enumerators
    public enum Precedence
    {
        AS(1), MD(2), E(3), P(4);
        
        private final int prec;
        private Precedence(int prec)
        {
            this.prec = prec;
        }
        public int getPrec()
        {
            return this.prec;
        }
    }

    public enum Associativity
    {
        LEFT(1), RIGHT(2), NONE(0);
        private final int assoc;
        private Associativity(int assoc)
        {
            this.assoc = assoc;
        }
        public int getAssoc()
        {
            return this.assoc;
        }
    }
    
    //Constructor
    Operator(String type)
    {
        //Set type
        this.type = type;
        
        //Determine precedence
        switch(this.type)
        {
            case "+":;
            case "−":;
            case "-": prec = Precedence.AS;break;
            case "~":;
            case "%":;
            case "*":;
            case "/": prec = Precedence.MD;break;
            case "√":;
            case "^": prec = Precedence.E;break;
            case "(":;
            case ")": prec = Precedence.P;break;
            default: prec = Precedence.AS;//Lowest precedence by default
        }
        
        //Determine associativity
        switch(this.type)
        {
            case "+":;
            case "−":;
            case "-":;
            case "%":;
            case "/":;
            case "*": assoc = Associativity.LEFT;break;
            case "√":;
            case "~":;
            case "^": assoc = Associativity.RIGHT;break;
            case "(":;
            case ")": assoc = Associativity.NONE;break;
            default: assoc = Associativity.NONE;//Unused precedence by default
        }
    }
    
    //Get Methods
    public String getType()
    {
        return this.type;
    }
    
    public int getPrec()
    {
        return this.prec.getPrec();
    }
    
    public final int getAssoc()
    {
        return this.assoc.getAssoc();
    }
}