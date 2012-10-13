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
		RegisterItems();
	}
	
	private void RegisterHandshake()
	{
		MessageLibrary.put(ComposerLibrary.ReadRelease, new ShakeHandsEvent());
	}
	
	private void RegisterUser()
	{
		MessageLibrary.put(ComposerLibrary.GetProfile, new LoadUserProfileEvent());
		MessageLibrary.put(ComposerLibrary.MyData, new LoadUserDataEvent());
	}
	
	private void RegisterMessenger()
	{
		MessageLibrary.put(ComposerLibrary.AcceptFriend, new AcceptFriendRequestEvent());
		MessageLibrary.put(ComposerLibrary.PrivateChat, new SendInstantMessageEvent());
	}
	
	private void RegisterCatalog()
	{
		MessageLibrary.put(ComposerLibrary.GetCataIndex, new InitializeCatalogEvent());
		MessageLibrary.put(ComposerLibrary.GetCataPage, new InitializeCatalogPageEvent());
		MessageLibrary.put(ComposerLibrary.BuyItem, new PurchaseCatalogItemEvent());
	}
	
	private void RegisterNavigator()
	{
		MessageLibrary.put(ComposerLibrary.CanCreateRoom, new RoomCreationCheckEvent());
		MessageLibrary.put(ComposerLibrary.CreateNewRoom, new CreateRoomEvent());
		MessageLibrary.put(ComposerLibrary.LoadOwnRooms, new ViewMyRoomsEvent());
		MessageLibrary.put(ComposerLibrary.LookOnAllRooms, new LoadPopulatedRoomsEvent());
	}
	
	private void RegisterRoom()
	{
		MessageLibrary.put(ComposerLibrary.StartRoom, new InitializeRoomEvent());
		MessageLibrary.put(ComposerLibrary.LoadRoomData, new FinishRoomLoadEvent());
		MessageLibrary.put(ComposerLibrary.LoadHeightmap, new ParseHeightMapEvent());
		MessageLibrary.put(ComposerLibrary.Talk, new SayEvent());
		MessageLibrary.put(ComposerLibrary.RequestWalk, new MovementEvent());
		MessageLibrary.put(ComposerLibrary.RequestLeaveRoom, new LeaveRoomEvent());
		MessageLibrary.put(ComposerLibrary.RequestShout, new ShoutEvent());
		MessageLibrary.put(ComposerLibrary.Sign, new ShowSignEvent());
		MessageLibrary.put(ComposerLibrary.StartChat, new StartTypingEvent());
		MessageLibrary.put(ComposerLibrary.EndChat, new StartTypingEvent());
		MessageLibrary.put(ComposerLibrary.ChangeLooks, new ChangeLooksEvent());
	}
	
	private void RegisterItems()
	{
		MessageLibrary.put(ComposerLibrary.LoadFloorInventory, new InitializeInventoryEvent());
	}
	public void handleRequest(Session Session, EventRequest Message) throws Exception
	{
		if (Message.GetHeader() == 4000)
		{
			String SWFBUILD = Message.GetBodyString().replace("{0}", "");
			
			if (!SWFBUILD.contains(Grizzly.HabboBuild))
			{
				Grizzly.WriteOut("Build Mismatch; May hit some un-runnable actions!");
				Grizzly.WriteOut("Grizzly's SWF Build: " + Grizzly.HabboBuild);
				Grizzly.WriteOut("Your SWF BUILD: " + SWFBUILD);
			}
		}
		
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
