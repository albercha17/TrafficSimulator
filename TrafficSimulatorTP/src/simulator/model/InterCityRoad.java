package simulator.model;

public class InterCityRoad extends Road{
	
	public InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	public void reduceTotalContamination() {
		int x=0;
		if(tiempo==Weather.SUNNY) {
			x=2;
		}
		else if(tiempo==Weather.CLOUDY) {
			x=3;
		}
		else if(tiempo==Weather.RAINY) {
			x=10;
		}
		else if(tiempo==Weather.WINDY) {
			x=15;
		}
		else if(tiempo==Weather.STORM) {
			x=20;
		}
		int y=(int)(((100.0-x)/100.0)*total_cont);
		total_cont=y;
	}
	public void updateSpeedLimit() {
		if(total_cont>alarma&&vel_max==lim_vel) {
			
			int y=(int)(vel_max*0.5);
			vel_max=y;
		}
		else if(total_cont<=alarma){
			vel_max=lim_vel;
		}
	}

	@Override
	public int calculateVehicleSpeed(Vehicle v) {
		int s=0;
		if(tiempo==Weather.STORM) {
			s=(int)Math.ceil(v.getV()*0.8);
			return s;
		}
		else {
			return vel_max;
		}
	}

}
