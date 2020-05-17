package extra.jtable;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private List<Junction> jList;
	private String[] _colNames = { "id", "Green", "Queues" };

	
	public JunctionsTableModel(Controller ctr) {
		jList= new ArrayList<Junction>();
		ctr.addObserver(this);
	}

	public void update() {
		// observar que si no refresco la tabla no se carga
		// La tabla es la represantación visual de una estructura de datos,
		// en este caso de un ArrayList, hay que notificar los cambios.
		
		// We need to notify changes, otherwise the table does not refresh.
		fireTableDataChanged();;		
	}
	
	public void setJunctionsList(List<Junction> events) {
		jList = events;
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
		return jList == null ? 0 : jList.size();
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
			s = jList.get(rowIndex).getId();
			break;
		case 1:
			s = jList.get(rowIndex).getGreen();
			break;
		case 2:
			s = jList.get(rowIndex).getQueues();
			break;
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		setJunctionsList(map.getJunctions());
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		jList.clear();
		setJunctionsList(map.getJunctions());
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		setJunctionsList(map.getJunctions());
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
}
