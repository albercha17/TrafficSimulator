package simulator.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ControlPanel extends JPanel implements TrafficSimObserver{
	private Controller ctr;
	private boolean _stopped= false;
	private JButton cargar, cambiarC, cambiarT, pausar, continuar, salir;
	private JToolBar jt;
	public ControlPanel(Controller ctr) {
		this.ctr=ctr;
		this.setLayout(new BorderLayout());
		jt= new JToolBar();
		cambiarTiempo();
		cambiarCont();
		cargar();
		pausar();
		continuar();
		salir();
		jt.setVisible(true);
		this.setVisible(true);
	}
	
	public void cambiarCont() {
		ImageIcon icono= new ImageIcon("resources/icons/co2class.png");
		cambiarC= new JButton();
		cambiarC.setIcon(icono);
		cambiarC.setVisible(true);
		cambiarC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new ChangeCO2ClassDialog(ctr);
			}
		});
		jt.add(cambiarC);
	}
	public void cambiarTiempo() {
		ImageIcon icono= new ImageIcon("resources/icons/weather.png");
		cambiarT= new JButton();
		cambiarT.setIcon(icono);
		cambiarT.setVisible(true);
		cambiarT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new ChangeWeatherDialog(ctr);
			}
		});
		jt.add(cambiarT);
	}
	public void cargar() {
		ImageIcon icono= new ImageIcon("resources/icons/open.png");
		cargar= new JButton();
		cargar.setIcon(icono);
		cargar.setVisible(true);
		cargar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc= new JFileChooser();
				int respuesta= fc.showOpenDialog(this);
					if(respuesta==JFileChooser.APPROVE_OPTION) {
						try {
						File file= fc.getSelectedFile();
						ctr.reset();
						InputStream input = new FileInputStream(file);
						ctr.loadEvents(input);
						
					}
						catch(Exception e) {
							JOptionPane.showMessageDialog(null, "Operación realizada correctamente");
						}
			}
			}});
		jt.add(cargar);
	}
	public void pausar() {
		ImageIcon icono= new ImageIcon("resources/icons/stop.png");
		pausar= new JButton();
		pausar.setIcon(icono);
		pausar.setVisible(true);
		pausar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		jt.add(pausar);
	}
	public void continuar() {
		ImageIcon icono= new ImageIcon("resources/icons/run.png");
		continuar= new JButton();
		continuar.setIcon(icono);
		continuar.setVisible(true);
		continuar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		jt.add(continuar);
	}
	public void salir() {
		ImageIcon icono= new ImageIcon("resources/icons/exit.png");
		salir= new JButton();
		salir.setIcon(icono);
		salir.setVisible(true);
		salir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int x= JOptionPane.showConfirmDialog(null, " DO YOU WANT TO EXIT?", " EXIT",JOptionPane.YES_NO_OPTION );
				if(x==0) System.exit(0);
			}
		});
		jt.add(salir);
	}
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
		try {
		ctrl.run(1);
		} catch (Exception e) {
		// TODO show error message
		_stopped = true;
		return;
		}
		SwingUtilities.invokeLater(new Runnable() {
		@Override
		public void run() {
		run_sim(n - 1);
		}
		});
		} else {
		enableToolBar(true);
		_stopped = true;
		}
		}
		private void stop() {
		_stopped = true;
		}

}
