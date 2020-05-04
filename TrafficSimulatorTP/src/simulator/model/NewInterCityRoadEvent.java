package simulator.model;

public class NewInterCityRoadEvent extends NewRoadEvent {
	public NewInterCityRoadEvent(int time, String id, String srcJun, String
			destJunc, int length, int co2Limit, int maxSpeed, Weather weather)
			{
		super(time, id, srcJun, destJunc, length,  co2Limit, maxSpeed, weather);
}
	@Override
	Road CreateRoad(RoadMap map) {
		InterCityRoad r=new InterCityRoad(id, map.getJunction(src), map.getJunction(dest), length, co2limit, maxspeed, weather);
		return r;
	}
	@Override
	public String toString() {
	return "New InterCityRoad '"+id+"'";
	}
}