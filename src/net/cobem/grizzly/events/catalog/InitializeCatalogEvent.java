package net.cobem.grizzly.events.catalog;

import java.util.Map;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.catalog.CatalogPage;
import net.cobem.grizzly.habbohotel.sessions.Session;


public class InitializeCatalogEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		Map<Integer, CatalogPage> Primary = Grizzly.GrabHabboHotel().GrabCatalogHandler().GrabPrimaryPages();
		
		Session.GrabResponse().Initialize(ComposerLibrary.CataIndex);
		Session.GrabResponse().AppendBoolean(true);
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendInt32(-1);
		Session.GrabResponse().AppendString("root");
		Session.GrabResponse().AppendBoolean(false);
		Session.GrabResponse().AppendBoolean(false);
		Session.GrabResponse().AppendInt32(Primary.size());
		
		for(CatalogPage Parent : Primary.values())
		{
			Map<Integer, CatalogPage> Secondary = Grizzly.GrabHabboHotel().GrabCatalogHandler().GrabSecondaryPages(Parent.ID);
			
			Session.GrabResponse().AppendBoolean(true);
			Session.GrabResponse().AppendInt32(Parent.IconColor);
			Session.GrabResponse().AppendInt32(Parent.IconImage);
			Session.GrabResponse().AppendInt32(Parent.ID);
			Session.GrabResponse().AppendString(Parent.Title.toLowerCase().replace(" ", "_"));
			Session.GrabResponse().AppendString(Parent.Title);
			Session.GrabResponse().AppendInt32(Secondary.size());
			
			for(CatalogPage Kid : Secondary.values())
			{
				Session.GrabResponse().AppendBoolean(true);
				Session.GrabResponse().AppendInt32(Kid.IconColor);
				Session.GrabResponse().AppendInt32(Kid.IconImage);
				Session.GrabResponse().AppendInt32(Kid.ID);
				Session.GrabResponse().AppendString(Kid.Title.toLowerCase().replace(" ", "_"));
				Session.GrabResponse().AppendString(Kid.Title);
				Session.GrabResponse().AppendInt32(0);	
			}
		}
		
		Session.GrabResponse().AppendBoolean(true);
		
		Session.SendResponse();
	}
}
