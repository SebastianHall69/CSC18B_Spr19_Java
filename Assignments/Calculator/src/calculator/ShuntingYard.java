package calculator;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Stack;
import java.math.BigDecimal;
import java.util.EmptyStackException;

public class ShuntingYard {
    //Separates a string builder into a string queue of operands/operators, removes all other string parts
    private static Queue<String> separateTokens(StringBuilder infix) 
    {
        //Declare and initialize variables
        Queue<String> tokens = new LinkedList<>();
        Pattern numPtrn = Pattern.compile("^\\d+(\\.\\d+)?");
        Pattern opPtrn = Pattern.compile("^[+\\-\\^()*\\/%√~−]");
        Matcher numMatch, opMatch;//Matcher for numbers and operations
        
        //Parse entire string into the token queue
        while(infix.length() > 0) 
        {
            //Set matchers for this round
            numMatch = numPtrn.matcher(infix);//Matches floats and ints
            opMatch = opPtrn.matcher(infix);//Matches +-/*^
            
            //First check if a number is found first
            if(numMatch.find())
            {
                tokens.add(numMatch.group());//Add number string to queue
                infix.delete(numMatch.start(), numMatch.end());//Remove string from stringbuilder
                
            }
            //Else if an operator is found
            else if(opMatch.find())
            {
                tokens.add(opMatch.group());
                infix.delete(opMatch.start(), opMatch.end());
            }
            //Else remove first character
            else 
            {
                infix.delete(0, 1);//Delete first character
            }
        }
        
        //Return the queue
        return tokens;
    }
    
    //Converts an infix string to a postfix string queue
    public static Queue<String> infixToPostfix(String infix) throws EmptyStackException
    {
        //Separate infix string into operators/operands and then apply shunting yard algorithm and return as string queue 
        return shuntingYard(separateTokens(new StringBuilder(infix)));
    }
    
    //Sorts a string queue from infix form to postfix and returns the queue
    private static Queue<String> shuntingYard(Queue<String> tokens) throws EmptyStackException
    {
        //Declare variables
        Queue<String> postfix = new LinkedList<>();//Postfix notation string
        Stack<String> operators = new Stack<>();//Operators stack
        
        //Iterate through queue and add tokens to stack or queue, determined by shunting yard
        tokens.forEach((s) -> {
            try
            {
                //Try to add to postfix queue as operand 
                BigDecimal x = new BigDecimal(s);//Throw exception if not operand
                postfix.add(s);
                
            }
            catch(NumberFormatException nfEx)
            {
                //Create operator from string
                Operator op = new Operator(s);
                
                //If operator is a left parenthesis
                if(op.getType().equals("("))
                {
                    operators.push(s);//Add operator to stack
                }
                
                //Else if operator is a right parenthesis
                else if(op.getType().equals(")"))
                {
                    //While next operator is not paired parenthesis and stack is not empty
                    while(!operators.peek().equals("(") && !operators.isEmpty())
                    {
                        postfix.add(operators.pop());//Add operators to postfix queue
                    }
                    //If the paired parenthesis is next pop it without adding
                    if(!operators.isEmpty() && operators.peek().equals("("))
                    {
                        operators.pop();
                    }
                       
                }
                
                //Else if stack is empty or top operator is a left parenthesis
                else if(operators.isEmpty() || operators.peek().equals("(")) 
                {
                    operators.push(s);//Add operator to stack
                }
                
                //Else if operator is a higher prec than top of stack or same prec and right assoc
                else if(op.getPrec() > new Operator(operators.peek()).getPrec() || 
                        op.getPrec() == new Operator(operators.peek()).getPrec() && 
                        op.getAssoc() == 2) 
                {
                    operators.push(s);//Add operator to stack
                }
                
                //Else if operator has lower prec than top of stack or same prec and left assoc
                else if(op.getPrec() < new Operator(operators.peek()).getPrec() ||
                        op.getPrec() == new Operator(operators.peek()).getPrec() && 
                        op.getAssoc() == 1) 
                {
                    //While above condition is true pop stack onto postfix queue
                    while(!operators.isEmpty() && op.getPrec() < new Operator(operators.peek()).getPrec() ||
                            !operators.isEmpty() && op.getPrec() == new Operator(operators.peek()).getPrec() && 
                            op.getAssoc() == 1) 
                    {
                        postfix.add(operators.pop());
                    }
                    //Then add operator to operators stack
                    operators.push(s);
                }
            }
        });
        
        //Then pop remaining operators onto postfix queue if any remain
        while(!operators.isEmpty())
        {
            postfix.add(operators.pop());
        }
        
        //Return postfix formatted queue
        return postfix;
    }
}
