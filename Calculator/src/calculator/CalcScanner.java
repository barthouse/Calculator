/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

import java.util.*;
//import java.util.regex.*;

// State    Char    New     Action
//
// START    /d      VALUE   value = /d; sign = 1;
// START    '-'     MINUS
// START    '+'     PLUS
// START    '*'     MULT
// START    '/'     DIV
// START    'q'     QUIT
// START    other   ERROR   error: expected /d, '-', '+', '*', '/', 'q'
//
// VALUE    /0      END     token(rational(value,1));
// VALUE    /d      VALUE   value *= 10; value += /d;
// VALUE    '_'     UNDER   
// VALUE    '/'     DENOM   numerator = value; value = 0;
// VALUE    other   ERROR   error: expected /s, /d, '_'
//
// UNDER    /d      NUMER   numerator = /d;
// UNDER    other   ERROR   error: expected /d
//
// NUMER    /d      NUMER   numerator *= 10; numerator += /d;
// NUMER    '/'     FRACT   
// NUMER    other   START   error: expected /s, '/', /d
//
// FRACT    /d      DENOM   denominator = /d;
// FRACT    other   ERROR   error: expected /d
//
// DENOM    /0      DENOM   token(rational(sign * value * demoninator + numerator, denominator));
// DENOM    /d      DENOM   denominator *= 10; demoninator += /d;
// DENOM    other   ERROR   error: expected /s, /d
//
// MINUS    /0      START   token(Subtract);
// MINUS    /d      VALUE   value = /d; sign = -1;
// MINUS    other   ERROR   error: expected /s, /d
//
// PLUS     /0      START   token(Add);
// PLUS     /d      VALUE   value = /d; sign = 1;
// PLUS     other   ERROR   error: expected /s, /d
//
// MUL      /0      START   token(Multiply);
// MUL      other   ERROR   error: expected /s
//
// DIV      /0      START   token(Divide);
// DIV      other   ERROR   error: expected /s
//
// QUIT     'u'     QUIT2
// QUIT     other   ERROR   error: expected 'u'
//
// QUIT2    'i'     QUIT3
// QUIT2    other   ERROR   error: expected 'i'
//
// QUIT3    't'     START   token(Quit);
// QUIT3    other   ERROR   error: expected 't'
//
// QUIT4    /0      END     token(Quit);
// QUIT4    other   ERROR   error: expected /s
//
// ERROR    other   ERROR
//


/**
 *
 * @author Bart
 */
public class CalcScanner {
    
    public enum State 
    {
        START,
        VALUE,
        UNDER,
        NUMER,
        FRACT,
        DENOM,
        MINUS,
        PLUS,
        MUL,
        DIV,
        QUIT,
        QUIT2,
        QUIT3,
        QUIT4,
        ERROR 
    }
    
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
            String tokenString = m_in.next();
            int position = 0;
            int value = 0;
            int numerator = 0;
            int denominator = 0;
            int sign = 1;
            State state = State.START;
            
