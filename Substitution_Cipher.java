/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DES_Encryption;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Suraj Singh
 */
class Substitution_Cipher {
    static int key[];
    static int inv_key[];
    public static void Key_Generator(int size) throws IOException
    {
        BufferedWriter bf = new BufferedWriter(new FileWriter("C:\\Users\\Suraj Singh\\Desktop\\Class Experiments\\INS\\S_Box.txt"));
        Set<Integer> set = new HashSet<Integer>();
        Random generator = new Random();
        key = new int[size];
        inv_key = new int[size];
        int value,count=0;
        while(set.size()!=size)
        {
            value = generator.nextInt(size);// to include n in the random output.
            if(!set.contains(value+1))
            {
                bf.write(value+" ");
                set.add(value+1);
                key[count++]=value;
                inv_key[value]=count-1;
            }
        }
        bf.flush();
        bf.close();
    }
    public static String substitute(String input)
    {
        StringBuffer output= new StringBuffer();
        int index = Integer.parseInt(input, 2);
        output.append(Integer.toBinaryString(key[index]));
        return output.toString();
    }
    public static String inverse_substitute(String input)
    {
        StringBuffer output= new StringBuffer();
        int index = Integer.parseInt(input, 2);
        output.append(Integer.toBinaryString(inv_key[index]));
        return output.toString();
    }
    public static void main(String arfs[]) throws IOException
    {
        Scanner s = new Scanner(System.in);  
        int count=0,value,n,length,index;
        String plainText,cipherText,binary,cipher,letter;
        StringBuffer binaryInput = new StringBuffer();
        StringBuffer binaryOutput = new StringBuffer();
        System.out.print("Enter the value of key length => ");
        n = s.nextInt();
        length = (int)Math.pow(2, n);
        key = new int[length];
        Key_Generator(length);
        //Generate key of length n;
        System.out.print("Enter input string : ");
        s.nextLine();
        plainText = s.nextLine();
        for(int i = 0;i<plainText.length();i++)
        {
            value = plainText.charAt(i);
            //System.out.print(value);
            binary = Integer.toBinaryString(value);
            while(binary.length()%8!=0)
                binary = "0"+binary;
            binaryInput.append(binary);
        }
        //length = binaryInput.length();
        while(binaryInput.length()%n!=0)
            binaryInput.append(0);
        index = binaryInput.length()/n;
        for(int i=0;i<index;i++)
        {
            cipher = binaryInput.substring(i*n, i*n+n);
            //System.out.print("\n"+letter);
            binary = substitute(cipher);
            while(binary.length()%n!=0)
                binary = "0"+binary;
            binaryOutput.append(binary);
        }     
        index = binaryOutput.length()/8;
        System.out.println("\nOutput after applying S-Box");
        for(int i=0;i<index;i++)
        {
            value = Integer.parseInt(binaryOutput.substring(i*8, i*8+8).toString(),2);
            System.out.print((char)value);
        }
        binaryInput.delete(0, binaryInput.length());
        System.out.println("\n\nOutput after applying reverse S-Box ");   
        index = binaryOutput.length()/n;
        for(int i=0;i<index;i++)
        {
            cipher = binaryOutput.substring(i*n, i*n+n);
            binary = inverse_substitute(cipher);
            while(binary.length()%n!=0)
                binary = "0"+binary;
            binaryInput.append(binary);
        }
        index = binaryInput.length()/8;
        for(int i=0;i<index;i++)
        {
            value = Integer.parseInt(binaryInput.substring(i*8, i*8+8).toString(),2);
            System.out.print((char)value);
        }
        System.out.println();
    }
}
