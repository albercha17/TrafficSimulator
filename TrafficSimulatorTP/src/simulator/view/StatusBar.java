package simulator.view;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver {
	JLabel time, Event;
	StatusBar(Controller ctr) {
		this.time = new JLabel("Time: " + 0);
		this.Event = new JLabel("Event added " + " ");
		initGUI();
		ctr.addObserver(this);
	}

	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));
		this.add(time);
		this.add(Box.createHorizontalStrut(40));
		this.add(Event);
	}
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		boolean x= false;
		this.time.setText(String.valueOf("Time: " + time));
		for(int i=0; i< events.size();i++) {
			if(time==events.get(i).getTime()&& !x) {
				Event.setText("Event added " + events.get(i).toString());
				x=true;
			}
		}
		if(!x) {

			Event.setText(" ");
		}
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		
		this.time.setText(String.valueOf("Time: " + time));
		Event.setText("Event added " + e.toString());
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.time.setText(String.valueOf("Time: " + time));
		Event.setText("Event added " + " ");
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
