package net.cobem.grizzly.habbohotel.pathfinding.ihi;

public class IHI_PathfinderValues
{

	/**
	 * The maximum height a path allowed to decrease in a single step.
	 */
	final float maximumFall;
	/**
	 * The maximum height a path allowed to increase in a single step.
	 */
	final float maximumJump;
	
	
	final	short[]		binaryHeap;
	final	short[]		F;
	final	short[]		G;
	final	short[]		H;
	
	final	short[]		parent;
	
	final	byte[][]	tiles;
	final	byte[]		X;
	final	byte[]		Y;
	final	float[][]	Z;
			short		count;
			short		lastID;
			short		location;
	
	IHI_PathfinderValues(byte[][] collisionMap, float[][] height, float maximumFall, float maximumJump)
	{
		this.tiles = new byte[collisionMap.length][collisionMap[0].length];

		int mapSize = collisionMap.length*collisionMap[0].length;

		this.X = new byte[mapSize];
		this.Y = new byte[mapSize];
		this.Z = height;
		this.H = new short[mapSize];
		this.G = new short[mapSize];
		this.F = new short[mapSize];
		
		this.count = 0;
		this.lastID = 0;
		
		this.binaryHeap = new short[mapSize];
		this.parent = new short[mapSize];
		
		this.location = 0;
		
		this.maximumFall = maximumFall;
		this.maximumJump = maximumJump;
		
		
		
	}
}
