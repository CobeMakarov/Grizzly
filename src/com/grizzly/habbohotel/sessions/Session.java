package com.grizzly.habbohotel.sessions;

import com.grizzly.Grizzly;
import com.grizzly.events.*;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.habbohotel.users.*;
import com.grizzly.habbohotel.rooms.RoomUser;

import org.jboss.netty.channel.Channel;

public class Session 
{
    private int ID;
    private Channel Channel;
    private User Habbo;
    private RoomUser RoomUser;
    
    private EventResponse Response;
    
    public boolean RecievedPolicy;
    
    public int OverideID = 0;
    
    public Session(Channel Channel, int ID) 
    {
        this.Channel = Channel;
        this.ID = ID;
        this.Response = new EventResponse();
        this.RoomUser = new RoomUser(this);
        
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
    
    public RoomUser GrabRoomUser()
    {
    	return this.RoomUser;
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
}
