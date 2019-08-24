package fcp;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.launchwrapper.Launch;

public class SimpleHashFinder {
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
		
		Launch.main(new String[] {"--gameDir", ".", "--assetsDir", ".", "--version", "1.7.10", "--tweakClass", "fcp.FcpTweaker"});
		FMLDeobfuscatingRemapper.INSTANCE.setup(null, Launch.classLoader, "/deobfuscation_data-1.7.10.lzma");
		
		Map<String, Map<String, String>> rawFieldMaps = ReflectionHelper.getPrivateValue(FMLDeobfuscatingRemapper.class, FMLDeobfuscatingRemapper.INSTANCE, "rawFieldMaps");
		Map<String, Map<String, String>> rawMethodMaps = ReflectionHelper.getPrivateValue(FMLDeobfuscatingRemapper.class, FMLDeobfuscatingRemapper.INSTANCE, "rawMethodMaps");
		
		for (Map<String, Map<String, String>> map : new Map[] {rawFieldMaps, rawMethodMaps})
			for (Entry<String, Map<String, String>> clazz : map.entrySet()) {
				checkHash(json, clazz.getKey());
				
				for (Entry<String, String> deobf : clazz.getValue().entrySet()) {
					checkHash(json, deobf.getKey());
					checkHash(json, deobf.getValue());
				}
			}
		
		System.out.println("\n\n\n\n{\n\t\"dehashes\": {");
		
		for (Entry<String, String> entry : json.dehashes.entrySet())
			System.out.println("\t\t\"" + entry.getKey() + "\": \"" + entry.getValue() + "\",");
		
		System.out.println("\t},\n\t\"missing-entries\": [");
		
		for (String missing : json.missingHashes)
			if (!json.dehashes.containsKey(missing))
				System.out.println("\t\t\"" + missing + "\",");
		
		System.out.println("\t]\n}");
	}
	
	private static class Json {
		public Map<String, String> dehashes;
		@SerializedName("missing-entries")
		public String[] missingHashes;
	}
	
	private static void checkHash(Json json, String dehashed) throws Exception {
		String hash = Tests.b(dehashed);
		
		if (contains(json.missingHashes, hash))
			json.dehashes.put(hash, dehashed);
	}
	
	private static boolean contains(String[] array, String val) {
		for (String s  : array)
			if (s.equals(val))
				return true;
		
		return false;
	}
}
