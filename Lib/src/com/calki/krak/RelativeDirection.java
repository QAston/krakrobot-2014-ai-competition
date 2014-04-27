package com.calki.krak;

public enum RelativeDirection {
    FRONT(0),
    LEFT(1),
    RIGHT(2),
    BACK(3);
		
		private int index;
		
		RelativeDirection(int index)
		{
			this.index = index;
		}
		
		public int getIndex()
		{
			return index;
		}

}
