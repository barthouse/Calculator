/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

import java.util.regex.*;

/**
 *
 * @author Bart
 */
public class Calculator {
    
    static Pattern m_multiplyDividePattern = Pattern.compile("\\s*[\\*/]\\s*");
    static Pattern m_addSubtractPattern = Pattern.compile("\\s*[\\+\\-]\\s*");
    
    static public Rational ParseTerm(CalcScanner in) throws CalculatorException
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
    
    static public Rational ParseExpression(CalcScanner in) throws CalculatorException
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
    
    static public Rational ParseInput(CalcScanner in) throws CalculatorException
    {
        Rational a = ParseExpression(in);

        Token token = in.nextToken();
        
        if(!token.isQuit()) throw new CalculatorException("Expected quit");
        
        return a;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String string = "-12_2/3 + 5_3/4 * 4 - 5/4 * 9/4 / 1/2 quit";
//        String string = "1 quit";
        CalcScanner s = new CalcScanner(string);
        
        try
        {
            Rational a = ParseInput(s);

            System.out.print("Answer = ");
            a.Print(System.out);
            System.out.print("\n");
        }
        catch(CalculatorException e)
        {
            System.out.print("Error:" + e + "\n");
        }
    }
    
}
