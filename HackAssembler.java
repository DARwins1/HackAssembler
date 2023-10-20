// Dylan Reed
// Project #6
// 3650.04
// 10/18/2023

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class HackAssembler {

	public static void main(String[] args) {
		// Get input from the user
		Scanner keyboardReader = new Scanner(System.in);
		System.out.print("Name of Hack file (excluding extension): ");
		String userInput = keyboardReader.next();

		try {
			// Create two parsers that read the file
			Scanner lFileReader = new Scanner(new File(userInput + ".asm"));
			AssemblerParser lParser = new AssemblerParser(lFileReader); // Exclusively looks for label symbols
			Scanner fileReader = new Scanner(new File(userInput + ".asm"));
			AssemblerParser parser = new AssemblerParser(fileReader);
			
			// Start our symbol table
			AssemblerSymbolTable sTable = new AssemblerSymbolTable();
			
			int instrIndex = -1;
			while (lParser.hasMoreCommands()) {
				lParser.advance();
				instrIndex++;
				if (lParser.commandType().equals("L")) {
					// Place in the symbol table at 
					sTable.addEntry(lParser.symbol(), instrIndex);
					instrIndex--; // L-Instructions don't count as real instructions
				}
			}

			try {
				// Make a new file to write to
				// NOTE: If a file with this name exists, it will be overwritten!
				PrintWriter fileWriter = new PrintWriter(new FileWriter(userInput + ".hack"));

				String machineCmd; // Machine code instruction to be written

				// Now we can start converting to machine code
				while (parser.hasMoreCommands()) {
					parser.advance(); // Get the next command
					machineCmd = "";

					// Evaluate the command based on type
					switch (parser.commandType()) {
					case "A":
						String binaryValue;
						String symbol = parser.symbol();
						try {
							// Try converting the value directly to binary
							binaryValue = Integer.toBinaryString(Integer.parseInt(symbol));
						} catch (NumberFormatException e) {
							// If conversion fails, then this must be a symbol, not an address
							// See if this symbol is new
							if (!sTable.contains(symbol))
							{
								// Symbol is new, add a new entry into the table
								sTable.addEntry(symbol);
							}
							// Now get the address from the symbol table
							binaryValue = Integer.toBinaryString(sTable.getAddress(symbol));
						}

						// Fill the gap between the beginning and the start of the value with 0's
						String fill = "0";
						fill = fill.repeat(15 - binaryValue.length());

						machineCmd = "0" + fill + binaryValue;
						fileWriter.println(machineCmd);
						break;
					case "C":
						// Piece together the command
						machineCmd = "111" + AssemblerCode.comp(parser.comp()) + AssemblerCode.dest(parser.dest())
								+ AssemblerCode.jump(parser.jump());
						fileWriter.println(machineCmd);
						break;
					}
				}
				System.out.println("File '" + userInput + ".asm' has been assembled into '" + userInput + ".hack'!");

				fileWriter.close();
			} catch (IOException e) {
				// File couldn't be written
				System.out.println("Error: '" + userInput + ".hack' could not be written!");
				System.out.println();
				System.exit(0);
			}

			fileReader.close();

		} catch (FileNotFoundException e) {
			// User-specified file could not be found
			System.out.println("Error: '" + userInput + ".asm' could not be found!");
			System.out.println();
			System.exit(0);
		}
	}
}
