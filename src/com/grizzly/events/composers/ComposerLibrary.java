package com.grizzly.events.composers;

public class ComposerLibrary 
{	
	/*
	 * Incoming
	 */
	public static short ReadRelease = 4000;
    public static short MyData = 962;
    public static short GetProfile = 346;
    public static short AcceptFriend = 3556;
    public static short PrivateChat = 978;
    public static short GetCataIndex = 1389;
    public static short GetCataPage = 3406;
    public static short BuyItem = 3737;
    public static short CanCreateRoom = 3847;
    public static short CreateNewRoom = 2222;
    public static short LoadOwnRooms = 3853;
    public static short StartRoom = 2897;
    public static short LoadRoomData = 2090;
    public static short LoadFloorInventory = 3777;
    public static short LoadHeightmap = 2840;
	public static short StartChat = 782;
	public static short EndChat = 348;
	public static short RequestShout = 1508;
	public static short Talk = 3073;
    public static short RoomFinished = 1189;
    public static short LookOnAllRooms = 3794;
    public static short RequestWalk = 3635;
    public static short RequestLeaveRoom = 2293;
    public static short Sign; //Um..
    public static short ChangeLooks = 1216;
    
	/*
	 * Outgoing
	 */
    public static int Login = 3984;
    public static int LoadProfile = 1295;
    public static int UserInfo = 3538; 
    public static int ModTool = 128;
    public static int SendCredits = 2124; 
    public static int ClubData = 270;
    public static int Alert = 1768;
	public static int AlertLink = 1622;
	public static int PendingFriends = 3671;
	public static int LoadFriends = 1386;
	public static int UpdateFriendState = 3231;
	public static int TalkOnChat = 1685;
	public static int CataIndex = 1908;
	public static int CataPage = 2099;
	public static int UpdateCata = 2596;
	public static int UpdateCataData = 2220;
	public static int BroughtItem = 1741;
	public static int UpdateInventory = 1366;
	public static int DisposeItemFromInventory = 1257;
	public static int SendInventory = 222;
	public static int AlertNewItems = 1464;
	public static int CanCreateNewRoom = 1894;
    public static int CreateNewRooom = 3489;
    public static int SendRoom = 442;
    public static int OwnRooms = 2703;
    public static int RoomStatuses = 3650;
    public static int RoomFull = 3728;
    public static int InitRoomProcess = 2291;
    public static int ModelAndId = 1332;
    public static int Papers = 1339;
	public static int LoadRightsOnRoom = 1387;
	public static int RoomOwnerPower = 3504;
	public static int RoomEvents = 381;
	public static int Heightmap1 = 1394;
	public static int Heightmap2 = 3907;
    public static int SerializeRoomData = 2559;
	public static int SendWallItem = 1280;
	public static int SendFloorItem = 2095;
	public static int UpdateFloorItem = 2207;
	public static int RemoveFloorItem = 390;
	public static int RemoveItem = 2731;
	public static int Bots = 1613;
	public static int BeforeUsers = 1671;
	public static int RoomUsers = 3293;
	public static int FloorItems = 602;
	public static int WallItems = 1671;
    public static int VipWallsAndFloors = 716;
	public static int Chat = 3612;
	public static int Shout = 460;
	public static int RoomPanel = 580;
	public static int UserData = 1613;
    public static int RoomData = 2102;
    public static int Wave = 283;
    public static int LeaveRoom = 2693;
    public static int LeavingRoom = 1861;
    public static int TypingToggle = 1267;
    public static int UpdateInfo = 179;
}
