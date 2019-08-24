package fcp;

import java.io.File;
import java.util.List;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class FcpTweaker implements ITweaker {

	@Override
	public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void injectIntoClassLoader(LaunchClassLoader classLoader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getLaunchTarget() {
		return "fcp.FcpTweaker";
	}

	@Override
	public String[] getLaunchArguments() {
		return new String[0];
	}
	
	public static void main(String[] args) {
		
	}
}
