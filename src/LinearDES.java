import java.util.*;

public class LinearDES
{
	public static void main(String [] args)
	{
		//ask user for input, key and mode of operation
		String input_str, operation, master_key;
		int input_int;
		String sub_key[] = new String [2];

		Scanner console = new Scanner(System.in);
		System.out.print("Enter operation (Encrypt / Decrypt): ");
		operation = console.nextLine();
			
		System.out.print("Enter key: ");
		master_key = console.nextLine();
		
		//K1 = (K1, K1, K1)
		//K2 = (K2, K2, K2)
		calculate_key(sub_key, master_key);
		if (operation.equalsIgnoreCase("decrypt"))
		{
			//reverse sequence of keys if decrypt
			String temp = sub_key[0];
			sub_key[0] =  sub_key[1];
			sub_key[1] = temp;
		}
	
		for(int i = 0; i < 16; i++)
		{
			input_str = Integer.toBinaryString(i);
			input_str = masking(input_str);
			
			//rotate left (4 bits)
			String input_str_rLeft = rotate('l', input_str);
			
			//split (A0)2 bits - (B0)2 bits
			String A0 = input_str_rLeft.substring(0, 2);
			
			String B0 = input_str_rLeft.substring(2, 4);
		
			//A1 = B0
			String A1 = B0;
			
			//F(RHS, K1)
			String Z = function_RHS_key(B0, sub_key, 0);
			
			//B1 = A0 XOR F(B0, K1)
			String B1 = XOR_op(A0, Z);
			
			//A2 = B1 
			String A2 = B1;
			
			//F(RHS, K2) Round 2
			Z = function_RHS_key(B1, sub_key, 1);
			
			//B2 = A1 XOR F(B1, K2)
			String B2 = XOR_op(A1, Z);
				
			//A2 -> RHS 
			//B2 -> LHS
			String output = "";
			output = B2 + A2;
			
			//Cipher = output rotate right
			String cipher = rotate('r', output);
			cipher = masking(cipher);
			
			System.out.println("Plaintext: " + input_str);
			System.out.println("Ciphertext: " + cipher);
			System.out.println();
		}
	
	}
	
	public static void calculate_key(String sub_key[], String master_key)
	{
		sub_key[0] = "";
		sub_key[1] = "";
		
		for (int i = 0; i < 3; i++)
		{
			sub_key[0] += master_key.substring(0, 1);
			
			sub_key[1] += master_key.substring(1, 2);
		}
	}
		
	public static String function_RHS_key(String RHS, String sub_key[], int key_index)
	{
		//B0 expand to 3 bit = X1, X2, X1
		String RHS_expand = 	RHS;
		RHS_expand += RHS.substring(0, 1);
		
		//I = B01(Ex) XOR K1
		String I_XOR = "";
		for (int i = 0; i < RHS_expand.length(); i++)
		{
			String temp = XOR_op(RHS_expand.substring(i, i+1), sub_key[key_index].substring(i, i+1));
			I_XOR += temp;	
		}

		//J = I XOR 1
		String J = "";
		J += XOR_op(I_XOR.substring(1, 2), "1");
		J += XOR_op(I_XOR.substring(2, 3), "1");
		
		//J rotate left = Z (output = 2 bits)
		String Z = rotate('l', J);
		
		return Z;
	}
	
	public static String XOR_op(String bin, String bin_2)
	{
		String result = "";
		
		for (int i = 0; i < bin.length(); i++)
		{
			if (bin.substring(i, i+1).equals(bin_2.substring(i, i+1)))
				result += "0";
			else
				result += "1";
		}
		
		return result;
	}
	
	public static String masking(String c)
	{
		String new_c = "";
		int length = c.length();
		
		switch(length)
		{
			case 1:	new_c = "000" + c;
						break;
			case 2:	new_c = "00" + c;
						break;
			case 3:	new_c = "0" + c;
						break;
			case 4: 	new_c = c;
						break;
		}
		
		return new_c;
	}
	
	public static String rotate(char direct, String binary)
	{
		String new_binary = "";
		if (direct == 'r')
		{
			new_binary = binary.substring(binary.length()-1, binary.length());
			new_binary += binary.substring(0, binary.length()-1);
		}	
		else
		{
			new_binary = binary.substring(1, binary.length());
			new_binary += binary.substring(0, 1);
		}
		
		return new_binary;
	}
	
	
}
