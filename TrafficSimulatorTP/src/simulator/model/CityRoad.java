package simulator.model;

public class CityRoad extends Road {

	public CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
        int x=0;
        if(tiempo==Weather.WINDY) {
            x=10;
        }
        else if(tiempo==Weather.STORM) {
            x=10;
        }
        else {
            x=2;
        }
        x=total_cont-x;
        if(x<0) {
           x=0;
        }
        else {
            total_cont=x;
    }
	}
	@Override
	void updateSpeedLimit() {
		
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		int x;
		x=(int)Math.ceil(((11.0-v.getC())/11.0)*lim_vel);
		return x;
	}
}
