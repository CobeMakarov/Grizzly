package net.cobem.grizzly.habbohotel.pathfinding;

import java.util.Collection;

public interface IPathfinder 
{
	void ApplyCollisionMap(byte[][] Map, float[][] Height);
    Collection<byte[]> Path(byte StartX, byte StartY, byte EndX, byte EndY, float MaxDrop, float MaxJump);
}
