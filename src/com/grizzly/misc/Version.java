package com.grizzly.misc;

public class Version 
{
	public int Major;
	public int Minor;
	public int Revision;
	public int Build;
	
	public Version(int Major, int Minor, int Revision, int Build)
	{
		this.Major = Major;
		this.Minor = Minor;
		this.Revision = Revision;
		this.Build = Build;
		
	}
	
	public String String()
	{
		return this.Major + "." + this.Minor + "." + this.Revision + "." + this.Build;
	}
}
