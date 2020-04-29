package simulator.model;

public class NewCityRoadEvent extends NewRoadEvent {
	public NewCityRoadEvent(int time, String id, String srcJun, String
			destJunc, int length, int co2Limit, int maxSpeed, Weather weather)
			{
			super(time, id, srcJun, destJunc, length,  co2Limit, maxSpeed, weather);
			}
	Road CreateRoad(RoadMap map) {
		CityRoad r=new CityRoad(id, map.getJunction(src), map.getJunction(dest), length, co2limit, maxspeed, weather);
		return r;
	}
}
