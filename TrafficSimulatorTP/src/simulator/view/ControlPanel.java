package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class ControlPanel extends JPanel implements TrafficSimObserver{
	private Controller ctr;
	private JTextField t;
	private List<Vehicle> listV;
	private List<Road> listR;
	private int time;
	private boolean _stopped= false;
	private JButton cargar, cambiarC, cambiarT, pausar, continuar, salir;
	private JToolBar jt;
	private JSpinner ticks;
	public ControlPanel(Controller ctr) {
		time=0;
		listV= new ArrayList<Vehicle>();
		listR= new ArrayList<Road>();
		this.ctr=ctr;
		this.setLayout(new BorderLayout());
		jt= new JToolBar();
		cambiarTiempo();
		cambiarCont();
		cargar();
		pausar();
		continuar();
		salir();
		ticks();
		jt.setVisible(true);
		
	}
	public void ticks() {
		ticks = new JSpinner(new SpinnerNumberModel(5, 0, 10000, 100));
		ticks.setToolTipText("number of ticks");
		ticks.setMaximumSize(new Dimension(70, 70));
		ticks.setMinimumSize(new Dimension(70, 70));
		ticks.setValue(0);
		ticks.setVisible(true);
		jt.add(ticks);
	}
	public void cambiarCont() {
		ImageIcon icono= new ImageIcon("resources/icons/co2class.png");
		cambiarC.setToolTipText("change the contamination");
		cambiarC= new JButton();
		cambiarC.setIcon(icono);
		cambiarC.setVisible(true);
		cambiarC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new ChangeCO2ClassDialog(ctr, listV, time);
			}
		});
		jt.add(cambiarC);
	}
	public void cambiarTiempo() {
		ImageIcon icono= new ImageIcon("resources/icons/weather.png");
		cambiarT.setToolTipText("change the weather");
		cambiarT= new JButton();
		cambiarT.setIcon(icono);
		cambiarT.setVisible(true);
		cambiarT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new ChangeWeatherDialog(ctr, listR, time);
			}
		});
		jt.add(cambiarT);
	}
	public void cargar() {
		ImageIcon icono= new ImageIcon("resources/icons/open.png");
		cargar.setToolTipText("load");
		cargar= new JButton();
		cargar.setIcon(icono);
		cargar.setVisible(true);
		cargar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc= new JFileChooser();
				int respuesta= fc.showOpenDialog(null);
					if(respuesta==JFileChooser.APPROVE_OPTION) {
						try {
						File file= fc.getSelectedFile();
						ctr.reset();
						InputStream input = new FileInputStream(file);
						ctr.loadEvents(input);
						
					}
						catch(Exception e) {
							JOptionPane.showMessageDialog(null, "well loaded");
						}
			}
			}});
		jt.add(cargar);
	}
	public void pausar() {
		ImageIcon icono= new ImageIcon("resources/icons/stop.png");
		pausar.setToolTipText("pause");
		pausar= new JButton();
		pausar.setIcon(icono);
		pausar.setVisible(true);
		int y=(int) ticks.getValue();
		pausar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				run_sim(y);
			}
		});
		jt.add(pausar);
	}
	public void continuar() {
		ImageIcon icono= new ImageIcon("resources/icons/run.png");
		continuar.setToolTipText("run");
		continuar= new JButton();
		continuar.setIcon(icono);
		continuar.setVisible(true);
		int y=Integer.parseInt(ticks.getValue().toString());
		continuar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_stopped= false;
				run_sim(y);
			}
		});
		jt.add(continuar);
	}
	public void salir() {
		ImageIcon icono= new ImageIcon("resources/icons/exit.png");
		salir.setToolTipText("exit");
		salir= new JButton();
		salir.setIcon(icono);
		salir.setVisible(true);
		salir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int x = JOptionPane.showOptionDialog(new JFrame(), "DO YOU WANT TO EXIT?", "EXIT",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (x == 0)System.exit(0);
			}
		});
		jt.add(salir);
	}
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		listR= map.getRoads();
		listV=map.getVehicles();
		this.time=time;
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		listR= map.getRoads();
		listV=map.getVehicles();
		this.time=time;
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		listR= map.getRoads();
		listV=map.getVehicles();
		this.time=time;
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		listR= map.getRoads();
		listV=map.getVehicles();
		this.time=time;
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		listR= map.getRoads();
		listV=map.getVehicles();
		this.time=time;
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
	public void enableToolBar(boolean x) {
		if(x) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				cambiarC.setEnabled(true);
				cambiarT.setEnabled(true);
				cargar.setEnabled(true);
				salir.setEnabled(true);
			}
			
		});
		}
		else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					cambiarC.setEnabled(false);
					cambiarT.setEnabled(false);
					cargar.setEnabled(false);
					salir.setEnabled(false);
				}
			});
		}
	}
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
		try {
		ctr.run(1);
		} catch (Exception e) {
		// TODO show error message
		_stopped = true;
		enableToolBar(true);
		return;
		}
		SwingUtilities.invokeLater(new Runnable() {
		@Override
		public void run() {
		run_sim(n - 1);
		try {
			Thread.sleep(50);
		}
		catch(InterruptedException e) {
			
		}
		}
		});
		} else {
		enableToolBar(true);
		_stopped = true;
		}
		}

}
