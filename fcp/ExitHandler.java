package fcp;

import java.util.Map.Entry;

public class ExitHandler extends Thread {
	@Override
	public void run() {
		try {
			StringBuffer buf = new StringBuffer();
			
			buf.append("\"dehashes\": {\n");
			
			for (Entry<String, String> entry : Transformer.dehashTable.entrySet())
				buf.append("\t\"" + entry.getKey() + "\": \"" + entry.getValue() + "\",\n");
			
			buf.append("},\n\"missing-entries\": [\n");
			
			for (String entry : Transformer.list)
				buf.append("\t\"" + entry + "\",\n");
			
			buf.append("]\n");
			
			System.out.println(buf);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
