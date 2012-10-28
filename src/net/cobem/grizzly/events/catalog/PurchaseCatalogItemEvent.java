package net.cobem.grizzly.events.catalog;

import net.cobem.grizzly.events.*;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.catalog.CatalogItem;
import net.cobem.grizzly.habbohotel.sessions.Session;

public class PurchaseCatalogItemEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		int Page = Request.PopInt();
		int Item = Request.PopInt();
		
		CatalogItem mItem = new CatalogItem(Item);
		
		if (mItem.Page != Page)
		{
			return;
		}
		
		if (Session.GrabHabbo().Credits < mItem.Cost)
		{
			return;
		}
		
		if (mItem.GrabBaseItem().Interaction == "pet")
		{
			return;
		}
		
		Session.GrabHabbo().Credits -= mItem.Cost;
		
		Session.GrabResponse().Initialize(ComposerLibrary.SendCredits);
		Session.GrabResponse().AppendString("" + Session.GrabHabbo().Credits + ".0");
		Session.SendResponse();
		
		Session.GrabHabbo().Append();
		
		Session.GrabResponse().Initialize(ComposerLibrary.BroughtItem);
		Session.GrabResponse().AppendInt32(mItem.ID);
		Session.GrabResponse().AppendString(mItem.GrabBaseItem().PublicTitle);
		Session.GrabResponse().AppendInt32(mItem.Cost);
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendBoolean(false);
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendBoolean(false);
		Session.SendResponse();

		Session.GrabResponse().Initialize(ComposerLibrary.AlertNewItems);
		Session.GrabResponse().AppendInt32(1);
		Session.GrabResponse().AppendInt32(mItem.GrabBaseItem().Type == "s" ? 1 : 2);
		Session.GrabResponse().AppendInt32(mItem.Quantity);

		for(int i = 0; i != mItem.Quantity; i++)
		{
			Session.GrabHabbo().GrabItems().AddItem(mItem.GrabBaseItem().ID, true);
		}

		Session.GrabResponse().Initialize(ComposerLibrary.UpdateInventory);
		Session.SendResponse();
	}
}
