package simulator.model;

import java.util.ArrayList;
import java.util.List;

import simulator.misc.Pair;

public class NewSetContClassEvent extends Event{
	private List<Pair<String,Integer>> cs;

	public NewSetContClassEvent(int time, List<Pair<String,Integer>> cs) {
		super(time);
		if(cs==null) {
			throw new IllegalArgumentException("The list is empty"); 
		}
		else {
			this.cs=new ArrayList<Pair<String,Integer>>(cs);
		}
		
		// ...
		}

	@Override
	void execute(RoadMap map) {
		for(int i=0;i<cs.size();i++) {
			map.getVehicle(cs.get(i).getFirst()).setContaminationClass(cs.get(i).getSecond());
		}
	}
	@Override
	public String toString() {
		String x="Change SetContClass:[ ";
		for(int i=0; i<cs.size();i++) x= x+" ("+cs.get(i).getFirst() + " "+ Integer.toString(cs.get(i).getSecond())+ ") ";
		x=x+ "]";
		return x;
	}
	}
