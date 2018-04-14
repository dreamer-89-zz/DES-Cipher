/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DES_Encryption;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
class Permutation_Box {
    static int key[];
    static int inv_key[];
    public static void Key_Generator(int size) throws IOException
    {
        // This is permutation key... This can be found under INS folder...
        BufferedWriter bf = new BufferedWriter(new FileWriter("C:\\Users\\Suraj Singh\\Desktop\\Class Experiments\\INS\\P_Box.txt"));
        Set<Integer> set = new HashSet<Integer>();
        Random generator = new Random();
        key = new int[size];
        int value,count=0;
        while(set.size()!=size)
        {
            value = generator.nextInt(size);// to include n in the random output.
            if(!set.contains(value+1))
            {
                bf.write(value+" ");
                set.add(value+1);
                key[count++]=value;
            }
        }
        bf.flush();
        bf.close();
    }
    public static String permute(String input)
    {
        StringBuffer output= new StringBuffer();
        for(int i=0;i<key.length;i++)
        {
            output.append(input.charAt(key[i]));
        }
        return output.toString();
    }
    public static void inverse_cal()
    {
        inv_key = new int[key.length];
        for(int i=0;i<key.length;i++)
            inv_key[key[i]]=i;
    }
    public static String inverse_permute(String input)
    {
        StringBuffer output = new StringBuffer();
        for(int i=0;i<key.length;i++)
        {
            output.append(input.charAt(inv_key[i]));
        }
        return output.toString();
    }
    public static void main(String arfs[]) throws IOException
    {
        Scanner s = new Scanner(System.in);
        int keySize,value,count=0,randomPermutation[];
        String input,letter,output,binary;
        int b[],index;
        StringBuffer binaryInput = new StringBuffer();
        StringBuffer binaryOutput = new StringBuffer();
        System.out.print("Enter the key size ");
        keySize = s.nextInt();
        b = new int[keySize];
        randomPermutation = new int[keySize];
        System.out.println("Enter the text ");
        s.nextLine();
        input = s.nextLine();
        Key_Generator(keySize);     
        for(int i = 0;i<input.length();i++)
        {
            value = input.charAt(i);
            //System.out.print(value);
            binary = Integer.toBinaryString(value);
            while(binary.length()%8!=0)
                binary = "0"+binary;
            binaryInput.append(binary);
        }
        while(binaryInput.length()%keySize!=0)
            binaryInput.append(0);
        index = binaryInput.length()/keySize;
        for(int i=0;i<index;i++)
        {
            letter = binaryInput.substring(i*keySize, i*keySize+keySize);
            //System.out.print("\n"+letter);
            binaryOutput.append(permute(letter));
        }
        System.out.println("\nOutput after applying P-Box");
        for(int i=0;i<index;i++)
        {
            value = Integer.parseInt(binaryOutput.substring(i*8, i*8+8).toString(),2);
            System.out.print((char)value);
        }
        //System.out.println(binaryOutput.toString());
        binaryInput.delete(0, binaryInput.length());
        System.out.println("\n\nOutput after applying reverse P-Box ");        
        inverse_cal();
        for(int i=0;i<index;i++)
        {
            letter = binaryOutput.substring(i*keySize, i*keySize+keySize);
            binaryInput.append(inverse_permute(letter));
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
