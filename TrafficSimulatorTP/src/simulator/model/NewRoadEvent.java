package simulator.model;

public abstract class NewRoadEvent extends Event{
	private int time;
	protected String id;
	protected String src;
	protected String dest;
	protected int length;
	protected int co2limit;
	protected int maxspeed;
	protected Weather weather;
	NewRoadEvent(int time, String id, String src, String
			dest, int maxspeed, int co2limit, int length , Weather weather) {
		super(time);
		this.id=id;
		this.src=src;
		this.dest=dest;
		this.length=length;
		this.co2limit=co2limit;
		this.maxspeed=maxspeed;
		this.weather=weather;
	}
	abstract Road CreateRoad(RoadMap map);
	@Override
	
	void execute(RoadMap map) {
		map.addRoad(CreateRoad( map));
	}

}
