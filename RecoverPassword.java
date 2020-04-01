import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 * University of Central Florida
 * CIS 3360 - Fall 2017
 * Author: Hylton Williamson
 */

public class RecoverPassword {

	public static void main(String[] args) {

		File file = new File(args[0]);
		String hash_Val = args[1];
		Long hash_ValInt = Long.parseLong(hash_Val);
		String fileName = args[0];
		String temp = "", saltedPassStr = "";
		long saltedPassLong, right, left, hash;
		Scanner s;
		int entries = 0, i = 0, count = 0, index = -1, j, salt = -1;
		String[][] passArray = new String[1000][2];

		System.out.println("CIS3360 Password Recovery by Hylton Williamson\n");
		System.out.println("   Dictionary file name        : " + fileName);
		System.out.println("   Salted password hash value  : " + hash_Val + "\n");
		System.out.println("Index   Word   Unsalted ASCII equivalent\n");

		try {
			// Read in the file into scanner and store the first line into
			// message.
			s = new Scanner(file);
			temp = s.nextLine();

			// Continues to use lines until one is left.
			while (s.hasNextLine()) {
				entries++;
				passArray[i][0] = temp;
				passArray[i][1] = turnToASCII(temp);
				
				// Tries all the hash values
				for (j = 0; j < 1000; j++) {
					count++;
					saltedPassStr = Integer.toString(j) + passArray[i][1];
					saltedPassLong = Long.parseLong(saltedPassStr);
					left = saltedPassLong / 100000000;
					right = saltedPassLong % 100000000;
					hash = ((243 * left) + right) % 85767489;
					
					//Check to see if a match is found and stopped one is found
					if (hash == hash_ValInt) {
						index = i;
						salt = j;
						break;
					}
				}
				i++;
				
				if(index != -1)
					break;
				temp = s.nextLine();
			}
			
			//Processes the last line if a match hasn't been found
			if (index == -1) {
				entries++;
				passArray[i][0] = temp;
				passArray[i][1] = turnToASCII(temp);
				
				for (j = 0; j < 1000; j++) {
					count++;
					saltedPassStr = Integer.toString(j) + passArray[i][1];
					saltedPassLong = Long.parseLong(saltedPassStr);
					left = saltedPassLong / 100000000;
					right = saltedPassLong % 100000000;
					hash = ((243 * left) + right) % 85767489;
					
					if (hash == hash_ValInt) {
						index = i;
						salt = j;
						break;
					}
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		print2DArray(passArray, entries);

		if (index >= 0) {
			System.out.println("Password recovered:");
			System.out.println("   Password             : " + passArray[index][0]);
			System.out.println("   ASCII value          : " + passArray[index][1]);
			System.out.printf("   Salt value           : %03d\n", salt);
			System.out.println("   Combinations tested  : " + count);

		} else {
			System.out.println("Password not found in dictionary\n");
			System.out.println("Combinations tested: "+count);
		}

		System.out.println("--------------------------------------------\n");
	}

	//Changes each character of a str into its ascii value and returns it as a string.
	static public String turnToASCII(String str) {
		String result = "";
		int i, asc_value;

		for (i = 0; i < str.length(); i++) {
			asc_value = (int) str.charAt(i);
			result += Integer.toString(asc_value);
		}
		
		return result;
	}

	//Print the 2D array holding the list password list
	static public void print2DArray(String[][] array, int num) {
		
		for (int i = 0; i < num; i++) {
			
			if (i == 99) {
				System.out.println("  " + (i + 1) + " :  " + array[i][0] + " " + array[i][1] + "\n");
				
			} else {
				System.out.println("   " + (i + 1) + " :  " + array[i][0] + " " + array[i][1]);
			}
		}
	}
}
