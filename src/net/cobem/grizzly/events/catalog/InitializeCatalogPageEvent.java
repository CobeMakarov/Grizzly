package net.cobem.grizzly.events.catalog;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.catalog.CatalogItem;
import net.cobem.grizzly.habbohotel.catalog.CatalogPage;
import net.cobem.grizzly.habbohotel.sessions.Session;

public class InitializeCatalogPageEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		CatalogPage Page = Grizzly.GrabHabboHotel().GrabCatalogHandler().GrabPageById(Request.PopInt());
		
		if (Page == null)
		{
			Grizzly.WriteOut(Session.GrabHabbo().Username + " tried to load a non-existant page!");
			return;
		}
		
		Session.GrabResponse().Initialize(ComposerLibrary.CataPage);
		Session.GrabResponse().AppendInt32(Page.ID);
		
		switch(Page.Layout.toString())
		{
			case "frontpage":
				Session.GrabResponse().AppendString("frontpage3");
				Session.GrabResponse().AppendInt32(3);
				Session.GrabResponse().AppendString(Page.Header);
				Session.GrabResponse().AppendString(Page.Description);
				Session.GrabResponse().AppendString("");
				Session.GrabResponse().AppendInt32(11);
				Session.GrabResponse().AppendString(""); //Special
				Session.GrabResponse().AppendString(""); //Text1 	
				Session.GrabResponse().AppendString("");
				Session.GrabResponse().AppendString(""); //Text2
				Session.GrabResponse().AppendString(""); //Details
				Session.GrabResponse().AppendString(""); //Tease2
				Session.GrabResponse().AppendString("Rares");
				Session.GrabResponse().AppendString("#FEFEFE");
				Session.GrabResponse().AppendString("#FEFEFE");
				Session.GrabResponse().AppendString("Click here for more info..");
				Session.GrabResponse().AppendString("magic.credits");
			break;
			
			case "spaces":
				Session.GrabResponse().AppendString("spaces_new");
				Session.GrabResponse().AppendInt32(1);
				Session.GrabResponse().AppendString(Page.Header);
				Session.GrabResponse().AppendInt32(1);
				Session.GrabResponse().AppendString(Page.Header); //Supposed to be Text1 but who needs it?
			break;
			
			case "trophies":
				Session.GrabResponse().AppendString("trophies");
				Session.GrabResponse().AppendInt32(1);
				Session.GrabResponse().AppendString(Page.Header);
				Session.GrabResponse().AppendInt32(2);
				Session.GrabResponse().AppendString(Page.Description);
				Session.GrabResponse().AppendString(""); //Details
				Session.GrabResponse().AppendInt32(0);
				Session.GrabResponse().AppendInt32(0);
				Session.GrabResponse().AppendInt32(-1);
			break;
			
			case "pets":
				Session.GrabResponse().AppendString("pets");
				Session.GrabResponse().AppendInt32(2);
				Session.GrabResponse().AppendString(Page.Header);
				Session.GrabResponse().AppendString(Page.Description);
				Session.GrabResponse().AppendInt32(4);
				Session.GrabResponse().AppendString(""); //Text1
				Session.GrabResponse().AppendString("Give a name:");
				Session.GrabResponse().AppendString("Pick a color:");
				Session.GrabResponse().AppendString("Pick a race:");
				Session.GrabResponse().AppendInt32(0);
				Session.GrabResponse().AppendInt32(0);
				Session.GrabResponse().AppendInt32(-1);
			break;
			
			default:
				Session.GrabResponse().AppendString(Page.Layout);
				Session.GrabResponse().AppendInt32(3);
				Session.GrabResponse().AppendString(Page.Header);
				Session.GrabResponse().AppendString(""); //desc?
				Session.GrabResponse().AppendString(""); //Special
				Session.GrabResponse().AppendInt32(3);
				Session.GrabResponse().AppendString(Page.Description); //Text1
				Session.GrabResponse().AppendString(""); //Details
				Session.GrabResponse().AppendString(""); //Teaser2
			break;
		}
		
		Session.GrabResponse().AppendInt32(Grizzly.GrabHabboHotel().GrabCatalogHandler().GrabItemsByPage(Page.ID).size());
		
		for(CatalogItem Item : Grizzly.GrabHabboHotel().GrabCatalogHandler().GrabItemsByPage(Page.ID).values())
		{
			Session.GrabResponse().AppendInt32(Item.ID);
			Session.GrabResponse().AppendString(Item.GrabBaseItem().PublicTitle);
			Session.GrabResponse().AppendInt32(Item.Cost);
			Session.GrabResponse().AppendInt32(0); // Pixels, removed in this build.
			Session.GrabResponse().AppendInt32(0);
			Session.GrabResponse().AppendBoolean(true);
			Session.GrabResponse().AppendInt32(1);
			{
				Session.GrabResponse().AppendString(Item.GrabBaseItem().Type);
				Session.GrabResponse().AppendInt32(Item.GrabBaseItem().Sprite);
				
				if (Item.Title.contains("wallpaper"))
				{
					Session.GrabResponse().AppendString(Item.Title.split("_")[2]);
				}
				else if(Item.Title.contains("floor"))
				{
					Session.GrabResponse().AppendString(Item.Title.split("_")[2]);
				}
				else if(Item.Title.contains("landscape"))
				{
					Session.GrabResponse().AppendString(Item.Title.split("_")[2]);
				}
				else
				{
					Session.GrabResponse().AppendString("");
				}
				
				Session.GrabResponse().AppendInt32(Item.Quantity);
				Session.GrabResponse().AppendInt32(-1);
				Session.GrabResponse().AppendBoolean(false);
			}

			Session.GrabResponse().AppendInt32(Item.ForVIP ? 1 : 0);

			Session.GrabResponse().AppendBoolean(false);
		}
		
		Session.GrabResponse().AppendInt32(-1);
		Session.GrabResponse().AppendBoolean(false);
		
		Session.SendResponse();
	}
}
