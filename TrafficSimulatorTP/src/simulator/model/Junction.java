package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {
	private List<Road> lista_carre;
	private Map<Junction,Road> mapa_carre;
	private List<List<Vehicle >> lista_colas;
	private int verde;
	private int ultimo_verde;
	private LightSwitchingStrategy Ecambio;
	private DequeuingStrategy Eextraer;
	private int CoorX;
	private int CoorY;
	
	public Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
        super(id);
        lista_carre= new ArrayList<Road>();
         mapa_carre= new HashMap<Junction,Road>();
         lista_colas= new ArrayList<List<Vehicle >>();
         verde=-1;
         ultimo_verde=0;
            if(lsStrategy==null || dqStrategy==null) {
                throw new IllegalArgumentException("Strategy can't be null");
            }
            else {
                Ecambio=lsStrategy;
                Eextraer=dqStrategy;
                if(xCoor<0 || yCoor<0) {
                    throw new IllegalArgumentException("Coordenatis can't be negative");
                }
                else {
                	CoorX=xCoor;
                	CoorY=yCoor;
                }
            }
            }
	public void addIncommingRoad(Road r) {
		if(r.getFin()==this) {
		lista_carre.add(r);
		lista_colas.add(new LinkedList<Vehicle>());
		}
		else {
			throw new IllegalArgumentException("This road isn't a incomming road");
		}
		
	}
	protected Road getR(Junction j) {
		Road r= mapa_carre.get(j);
		 return r;
	}
	public void addOutGoingRoad(Road r){
		if(mapa_carre.get(r.getFin())==null||r.getini()==this) {
			 mapa_carre.put(r.getFin(), r);
		}
		else {
			throw new IllegalArgumentException("This road can't go to this junction.");
		}
		
	}
	void enter(Vehicle v) {
		for(int i=0; i<lista_carre.size();i++) {
			if(lista_carre.get(i)==v.getR()) {
				lista_colas.get(i).add(v);
			}
		}
		
	}
	public Road roadTo(Junction j) {
		return mapa_carre.get(j);
	}
	@Override
	void advance(int time) {
			if(verde!=-1) {
				if(lista_colas.get(verde).size()>0) {
				List <Vehicle> listV=Eextraer.dequeue(lista_colas.get(verde));
				while(listV.size()>0) {
					listV.get(0).moveToNextRoad();
					for(int i=0; i<lista_colas.get(verde).size();i++) {
						if(lista_colas.get(verde).get(i).getId()==listV.get(0).getId()) {
							lista_colas.get(verde).remove(i);
						}
					}
					listV.remove(0);
				}
			}
			}
		if(verde!=Ecambio.chooseNextGreen(lista_carre, lista_colas, verde, ultimo_verde, time)) {
			int x=Ecambio.chooseNextGreen(lista_carre, lista_colas, verde, ultimo_verde, time);
			verde= x;
			ultimo_verde=time; 
		}
	}
	@Override
	public JSONObject report() {
		JSONObject jo= new JSONObject();
		JSONArray listT= new JSONArray();
		jo.put("id", _id);
		if(verde==-1) {
			jo.put("green", "none");
		}
		else {
			jo.put("green", lista_carre.get(verde).getId());
		}
		if(lista_carre.size()>0) {
		for(int i=0;i<lista_carre.size();i++) {
			JSONObject jo2= new JSONObject();
			JSONArray listV= new JSONArray();
			jo2.put("road", lista_carre.get(i).getId());
			for(Vehicle j: lista_colas.get(i)) {
				listV.put(j.getId());
			}
			jo2.put("vehicles", listV);
		    listT.put(jo2);
		}
		}
		jo.put("queues", listT);
		return jo;
}
}
