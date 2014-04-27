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
		
		public RelativeDirection turnAsRelative(GlobalDirection turnedTo)
		{
			return globalAsRelative[this.getIndex()][turnedTo.getIndex()];
		}
		
		private static RelativeDirection[][] globalAsRelative =
		{
		// north
		 {RelativeDirection.FRONT,  RelativeDirection.BACK, RelativeDirection.RIGHT, RelativeDirection.LEFT},
		 // south
		 {RelativeDirection.BACK,  RelativeDirection.FRONT, RelativeDirection.LEFT, RelativeDirection.RIGHT},
		 // east
		 {RelativeDirection.LEFT,  RelativeDirection.RIGHT, RelativeDirection.FRONT, RelativeDirection.BACK},
		// west
		 {RelativeDirection.RIGHT,  RelativeDirection.LEFT, RelativeDirection.BACK, RelativeDirection.FRONT},
		};
		
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
