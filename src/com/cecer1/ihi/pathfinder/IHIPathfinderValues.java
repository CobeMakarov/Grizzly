package com.cecer1.ihi.pathfinder;

//
//Copyright (c) 2012 Cecer
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.
//

public class IHIPathfinderValues 
{
	public final short[] BinaryHeap;
    /// <summary>
    /// (Estimated) total cost.
    /// </summary>
    public final short[] F;
    /// <summary>
    /// Cost so far.
    /// </summary>
    public final short[] G;
    /// <summary>
    /// Estimated remaining cost.
    /// </summary>
    public final short[] H;
    
    /// <summary>
    /// The maximum allowed drop distance.
    /// </summary>
    public final float MaxDrop;
    /// <summary>
    /// The maximum allowed jump distance.
    /// </summary>
    public final float MaxJump;
    /// <summary>
    /// The index of the parent tile.
    /// </summary>
    public final short[] Parent;
    public final byte[][] Tiles;
    /// <summary>
    /// The tiles X coords.
    /// </summary>
    public final byte[] X;
    /// <summary>
    /// The tiles Y coords.
    /// </summary>
    public final byte[] Y;
    /// <summary>
    /// The tiles Z coords.
    /// </summary>
    public final float[][] Z;
    public short Count;
    public short LastID;

    public short Location;

    public IHIPathfinderValues(byte[][] collisionMap, float[][] height, float maxDrop, float maxJump)
    {
        Tiles = new byte[collisionMap.length][collisionMap[0].length];

        X = new byte[Tiles.length];
        Y = new byte[X.length];
        Z = height;
        H = new short[X.length];
        G = new short[X.length];
        F = new short[X.length];

        Count = 0;
        LastID = 0;

        BinaryHeap = new short[X.length];
        Parent = new short[X.length];

        Location = 0;

        MaxDrop = maxDrop;
        MaxJump = maxJump;
    }
}
