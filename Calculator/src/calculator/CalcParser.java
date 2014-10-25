/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

import java.util.*;

/**
 *
 * @author Bart
 */
public class CalcParser {
    
    static private Rational ParseTerm(CalcScanner in) throws CalculatorException
    {
        Rational a = in.nextRational();
        
        if (in.hasNextToken() && 
            (in.peekNextToken().isMultiply() || in.peekNextToken().isDivide()))
        {
            Token token = in.nextToken();
                
            Rational b = ParseTerm(in);

            if (token.m_type == Token.Type.Multiply)
                a.Multiply(b);
            else
                a.Divide(b);
        }
        
        return a;
    }
    
    static private Rational ParseExpression(CalcScanner in) throws CalculatorException
    {
        Rational a = ParseTerm(in);
        
        if (in.hasNextToken() &&
            (in.peekNextToken().isAdd() || in.peekNextToken().isSubtract()))
        {
            Token token = in.nextToken();
                        
            Rational b = ParseExpression(in);

            if (token.m_type == Token.Type.Add)
                a.Add(b);
            else
                a.Subtract(b);
        }
        
        return a;
    }
        
    public static Rational ParseInput(Scanner in) throws CalculatorException
    {
        CalcScanner calcScanner = new CalcScanner(in);
        
        Rational a = ParseExpression(calcScanner);

        if (!calcScanner.hasNextToken()) throw new CalculatorException("Expected quit");
        
        Token token = calcScanner.nextToken();
        
        if(!token.isQuit()) throw new CalculatorException("Expected quit");
        
        return a;
    }
    
    public static void TestSuccessCase(String input, Rational expectedValue)
    {
        Rational result;
        
        try
        {
            result = ParseInput(new Scanner(input));

            if (!result.equals(expectedValue))
            {   
                System.out.print("Test Failure:");
                System.out.print(" Input:'" + input);
                System.out.print("' Expected:" + expectedValue.toString());
                System.out.print(" Acutal: " + result.toString() + "\n");
            }
        }
        catch(CalculatorException e)
        {
            System.out.print("Test Failure:");
            System.out.print(" Input:'" + input);
            System.out.print("' Generated Exception:" + e + "\n");
        }

    }
    
    public static void TestFailureCase(String input)
    {   
        boolean exceptionOccured = false;
        
        try
        {
            ParseInput(new Scanner(input));            
        }
        catch(CalculatorException e)
        {
            exceptionOccured = true;
        }
        
        if (!exceptionOccured)
        {
            System.out.print("Test Failure:");
            System.out.print(" Input:'" + input);
            System.out.print("' Expected exception\n");
        }
    }
    
    public static void Test()
    {
        // Success cases
        try
        {
            TestSuccessCase("0 quit", new Rational(0));
            TestSuccessCase("1 quit", new Rational(1));
            TestSuccessCase("0/1 quit", new Rational(0));
            TestSuccessCase("1 + 1 quit", new Rational(2));
            TestSuccessCase("4/8 quit", new Rational(1, 2));
            TestSuccessCase("12_4/8 quit", new Rational(25, 2));
            TestSuccessCase("1/2 + 1/2 quit", new Rational(1));
            TestSuccessCase("1/2 - 1/2 quit", new Rational(0));
            TestSuccessCase("1/2 * 3/4 quit", new Rational(3,8));
            TestSuccessCase("1/2 / 3/4 quit", new Rational(2,3));
            TestSuccessCase("1 + 4 * 2 - 3 quit", new Rational(6));

            // Failure cases
            TestFailureCase("1/2");
            TestFailureCase("1/0 quit");
            TestFailureCase("1 2 quit");
            TestFailureCase("1/ 2 quit");
            TestFailureCase("1/2 quite");
            TestFailureCase("1 /2 quit");
            TestFailureCase("1_ /2 quit");
            TestFailureCase("1_2/ quit");
            TestFailureCase("1_/ quit");
            TestFailureCase("_2/1 quit");
            TestFailureCase("1/2+ 1/2 quit");
            TestFailureCase("+1/+2 + 1/2 quit");
            TestFailureCase("1/2 + + 1/2 quit");
        }
        catch(CalculatorException e)
        {
            System.out.print("Unexpected exception testing: " + e + "\n");
        }
        
    }

}
