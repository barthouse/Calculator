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
public class CalculatorException extends Exception {
    
    public String m_error;
    
    CalculatorException() {}
    CalculatorException(String error) { m_error = error; }
    
    @Override
    public String toString()
    {
        return m_error;
    }
    
}
