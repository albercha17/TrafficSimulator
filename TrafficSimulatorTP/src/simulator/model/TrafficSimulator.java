package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
// mirar el onError
public class TrafficSimulator implements Observable<TrafficSimObserver>{
 
    private RoadMap MapaCarreteras;
    private List<Event> ListaEventos;
    private int TiempoSimulacion;
    private List<TrafficSimObserver> listOb;

public TrafficSimulator() {
	TiempoSimulacion=0;
	MapaCarreteras=new RoadMap();
	ListaEventos= new ArrayList<Event>();
	listOb= new ArrayList<TrafficSimObserver>();
}
public RoadMap getM() {
	return MapaCarreteras;
}
public void addEvent(Event e) {
	ListaEventos.add(e);
	for(TrafficSimObserver i : listOb) {
		i.onEventAdded(MapaCarreteras, ListaEventos, e, TiempoSimulacion);
	}
}

public void advance() {
	TiempoSimulacion++;
	for(TrafficSimObserver i : listOb) {
		i.onAdvanceStart(MapaCarreteras, ListaEventos, TiempoSimulacion);
	}
	for(int i=0; i<ListaEventos.size();i++) {
		if(ListaEventos.get(i).getTime()==TiempoSimulacion) {
			ListaEventos.get(i).execute(MapaCarreteras);
		}
	}
	for(int j=0; j<ListaEventos.size();j++) {
		if(ListaEventos.get(j).getTime()<=TiempoSimulacion) {
			ListaEventos.remove(j);
		}
		
	}
	for(int x=0; x<MapaCarreteras.getJunctions().size();x++) {
		MapaCarreteras.getJunctions().get(x).advance(TiempoSimulacion);
	}
	for(int y=0; y<MapaCarreteras.getRoads().size();y++) {
		MapaCarreteras.getRoads().get(y).advance(TiempoSimulacion);
	}
	for(TrafficSimObserver i : listOb) {
		i.onAdvanceEnd(MapaCarreteras, ListaEventos, TiempoSimulacion);
	}
}

public void reset() {
	TiempoSimulacion=0;
	MapaCarreteras.reset();
	ListaEventos.clear();
	for(TrafficSimObserver i : listOb) {
		i.onReset(MapaCarreteras, ListaEventos, TiempoSimulacion);
	}
}

public JSONObject report() {
	JSONObject jo= new JSONObject();
	jo.put("time", TiempoSimulacion);
	jo.put("state", MapaCarreteras.report());
return jo;
}
@Override
public void addObserver(TrafficSimObserver o) {
	listOb.add(o);
	for(TrafficSimObserver i : listOb) {
		i.onRegister(MapaCarreteras, ListaEventos, TiempoSimulacion);
	}
	
}
@Override
public void removeObserver(TrafficSimObserver o) {
	listOb.remove(o);
}
}