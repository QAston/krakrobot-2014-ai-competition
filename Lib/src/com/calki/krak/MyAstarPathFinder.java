package com.calki.krak;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MyAstarPathFinder implements PathFinder {

	private static class AstarNode {
		Position pos;
		GlobalDirection orientation;
		int g;
		int f;

		public static class WeakCmp implements WeakComparator<AstarNode> {

			@Override
			public boolean lessThan(AstarNode t1, AstarNode t2) {
				// TODO Auto-generated method stub
				return t1.f < t2.f || (t1.f == t2.f && t1.g < t2.g);
			}

		}
	}

	private static class AstarField {
		Position fromWhere;
		boolean visited;
	}

	public MyAstarPathFinder(Position start, GlobalDirection startOrientation, Position finish, Map map) {
		this.map = map;
		this.start = start;
		this.goal = finish;
		this.startOrientation = startOrientation;
	}

	private Map map;
	private Position start;
	private GlobalDirection startOrientation;
	private Position goal;
	private AstarField[][] fields = new AstarField[Map.SIZE][Map.SIZE];

	protected int calcHeurystyka(Position pos) {
		return Math.abs(pos.x - goal.x) + Math.abs(pos.y - goal.y);
	}

	protected int calcKoszt(RelativeDirection dir) {
		switch (dir) {
		case BACK:
			return 3;
		case LEFT:
			return 2;
		case RIGHT:
			return 2;
		case FRONT:
			return 1;
		default:
			throw new RuntimeException("CAlc cost exception");
		}
	}

	public List<Position> getPath() {

		Heap<AstarNode> open = new Heap<MyAstarPathFinder.AstarNode>(
				new ArrayList<MyAstarPathFinder.AstarNode>(15),
				new AstarNode.WeakCmp());

		AstarNode current = new AstarNode();
		current.pos = start;
		current.orientation = startOrientation;
		current.g = 0;
		current.f = current.g + calcHeurystyka(current.pos);

		open.insert(current);

		while (true) {
			if (open.size() == 0) {
				throw new RuntimeException("astart fail");
			} else {
				current = open.removeMin();

				if (current.pos == goal) {
					break;
				} else {
					for (RelativeDirection dir : RelativeDirection.values()) {
						AstarNode next = new AstarNode();
						next.orientation = startOrientation.relativeFrom(dir);
						next.pos = current.pos.getNeighbour(next.orientation);
						if (next.pos != null) {
							AstarField field = fields[next.pos.x][next.pos.y];
							if (field == null
									&& map.getField(next.pos).canTravel()) {
								next.g = current.g + calcKoszt(dir);
								next.f = current.g + calcHeurystyka(next.pos);
								open.insert(next);

								field = new AstarField();

								field.visited = true;
								field.fromWhere = current.pos;

								fields[next.pos.x][next.pos.y] = field;
							}
						}
					}
				}
			}
		}

		List<Position> posList = new ArrayList<>();
		//posList.add(start);
		{
			Stack<Position> posStack = new Stack<>();
			Position iterPos = goal;
			AstarField field;

			while (start != iterPos) {
				field = fields[iterPos.x][iterPos.y];
				posStack.push(iterPos);
				iterPos = field.fromWhere;
			}
			while(!posStack.isEmpty())
			{
				posList.add(posStack.peek());
				posStack.pop();
			}
		}
		return posList;
	}
}
