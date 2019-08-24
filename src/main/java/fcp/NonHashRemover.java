package fcp;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class NonHashRemover {
	private static final int LEN = "V7#^[H_UNX)oqt5Pp;6E2_jh=a=<i4'lPDt]$3Xg".length();
	
	public static void main(String[] args) throws Exception {
		File file = new File(args[0]);
		
		byte[] bytes;
		try (FileInputStream in = new FileInputStream(file)) {
			bytes = new byte[in.available()];
			in.read(bytes);
		}
		
		String contents = new String(bytes);
		Json json = new GsonBuilder().create().fromJson(contents, Json.class);
		
		if (json.dehashes == null) {
			System.out.println("wtf");
			System.exit(1);
		}
		
		List<String> toRemove = new ArrayList<>();
		for (String hash : json.missingHashes)
			if (hash.length() != LEN)
				toRemove.add(hash);
		
		System.out.println("\n\n\n\n{\n\t\"dehashes\": {");
		
		for (Entry<String, String> entry : json.dehashes.entrySet())
			System.out.println("\t\t\"" + entry.getKey() + "\": \"" + entry.getValue() + "\",");
		
		System.out.println("\t},\n\t\"missing-entries\": [");
		
		for (String missing : json.missingHashes)
			if (!toRemove.contains(missing))
				System.out.println("\t\t\"" + missing + "\",");
		
		System.out.println("\t]\n}");
	}
	
	private static class Json {
		public Map<String, String> dehashes;
		@SerializedName("missing-entries")
		public String[] missingHashes;
	}
}
