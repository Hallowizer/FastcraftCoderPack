package fcp;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class PluginThing implements IFMLLoadingPlugin {
	@Override
	public String[] getASMTransformerClass() {
		return new String[] {"fcp.Transformer"};
	}
	
	@Override
	public String getModContainerClass() {
		return null;
	}
	
	@Override
	public String getSetupClass() {
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) {
		((LaunchClassLoader) getClass().getClassLoader()).addClassLoaderExclusion("fcp.Tests");
		
		Runtime.getRuntime().addShutdownHook(new ExitHandler());
		
		try {
			Class.forName("fastcraft.B", true, getClass().getClassLoader());
			System.out.println("Loaded class B!");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
