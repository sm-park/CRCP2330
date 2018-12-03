import java.io.*;

public class Assembler
{

	/*	streamLine(String input)
		------------------------------------------
		Takes an input string and returns it
		without any whitespace and comments.    	*/

	public static String streamLine(String input)
	{
		String output = "";
		int index = 0;

		while(index < input.length())
		{
			if(input.charAt(index) == '/')
			{
				break;
			}
			else if(input.charAt(index) != ' ' && input.charAt(index) != '\n')
			{
				output = output + input.charAt(index);
			}
			index++;
		}

		return output;
	}

	/*	instructionA(String input)
		------------------------------------------
		Takes an input A instruction and turns it
		into binary code.							*/

	public static String instructionA(String input)
	{
		String output = "0";
		String value = input.substring(1);

		// Translate following value into a binary array of 15.

		int decimal = Integer.parseInt(value);

		int [] binary = new int [15];

		int index = 0;
		while (decimal > 0)
		{
			binary[index] = decimal % 2;
			decimal = decimal / 2;
			index++;
		}

		// Add binary values to output command in reverse order.

		for(int i = 14; i >= 0; i--)
		{
			output = output + Integer.toString(binary[i]);
		}

		return output;
	}

	/*	instructionC(String input)
		------------------------------------------
		Takes an input C instruction and turns it
		into binary code.							*/

	public static String instructionC(String input)
	{
		String 	output 	= "111";
		boolean d 	 	= false;
		boolean j 	 	= false;
		boolean temp	= true;

		String 	dest	= "";
		String 	comp 	= "";
		String  jump	= "";

		// Search command line for dest and jump.

		int index = 0;
		while(index < input.length())
		{
			if(input.charAt(index) == '=')
			{
				d = true;
			}
			else if(input.charAt(index) == ';')
			{
				j = true;
			}
			index++;
		}

		// Separate command line to parts.

		index = 0;
		while(index < input.length())
		{
			if(input.charAt(index) == '=')
			{
				d = false;
				index++;
				continue;
			}
			if(input.charAt(index) == ';')
			{
				// temp tells whether to separate the jmp or skip
				temp = false;
				index++;
				continue;
			}

			if(d && temp)
			{
				dest = dest + input.charAt(index);
			}
			else if(!d && temp)
			{
				comp = comp + input.charAt(index);
			}
			else if(j)
			{
				jump = jump + input.charAt(index);
			}

			index++;
		}

		comp = compCommand(comp);
		dest = destCommand(dest);
		jump = jumpCommand(jump);

		output = output + comp + dest + jump;

		return output;
	}

	/*	compCommand(String input)
		------------------------------------------
		Turns a comp command to a binary command.	*/

	public static String compCommand(String input)
	{
		String output = "";

		     if(input.equals("0"))		{ output = "0101010"; }
		else if(input.equals("1"))		{ output = "0111111"; }
		else if(input.equals("-1"))		{ output = "0111010"; }
		else if(input.equals("D"))		{ output = "0001100"; }
		else if(input.equals("A"))		{ output = "0110000"; }
		else if(input.equals("M"))		{ output = "1110000"; }
		else if(input.equals("!D"))		{ output = "0001101"; }
		else if(input.equals("!A"))		{ output = "0110001"; }
		else if(input.equals("!M"))		{ output = "1110001"; }
		else if(input.equals("D+1"))	{ output = "0011111"; }
		else if(input.equals("A+1"))	{ output = "0110111"; }
		else if(input.equals("M+1"))	{ output = "1110111"; }
		else if(input.equals("D-1"))	{ output = "0001110"; }
		else if(input.equals("A-1"))	{ output = "0110010"; }
		else if(input.equals("M-1"))	{ output = "1110010"; }
		else if(input.equals("D+A"))	{ output = "0000010"; }
		else if(input.equals("D+M"))	{ output = "1000010"; }
		else if(input.equals("D-A"))	{ output = "0010011"; }
		else if(input.equals("D-M"))	{ output = "1010011"; }
		else if(input.equals("A-D"))	{ output = "0000111"; }
		else if(input.equals("M-D"))	{ output = "1000111"; }
		else if(input.equals("D&A"))	{ output = "0000000"; }
		else if(input.equals("D&M"))	{ output = "1000000"; }
		else if(input.equals("D|A"))	{ output = "0010101"; }
		else if(input.equals("D|M"))	{ output = "1010101"; }

		return output;
	}

	/*	destCommand(String input)
		------------------------------------------
		Turns a dest command to a binary command.	*/

	public static String destCommand(String input)
	{
		String output = "";

		     if(input.equals("M"))   { output = "001"; }
		else if(input.equals("D"))   { output = "010"; }
		else if(input.equals("MD"))  { output = "011"; }
		else if(input.equals("A"))   { output = "100"; }
		else if(input.equals("AM"))  { output = "101"; }
		else if(input.equals("AD"))  { output = "110"; }
		else if(input.equals("AMD")) { output = "111"; }
		else if(input.equals(""))	 { output = "000"; }

		return output;
	}


	/*	jumpCommand(String input)
		------------------------------------------
		Turns a jump command to a binary command.	*/

	public static String jumpCommand(String input)
	{
		String output = "";

			 if(input.equals("JGT")) { output = "001"; }
		else if(input.equals("JEQ")) { output = "010"; }
		else if(input.equals("JGE")) { output = "011"; }
		else if(input.equals("JLT")) { output = "100"; }
		else if(input.equals("JNE")) { output = "101"; }
		else if(input.equals("JLE")) { output = "110"; }
		else if(input.equals("JMP")) { output = "111"; }
		else if(input.equals("")) 	 { output = "000"; }

		return output;
	}

	public static void main(String [] args)
	{
		String fileName = args[0];
		String line = null;

		try
		{
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			BufferedWriter writer = new BufferedWriter(new FileWriter("output.hack"));

			while((line = bufferedReader.readLine()) != null)
			{
				// Take out whitespace and comments from command
				String command = streamLine(line);

				// If command is not empty
				if(!(command.equals("")))
				{
					if(command.charAt(0) == '@')
					{
						command = instructionA(command);
					}
					else
					{
						command = instructionC(command);
					}
					writer.write(command);
					writer.write("\n");
				}
			}

			writer.close();
			bufferedReader.close();
		}
		catch(FileNotFoundException ex)
		{
			System.out.println("Unable to open file.");
		}
		catch(IOException ex)
		{
			System.out.println("Error reading file.");
		}
	}
}