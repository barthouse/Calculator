/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Bart
 */
public class Rational {
    
    public void Print(PrintStream out)
    {
        if (m_numerator == 0)
        {
            out.print("0");
        }
        else
        {
            int whole = m_numerator / m_denominator;
            int numerator = m_numerator - whole * m_denominator;

            if (whole != 0)
            {
                out.print(whole);
            }

            if (whole != 0 && numerator != 0)
            {
                out.print("_");
            }

            if(numerator != 0)
            {
                out.print(numerator + "/" + m_denominator);
            }
        }
    }

    static Pattern m_wholeAndFractionPattern = Pattern.compile("(-?+)(\\d+)_(\\d+)/(\\d+)");
    static Pattern m_FractionPattern = Pattern.compile("(-?+)(\\d+)/(\\d+)");
    static Pattern m_wholePattern = Pattern.compile("(-?+)(\\d+)");
    
    static Rational GetNext(Scanner in) throws CalculatorException
    {        
        // match longest pattern first
        if( in.hasNext(m_wholeAndFractionPattern) )
        {
            String matchString = in.next(m_wholeAndFractionPattern);
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

            return new Rational(numerator, denominator);

        }
        else if( in.hasNext(m_FractionPattern) )
        {
            String matchString = in.next(m_FractionPattern);
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

            return new Rational(numerator, denominator);

        }
        else if( in.hasNext(m_wholePattern) )
        {
            String matchString = in.next(m_wholePattern);
            Matcher m = m_wholePattern.matcher(matchString);
            boolean found = m.find();
            assert(found);
           
            int numerator = Integer.parseInt(m.group(2));
            int denominator = 1;
            
            if ( m.group(1).length() == 1)
            {
                numerator *= -1;
            }

            return new Rational(numerator, denominator);

        }
        else
        {
            throw new CalculatorException("Expected Rational");
        }
    
    }
    
    static int Gcd(int a, int b)
    {
        assert(a>0);
        
        if (b == 0) return a;
        return Gcd(b, a % b);
    }
    
    static int Lcm(int a, int b)
    {
        return a * b / Gcd(a,b);
    }
    
    Rational ( int numerator, int denominator )
    {
        assert(denominator != 0 || ((numerator == 0) && (denominator == 0)));
        
        m_numerator = numerator;
        m_denominator = denominator;
        
        Simplify();
    }
    
    void Add(Rational in)
    {
        int lcm = Lcm(m_denominator, in.m_denominator);
        
        m_numerator *= lcm / m_denominator;
        m_denominator = lcm;
                
        m_numerator += in.m_numerator * (lcm / in.m_denominator);
        
        Simplify();
    }
    
    void Subtract(Rational in)
    {
        int lcm = Lcm(m_denominator, in.m_denominator);
        
        m_numerator *= lcm / m_denominator;
        m_denominator = lcm;
                
        m_numerator -= in.m_numerator * (lcm / in.m_denominator);
        
        Simplify();
    }
    
    void Multiply(Rational in)
    {
        m_numerator *= in.m_numerator;
        m_denominator *= in.m_denominator;
        
        Simplify();
    }
    
    void Divide(Rational in)
    {
        m_numerator *= in.m_denominator;
        m_denominator *= in.m_numerator;
        
        Simplify();
    }

    private void Simplify()
    {
        if (m_numerator == 0) 
            m_denominator = 0;
        else
        {
            int gcd = Gcd(m_numerator, m_denominator);
            
            m_numerator /= gcd;
            m_denominator /= gcd;
        }
    }
    
    int m_numerator;
    int m_denominator;
    
}
