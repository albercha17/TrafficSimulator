package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
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
import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog {
	private int NumContMax= 10;
	private List<Vehicle> listV;
	private int time;
	private JSpinner ticks, contClass, vehicle;
	private JTextField t, c, v;
	private JButton ok, salir;
	private List<Pair<String,Integer>> cs;
	
	ChangeCO2ClassDialog(Controller ctr, List<Vehicle> listV, int time){
		if(!listV.isEmpty()) {
		this.listV=listV;
		this.time=time;
		cs = new ArrayList<Pair<String,Integer>>();
		 JDialog d = new JDialog(this, "change Weather class"); 
		 this.setLayout(new BorderLayout());
      vehicles();
      contClass();
      ticks();
      aceptar(ctr);
      cancelar();
      this.setVisible(true);
		}
		else {
			JOptionPane.showMessageDialog(null, "The Vehicle list is empty");
		}
	}
	public void aceptar(Controller ctr) {
		 ok = new JButton();
		ok.setVisible(true);
		if(t!=null|| c!=null||v!=null) {
		Pair<String,Integer> p= new Pair<String,Integer>(v.getText(),Integer.parseInt(c.getText()));
		cs.add(p);
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewSetContClassEvent e = new NewSetContClassEvent(Integer.parseInt(t.getText()),cs);
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
		add(t);
		add(ticks);
	}
	
	public void vehicles() {
		String vehicles[]= new String[listV.size()];
		for(int i=0; i<vehicles.length; i++) {
			vehicles[i]=listV.get(i).getId();
		}
	    SpinnerModel model1 = new SpinnerListModel(vehicles);
		vehicle = new JSpinner(model1);
		v=new JTextField();
		vehicle.setToolTipText("Vehicle");
		vehicle.setMaximumSize(new Dimension(70, 70));
		vehicle.setMinimumSize(new Dimension(70, 70));
		vehicle.setValue(0);
		vehicle.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// Ponemos el valor del JSpinner en el JTextField
			v.setText(vehicle.getValue().toString());
			}
		
		});
		add(vehicle);
		add(v);
	}
	public void contClass() {
		contClass = new JSpinner(new SpinnerNumberModel(5, 0, 10, 100));
		c=new JTextField();
		contClass.setToolTipText("CO2 Class");
		contClass.setMaximumSize(new Dimension(70, 70));
		contClass.setMinimumSize(new Dimension(70, 70));
		contClass.setValue(0);
		contClass.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// Ponemos el valor del JSpinner en el JTextField
			c.setText(contClass.getValue().toString());
			}
		
		});
		add(c);
		add(contClass);
	}
}