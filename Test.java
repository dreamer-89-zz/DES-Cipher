/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DES_Encryption;

import java.util.Scanner;
import DES_Encryption.BasicOperations;
/**
 *
 * @author Suraj Singh
 */
class Test {
    public static void main(String arfs[])
    {
        Scanner s = new Scanner(System.in);
        String in1,in2;
        in1 = s.next();
        in2 = s.next();
        StringBuffer one = new StringBuffer();
        StringBuffer two = new StringBuffer();
        for(int i=0;i<in1.length();i++)
        {
            one.append("0"+Integer.toBinaryString(in1.charAt(i)));
        }
        for(int i=0;i<in2.length();i++)
        {
            two.append("0"+Integer.toBinaryString(in2.charAt(i)));
        }
        System.out.println(one.toString());
        System.out.println(two.toString());
        System.out.println();
        System.out.println(Integer.toBinaryString(Integer.parseInt(one.toString(), 2)+Integer.parseInt(two.toString(),2)));
        System.out.println(Integer.toBinaryString(Integer.parseInt(one.toString(), 2)-Integer.parseInt(two.toString(),2)));
        System.out.println(Integer.toBinaryString(Integer.parseInt(one.toString(), 2)*Integer.parseInt(two.toString(),2)));
        System.out.println(Integer.toBinaryString(Integer.parseInt(one.toString(), 2)/Integer.parseInt(two.toString(),2)));
        System.out.println();
        System.out.println(BasicOperations.Addition(one, two, true));
        System.out.println(BasicOperations.Subtract(one, two, true));
        System.out.println(BasicOperations.Multiply(one, two, true));
        System.out.println(BasicOperations.Divide(one, two, false, true));
        System.out.println(BasicOperations.Divide(one, two, true, true));
    }
}
