package com.grizzly.events.user;

import com.grizzly.events.*;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.events.messenger.InitFriends;
import com.grizzly.habbohotel.sessions.Session;

public class LoadMyData implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request) throws Exception 
	{
		EventResponse Message = new EventResponse();
		
		Message.Initialize(ComposerLibrary.UserInfo);
        Message.AppendInt32(Session.GrabHabbo().ID);
        Message.AppendString(Session.GrabHabbo().Username);
        Message.AppendString(Session.GrabHabbo().Look);
        Message.AppendString(Session.GrabHabbo().Gender.toString());
        Message.AppendString(Session.GrabHabbo().Motto);
        Message.AppendString(Session.GrabHabbo().Username.toLowerCase());
        Message.AppendBoolean(true);
        Message.AppendInt32(8); //8
        Message.AppendInt32(1); //3
        Message.AppendInt32(1); //3
        Message.AppendBoolean(true);
        Message.AppendString("26-07-2012");
        Message.AppendBoolean(false);
        Message.AppendBoolean(false);
		Session.SendResponse(Message);
		
		/*
		 * Credits
		 */
		Message.Initialize(ComposerLibrary.SendCredits);
		Message.AppendString("" + Session.GrabHabbo().Credits + ".0");
		Session.SendResponse(Message);
		
		/*
		 * Club
		 */
		Message.Initialize(ComposerLibrary.ClubData);
		Message.AppendString("club_habbo");
        Message.AppendInt32(1); // DaysLeft
        Message.AppendInt32(1); // Months Left
        Message.AppendInt32(0); // Years left wtf?
        Message.AppendInt32(2); // VIP (1 = no / 2 = yes)
        Message.AppendBoolean(false);
        Message.AppendBoolean(true);
        Message.AppendInt32(0);
        Message.AppendInt32(1); // Days I have
        Message.AppendInt32(0);
		Session.SendResponse(Message);
		
		/*
		 * Friends
		 */
		(new InitFriends()).Parse(Session, Request);
		
		/*
		 * Reload Friends Messenger
		 */
		Session.GrabHabbo().RefreshMessenger(true);
		
		Session.SendAlert("Welcome to Grizzly Alpha Testing!", "http://cobem.net/");
	}
}

