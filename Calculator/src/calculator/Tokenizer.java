/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

import java.util.*;
import java.util.regex.*;

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
public class Tokenizer {
    
    public enum State 
    {
        START,
        INTEGER_OR_NUMERATOR,
        UNDERSCORE,
        NUMERERATOR,
        FRACTION,
        DENOMINATOR,
        MINUS,
        PLUS,
        MULTIPLY,
        DIVIDE
    }
    
    public Tokenizer()
    {
        // do nothing
    }
    
    private String m_string;
    private int    m_position;
    private int    m_integerOrNumerator;
    private int    m_integer;
    private int    m_numerator;
    private int    m_denominator;
    private int    m_sign;
    private State  m_state;
    private Token  m_token;
    
    private void evalStartState(char c) throws CalculatorException
    {
        if (Character.isSpaceChar(c))
        {
            // not expected
            assert(false);
        }
        else if (Character.isDigit(c))
        {
            m_integerOrNumerator = c - '0';
            m_sign = 1;
            m_state = State.INTEGER_OR_NUMERATOR;
        }
        else if (c == '-')
        {
            m_state = State.MINUS;
        }
        else if (c == '+')
        {
            m_state = State.PLUS;
        }
        else if (c == '*')
        {
            m_state = State.MULTIPLY;
        }
        else if (c == '/')
        {
            m_state = State.DIVIDE;
        }
        else if (c == 'q' || c == 'Q')
        {
            if (m_string.equalsIgnoreCase("quit"))
            {
                m_token = new Token(Token.Type.Quit);                
            }    
            else
            {
                throw new CalculatorException("expected: \"quit\".");
            }
        }
        else
        {
            throw new CalculatorException("expected: /d, '+', '-', '/' '*' or 'q'.");
        }        
    }
    
    private void evalIntegerOrNumeratorState(char c) throws CalculatorException
    {
        if (c == 0)
        {
            m_token = new Token(new Rational(m_sign * m_integerOrNumerator, 1));
        }
        else if (Character.isDigit(c))
        {
            m_integerOrNumerator *= 10;
            m_integerOrNumerator += c - '0';
        }
        else if (c == '_')
        {
            m_integer = m_integerOrNumerator;
            m_state = State.UNDERSCORE;
        }
        else if (c == '/')
        {
            m_integer = 0;
            m_numerator = m_integerOrNumerator;
            m_state = State.FRACTION;
        }
        else
        {
            throw new CalculatorException("expected: /s, /d, '_'");
        }
    }
    
    private void evalUnderscoreState(char c) throws CalculatorException
    {
        if (Character.isDigit(c))
        {
            m_numerator = c - '0';
            m_state = State.NUMERERATOR;
        }
        else
        {
            throw new CalculatorException("expected: /d");
        }
    }
    
    private void evalNumeratorState(char c) throws CalculatorException
    {
        if (Character.isDigit(c))
        {
            m_numerator *= 10;
            m_numerator += c - '0';
        }
        else if (c == '/')
        {
            m_state = State.FRACTION;
        }
        else
        {
            throw new CalculatorException("expected: /s, /d, '/'");
        }        
    }
    
    private void evalFractionState(char c) throws CalculatorException
    {
        if (Character.isDigit(c))
        {
            m_denominator = c - '0';
            m_state = State.DENOMINATOR;
        }
        else
        {
            throw new CalculatorException("expected: /d");
        }        
    }

    private void evalDenominatorState(char c) throws CalculatorException
    {
        if (c == 0)
        {
            m_token = new Token(new Rational(m_sign*((m_integer * m_denominator) + m_numerator), m_denominator));
        }
        else if (Character.isDigit(c))
        {
            m_denominator *= 10;
            m_denominator += c - '0';
        }
        else
        {
            throw new CalculatorException("expected: /s, /d");
        }        
    }
    
    private void evalMinusState(char c) throws CalculatorException
    {
        if (c == 0)
        {
            m_token = new Token(Token.Type.Subtract);
        }
        else if (Character.isDigit(c))
        {
            m_integerOrNumerator = c - '0';
            m_sign = -1;
            m_state = State.INTEGER_OR_NUMERATOR;
        }
        else
        {
            throw new CalculatorException("expected: /s, /d");
        }        
    }
            
    private void evalPlusState(char c) throws CalculatorException
    {
        if (c == 0)
        {
            m_token = new Token(Token.Type.Add);
        }
        else if (Character.isDigit(c))
        {
            m_integerOrNumerator = c - '0';
            m_sign = 1;
            m_state = State.INTEGER_OR_NUMERATOR;
        }
        else
        {
            throw new CalculatorException("expected: /s, /d");
        }        
    }

    private void evalMultiplyState(char c) throws CalculatorException
    {
        if (c == 0)
        {
            m_token = new Token(Token.Type.Multiply);
        }
        else
        {
            throw new CalculatorException("expected: /s");
        }
    }

    private void evalDivideState(char c) throws CalculatorException
    {
        if (c == 0)
        {
            m_token = new Token(Token.Type.Divide);
        }
        else
        {
            throw new CalculatorException("expected: /s");
        }
    }

    private void evalState(char c) throws CalculatorException
    {
        switch(m_state)
        {
            case START: evalStartState(c); break;
            case INTEGER_OR_NUMERATOR: evalIntegerOrNumeratorState(c); break;
            case UNDERSCORE: evalUnderscoreState(c); break;
            case NUMERERATOR: evalNumeratorState(c); break;
            case FRACTION: evalFractionState(c); break;
            case DENOMINATOR: evalDenominatorState(c); break;
            case MINUS: evalMinusState(c); break;
            case PLUS: evalPlusState(c); break;
            case MULTIPLY: evalMultiplyState(c); break;
            case DIVIDE: evalDivideState(c); break;
            default: assert(false);
        }
    }
        
    public Token stringToToken(String string) throws CalculatorException
    {
        m_string = string;
        m_position = 0;
        m_state = State.START;
        m_token = null;

        while(m_token == null)
        {
           char c = 0;

           if (m_position < m_string.length()) 
               c = string.charAt(m_position++);
           
           evalState(c);
        }       
        
        return m_token;
    }
    
    public Token stringToToken2(String string) throws CalculatorException
    {
        Scanner in = new Scanner(string);

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

            return new Token(new Rational(numerator, denominator));

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

            return new Token(new Rational(numerator, denominator));

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

            return new Token(new Rational(numerator, denominator));

        }
        else if( in.hasNext(m_operatorPattern) )
        {
            String matchString = in.next(m_operatorPattern);
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

            return new Token(type);

        }
        else if( in.hasNext(m_quitPattern) )
        {
            String matchString = in.next(m_quitPattern);
            Matcher m = m_quitPattern.matcher(matchString);
            boolean found = m.find();
            assert(found);

            return new Token(Token.Type.Quit);
        }
        else
        {
            throw new CalculatorException("Unrecognized token");
        }
    }
    
    private static final Pattern m_wholeAndFractionPattern = Pattern.compile("(-?+)(\\d+)_(\\d+)/(\\d+)");
    private static final Pattern m_FractionPattern = Pattern.compile("(-?+)(\\d+)/(\\d+)");
    private static final Pattern m_wholePattern = Pattern.compile("(-?+)(\\d+)");
    private static final Pattern m_operatorPattern = Pattern.compile("\\s*([\\+\\-\\*\\/])\\s*");    
    private static final Pattern m_quitPattern = Pattern.compile("quit");
        
}
