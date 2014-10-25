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
public class Token {
    
    public enum Type { Rational, Add, Subtract, Divide, Multiply, Quit }
    
    public Type m_type;
    public Rational m_rational;
    
    public Token(Type type)
    {
        m_type = type;
    }
    
    public Token(Rational rational)
    {
        m_type = Type.Rational;
        m_rational = rational;
    }
    
    boolean isAdd() { return m_type == Type.Add; }
    boolean isSubtract() { return m_type == Type.Subtract; }
    boolean isDivide() { return m_type == Type.Divide; }
    boolean isMultiply() { return m_type == Type.Multiply; }
    boolean isRational() { return m_type == Type.Rational; }
    boolean isQuit() { return m_type == Type.Quit; }
    
}
