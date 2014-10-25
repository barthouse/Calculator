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
public class Calculator {
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
//        String input = "-12_2/3 + 5_3/4 * 4 - 5/4 * 9/4 / 1/2 quit";
//        Scanner scanner = new Scanner(input);
        
        try
        {
//            Rational a = CalcParser.ParseInput(scanner);
            Rational a = CalcParser.ParseInput(new Scanner(System.in));

            System.out.print("Answer = ");
            System.out.printf(a.toString());
            System.out.print("\n");
        }
        catch(CalculatorException e)
        {
            System.out.print("Error:" + e + "\n");
        }
    }
    
}
