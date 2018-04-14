/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DES_Encryption;

import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Suraj Singh
 */
class BasicOperations {
    static int keySize;
    // Removes the redundant 0's from the output.
    public static StringBuffer ConvertToHexaDecimal(StringBuffer input)
    {
        StringBuffer result = new StringBuffer();
        int len = input.length();
        String zero = "000";
        int r = len%4;
        if(r!=0)
        {
            input.insert(0,zero.substring(0, 4-r));
        }
        for(int i=0;i<input.length();i = i+4)
        {
            result.append(Integer.toHexString(  Integer.parseInt(input.substring(i, i+4), 2) ));
        }
        return result;
    }
    public static StringBuffer Trim(StringBuffer input)
    {
        for(int i=0;i<input.length();)
        {
            if(input.charAt(i)!='0')
                break;
            input.replace(i, i+1,"");
        }
        return input;
    }
    // Determine the two's complement of smaller number
    // minuendLength is required to append 1 in the beginning to obtain the complement as large as the minuend.
    public static StringBuffer TwosComplement(StringBuffer input,int minuendLength,boolean binaryInput)
    {
        int i;
        StringBuffer str= new StringBuffer();
        StringBuffer binary = !binaryInput?ConvertToBinary(input):input;
        StringBuffer sb = new StringBuffer();
        for(i=0;i<minuendLength-binary.length();i++)
            sb.append("1");
        for(i=0;i<binary.length();i++)
        {
            sb.append(binary.charAt(i)=='0'?"1":"0");
        }     
        i=0;
        // Bug: need to cound no of 0s
        while(i<sb.length())
        {
            if(sb.charAt(i++)=='0')str.append("0");
            else break;
        }
        // Bug : Need to cound the number of 0s before sb, since addition is returning trimmed result;
       sb = new StringBuffer(Addition(sb,new StringBuffer("1"),true));
       sb.insert(0,str.toString());
       return sb;
    }
    // Will return the binary form of given input number in reverse direction to help in arithmetic operations
    public static StringBuffer ConvertToBinary(StringBuffer input)
    {   
        StringBuffer sb = new StringBuffer();
        int number;
        for(int i=0;i<input.length();i++)
        {
            number = input.charAt(i);
            sb.append("0"+Integer.toBinaryString(number));
        }
        return sb.reverse();
    }
    public static StringBuffer Multiply(StringBuffer a, StringBuffer b, boolean binaryInput)
    {
        StringBuffer sb = new StringBuffer();
        boolean set,res;
        StringBuffer multiplier = new StringBuffer(a.toString());
        multiplier = !binaryInput?ConvertToBinary(multiplier):multiplier.reverse();// return binary form in reverse.
        StringBuffer multiplicand = new StringBuffer(b.toString());
        multiplicand = !binaryInput?ConvertToBinary(multiplicand):multiplicand.reverse();// return binary form in reverse.
        boolean carry;
        int result[] = new int[2*Math.max(multiplier.length(), multiplicand.length())];
        int i,j,count;
        for(i=0;i<multiplier.length();i++)
        {
            if(multiplier.charAt(i)=='1')
            {
                carry = false;
                count=0;
                for(j=i;j<i+multiplicand.length();j++)
                {
                    set = multiplicand.charAt(count++)=='1';
                    res = result[j]==1;                            
                    result[j] = res^set^carry?1:0; 
                    carry = ((carry&&res)||(carry&&set)||(set&&res))?true:false;                   
                }
                if(carry)
                    result[j]=1;
            }
        }
        for(i=0;i<result.length;i++)
            sb.append(result[i]==0?"0":"1");
        sb = Trim(sb.reverse());
        if(sb.length()!=0)
            return sb;
        return new StringBuffer("0");
    }
    public static StringBuffer Addition(StringBuffer a, StringBuffer b,boolean binaryInput)
    {
        StringBuffer add1 = new StringBuffer(a.toString()),add2= new StringBuffer(b.toString());
        if(!binaryInput)
        {
            add1 = ConvertToBinary(add1); // will get the binary form in reverse
            add2 = ConvertToBinary(add2); // will get the binary form in reverse
        }
        else
        {
            add1 = add1.reverse(); // should be assigned reverse of original a
            add2 = add2.reverse(); // should be assigned reverse of original b
        }
        int result[] = new int[Math.max(add1.length(), add2.length())+1];
        StringBuffer sb = new StringBuffer();
        boolean carry = false,addend,augend;
        int i;
        for(i=0;i<Math.min(add1.length(),add2.length());i++)
        {
            addend = add1.charAt(i)=='1'?true:false;
            augend = add2.charAt(i)=='1'?true:false;
            result[i] = addend^augend^carry?1:0;
            carry = ((carry&&addend)||(carry&&augend)||(addend&&augend))?true:false;
        }
        for(;i<Math.max(add1.length(),add2.length());i++)
            if(add1.length() > add2.length())
            {
                result[i] = add1.charAt(i)=='1' ^ carry?1:0;
                carry = carry && add1.charAt(i)=='1';
            }
        else
            {
                result[i] = add2.charAt(i)=='1' ^ carry?1:0;
                carry = carry && add2.charAt(i)=='1';
            }
        if(carry)
            result[i]=1;
        // if both are of same length, then need to add below check.
        for(i=0;i<result.length;i++)
            sb.append(result[i]==0?"0":"1");
        return Trim(sb.reverse());        
    }
    // Used in Subtraction
    public static StringBuffer TrimBuffer(StringBuffer input)
    {
        while(input.charAt(0)=='0')
            input.delete(0, 1);
        return input;
    }
    // if inputBinary is true, then a is the first parameter, and b is the two's complement of second parameter
    public static StringBuffer Subtract(StringBuffer a, StringBuffer b,boolean inputBinary)
    {
        int i,maxLength;
        StringBuffer result;
        StringBuffer minuend = !inputBinary?ConvertToBinary(a):a;
        StringBuffer subtrahend = !inputBinary?ConvertToBinary(b):b;
        subtrahend = TwosComplement(subtrahend,minuend.length(),true);
        result = Addition(minuend,subtrahend,true);
        maxLength = Math.max(a.length(), b.length());
        i = result.length();
        // Bug fix for negative numbers
        if(i>maxLength)
        { 
            result.deleteCharAt(0);
            return result;
        }
        else 
        {
            return TwosComplement(result,maxLength,true);
        }
    }
    // Modulous = true, will return the mod, Modulous = false will return the quotient
    public static StringBuffer Divide(StringBuffer a, StringBuffer b,boolean modulous,boolean binaryInput)
    {
        StringBuffer divisor = new StringBuffer(b.toString());
        divisor = Trim(!binaryInput?ConvertToBinary(divisor).reverse():divisor);
        StringBuffer dividend = new StringBuffer(a.toString());
        dividend = Trim(!binaryInput?ConvertToBinary(dividend).reverse():dividend);
        boolean carried = false;
        int rem=0;
        StringBuffer quotient = new StringBuffer();
        // Set remainder as first divisor.length bits from the dividend
        StringBuffer remainder = new StringBuffer();
        if(dividend.length() < divisor.length())
        {
            return modulous?(dividend.length()!=0?dividend:new StringBuffer("0")):new StringBuffer("0");
        }
        int i=0;
        // It is used in division subtractions
        StringBuffer divisorComplement = TwosComplement(divisor,divisor.length(),true);
        StringBuffer divisorAugmentedComplement = new StringBuffer(divisorComplement);
        divisorAugmentedComplement.insert(0, "1");
        divisorComplement = Trim(divisorComplement);
        for(;i< dividend.length();)
        {
            // append next bit in the remainder from divident to make it larger, equal to divisor
            if(remainder.length()<divisor.length() || 
                    (remainder.length()==divisor.length() && remainder.toString().compareTo(divisor.toString())<0))
            {  
                remainder.append(dividend.substring(i, i+1));
                i++;
                if(carried)
                    quotient.append("0");
                carried = true;
//                continue;
            }
            remainder = Trim(remainder);
            // This is to add 1 in the beginning of the divisor's complement to do the subtraction in the 2's complement
            if(remainder.length()>divisor.length() ||
                    (remainder.length()==divisor.length() && remainder.toString().compareTo(divisor.toString())>=0))
            {
                if(remainder.length()> divisorComplement.length()) 
                    remainder = new StringBuffer(Addition(remainder,divisorAugmentedComplement,true).reverse().substring(0, Math.max(remainder.length(),divisorAugmentedComplement.length()))).reverse();
                else
                    remainder = new StringBuffer(Addition(remainder,divisorComplement,true).reverse().substring(0, Math.max(remainder.length(),divisorComplement.length()))).reverse();
                if(remainder.length()>divisor.length())
                    remainder = new StringBuffer(remainder.substring(remainder.length()-divisor.length()));
                remainder = Trim(remainder);
                quotient.append("1");
                carried = false;
            }
        }// End of for loop
        if(carried)
            quotient.append("0");
        if(!modulous)
            return Trim(quotient).length()==0?new StringBuffer("0"):Trim(quotient);
        return Trim(remainder).length()==0?new StringBuffer("0"):Trim(remainder);
    }
    
