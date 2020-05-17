package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
import javax.swing.SwingConstants;
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
	private String r, w;
	private int t, time;
	private JButton ok, salir;
	private List<Road> listR;
	private List<Pair<String,Weather>> ws;
	private JLabel Jt, Jr, Jw;
	
	ChangeWeatherDialog(Controller ctr, List<Road> listR, int time){
		if(!listR.isEmpty()) {
			this.setLayout(new GridLayout(6,2,5,10));
			this.setSize(600,300);
			this.setLocationRelativeTo(null);
			this.setTitle("change Weather class");
			t=-1;
		this.listR=listR;
		this.time=time;
		ws = new ArrayList<Pair<String,Weather>>();
      road();
      weather();
      ticks();
      aceptar(ctr);
      cancelar();
      this.setVisible(true);
		}
		else {
			JOptionPane.showMessageDialog(null, "The Road list is empty");
		}
	}
	public void aceptar(Controller ctr) {
		 ok = new JButton("Aceptar");
			ok.setVisible(true);
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				w=weather.getValue().toString();
				t=Integer.parseInt(ticks.getValue().toString());	
				r=road.getValue().toString();
					Weather wea= Weather.valueOf(w);
					Pair<String,Weather> p= new Pair<String,Weather>(r,wea);
					ws.add(p);
					t=t+time;
				SetWeatherEvent e = new SetWeatherEvent(t,ws);
				ctr.addEvent(e);
				dispose();
			}
		});
		add(ok);
	}
	public void cancelar() {
		salir= new JButton("Cancelar");
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
		ticks = new JSpinner(new SpinnerNumberModel(10, 0, 10000, 10));
		ticks.setToolTipText("Ticks");
		ticks.setMaximumSize(new Dimension(70, 70));
		Jt=new JLabel("Ticks: ", SwingConstants.LEFT);
		add(Jt);
		add(ticks);
	
	}
	public void road() {
		String roads[]= new String[listR.size()];
		for(int i=0; i<roads.length; i++) {
			roads[i]=listR.get(i).getId();
		}
	    SpinnerModel model1 = new SpinnerListModel(roads);
		road = new JSpinner(model1);
		road.setToolTipText("Road");
		road.setMaximumSize(new Dimension(70, 70));
		Jr=new JLabel("Road: ", SwingConstants.LEFT);
		add(Jr);
		add(road);
		road.setVisible(true);
	}
	public void weather() {
		String weathers[]={"SUNNY", "CLOUDY", "RAINY", "WINDY", "STORM"};
		SpinnerModel model1 = new SpinnerListModel(weathers);
		weather = new JSpinner(model1);
		weather.setToolTipText("Weather");
		weather.setMaximumSize(new Dimension(70, 70));
		Jw=new JLabel("Weather: ", SwingConstants.LEFT);
		weather.setVisible(true);
		add(Jw);
		add(weather);
		
	}
}