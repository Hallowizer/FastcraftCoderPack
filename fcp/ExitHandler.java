package fcp;

import java.util.Map.Entry;

public class ExitHandler extends Thread {
	@Override
	public void run() {
		System.out.println("Dehash table:");
		
		for (Entry<String, String> entry : Transformer.dehashTable.entrySet())
			System.out.println(entry.getKey() + " -> " + entry.getValue());
		
		System.out.println("\nMissing entries:");
		
		for (String entry : Transformer.list)
			System.out.println(entry);
	}
}
