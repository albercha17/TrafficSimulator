package extra.jtable;

import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Road;

public class RoadsTableModel extends AbstractTableModel  implements TrafficSimObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private List<Road> rList;
	private String[] _colNames = { "ID", "Lenght", "Weather","Max Speed", "Speed","Total Cont", "Distance" };
	
	public RoadsTableModel(Controller ctr) {
		ctr.addObserver(this);
	}

	public void update() {
		// observar que si no refresco la tabla no se carga
		// La tabla es la represantación visual de una estructura de datos,
		// en este caso de un ArrayList, hay que notificar los cambios.
		
		// We need to notify changes, otherwise the table does not refresh.
		fireTableDataChanged();;		
	}
	
	public void setRoadsList(List<Road> events) {
		rList = events;
		update();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	//si no pongo esto no coge el nombre de las columnas
	//
	//this is for the column header
	@Override
	public String getColumnName(int col) {
		return _colNames[col];
	}

	@Override
	// método obligatorio, probad a quitarlo, no compila
	//
	// this is for the number of columns
	public int getColumnCount() {
		return _colNames.length;
	}

	@Override
	// método obligatorio
	//
	// the number of row, like those in the events list
	public int getRowCount() {
		return rList == null ? 0 : rList.size();
	}

	@Override
	// método obligatorio
	// así es como se va a cargar la tabla desde el ArrayList
	// el índice del arrayList es el número de fila pq en este ejemplo
	// quiero enumerarlos.
	//
	// returns the value of a particular cell 
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = rList.get(rowIndex).getId();
			break;
		case 1:
			s = Integer.toString(rList.get(rowIndex).getLongitud());
			break;
		case 2:
			s = rList.get(rowIndex).getWeather();
			break;
		case 3:
			s = Integer.toString(rList.get(rowIndex).getMaxS());
			break;
		case 4:
			s = Integer.toString(rList.get(rowIndex).getSpeedL());
			break;
		case 5:
			s = Integer.toString(rList.get(rowIndex).getTotalC());
			break;
		case 6:
			s = Integer.toString(rList.get(rowIndex).getCO2());
			break;
		}
		return s;
	}
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		setRoadsList(map.getRoads());
	}
	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		setRoadsList(map.getRoads());
		
	}
	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		
		
	}
	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		rList.clear();
		setRoadsList(map.getRoads());
	}
	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		setRoadsList(map.getRoads());
		
	}
	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
}

	