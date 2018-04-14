/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DES_Encryption;

import java.util.Scanner;

/**
 *
 * @author Suraj Singh
 */
class External_Exam {
    // Miller Rabin primality Testing
    public static boolean Is_Prime(StringBuffer number)
    {
        int a[] = new int[]{2,3,5};
        StringBuffer bases[] = new StringBuffer[a.length];
        for(int i=0;i<a.length;i++)
            bases[i] = new StringBuffer(Long.toBinaryString(a[i]));
        int index=0;
        StringBuffer intermediate,m,q,T,power2,Tadd1;
        int k=0,i;
        boolean flag = true;
        if(number.toString().matches("0*")|| number.toString().matches("0*1"))
            return false;
        if(number.toString().matches("0*10")||number.toString().matches("0*11")||number.toString().matches("0*101"))
            return true;
        // Step 1. find m*2^k = n-1 = subtract_1
        StringBuffer subtract_1 = BasicOperations.Subtract(number, new StringBuffer("1"), true);
        
        //Testing
//        subtract_1 = new StringBuffer("111000");
//        number = new StringBuffer("111001");
        q = new StringBuffer(subtract_1);
        // Step 2. find k
        while(true)
        {
            // Call modulous operation.
            intermediate = BasicOperations.Divide(q,new StringBuffer("10"),true,true);            
            q = BasicOperations.Divide(q, new StringBuffer("10"), false, true);
            if(intermediate.toString().matches("0*"))k++;// = BasicOperations.Addition(k.toString(), "1", true);
            else
                break;
        }
        // Find m
        power2 = Power(new StringBuffer("10"),new StringBuffer(Long.toBinaryString(k)));
        m = BasicOperations.Divide(subtract_1,power2,false, true);
        while(index<bases.length)
        {
            // T =  a^m mod n
            T = ModPow(bases[index],m,number);
            index++;
            // T = + 1 or -1 return prime;
            if(T.toString().matches("0*1")|| 
                    BasicOperations.Divide(BasicOperations.Addition(T, new StringBuffer("1"),true),number,true,true).toString().matches("0*"))
            {
                continue;
            }
            for(i=0;i<k-1;i++)//(StringBuffer i = new StringBuffer("1"); i.toString().compareTo(k.toString())<1; i = BasicOperations.Addition(i.toString(), "1", true))
            {
                // T = T*T mod n
                T = BasicOperations.Multiply(T, T,true);
                T = BasicOperations.Divide(T, number, true, true);
                Tadd1 = BasicOperations.Addition(T, new StringBuffer("1"),true);
                // if T = -1, then return "a prime"
                if(BasicOperations.Divide(Tadd1,number,true,true).toString().matches("0*"))
                    break;
                // if T = +1, then return " a composite"
                if(T.toString().matches("0*1"))
                {
                    flag = false;
                    break;
                }
            }
            if(i==k-1)flag = false;
            if(!flag)
                break;
        }
        return flag;
    }   
    // Calculate power function
    public static StringBuffer Power(StringBuffer number, StringBuffer p)
    {
        StringBuffer result = new StringBuffer("1");
        StringBuffer x = new StringBuffer(number);
        boolean mod2;
        while(!p.toString().matches("0*"))
        {
            mod2 = BasicOperations.Divide(p, new StringBuffer("10"), true, true).toString().matches("0*");
            // number %2 != 0
            if(!mod2)
                result = BasicOperations.Multiply(x,result,true);     
            x = BasicOperations.Multiply(x, x,true);
            p = BasicOperations.Divide(p, new StringBuffer("10"), false, true);
        }
        return result;
    }    
    // Calculate number^power % modulous
    public static StringBuffer ModPow(StringBuffer number, StringBuffer power, StringBuffer modulous)
    {
        StringBuffer x= new StringBuffer(number),answer=new StringBuffer("1"), p = new StringBuffer(power);
        // run till p > 0
        while(!p.toString().matches("0+"))
        {
            // if power%2!=0 then answer = answer*y;
            if(!BasicOperations.Divide(p, new StringBuffer("10"), true, true).toString().matches("0*"))
            {
                // ans = ans*x;
                answer = BasicOperations.Multiply(answer, x,true);
                // ans = ans % modulous
                answer = BasicOperations.Divide(answer, modulous, true, true);
            }            
            // x = x*x % mod
            x = BasicOperations.Multiply(x, x,true);
            x = BasicOperations.Divide(x,modulous,true,true);
            // p = p/2;
            p = BasicOperations.Divide(p, new StringBuffer("10"), false, true);
        }
        return answer;
    }
    // Calculate number^-1 % modulous => d
    // We use Extended Euclidean Algorithm to find the inverse since e and phi(n) are always coprime with each other
    public static StringBuffer Multiplicative_Inverse(StringBuffer number, StringBuffer modulous)
    {
        StringBuffer r1,r2,t1,t2,q,r,t,x,y;
        boolean tsign,t1sign,t2sign; // positive
        tsign = t1sign = t2sign = true;
        r1 = modulous;
        r2 = number;
        t1 = new StringBuffer("0");
        t2 = new StringBuffer("1");
        while(!r2.toString().matches("0+"))
        {
            // q = r1/r2;
            q = BasicOperations.Divide(r1, r2, false,true);
            // r = r1 - r2* q;
            r = BasicOperations.Subtract(r1, BasicOperations.Multiply(q, r2,true), true);
            r1 = r2; r2 = r;
            
            
            // t = t1 - t2*q;
            x = BasicOperations.Multiply(q, t2,true);
            if(t1sign && t2sign)
            {
                tsign = compareTwoNumbers(t1,x);
                t = BasicOperations.Subtract(t1, x, true);
            }
            else if(t1sign && !t2sign)
            {
                tsign = true;
                 t = BasicOperations.Addition(t1, x, true);
            }
            else if(!t1sign && t2sign)
            {
                tsign = false;
                t = BasicOperations.Addition(t1, x, true);
            }
            else
            {
                tsign = compareTwoNumbers(x,t1);
                t = BasicOperations.Subtract(t1, x, true);
            }
            // t1 <- t2, t2<-t
            t1 = t2; t2 = t;
            

            // Sign changes
            t1sign = t2sign; t2sign = tsign;
        }
        if(r1.toString().matches("0*1")) 
        {
            if(t1sign)
                return t1;
            else
                return BasicOperations.Subtract(modulous, t1, true);
        }
        return null;
    }
    // This method is used to whether the result of subtraction will be negative or positive.
    public static boolean compareTwoNumbers(StringBuffer input1, StringBuffer input2)
    {
        int i;
        if(input1.length() > input2.length())
            return true;
        else if (input1.length()==input2.length())
        {
            for(i=0;i<input1.length();i++)
            {
                if(input1.charAt(i)<input2.charAt(i))
                    return false;
            }
            return true;
        }   
        return false;
    }
    public static void main(String ... orange)
    {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter n => ");
        int n = s.nextInt();
        System.out.println();
        int k,count;
        StringBuffer sb,number,value_k;
        sb = new StringBuffer();
        sb.append(1);
        for(int i=0;i<n;i++)
                sb.append(0); 
        k=1;
        count=-1;
        for(int i=0;i<10;i++)
        {    
            for(k=count+2;true;k+=2)
            {
                value_k = new StringBuffer(Integer.toBinaryString(k));
                number = BasicOperations.Subtract(sb, value_k, true);
                if(Is_Prime(number))
                    break;
            }
            count = k;
            System.out.println("k=>"+k);
        }
    }
}
