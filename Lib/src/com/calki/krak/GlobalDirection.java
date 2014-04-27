package com.calki.krak;

public enum GlobalDirection {
		NORTH(0),
		SOUTH(1),
		EAST(2),
		WEST(3);
		
		private int index;
		
		GlobalDirection(int index)
		{
			this.index = index;
		}
		
		public int getIndex()
		{
			return index;
		}
		
		public GlobalDirection relativeFrom(RelativeDirection relative)
		{
			return relativeAsGlobal[getIndex()][relative.getIndex()];
		}
		
		private static GlobalDirection[][] relativeAsGlobal =
		{
		// north
		 {NORTH,  WEST, EAST, SOUTH},
		 // south
		 {SOUTH,  EAST, WEST, NORTH},
		 // east
		 {EAST,  NORTH, SOUTH, WEST},
		// west
		 {WEST,  SOUTH, NORTH, EAST},
		};
}
