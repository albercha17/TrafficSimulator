package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject {
	private List<Junction> itinerario;
	private int contador;
	private int vel_max;
	private int vel_actual;
	private VehicleStatus estado;
	private Road carretera;
	private int localizacion;
	private int grad_cont;
	private int cont_total;
	private int distanciaT;
	
	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) {
			super(id);
			vel_actual=0;
			estado=VehicleStatus.PENDING;
			cont_total=0;
			contador=0;
			distanciaT=0;
			carretera=null;
			localizacion=0;
			grad_cont=0;
			if(maxSpeed >0){
				vel_max=maxSpeed;
				setContaminationClass(contClass);
				if(itinerary.size()<2) {
					throw new IllegalArgumentException("List is empty");
				}
				else {
					itinerario=Collections.unmodifiableList(new ArrayList<>(itinerary)); 
				}
			}
			else {
				 throw new IllegalArgumentException("Speed must be positive");
			}
			}
	public void setSpeed(int s) {
		if(s<0) {
			 throw new IllegalArgumentException("Speed must be positive");
		}
		else {
		if(s>vel_max) {
			vel_actual=vel_max;
		}
		else {
			vel_actual=s;
		}
		}
		
	}
	public void setContaminationClass(int c) {
		if(c<0||c>10) {
			 throw new IllegalArgumentException("Contamination must be between 0-10");
		}
		else {
			grad_cont=c;
		}
	}
	public void moveToNextRoad() { 
		if(getEstado()==VehicleStatus.PENDING) {
			contador++;
			carretera=itinerario.get(0).getR(itinerario.get(contador));
			localizacion=0;
			vel_actual=0;
			carretera.enter(this);
			vel_actual=vel_max;
			estado=VehicleStatus.TRAVELING;
			
		}
		else {
			contador++;
			if(contador==itinerario.size()) {
				estado=VehicleStatus.ARRIVED;
				vel_actual=0;
				carretera.exit(this);
			}
			else if(contador<itinerario.size()) {
			carretera.exit(this);
			carretera=itinerario.get(contador-1).getR(itinerario.get(contador));
			localizacion=0;
			vel_actual=0;
			carretera.enter(this);
			vel_actual=vel_max;
			estado=VehicleStatus.TRAVELING;
		}
			
		}
	}
	public List<Junction> getI() {
		return itinerario;
	}
	
	public int getL() {
		return localizacion;
	}
	public int getV() {
		return vel_actual;
	}
	public int getC() {
		return grad_cont;
	}
	public Road getR() {
		return carretera;
	}

	public void advance(int time) {
		int previaL;
		int cont;
		if(getEstado()==VehicleStatus.TRAVELING) {
			previaL=localizacion;
			if(localizacion+vel_actual< carretera.getLongitud()) {
				localizacion=localizacion+vel_actual;
			}
			else {
				localizacion=carretera.getLongitud();
			}
			previaL=localizacion-previaL;
			distanciaT=distanciaT+previaL;
			cont=grad_cont*previaL;
			cont_total=cont_total+cont;
			carretera.addContamination(cont); 
			if(localizacion>=carretera.getLongitud()) {
				if(contador==itinerario.size()-1) {
					itinerario.get(contador).enter(this);
					estado=VehicleStatus.WAITING;
					vel_actual=0;
				}
				else  if(itinerario.size()-1>contador){
				if(!itinerario.contains(carretera.getFin())){
					 throw new IllegalArgumentException("The itinerary doesn't contain the road");
				}
				else {
					itinerario.get(contador).enter(this);
					estado=VehicleStatus.WAITING;
					vel_actual=0;
				}
				}
			}
		}
		else {
			vel_actual=0;
		}
		
	} 
public int compareTo(Vehicle v) {
	if(this.localizacion==v.localizacion)return 0;
	if(this.localizacion<v.localizacion) return -1;
	return 1;
}

	@Override
	public JSONObject report() {
		JSONObject jo= new JSONObject();
		jo.put("id", _id);
		jo.put("speed", vel_actual);
		jo.put("distance", distanciaT);
		jo.put("co2", cont_total);
		jo.put("class",grad_cont);
		if(getEstado()==VehicleStatus.ARRIVED) {
			jo.put("status", "ARRIVED");
		}
		else if(getEstado()==VehicleStatus.PENDING) {
			jo.put("status", "PENDING");
		}
		else if(getEstado()==VehicleStatus.TRAVELING) {
			jo.put("status", "TRAVELING");
		}
		else if(getEstado()==VehicleStatus.WAITING) {
			jo.put("status", "WAITING");
		}
		if(getEstado()!=VehicleStatus.ARRIVED) {
			jo.put("road", carretera.getId());
			jo.put("location", localizacion);
		}
			
		return jo;
	}
	public VehicleStatus getEstado() {
		return estado;
	}

}
