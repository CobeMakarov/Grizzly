package net.cobem.grizzly.habbohotel.catalog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import net.cobem.grizzly.Grizzly;


public class CatalogHandler 
{
	private Map<Integer, CatalogPage> Pages;
	private Map<Integer, CatalogItem> Items;
	
	public CatalogHandler() throws SQLException
	{
		Pages = new HashMap<Integer, CatalogPage>();
		
		Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_store_pages");
		
		ResultSet CatalogPages = Grizzly.GrabDatabase().GrabTable();
		
		while(CatalogPages.next())
		{
			Pages.put(new Integer(CatalogPages.getInt("id")), new CatalogPage(CatalogPages));
		}
		
		Items = new HashMap<Integer, CatalogItem>();
		
		Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_store_items");
		
		ResultSet CatalogItems = Grizzly.GrabDatabase().GrabTable();
		
		while(CatalogItems.next())
		{
			Items.put(new Integer(CatalogItems.getInt("id")), new CatalogItem(CatalogItems));
		}
		
		Grizzly.WriteOut("Loaded " + Items.size() + " catalog items for " + Pages.size() + " catalog pages!");
	}
	
	public Map<Integer, CatalogPage> GrabCatalogPages()
	{
		return Pages;
	}
	
	public Map<Integer, CatalogPage> GrabPrimaryPages()
	{
		Map<Integer, CatalogPage> Primaries = new HashMap<Integer, CatalogPage>();
		
		for(CatalogPage Page : Pages.values())
		{
			if (Page.Parent == -1)
			{
				Primaries.put(new Integer(Page.ID), Page);
			}
		}
		
		return Primaries;
	}
	
	public Map<Integer, CatalogPage> GrabSecondaryPages(int Parent)
	{
		Map<Integer, CatalogPage> Primaries = new HashMap<Integer, CatalogPage>();
		
		for(CatalogPage Page : Pages.values())
		{
			if (Page.Parent == Parent)
			{
				Primaries.put(new Integer(Page.ID), Page);
			}
		}
		
		return Primaries;
	}
	
	public CatalogPage GrabPageById(int ID)
	{
		if (Pages.containsKey(ID))
		{
			return Pages.get(ID);
		}
		else
		{
			return null;
		}
	}
	
	public Map<Integer, CatalogItem> GrabItemsByPage(int ID)
	{
		Map<Integer, CatalogItem> Itemz = new HashMap<Integer, CatalogItem>();
		
		for(CatalogItem Item : Items.values())
		{
			if (Item.Page == ID)
			{
				Itemz.put(new Integer(Item.ID), Item);
			}
		}
		
		return Itemz;
	}
}
