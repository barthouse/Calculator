/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

import java.util.*;
import java.util.regex.*;

/**
 *
 * @author Bart
 */
public class CalcScanner {
    
    CalcScanner(String in)
    {
        m_in = new Scanner(in);
        m_nextToken = null;
    }
    
    boolean hasNextRational() throws CalculatorException
    {
        if(hasNextToken())
        {
            return m_nextToken.isRational();
        }
        
        return false;
    }
    
    Rational nextRational() throws CalculatorException
    {
        if (hasNextRational())
        {
            Rational rational = m_nextToken.m_rational;
            m_nextToken = null;
            
            return rational;
        }
        
        throw new CalculatorException("Expected Rational");
    }

    boolean hasNextToken() throws CalculatorException
    {
        if (m_nextToken == null && m_in.hasNext())
        {            
            // match longest pattern first
            if( m_in.hasNext(m_wholeAndFractionPattern) )
            {
                String matchString = m_in.next(m_wholeAndFractionPattern);
                Matcher m = m_wholeAndFractionPattern.matcher(matchString);
                boolean found = m.find();
                assert(found);

                int whole = Integer.parseInt(m.group(2));
                int numerator = Integer.parseInt(m.group(3));
                int denominator = Integer.parseInt(m.group(4));

                if (denominator == 0) throw new CalculatorException("Denominator can not be zero");

                numerator += whole * denominator;

                if ( m.group(1).length() == 1)
                {
                    numerator *= -1;
                }
                
                m_nextToken = new Token(new Rational(numerator, denominator));
                
            }
            else if( m_in.hasNext(m_FractionPattern) )
            {
                String matchString = m_in.next(m_FractionPattern);
                Matcher m = m_FractionPattern.matcher(matchString);
                boolean found = m.find();
                assert(found);

                int numerator = Integer.parseInt(m.group(2));
                int denominator = Integer.parseInt(m.group(3));

                if (denominator == 0) throw new CalculatorException("Denominator can not be zero");

                if ( m.group(1).length() == 1)
                {
                    numerator *= -1;
                }

                m_nextToken = new Token(new Rational(numerator, denominator));
                
            }
            else if( m_in.hasNext(m_wholePattern) )
            {
                String matchString = m_in.next(m_wholePattern);
                Matcher m = m_wholePattern.matcher(matchString);
                boolean found = m.find();
                assert(found);

                int numerator = Integer.parseInt(m.group(2));
                int denominator = 1;

                if ( m.group(1).length() == 1)
                {
                    numerator *= -1;
                }

                m_nextToken = new Token(new Rational(numerator, denominator));
                
            }
            else if( m_in.hasNext(m_operatorPattern) )
            {
                String matchString = m_in.next(m_operatorPattern);
                Matcher m = m_operatorPattern.matcher(matchString);
                boolean found = m.find();
                assert(found);

                Token.Type type;
                
                if (m.group(1).charAt(0) == '+')
                    type = Token.Type.Add;
                else if (m.group(1).charAt(0) == '-')
                    type = Token.Type.Subtract;
                else if (m.group(1).charAt(0) == '*')
                    type = Token.Type.Multiply;
                else 
                {
                    assert(m.group(1).charAt(0) == '/');
                    type = Token.Type.Divide;
                }
            
                m_nextToken = new Token(type);
                
            }
            else if( m_in.hasNext(m_quitPattern) )
            {
                String matchString = m_in.next(m_quitPattern);
                Matcher m = m_quitPattern.matcher(matchString);
                boolean found = m.find();
                assert(found);
                
                m_nextToken = new Token(Token.Type.Quit);
            }
            else
            {
                throw new CalculatorException("Unrecognized token");
            }
        }
        
        return (m_nextToken != null);
    }

    Token peekNextToken()throws CalculatorException
    {   
        if (!hasNextToken())
        {
            throw new NoSuchElementException();
        }
        
        return m_nextToken;
    }
    
    Token nextToken()throws CalculatorException
    {
        if (!hasNextToken())
        {
            throw new NoSuchElementException();
        }
        
        Token nextToken = m_nextToken;
        m_nextToken = null;
        
        return nextToken;
    }

    static Pattern m_wholeAndFractionPattern = Pattern.compile("(-?+)(\\d+)_(\\d+)/(\\d+)");
    static Pattern m_FractionPattern = Pattern.compile("(-?+)(\\d+)/(\\d+)");
    static Pattern m_wholePattern = Pattern.compile("(-?+)(\\d+)");
    
    static Pattern m_operatorPattern = Pattern.compile("\\s*([\\+\\-\\*\\/])\\s*");
    
    static Pattern m_quitPattern = Pattern.compile("quit");

    private final Scanner m_in;
    private Token m_nextToken;
    
}
