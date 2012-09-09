package com.grizzly.events.catalog;

import java.util.Map;

import com.grizzly.Grizzly;
import com.grizzly.events.Event;
import com.grizzly.events.EventRequest;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.habbohotel.catalog.CatalogPage;
import com.grizzly.habbohotel.sessions.Session;

public class InitCatalog implements Event
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
