package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Road extends SimulatedObject {
	protected Junction origen;
	protected Junction fin;
	protected int longitud;
	protected int vel_max;
	protected int lim_vel;
	protected int alarma;
	protected Weather tiempo;
	protected int total_cont;
	protected List<Vehicle> lista_v;
	protected int contador;
	
	public Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
			super(id);
			total_cont=0;
			lista_v= new ArrayList<Vehicle>();
			if(maxSpeed<0) {
				 throw new IllegalArgumentException("Speed must be positive");
			}
			else {
				vel_max=maxSpeed;
				lim_vel=vel_max;
				if(contLimit<0) {
					 throw new IllegalArgumentException("contLimit must be positive");
				}
				else {
					alarma=contLimit;
					if(length<0){
						throw new IllegalArgumentException("Length must be positive");
					}
					else {
						longitud=length;
						setWeather(weather);
							if(destJunc==null||srcJunc==null) {
								throw new IllegalArgumentException("Contamination must be positive");
							}
							else {
								origen=srcJunc;
								fin=destJunc;
							}
						}
					}
				}
			contador=lista_v.size();
			}
protected Junction getFin() {
	return fin;
}
protected Junction getini() {
	return origen;
}
	protected int getLongitud() {
		return longitud;
	}
	void enter(Vehicle v) {
		if(v.getV()!=0||v.getL()!=0) {
			throw new IllegalArgumentException("Contamination must be positive");
		}
		else {
			lista_v.add(v);
			contador++;
		}
	}
	void exit(Vehicle v) {	
		for(int i=0; i<lista_v.size();i++) {
			if(lista_v.get(i)==v) {
				lista_v.remove(i);
				contador--;
		}
		}
	}
	
	void setWeather(Weather w) {
		if(w==null) {
			throw new IllegalArgumentException("Incorrect Weather");
		}
		else {
			tiempo=w;
		}
	}
	
	void addContamination(int c) {
		if(c<0) {
			throw new IllegalArgumentException("Incorrect Weather");
		}
		else {
			total_cont=total_cont+c;
		}
	}
	abstract void reduceTotalContamination();
	abstract void updateSpeedLimit();
	abstract int calculateVehicleSpeed(Vehicle v);
	@Override
	protected void advance(int time) {
		if(this.total_cont!=0) {
		reduceTotalContamination();
		}
		updateSpeedLimit();
		for(int i=0; i<lista_v.size();i++) {
			if(vel_max<=calculateVehicleSpeed(lista_v.get(i))) {
				lista_v.get(i).setSpeed(vel_max);
				lista_v.get(i).advance(time);
			}
			else {
			lista_v.get(i).setSpeed(calculateVehicleSpeed(lista_v.get(i)));
			lista_v.get(i).advance(time);
			}
		}
		//Collections.sort(lista_v); // nose como va esto
	}
	public JSONObject report() {
		JSONObject jo= new JSONObject();
		jo.put("id", _id);
		jo.put("speedlimit", vel_max);
		if(tiempo==Weather.CLOUDY) {
			jo.put("weather","CLOUDY" );
		}
		else if(tiempo==Weather.RAINY) {
			jo.put("weather","RAINY" );
		}
		else if(tiempo==Weather.STORM) {
			jo.put("weather","STORM" );
		}
		else if(tiempo==Weather.SUNNY) {
			jo.put("weather","SUNNY" );
		}
		else if(tiempo==Weather.WINDY) {
			jo.put("weather","WINDY" );
		}
		jo.put("co2", total_cont);
		JSONArray listV= new JSONArray();
		for(Vehicle i:lista_v) {
			listV.put(i.getId());
		}
		jo.put("vehicles", listV);
		return jo;
	}
}
