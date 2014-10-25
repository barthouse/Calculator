/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

/**
 *
 * @author Bart
 */
public class Rational {
        
    @Override
    public String toString()
    {
        String string = "";
        
        assert(m_denominator != 0);
        
        if (m_numerator == 0)
        {
            string = "0";
        }
        else
        {
            int whole = m_numerator / m_denominator;
            int numerator = m_numerator - whole * m_denominator;

            if (whole != 0)
            {
                string = Integer.toString(whole);
            }

            if (whole != 0 && numerator != 0)
            {
                string += "_";
            }

            if(numerator != 0)
            {
                string += Integer.toString(numerator);
                string += "/";
                string += Integer.toString(m_denominator);
            }
        }
        
        return string;
    }
        
    public Rational (int wholeNumber)
    {
        m_numerator = wholeNumber;
        m_denominator = 1;
    }
    
    public Rational ( int numerator, int denominator ) throws CalculatorException
    {
        if (denominator == 0) 
        {
            throw new CalculatorException("Rational can not have zero denomiator");
        }
                
        m_numerator = numerator;
        m_denominator = denominator;
        
        Simplify();
    }
    
    public void Add(Rational in)
    {
        int lcm = Fractions.Lcm(m_denominator, in.m_denominator);
        
        m_numerator *= lcm / m_denominator;
        m_denominator = lcm;
                
        m_numerator += in.m_numerator * (lcm / in.m_denominator);
        
        Simplify();
    }
    
    public void Subtract(Rational in)
    {
        int lcm = Fractions.Lcm(m_denominator, in.m_denominator);
        
        m_numerator *= lcm / m_denominator;
        m_denominator = lcm;
                
        m_numerator -= in.m_numerator * (lcm / in.m_denominator);
        
        Simplify();
    }
    
    public void Multiply(Rational in)
    {
        m_numerator *= in.m_numerator;
        m_denominator *= in.m_denominator;
        
        Simplify();
    }
    
    public void Divide(Rational in)
    {
        m_numerator *= in.m_denominator;
        m_denominator *= in.m_numerator;
        
        Simplify();
    }

    private void Simplify()
    {
        if (m_numerator == 0) 
            m_denominator = 1;
        else
        {
            
            int gcd = Fractions.Gcd(Math.abs(m_numerator), m_denominator);
            
            m_numerator /= gcd;
            m_denominator /= gcd;
        }
    }
    
    private int m_numerator;
    private int m_denominator;
    
}
