/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DES_Encryption;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
/**
 *
 * @author Suraj Singh
 */
class RSA {
    // Apply the miller rabin primality testing
    public static boolean Is_Prime(StringBuffer number)
    {
        int a[] = new int[]{2,3,5};
        StringBuffer bases[] = new StringBuffer[a.length];
        for(int i=0;i<a.length;i++)
        {
            bases[i] = new StringBuffer(Long.toBinaryString(a[i]));
        }
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
    // size will give the number of bits require in the generated prime.
    // if size 512 then, we need to generate 512 bit large prime number.
    public static StringBuffer Generate_Large_Primes(int size)
    {
        //int prime[] = new int[size];
        StringBuffer sb = new StringBuffer();
        Random m = new Random();
        // Continuously generate the number until it is found to be random.
        while(true)
        {
            for(int i=0;i<size-1;i++)
                sb.append(m.nextInt(2));
            sb.append(1);// last bit made 1 to make it odd.
            //System.out.println(Long.parseLong(sb.toString(),2));
            //checkPrime(sb,"e");
            if(Is_Prime(sb))
                
            {
                System.out.println(BasicOperations.ConvertToHexaDecimal(sb));
                System.out.println("SUCCESSFUL !!!");
                break;
            }
            else
            {
                System.out.println("UNSUCCESSFUL !!!");
                System.out.println(BasicOperations.ConvertToHexaDecimal(sb));
                sb.delete(0, sb.length());
            }
        }
        return sb;
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
    
    // Part of testing process
    public static boolean isprime(BigInteger n)
        {
            BigInteger two = new BigInteger("2");
            BigInteger three = new BigInteger("3");
             if(n.compareTo(BigInteger.ONE)==0 || n.compareTo(two)==0)
            {
                return true;
            }
          BigInteger half=n.divide(two);

           for(BigInteger i=three; i.compareTo(half)<=0;i=i.add(two))
            {

                if(n.mod(i).equals(BigInteger.ZERO))
                {
                  return false; 
                }

            }
             return true;

        }
    public static void checkPrime(StringBuffer input, String var)
    {        
        
        //long i,j;
        //num = Long.parseLong(input.toString(), 2);
//         BigInteger num = new BigInteger(input.toString(),2);
//         boolean check = isprime(num);
//         if(check)
//             System.out.println("  PRIME !!!");
//         else 
//             System.out.println("  NOT PRIME !!!");
        long i,j,num;
        //BigInteger sqrt = bigIntSqRootCeil(num);
        num = Long.parseLong(input.toString(),2);
        //System.out.println(" sqrt "+sqrt.toString());
        System.out.println(var+" "+num);
        if(input.toString().matches("0*1"))
        {
            System.out.println("Not prime");
            return;
        }
//        if(var.equals("p")||var.equals("q")||var.equals("n")||var.equals("phi"))
//            return;
        for(i=2;i <=Math.sqrt(num);i++)
            if(num%i==0)break;
        if(i>Math.sqrt(num))System.out.println("It is prime");
        else System.out.println("it is not prime and is divisible by "+i+" "+(num/(1l*i)));
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
    public static StringBuffer TrimBuffer(StringBuffer input)
    {
        while(input.charAt(0)=='0')
            input.delete(0, 1);
        return input;
    }
    public static void main(String args[])
    {
        StringBuffer p,q,n,phi,e,d;
        int i,j,size,block;
        long pp;
        boolean sign;
        String value,line;
        StringBuffer inputText = new StringBuffer();
        StringBuffer cipherText,plainText,ee,dd,nn;
        cipherText = new StringBuffer();
        plainText = new StringBuffer();
        Scanner s = new Scanner(System.in);
        System.out.print("Enter the key size ");
        size = s.nextInt();
        if(size%8!=0)
        {
            System.out.println("Enter the key in byte size ");
            return;
        }        
        block = size/16; // block denotes the number of bits to be taken from plaintext
//        System.out.println(Long.MAX_VALUE);
//        BigInteger big = new BigInteger("1011110110111100011101000111000000101010111110110001001101000001", 2);
//        System.out.println(big.toString(10));
//        System.out.println(Long.parseLong("1011110110111100011101000111000000101010111110110001001101000001", 2));
        //System.out.println(Integer.parseInt("1011110110111100011101000111000000101010111110110001001101000001", 2));
//        String ch,ee,ph;
//        ee = Long.toBinaryString(12);
//        ph = Long.toBinaryString(26);
//        pp = Long.parseLong(Multiplicative_Inverse(new StringBuffer(ee),new StringBuffer(ph)).toString(),2);
//        System.out.println(pp);
//        for(i=2;i<50;i++)
//        {
//        System.out.print(i+" "+Is_Prime(new StringBuffer(Long.toBinaryString(i))));
//        checkPrime(new StringBuffer(Long.toBinaryString(i)),"p");
//        }
       // System.out.println(Is_Prime(new StringBuffer(Long.toBinaryString(3))));
//        System.out.println(five.matches("0*101"));
//        System.out.println(five.matches("101"));
//        System.out.println(Is_Prime(new StringBuffer(Long.toBinaryString(5))));
//        for(i=0;i<56;i++)
//        {
//            System.out.println(i+" "+Is_Prime(new StringBuffer(Long.toBinaryString(i))));
//        }
        
//        do
//        {
//         i = s.nextInt();
//         j = s.nextInt();
//        StringBuffer input1 = new StringBuffer(Long.toBinaryString(i));
//        StringBuffer input2 = new StringBuffer(Long.toBinaryString(j));
//        sign = compareTwoNumbers(input1,input2);
//        System.out.println("i+j "+Long.parseLong(BasicOperations.Addition(input1, input2, true).toString(),2) +" , "+(i+j));
//        // Bug fix for negative result during subtraction 
//        if(sign)
//            System.out.println("i-j "+Long.parseLong(BasicOperations.Subtract(input1, input2, true).toString(),2)+ " , "+(i-j));
//        else 
//            System.out.println("i-j  -"+Long.parseLong(BasicOperations.Subtract(TrimBuffer(input1), TrimBuffer(input2), true).toString(),2)+ " , "+(i-j));
//        System.out.println("i*j "+Long.parseLong(BasicOperations.Multiply(input1, input2, true).toString(),2)+" , "+(i*j));
//        System.out.println("i/j "+Long.parseLong(BasicOperations.Divide(input1, input2, false, true).toString(),2)+" , "+(i/j));
//        System.out.println("i%j "+Long.parseLong(BasicOperations.Divide(input1, input2, true, true).toString(),2)+" , "+(i%j));
//        System.out.println("Do you want to continue ");
//        ch = s.next();
////        if(ch!="Y"&&ch!="y")
////            break;
//        System.out.println("\n");
//        }while(ch.equals("y")||ch.equals("Y"));
        //BasicOperations.Addition(d, d, true);
        p = Generate_Large_Primes(size);
        System.out.println("P found!!!");
//        System.out.println(Long.parseLong(p.toString(),2));
        q = Generate_Large_Primes(size);
       // System.out.println("Q found!!!");
        //System.out.println(Long.parseLong(q.toString(),2));
        n = BasicOperations.Multiply(p, q,true);
        //System.out.println(Long.parseLong(n.toString(),2));
        phi = BasicOperations.Multiply(BasicOperations.Subtract(p, new StringBuffer("1"), true), BasicOperations.Subtract(q, new StringBuffer("1"), true),true);
       // System.out.println("phi "+Long.parseLong(phi.toString(),2));
        while(true)
        {
        e = Generate_Large_Primes(size/2);
        //System.out.println(Long.parseLong(e.toString(),2));
        if(!BasicOperations.Divide(phi, e, true, true).toString().matches("0*"))
            break;
        }
       // System.out.println(Long.parseLong(e.toString(),2));
        d = Multiplicative_Inverse(e,phi);
       // System.out.println(Long.parseLong(d.toString(),2));
//      
        ee = new StringBuffer(e.toString());
        dd = new StringBuffer(d.toString());
        nn = new StringBuffer(n.toString());
//        BigInteger plain = new BigInteger("97");
//        BigInteger ee = new BigInteger(e.toString(),2);
//        BigInteger nn = new BigInteger(n.toString(),2);
//        BigInteger cip = plain.modPow(ee, nn);
//        BigInteger dd = new BigInteger(d.toString(),2);
//        System.out.println(cip.toString(16));
//        plain = cip.modPow(dd, nn);
//        System.out.println(plain.toString());
        
        //MOd pow testing
        //new StringBuffer(plain.toString(2))
//        StringBuffer result =ModPow(new StringBuffer(Long.toBinaryString(97)),new StringBuffer(ee.toString(2)),
//                new StringBuffer(nn.toString(2)));
//        System.out.println(BasicOperations.ConvertToHexaDecimal(result));  
        
        
//        if(size<=32)
//        {
////        checkPrime(p,"p");
////        checkPrime(q,"q");
////        checkPrime(n,"n");
////        checkPrime(phi,"phi");
////        checkPrime(e,"e");
////        checkPrime(d,"d");
//        System.out.println("Public key pair ("+Long.parseLong(e.toString(),2)+", "+Long.parseLong(n.toString(),2)+")");
//        System.out.println("Public key pair ("+Long.parseLong(d.toString(),2)+", "+Long.parseLong(n.toString(),2)+")");
//        }else
//        {            
//            checkPrime(p,"p");
//            checkPrime(q,"q");
            p = BasicOperations.ConvertToHexaDecimal(p);
            q = BasicOperations.ConvertToHexaDecimal(q);
            n = BasicOperations.ConvertToHexaDecimal(n);
//            phi = BasicOperations.ConvertToHexaDecimal(phi);
            e = BasicOperations.ConvertToHexaDecimal(e);
            d = BasicOperations.ConvertToHexaDecimal(d);
            
            System.out.println("P => "+p);
            System.out.println("Q => "+q);
            System.out.println("N => "+n);
            System.out.println("E => "+e);
            System.out.println("D => "+d);
            
           // System.out.println("p "+p+"\nq "+q+"\nn "+n+"\nphi "+phi+"\ne "+e);
            System.out.println("Public Key Pair "+e+","+n);
            System.out.println("Private Key Pair "+d+", "+n);
//        }
        
        System.out.println("\nEnter the plaintext ");
        ArrayList<String> list = new ArrayList<String>();
        s.nextLine();
        while( (line=s.nextLine())!=null && line.length()!=0)
            inputText.append(line+"\n");
        while(inputText.length()%block!=0)
            inputText.append(" ");
        System.out.println("*****************************CIPHERTEXT*****************************");
        for(i=0;i<inputText.length()/block;i=block+i)
        {              
//            for(j=i;j<block+i;j++) 
//            {   
//                pp = inputText.charAt(j);
//                System.out.print(inputText.charAt(j));
//                cipherText.append("0"+Long.toBinaryString(pp));
//            }
//            System.out.println();
            for(j=i;j<i+block;j++)
            {
                pp = inputText.charAt(j);
                cipherText.append("0"+Long.toBinaryString(pp));
            }
////            System.out.print(inputText.charAt(i));
            cipherText = ModPow(cipherText,ee,nn);
            System.out.print(BasicOperations.ConvertToHexaDecimal(cipherText).toString()+"\n");
            list.add(cipherText.toString());
            cipherText.delete(0, cipherText.length());
            //list.add(cip.toString(2));
            // ORIGINAL :  01110011 01110101
            // cipher : 111101101001011001001010000111   00111101 10100101 10010010 10000111
            //  01000010111000100100011101001000
            //  01000010111000100100011101001000
            // p  1110011 01110101
        }
//        for(i=0;i<list.size();i++)
//        {
//            System.out.println(inputText.charAt(i)+"=>"+BasicOperations.ConvertToHexaDecimal(new StringBuffer(list.get(i)))+" ");
//        }
        System.out.println();
        System.out.println("\n*****************************PLAINTEXT*****************************");
        for(i=0;i<list.size();i++)
        {
            cipherText = new StringBuffer(list.get(i));
            //cipherText = new StringBuffer(new BigInteger("2aca13df6525a034930220fbd3ed185d",16).toString());
            plainText = ModPow(cipherText,dd,nn);   
            for(j=0;j<block;j++)
            {                
                pp = Long.parseLong(plainText.substring(j*8, j*8+8).toString(),2);
//                pp = Integer.parseInt(value, 2);                
                System.out.print((char)pp);
            }
            // 01110011 01110101
//            while(plainText.length()%8!=0)
//                plainText.insert(0,"0");
//            for(j=0;j<plainText.length();j=j+8)
//            {
//                value = plainText.substring(j, j+8);
//                pp = Integer.parseInt(value, 2);                
//                System.out.print((char)pp);
//            }
           // plainText.append((char)pp);
        }
       // System.out.print(plainText);
    }
}
