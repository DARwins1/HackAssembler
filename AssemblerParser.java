// Dylan Reed
// Project #6
// 3650.04
// 10/18/2023

import java.util.Scanner;

public class AssemblerParser {
	private Scanner lineReader;
	private String currentCommand;

	public AssemblerParser(Scanner fileReader) {
		lineReader = fileReader;
	}

	public boolean hasMoreCommands() {
		return lineReader.hasNext();
	}

	public void advance() {
		// Get the next non-comment, non-blank line
		do {
			currentCommand = lineReader.nextLine();
			
			if (currentCommand.indexOf("/") >= 0) {
				// If there's a comment on this line, try to prune it off
				currentCommand = currentCommand.substring(0, currentCommand.indexOf("/"));
			}
			currentCommand = currentCommand.trim();
		} while (currentCommand.length() == 0 && lineReader.hasNext());
	}

	public String commandType() {
		if (currentCommand.contains("@")) {
			return "A"; // A_COMMAND type
		} else if (currentCommand.contains("=") || currentCommand.contains(";")) {
			return "C"; // C_COMMAND type
		} else {
			return "L"; // L_COMMAND type
		}
	}

	public String symbol() {
		if (commandType().equals("A")) {
			return currentCommand.substring(1);
		} else if (commandType().equals("L")) {
			return currentCommand.substring(1, currentCommand.length() - 1);
		} else {
			return "";
		}
	}

	public String dest() {
		if (commandType().equals("C") && currentCommand.indexOf("=") > 0) {
			return currentCommand.substring(0, currentCommand.indexOf("="));
		} else {
			return "";
		}
	}

	public String comp() {
		if (commandType().equals("C")) {
			int endIndex = currentCommand.indexOf(";"); // Read until ";"
			if (endIndex < 0) {
				// If no ";", then read until the end of the line
				endIndex = currentCommand.length();
			}
			return currentCommand.substring(currentCommand.indexOf("=") + 1, endIndex);
		} else {
			return "";
		}
	}

	public String jump() {
		if (commandType().equals("C") && currentCommand.indexOf(";") > 0) {
			return currentCommand.substring(currentCommand.indexOf(";") + 1);
		} else {
			return "";
		}
	}
}
