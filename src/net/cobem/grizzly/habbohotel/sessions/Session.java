package net.cobem.grizzly.habbohotel.sessions;


import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.events.*;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.events.rooms.*;
import net.cobem.grizzly.habbohotel.rooms.Actor;
import net.cobem.grizzly.habbohotel.users.*;

import org.jboss.netty.channel.Channel;

public class Session 
{
    private int ID;
    private Channel Channel;
    private User Habbo;
    private Actor Actor;
    
    private EventResponse Response;
    
    public boolean RecievedPolicy;
    
    public int OverideID = 0;
    public String OverideMessage;
    
    public Session(Channel Channel, int ID) 
    {
        this.Channel = Channel;
        this.ID = ID;
        this.Response = new EventResponse();
        this.Actor = new Actor(this);
        
        this.Habbo = new User(GrabIP());
        
    }

    public int GrabID()
    {
    	return ID;
    }
    
    public Channel GrabChannel()
    {
    	return Channel;
    }

    public String GrabIP() 
    {
        if (Channel == null)
        {
        	return "NULL";
        }
        else
        {
        	 return Channel.getRemoteAddress().toString().split(":")[0].substring(1);
        }
    }
    
    public User GrabHabbo()
    {
    	return Habbo;
    }
    
    public Actor GrabActor()
    {
    	return this.Actor;
    }
    
    public EventResponse GrabResponse()
    {
    	return Response;
    }
    
    public void SetHabbo(User User) 
    {
        Habbo = User;
    }
    
	public void SendResponse(EventResponse Message)
	{
		if (Grizzly.GrabConfig().GrabValue("net.grizzly.packetlog").equals("1"))
		{
			Grizzly.WriteOut("[" + Message.GetHeader() + "] " + Message.GetBodyString() + " sent by " + this.GrabIP());
		}
		
		this.Channel.write(Message);
	}

	public void SendResponse()
	{
		if (Grizzly.GrabConfig().GrabValue("net.grizzly.packetlog").equals("1"))
		{
			Grizzly.WriteOut("[" + Response.GetHeader() + "] " + Response.GetBodyString() + " sent by " + this.GrabIP());
		}
		this.Channel.write(Response);
	}
	
	public EventResponse ReturnResponse()
	{
		return Response;
	}
	
	public void SendAlert(String Message, String Link)
	{
		EventResponse Alert = new EventResponse();
		
		if (Link == null)
		{
			Alert.Initialize(ComposerLibrary.Alert);
			Alert.AppendString(Message);
			Alert.AppendString("");
		}
		else
		{
			Alert.Initialize(ComposerLibrary.AlertLink);
			Alert.AppendString(Message);
			Alert.AppendString(Link);
		}
		
		SendResponse(Alert);
	}
	
	public void LeaveRoom()
	{
		if (!GrabActor().InRoom())
		{
			return;
		}
		
		GrabResponse().Initialize(ComposerLibrary.LeavingRoom);
		GrabResponse().AppendBoolean(false);
		SendResponse();
		
		GrabActor().CurrentRoom.RemoveUser(this);
	}
	
	public void EnterRoom(int ID)
	{
		this.OverideID = ID;
		
		(new InitializeRoomEvent()).Parse(this, null);
	}
	
	public void Chat(String Message, boolean Shout)
	{
		this.OverideMessage = Message;
		
		if (Shout)
		{
			(new ShoutEvent()).Parse(this, null);
		}
		else
		{
			(new SayEvent()).Parse(this, null);
		}
	}
}
