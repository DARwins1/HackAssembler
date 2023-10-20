// Dylan Reed
// Project #6
// 3650.04
// 10/18/2023

import java.util.HashMap;

public class AssemblerSymbolTable {
	private HashMap<String, Integer> table;
	private int memIndex;

	public AssemblerSymbolTable() {
		table = new HashMap<String, Integer>();
		memIndex = 16; // Start allocating new symbol spaces at Memory[16]

		// Insert the predefined symbols
		table.put("SP", 0);
		table.put("LCL", 1);
		table.put("ARG", 2);
		table.put("THIS", 3);
		table.put("THAT", 4);
		table.put("SCREEN", 16384);
		table.put("KBD", 24576);

		// R0-R15
		for (int i = 0; i < 16; i++) {
			table.put("R" + i, i);
		}
	}

	public void addEntry(String symbol) {
		table.put(symbol, memIndex);
		memIndex++;
	}
	
	// Override for label symbols with specified positions
	public void addEntry(String symbol, int index) {
		table.put(symbol, index);
	}

	public boolean contains(String symbol) {
		return table.containsKey(symbol);
	}

	public int getAddress(String symbol) {
		return table.get(symbol);
	}
}
