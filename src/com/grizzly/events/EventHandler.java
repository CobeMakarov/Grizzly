package com.grizzly.events;

import java.util.Map;
import java.util.HashMap;

import com.grizzly.Grizzly;
import com.grizzly.events.catalog.*;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.events.handshake.*;
import com.grizzly.events.messenger.*;
import com.grizzly.events.navigator.*;
import com.grizzly.events.rooms.*;
import com.grizzly.events.user.*;
import com.grizzly.habbohotel.sessions.*;

public class EventHandler 
{
	private Map<Short, Event> MessageLibrary;
	
	public EventHandler()
	{
		MessageLibrary = new HashMap<Short, Event>();
		
		RegisterHandshake();
		RegisterUser();
		RegisterMessenger();
		RegisterCatalog();
		RegisterNavigator();
		RegisterRoom();
	}
	
	private void RegisterHandshake()
	{
		MessageLibrary.put(ComposerLibrary.ReadRelease, new Login());
	}
	
	private void RegisterUser()
	{
		MessageLibrary.put(ComposerLibrary.GetProfile, new LoadUserProfile());
		MessageLibrary.put(ComposerLibrary.MyData, new LoadMyData());
	}
	
	private void RegisterMessenger()
	{
		MessageLibrary.put(ComposerLibrary.AcceptFriend, new AcceptRequest());
		MessageLibrary.put(ComposerLibrary.PrivateChat, new ComposeMessage());
	}
	
	private void RegisterCatalog()
	{
		MessageLibrary.put(ComposerLibrary.GetCataIndex, new InitCatalog());
		MessageLibrary.put(ComposerLibrary.GetCataPage, new InitPage());
		MessageLibrary.put(ComposerLibrary.BuyItem, new PurchaseItem());
	}
	
	private void RegisterNavigator()
	{
		MessageLibrary.put(ComposerLibrary.CanCreateRoom, new CreateRoomCheck());
		MessageLibrary.put(ComposerLibrary.CreateNewRoom, new CreateRoom());
		MessageLibrary.put(ComposerLibrary.LoadOwnRooms, new LoadOwnRooms());
	}
	
	private void RegisterRoom()
	{
		MessageLibrary.put(ComposerLibrary.StartRoom, new InitRoom());
	}
	
	public void handleRequest(Session Session, EventRequest Message) throws Exception
	{
		if (Grizzly.GrabConfig().GrabValue("net.grizzly.packetlog").equals("1"))
		{
			Grizzly.WriteOut("[" + Message.GetHeader() + "] " + Message.GetBodyString() + " sent to " + Session.GrabIP());
		}
		
		if (this.MessageLibrary.containsKey(Message.GetHeader()))
		{
			this.MessageLibrary.get(Message.GetHeader()).Parse(Session, Message);
		}
	}
}
