import java.io.*;
import java.util.*;

public class Assembler
{

    private static ArrayList <String>  symbols;     // Two ArrayLists that contain the symbol table
    private static ArrayList <Integer> addresses;   // of names and their corresponding addresses.

    private static int currentAddress;              // Address of the next new user-defined symbol.
    private static int currentLine;                 // Current number of processed command lines. 

    //==================================================//
    //  METHOD: addLabel                                //
    //--------------------------------------------------//
    //  Adds a new label to symbols and addresses with  //
    //  input in symbols and lineNum in addresses.      //
    //==================================================//  

    public static void addLabel(String input, int lineNum)
    {
        String symbolName = input.substring(1,input.length()-1);
        symbols.add(symbolName);
        addresses.add(lineNum);
    }

    //==================================================//
    //  METHOD: streamLine                              //
    //--------------------------------------------------//
    //  Takes an input and returns it without comments  //
    //  and whitespace.                                 //
    //==================================================//

    public static String streamLine(String input)
    {
        String output = "";
        int    index  = 0;

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

    //==================================================//
    //  METHOD: searchSymbols                           //
    //--------------------------------------------------//
    //  Searches through symbols; returns -1 if input   //
    //  is not found and returns the index at which     //
    //  input was found in symbols otherwise.           //
    //==================================================//

    public static int searchSymbols(String input)
    {
        int output = -1;

        for(int i = 0; i < symbols.size(); i++)
        {
            if(symbols.get(i).equals(input))
            {
                output = i;
            }
        }

        return output;
    }

    //==================================================//
    //  METHOD: instructionA                            //
    //--------------------------------------------------//
    //  Takes an input command and translates it into   //
    //  a binary command.                               //
    //==================================================//                            

    public static String instructionA(String input)
    {
        String output = "0";
        String value = input.substring(1);
        int [] binary = new int [15];

        // CASE ONE: VALUE IS NOT A SYMBOL
        try
        {
            int decimal = Integer.parseInt(value);
            binary = toBinary(decimal);
        }
        catch(NumberFormatException e)
        {
            // CASE TWO: VALUE IS A PRE-DEFINED SYMBOL
            if(value.equals("R0"))          { binary = toBinary(0);     }
            else if(value.equals("R1"))     { binary = toBinary(1);     }
            else if(value.equals("R2"))     { binary = toBinary(2);     }
            else if(value.equals("R3"))     { binary = toBinary(3);     }
            else if(value.equals("R4"))     { binary = toBinary(4);     }
            else if(value.equals("R5"))     { binary = toBinary(5);     }
            else if(value.equals("R6"))     { binary = toBinary(6);     }
            else if(value.equals("R7"))     { binary = toBinary(7);     }
            else if(value.equals("R8"))     { binary = toBinary(8);     }
            else if(value.equals("R9"))     { binary = toBinary(9);     }
            else if(value.equals("R10"))    { binary = toBinary(10);    }
            else if(value.equals("R11"))    { binary = toBinary(11);    }
            else if(value.equals("R12"))    { binary = toBinary(12);    }
            else if(value.equals("R13"))    { binary = toBinary(13);    }
            else if(value.equals("R14"))    { binary = toBinary(14);    }
            else if(value.equals("R15"))    { binary = toBinary(15);    }
            else if(value.equals("SCREEN")) { binary = toBinary(16384); }
            else if(value.equals("KEY"))    { binary = toBinary(24576); }
            else if(value.equals("SP"))     { binary = toBinary(0);     }
            else if(value.equals("LCL"))    { binary = toBinary(1);     }
            else if(value.equals("ARG"))    { binary = toBinary(2);     }
            else if(value.equals("THIS"))   { binary = toBinary(3);     }
            else if(value.equals("THAT"))   { binary = toBinary(4);     }

            // CASE THREE AND FOUR: USER-DEFINED SYMBOL
            else
            {
                int symIndex = searchSymbols(value);

                // CASE THREE: SYMBOL HAS NOT BEEN DEFINED
                if(symIndex == -1)
                {
                    symbols.add(value);
                    addresses.add(currentAddress);
                    binary = toBinary(currentAddress);
                    currentAddress++;
                }
                // CASE FOUR: SYMBOL HAS BEEN DEFINED PREVIOUSLY
                else
                {
                    binary = toBinary(addresses.get(symIndex));
                }
            }
        }

        // Reverse the value to correct order.
        for(int i = 14; i >= 0; i--)
        {
            output = output + Integer.toString(binary[i]);
        }

        return output;
    }

    //==================================================//
    //  METHOD: toBinary                                //
    //--------------------------------------------------//
    //  Takes a decimal input and translates it into    //
    //  binary. (ORDER IS IN REVERSE!!)                 //
    //==================================================//                           

    public static int[] toBinary(int input)
    {
        int [] output = new int [15];
        int    index  = 0;

        while(input > 0)
        {
            output[index] = input % 2;
            input         = input / 2;
            index++;
        }

        return output;
    }

    //==================================================//
    //  METHOD: instructionC                            //
    //--------------------------------------------------//
    //  Takes an input command and translates it into   //
    //  a binary command.                               //
    //==================================================//      

    public static String instructionC(String input)
    {
        String  output  = "111";
        boolean d       = false;
        boolean j       = false;
        boolean temp    = true;

        String  dest    = "";
        String  comp    = "";
        String  jump    = "";

        // SEARCH IF DEST OR JUMP EXIST

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

        // SEPARATE COMMAND LINE TO PARTS

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
                // TEMP DETERMINES IF JUMP WILL BE PARSED OR SKIPPED
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

    //==================================================//
    //  METHOD: compCommand                             //
    //--------------------------------------------------//
    //  Takes an input comp field and turns it into     //
    //  a binary code.                                  //
    //==================================================//

    public static String compCommand(String input)
    {
        String output = "";

        if(input.equals("0"))           { output = "0101010"; }
        else if(input.equals("1"))      { output = "0111111"; }
        else if(input.equals("-1"))     { output = "0111010"; }
        else if(input.equals("D"))      { output = "0001100"; }
        else if(input.equals("A"))      { output = "0110000"; }
        else if(input.equals("M"))      { output = "1110000"; }
        else if(input.equals("!D"))     { output = "0001101"; }
        else if(input.equals("!A"))     { output = "0110001"; }
        else if(input.equals("!M"))     { output = "1110001"; }
        else if(input.equals("D+1"))    { output = "0011111"; }
        else if(input.equals("A+1"))    { output = "0110111"; }
        else if(input.equals("M+1"))    { output = "1110111"; }
        else if(input.equals("D-1"))    { output = "0001110"; }
        else if(input.equals("A-1"))    { output = "0110010"; }
        else if(input.equals("M-1"))    { output = "1110010"; }
        else if(input.equals("D+A"))    { output = "0000010"; }
        else if(input.equals("D+M"))    { output = "1000010"; }
        else if(input.equals("D-A"))    { output = "0010011"; }
        else if(input.equals("D-M"))    { output = "1010011"; }
        else if(input.equals("A-D"))    { output = "0000111"; }
        else if(input.equals("M-D"))    { output = "1000111"; }
        else if(input.equals("D&A"))    { output = "0000000"; }
        else if(input.equals("D&M"))    { output = "1000000"; }
        else if(input.equals("D|A"))    { output = "0010101"; }
        else if(input.equals("D|M"))    { output = "1010101"; }

        return output;
    }

    //==================================================//
    //  METHOD: destCommand                             //
    //--------------------------------------------------//
    //  Takes an input dest field and turns it into     //
    //  a binary code.                                  //
    //==================================================//   

    public static String destCommand(String input)
    {
        String output = "";

        if(input.equals("M"))        { output = "001"; }
        else if(input.equals("D"))   { output = "010"; }
        else if(input.equals("MD"))  { output = "011"; }
        else if(input.equals("A"))   { output = "100"; }
        else if(input.equals("AM"))  { output = "101"; }
        else if(input.equals("AD"))  { output = "110"; }
        else if(input.equals("AMD")) { output = "111"; }
        else if(input.equals(""))    { output = "000"; }

        return output;
    }

    //==================================================//
    //  METHOD: jumpCommand                             //
    //--------------------------------------------------//
    //  Takes an input jump field and turns it into     //
    //  a binary code.                                  //
    //==================================================// 

    public static String jumpCommand(String input)
    {
        String output = "";

        if(input.equals("JGT"))      { output = "001"; }
        else if(input.equals("JEQ")) { output = "010"; }
        else if(input.equals("JGE")) { output = "011"; }
        else if(input.equals("JLT")) { output = "100"; }
        else if(input.equals("JNE")) { output = "101"; }
        else if(input.equals("JLE")) { output = "110"; }
        else if(input.equals("JMP")) { output = "111"; }
        else if(input.equals(""))    { output = "000"; }

        return output;
    }

    public static void main(String [] args)
    {
        String fileName = args[0];
        String line = null;

        currentLine     = 0;
        currentAddress  = 16;

        symbols = new ArrayList <String> ();
        addresses = new ArrayList <Integer> ();

        // READ THROUGH FILE AND ADD LABELS

        try
        {
            FileReader     iniFileReader     = new FileReader(fileName);
            BufferedReader iniBufferedReader = new BufferedReader(iniFileReader);
            BufferedWriter iniBufferedWriter = new BufferedWriter(new FileWriter("test.hack"));

            while((line = iniBufferedReader.readLine()) != null)
            {
                String commandLine = streamLine(line);

                if(!(commandLine.equals("")))
                {
                    if(commandLine.charAt(0) == '(')
                    {
                        addLabel(commandLine,currentLine);
                    }
                    else
                    {
                        currentLine++;
                    }
                }
            }

            iniBufferedReader.close();
        }
        catch(FileNotFoundException ex)
        {
            System.out.println("Unable to open file.");
        }
        catch(IOException ex)
        {
            System.out.println("Error reading file.");
        }

        // READ THROUGH FILE AND TRANSLATE INTO BINARY CODE

        try
        {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            BufferedWriter writer = new BufferedWriter(new FileWriter("output.hack"));

            while((line = bufferedReader.readLine()) != null)
            {
                // TAKE OUT WHITESPACE FROM LINE
                String command = streamLine(line);

                // IF COMMAND EXISTS
                if(!(command.equals("")) && command.charAt(0) != '(')
                {
                    if(command.charAt(0) == '@')
                    {
                        currentLine++;
                        command = instructionA(command);
                    }
                    else
                    {
                        currentLine++;
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