            while(m_nextToken == null)
            {
                char c = 0;
                
                if (position < tokenString.length()) 
                    c = tokenString.charAt(position++);
                
                switch(state)
                {
                    case START:
                        if (Character.isSpaceChar(c))
                        {
                            // do nothing
                        }
                        else if (Character.isDigit(c))
                        {
                            value = c - '0';
                            sign = 1;
                            state = State.VALUE;
                        }
                        else if (c == '-')
                        {
                            state = State.MINUS;
                        }
                        else if (c == '+')
                        {
                            state = State.PLUS;
                        }
                        else if (c == '*')
                        {
                            state = State.MUL;
                        }
                        else if (c == '/')
                        {
                            state = State.DIV;
                        }
                        else if (c == 'q')
                        {
                            state = State.QUIT;
                        }
                        else
                        {
                            throw new CalculatorException("expected: /d, '+', '-', '/' '*' or 'q'.");
                        }
                        break;

                    case VALUE:
                        if (c == 0)
                        {
                            m_nextToken = new Token(new Rational(sign * value, 1));
                        }
                        else if (Character.isDigit(c))
                        {
                            value *= 10;
                            value += c - '0';
                            state = State.VALUE;
                        }
                        else if (c == '_')
                        {
                            numerator = 0;
                            state = State.UNDER;
                        }
                        else if (c == '/')
                        {
                            numerator = value;
                            value = 0;
                            state = State.FRACT;
                        }
                        else
                        {
                            throw new CalculatorException("expected: /s, /d, '_'");
                        }
                        break;
                        
                    case UNDER:
                        if (Character.isDigit(c))
                        {
                            numerator = c - '0';
                            state = State.NUMER;
                        }
                        else
                        {
                            throw new CalculatorException("expected: /d");
                        }
                        break;

                    case NUMER:
                        if (Character.isDigit(c))
                        {
                            numerator *= 10;
                            numerator += c - '0';
                        }
                        else if (c == '/')
                        {
                            state = State.FRACT;
                        }
                        else
                        {
                            throw new CalculatorException("expected: /s, /d, '/'");
                        }
                        break;
                        
                    case FRACT:
                        if (Character.isDigit(c))
                        {
                            denominator = c - '0';
                            state = State.DENOM;
                        }
                        else
                        {
                            throw new CalculatorException("expected: /d");
                        }
                        break;

                    case DENOM:
                        if (c == 0)
                        {
                            m_nextToken = new Token(new Rational(sign*((value * denominator) + numerator), denominator));
                        }
                        else if (Character.isDigit(c))
                        {
                            denominator *= 10;
                            denominator += c - '0';
                        }
                        else
                        {
                            throw new CalculatorException("expected: /s, /d");
                        }
                        break;

                    case MINUS:
                        if (c == 0)
                        {
                            m_nextToken = new Token(Token.Type.Subtract);
                        }
                        else if (Character.isDigit(c))
                        {
                            value = c - '0';
                            sign = -1;
                            state = State.VALUE;
                        }
                        else
                        {
                            throw new CalculatorException("expected: /s, /d");
                        }
                        break;

                    case PLUS:
                        if (c == 0)
                        {
                            m_nextToken = new Token(Token.Type.Add);
                        }
                        else if (Character.isDigit(c))
                        {
                            value = c - '0';
                            sign = 1;
                            state = State.VALUE;
                        }
                        else
                        {
                            throw new CalculatorException("expected: /s, /d");
                        }
                        break;

                    case MUL:
                        if (c == 0)
                        {
                            m_nextToken = new Token(Token.Type.Multiply);
                        }
                        else
                        {
                            throw new CalculatorException("expected: /s");
                        }
                        break;

                    case DIV:
                        if (c == 0)
                        {
                            m_nextToken = new Token(Token.Type.Divide);
                        }
                        else
                        {
                            throw new CalculatorException("expected: /s");
                        }
                        break;

                    case QUIT:
                        if (c == 'u')
                        {
                            state = State.QUIT2;                            
                        }
                        else
                        {
                            throw new CalculatorException("expected: 'u'");
                        }
                        break;
                        
                    case QUIT2:
                        if (c == 'i')
                        {
                            state = State.QUIT3;                            
                        }
                        else
                        {
                            throw new CalculatorException("expected: 'i'");
                        }
                        break;

                    case QUIT3:
                        if (c == 't')
                        {
                            state = State.QUIT4;                            
                        }
                        else
                        {
                            throw new CalculatorException("expected: 't'");
                        }
                        break;

                    case QUIT4:
                        if (c == 0)
                        {
                            m_nextToken = new Token(Token.Type.Quit);
                        }
                        else
                        {
                            throw new CalculatorException("expected: /s");
                        }
                        break;

                    default:
                        assert(false);
                }
            }

/*
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
*/
            
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

/*    
    private static Pattern m_wholeAndFractionPattern = Pattern.compile("(-?+)(\\d+)_(\\d+)/(\\d+)");
    private static Pattern m_FractionPattern = Pattern.compile("(-?+)(\\d+)/(\\d+)");
    private static Pattern m_wholePattern = Pattern.compile("(-?+)(\\d+)");
    
    private static Pattern m_operatorPattern = Pattern.compile("\\s*([\\+\\-\\*\\/])\\s*");
    
    private static Pattern m_quitPattern = Pattern.compile("quit");
*/
    
    private final Scanner m_in;
    private Token m_nextToken;
    
}