    public static void main(String arfs[])
    {
        Scanner s = new Scanner(System.in);
        String input1,input2,binaryInput1,binaryInput2;
        StringBuffer addResult = new StringBuffer();
//        System.out.print("Enter the 8 character string1 :  ");
//        input1 = s.next();
//        System.out.println();
//        System.out.println("Hexadecimal Form :");
//        binaryInput1 = ConvertToBinary(input1).toString();
//        for(int i=binaryInput1.length()/4;i>0;i--)
//            System.out.print(Integer.toHexString(Integer.parseInt(binaryInput1.substring(binaryInput1.length()-4*i, binaryInput1.length()-4*i+4),2)));
//        System.out.println();
//        System.out.print("Enter the 8 character string2 :  ");
//        input2 = s.next();
//        System.out.println();
//        System.out.println("Hexadecimal Form : ");
//        binaryInput2 = ConvertToBinary(input2).toString();
//        System.out.println();
        
        
        //ADDITION OPERATION
//        addResult = Addition(input1,input2,false); 
//        for(int i=addResult.length()/4;i>0;i--)
//            System.out.print(Integer.toHexString(Integer.parseInt(addResult.substring(addResult.length()-4*i, addResult.length()-4*i+4),2)));       
//        System.out.println();
//        
//        //SUBTRACTION OPERATION
//        addResult = Subtract(input1,input2,false); 
//        for(int i=addResult.length()/4;i>0;i--)
//            System.out.print(Integer.toHexString(Integer.parseInt(addResult.substring(addResult.length()-4*i, addResult.length()-4*i+4),2)));       
//        System.out.println();
//        
//        //MULTIPLY OPERATION
////        addResult = Multiply(input1,input2);
////        for(int i=addResult.length()/4;i>0;i--)
////            System.out.print(Integer.toHexString(Integer.parseInt(addResult.substring(addResult.length()-4*i, addResult.length()-4*i+4),2)));       
////        System.out.println();
////        
////        // Checking if multiplication is working properly or not.
////        addResult = Divide(addResult.toString(),ConvertToBinary(input2).toString(),true,true);
////        while(addResult.length()%4!=0)
////            addResult = new StringBuffer("0").append(addResult);
////        for(int i=addResult.length()/4;i>0;i--)
////            System.out.print(Integer.toHexString(Integer.parseInt(addResult.substring(addResult.length()-4*i, addResult.length()-4*i+4),2)));       
////        System.out.println();
//        
//        //DIVIDE OPERATION
//        addResult = Divide(input1,input2,false,false); 
//        while(addResult.length()%4!=0)
//            addResult = new StringBuffer("0").append(addResult);
//        for(int i=addResult.length()/4;i>0;i--)
//            System.out.print(Integer.toHexString(Integer.parseInt(addResult.substring(addResult.length()-4*i, addResult.length()-4*i+4),2)));       
//        System.out.println();
//        
//        //MODULOUS OPERATION
//        addResult = Divide(input1,input2,true,false);
//        while(addResult.length()%4!=0)
//            addResult = new StringBuffer("0").append(addResult);
//        for(int i=addResult.length()/4;i>0;i--)
//            System.out.print(Integer.toHexString(Integer.parseInt(addResult.substring(addResult.length()-4*i, addResult.length()-4*i+4),2)));       
//        System.out.println();
    }
}
