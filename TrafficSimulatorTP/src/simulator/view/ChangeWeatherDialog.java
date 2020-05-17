package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.NewSetContClassEvent;
import simulator.model.Road;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog {
	static final int NumW = 5;
	private JSpinner ticks, weather, road;
	private JTextField t,r, w;
	private JButton ok, salir;
	private List<Road> listR;
	private List<Pair<String,Weather>> ws;
	
	ChangeWeatherDialog(Controller ctr, List<Road> listR, int time){
		if(!listR.isEmpty()) {
		this.listR=listR;
		 JDialog d = new JDialog(this, "change Weather class"); 
		 this.setLayout(new BorderLayout());
      road();
      weather();
      ticks();
      aceptar(ctr);
      this.setVisible(true);
		}
		else {
			JOptionPane.showMessageDialog(null, "The Road list is empty");
		}
	}
	public void aceptar(Controller ctr) {
		 ok = new JButton();
			ok.setVisible(true);
		if(t!=null|| r!=null||w!=null) {
			Weather weather= Weather.valueOf(w.getText());
			Pair<String,Weather> p= new Pair<String,Weather>(r.getText(),weather);
			ws.add(p);
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SetWeatherEvent e = new SetWeatherEvent(Integer.parseInt(t.getText()),ws);
				ctr.addEvent(e);
			}
		});
		add(ok);
		}
	}
	public void cancelar() {
		salir= new JButton();
		salir.setVisible(true);
		salir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				dispose();
			}
		});
		add(salir);
	}
	public void ticks() {
		ticks = new JSpinner(new SpinnerNumberModel(5, 0, 10000, 100));
		t=new JTextField();
		ticks.setToolTipText("Ticks");
		ticks.setMaximumSize(new Dimension(70, 70));
		ticks.setMinimumSize(new Dimension(70, 70));
		ticks.setValue(0);
		ticks.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// Ponemos el valor del JSpinner en el JTextField
			t.setText(ticks.getValue().toString());
			}
		
		});
		add(ticks);
		ticks.setVisible(true);
		add(t);
	}
	public void road() {
		String roads[]= new String[listR.size()];
		for(int i=0; i<roads.length; i++) {
			roads[i]=listR.get(i).getId();
		}
	    SpinnerModel model1 = new SpinnerListModel(roads);
		road = new JSpinner(model1);
		r=new JTextField();
		road.setToolTipText("Road");
		road.setMaximumSize(new Dimension(70, 70));
		road.setMinimumSize(new Dimension(70, 70));
		road.setValue(0);
		road.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// Ponemos el valor del JSpinner en el JTextField
			r.setText(road.getValue().toString());
			}
		
		});
		add(road);
		road.setVisible(true);
		add(r);
	}
	public void weather() {
		String weathers[]={"SUNNY", "CLOUDY", "RAINY", "WINDY", "STORM"};
		SpinnerModel model1 = new SpinnerListModel(weathers);
		weather = new JSpinner(model1);
		w=new JTextField();
		weather.setToolTipText("Weather");
		weather.setMaximumSize(new Dimension(70, 70));
		weather.setMinimumSize(new Dimension(70, 70));
		weather.setValue(0);
		weather.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// Ponemos el valor del JSpinner en el JTextField
			w.setText(weather.getValue().toString());
			}
		
		});
		add(weather);
		weather.setVisible(true);
		add(w);
	}
}