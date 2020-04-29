package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {
	private List<Junction> lista_cruces;
	private List<Vehicle> lista_vehi;
	private List<Road> lista_carre;
	private Map<String,Junction> mapa_cruces;
	private Map<String,Road> mapa_carre;
	private Map<String,Vehicle> mapa_vehi;
	
	protected RoadMap() {
		lista_cruces=new ArrayList<Junction>();
		lista_vehi= new ArrayList<Vehicle>();
		lista_carre= new ArrayList<Road>();
		mapa_carre=  new HashMap<String,Road>();
		mapa_cruces=  new HashMap<String,Junction>();
		mapa_vehi=  new HashMap<String,Vehicle>();
	}
	void addJunction(Junction j){
		lista_cruces.add(j);
		mapa_cruces.put(j._id, j);
	}
	void addRoad(Road r) {
		if(mapa_carre.get(r._id)==null) {
			if(mapa_cruces.containsValue(r.origen) && mapa_cruces.containsValue(r.getFin())) {
				lista_carre.add(r);
				mapa_carre.put(r._id, r);
			}
			else {
				throw new IllegalArgumentException("One Junction isn't exist");
			}
		}
		else {
			throw new IllegalArgumentException("The road already exist");
			
		}
		for(int i=0;i<lista_cruces.size();i++) {
			if(lista_cruces.get(i)==r.getFin()) {
				lista_cruces.get(i).addIncommingRoad(r);
			}
			
		}
		for(int i=0;i<lista_cruces.size();i++) {
			if(lista_cruces.get(i)==r.getini()) {
				lista_cruces.get(i).addOutGoingRoad(r);
			}
			
			
		}
		
		
	}
	void addVehicle(Vehicle v) {
		int i=0;
		boolean falso=false;
		if(mapa_vehi.get(v._id)==null) {
			/*while(i<v.getI().size()|| !falso) { // mirar esto
				if(lista_carre.contains(v.getI().get(i).getR(v.getI().get(i)))==false) {
					falso=true;
				}
				i++;
			}*/
			if(!falso) {
				lista_vehi.add(v);
				mapa_vehi.put(v._id, v);
				/*for(int j=0;j<v.getI().size();j++) {
					v.getI().get(i).enter(v);
				}*/
					
				
			}
		}
		else {
			throw new IllegalArgumentException("The vehicle already exist");
		}
		
	}
	public Junction getJunction(String id){
		if(mapa_cruces.get(id)==null)return null;
		else return mapa_cruces.get(id);
	}
	public Road getRoad(String id) {
		if(mapa_carre.get(id)==null)return null;
		else return mapa_carre.get(id);
		
		
	}
	public Vehicle getVehicle(String id) {
		if(mapa_vehi.get(id)==null)return null;
		else return mapa_vehi.get(id);
	}
	public List<Junction>getJunctions(){
		 return lista_cruces;
		
	}
	public List<Road>getRoads(){
		return lista_carre;
		
	}
	public List<Vehicle>getVehicles(){
		return lista_vehi;
		
	}
	void reset() {
		lista_cruces.clear();
		lista_carre.clear();
		lista_vehi.clear();
		mapa_cruces.clear();
		mapa_carre.clear();
		mapa_vehi.clear();
	}
	public JSONObject report(){
		JSONArray listV= new JSONArray();
		JSONArray listJ= new JSONArray();
		JSONArray listR= new JSONArray();
		JSONObject jo= new JSONObject();
		for(int i=0; i< lista_cruces.size(); i++) {
			listJ.put(lista_cruces.get(i).report());
		}
		jo.put("junctions", listJ);
		for(int i=0; i< lista_carre.size(); i++) {
			listR.put(lista_carre.get(i).report());
		}
		jo.put("roads" , listR);
		for(int i=0; i< lista_vehi.size(); i++) {
			listV.put(lista_vehi.get(i).report());
		}
		jo.put("vehicles",listV );
		
		return jo;
	}
}
