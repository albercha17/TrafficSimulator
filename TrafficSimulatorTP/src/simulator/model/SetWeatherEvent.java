package simulator.model;

import java.util.ArrayList;
import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event {
	private List<Pair<String,Weather>> ws;
	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) {
		super(time);
		if(ws==null) {
			throw new IllegalArgumentException("The list is empty"); 
		}
		else {
			this.ws=new ArrayList<Pair<String,Weather>>(ws);
		}
		
		// ...
		}

	@Override
	void execute(RoadMap map) {
		for(int i=0;i<ws.size();i++) {
			map.getRoad(ws.get(i).getFirst()).setWeather(ws.get(i).getSecond());
		}
	}
	@Override
	public String toString() {
		String x="Change Weather:[ ";
		for(int i=0; i<ws.size();i++) x= x+" ("+ws.get(i).getFirst() + " "+ ws.get(i).getSecond().toString() + ") ";
		x=x+ "]";
		return x;
	}
}
