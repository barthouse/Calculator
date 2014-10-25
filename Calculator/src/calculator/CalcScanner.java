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
public class CalcScanner {
    
    public CalcScanner(Scanner in)
    {
        m_in = in;
        m_nextToken = null;
    }
    
    public boolean hasNextRational() throws CalculatorException
    {
        if(hasNextToken())
        {
            return m_nextToken.isRational();
        }
        
        return false;
    }
    
    public Rational nextRational() throws CalculatorException
    {
        if (hasNextRational())
        {
            Rational rational = m_nextToken.m_rational;
            m_nextToken = null;
            
            return rational;
        }
        
        throw new CalculatorException("Expected Rational");
    }
    
    public boolean hasNextToken() throws CalculatorException
    {
        if (m_nextToken == null && m_in.hasNext())
        {            
            m_nextToken =  m_tokenizer.stringToToken(m_in.next());
        }
        
        return (m_nextToken != null);
    }

    public Token peekNextToken()throws CalculatorException
    {   
        if (!hasNextToken())
        {
            throw new NoSuchElementException();
        }
        
        return m_nextToken;
    }
    
    public Token nextToken()throws CalculatorException
    {
        if (!hasNextToken())
        {
            throw new NoSuchElementException();
        }
        
        Token nextToken = m_nextToken;
        m_nextToken = null;
        
        return nextToken;
    }

    private final Tokenizer m_tokenizer = new Tokenizer();
    private final Scanner m_in;
    private Token m_nextToken;
    
}
