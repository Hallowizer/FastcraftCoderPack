package fcp;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;
import net.minecraft.launchwrapper.IClassTransformer;

public class Transformer implements IClassTransformer {
	public static final List<String> list = new ArrayList<>();
	public static final Map<String, String> dehashTable = new HashMap<>();
	
	private static final MessageDigest MD; // Let's not turn it into bytes like FastCraft did...
	private static final Charset UTF8 = Charset.forName("UTF-8");
	
	static {
		try {
			MD = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (!name.equals("fastcraft.B")) {
			checkHash(transformedName, basicClass);
			return basicClass;
		}
		
		ClassNode clazz = new ClassNode();
		new ClassReader(basicClass).accept(clazz, 0);
		
		for (MethodNode method : clazz.methods)
			if (method.name.equals("doStuff") && method.desc.equals("()V"))
				for (AbstractInsnNode insn : (Iterable<AbstractInsnNode>)() -> method.instructions.iterator())
					if (insn instanceof LdcInsnNode) {
						LdcInsnNode ldc = (LdcInsnNode) insn;
						
						if (ldc.cst instanceof String)
							list.add((String) ldc.cst);
					}
		
		return basicClass;
	}
	
	private void checkHash(String name, byte[] bytes) {
		ClassNode clazz = new ClassNode();
		new ClassReader(bytes).accept(clazz, 0);
		
		test(clazz.name);
		
		for (FieldNode field : clazz.fields) {
			test(field.name);
			test(field.desc);
		}
		
		for (MethodNode method : clazz.methods) {
			test(method.name);
			test(method.desc);
		}
	}
	
	private void test(String text) {
		String out;
		try {
			out = Tests.b(text); // Hash string and convert to base 85
		} catch (Exception e) {
			return;
		}
		MD.reset();
		
		if (list.contains(out)) {
			dehashTable.put(out, text);
			list.remove(out);
		}
	}
}
