/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Bart
 */
public class Operator {
    
    static Pattern m_pattern = Pattern.compile("\\s*([\\+\\-\\*\\/])\\s*");
    
    Operator(Type type)
    {
        m_type = type;
    }
        
    public static Operator GetNext(Scanner in) throws CalculatorException
    {        
        if( in.hasNext(m_pattern) )
        {
            String operatorString = in.next(m_pattern);
            Matcher m = m_pattern.matcher(operatorString);
            boolean found = m.find();
            assert(found);
            
            Type type;
            
            if (m.group(1).charAt(0) == '+')
                type = Type.Add;
            else if (m.group(1).charAt(0) == '-')
                type = Type.Subtract;
            else if (m.group(1).charAt(0) == '*')
                type = Type.Multiply;
            else 
            {
                assert(m.group(1).charAt(0) == '/');
                type = Type.Divide;
            }
            
            return new Operator(type);

        }
        else
        {
            throw new CalculatorException("Expected operator");
        }
    
    }
    
    static enum Type { Add, Subtract, Divide, Multiply };
    
    Type m_type;
    
}
