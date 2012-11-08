package net.cobem.grizzly.plugins;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.habbohotel.sessions.Session;
import net.cobem.grizzly.misc.Configuration;

public class PluginHandler
{
	private List<File> PluginsList;
	
	private List<HabboPlugin> Plugins;
	
	public PluginHandler()
	{
		PluginsList = new ArrayList<File>();
		Plugins = new ArrayList<HabboPlugin>();
		
		for(File Plugin : new File((Grizzly.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "plugins/").replace("/bin", "")).listFiles())
		{
			if (!Plugin.getName().contains("properties"))
			{
				PluginsList.add(Plugin);
			}
		}
		
		InitializePlugins();
	}
	
	private void InitializePlugins()
	{
		Grizzly.WriteOut("");
		
		for(File Plugin : PluginsList)
		{
			try 
			{
				Configuration PluginConfig = new Configuration();
				
				PluginConfig.Load((Grizzly.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "plugins").replace("/bin", "") + "/plugin.properties");
				
				ClassLoader Child = URLClassLoader.newInstance(new URL[] { Plugin.toURI().toURL() }, this.getClass().getClassLoader());
				
				String ConfigName = Plugin.getName().replace(".jar", "");
				
				try
				{
					PluginConfig.GrabValue(ConfigName + ".namespace");
				}
				catch(NullPointerException Ex) { continue; }
				
				if (!(PluginConfig.GrabValue(ConfigName + ".enabled").contains("true")))
				{
					continue;
				}
				
				String Namespace = PluginConfig.GrabValue(ConfigName + ".namespace");
				String Author = PluginConfig.GrabValue(ConfigName + ".author");
				String Version = PluginConfig.GrabValue(ConfigName + ".version");
				
				Class<?> PluginClass = Class.forName(Namespace, true, Child);
				
				HabboPlugin PluginInstance = PluginClass.asSubclass(HabboPlugin.class).newInstance();
				
				Grizzly.WriteOut(Plugin.getName() + " v" + Version + " by " + Author);
				
				PluginInstance.OnRegister(ConfigName, Author, Version);
				
				Plugins.add(PluginInstance);
			} catch (Exception e) { e.printStackTrace(); }
		}
		
		Grizzly.WriteOut("");
		
		Grizzly.WriteOut("Loaded " + Plugins.size() + " plugins");
	}
	
	public boolean RunEvent(HabboEvent Event, Session Session)
	{
		for(HabboPlugin Plugin : Plugins)
		{
			Plugin.OnEvent(Event, Session);
		}
		
		return true;
	}
	
	public boolean DestructPlugin(Session Session, String Name)
	{
		int Count = Plugins.size();
		
		for(HabboPlugin Plugin : Plugins)
		{
			try 
			{
				if (Plugin.GrabName().equalsIgnoreCase(Name))
				{
					Grizzly.WriteOut(Plugin.GrabName() + " has been destructed by " + Session.GrabHabbo().Username);
					
					Plugins.remove(Plugin);
					break;
				}
			} catch (Exception Ex) { Grizzly.WriteOut(Ex.getStackTrace()); }
		}
		
		return (Count != Plugins.size());
	}
}
