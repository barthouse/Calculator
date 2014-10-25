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
public class Math {
    
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
    
}
