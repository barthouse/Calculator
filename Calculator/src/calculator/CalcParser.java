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

        Token token = calcScanner.nextToken();
        
        if(!token.isQuit()) throw new CalculatorException("Expected quit");
        
        return a;
    }

}
