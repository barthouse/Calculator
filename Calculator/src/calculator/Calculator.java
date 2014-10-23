/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

import java.util.regex.*;
import java.util.*;

/**
 *
 * @author Bart
 */
public class Calculator {
    
    static Pattern m_multiplyDividePattern = Pattern.compile("\\s*[\\*/]\\s*");
    static Pattern m_addSubtractPattern = Pattern.compile("\\s*[\\+\\-]\\s*");
    
    static public Rational ParseTerm(Scanner in) throws CalculatorException
    {
        Rational a = Rational.GetNext(in);
                
        if(in.hasNext(m_multiplyDividePattern))
        {
            Operator op = Operator.GetNext(in);

            assert(op.m_type == Operator.Type.Multiply ||
                   op.m_type == Operator.Type.Divide);

            Rational b = ParseTerm(in);

            if (op.m_type == Operator.Type.Multiply)
                a.Multiply(b);
            else
                a.Divide(b);
        }
        
        return a;
    }
    
    static public Rational ParseExpression(Scanner in) throws CalculatorException
    {
        Rational a = ParseTerm(in);
        
        if (in.hasNext(m_addSubtractPattern))
        {
            Operator op = Operator.GetNext(in);
        
            assert(op.m_type == Operator.Type.Add ||
                   op.m_type == Operator.Type.Subtract);
            
            Rational b = ParseExpression(in);

            if (op.m_type == Operator.Type.Add)
                a.Add(b);
            else
                a.Subtract(b);
        }
        
        return a;
    }
    
    static public Rational ParseInput(Scanner in) throws CalculatorException
    {
        Rational a = ParseExpression(in);

        if(!in.hasNext("quit")) throw new CalculatorException("Expected quit");
        
        return a;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String string = "-12_2/3 + 5_3/4 * 4 - 5/4 * 9/4 / 1/2 quit";
        Scanner s = new Scanner(string);
        
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
