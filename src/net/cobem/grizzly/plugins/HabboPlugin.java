package net.cobem.grizzly.plugins;

import net.cobem.grizzly.habbohotel.sessions.Session;

public interface HabboPlugin
{
	public void OnEvent(HabboEvent Event, Session Session);
	public void OnRegister(String Name, String Author, String Version);
	public String GrabName();
	public String GrabAuthor();
	public String GrabVersion();
}
