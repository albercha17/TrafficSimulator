package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
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
import javax.swing.SwingConstants;
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
	private JLabel Jt, Jc, Jv;
	private JSpinner ticks, contClass, vehicle;
	private String v;
	private int c, t;
	private JButton ok, salir;
	private List<Pair<String,Integer>> cs;
	
	ChangeCO2ClassDialog(Controller ctr, List<Vehicle> listV, int time){
		if(listV.size()!=0) {
			this.setLayout(new GridLayout(6,2,5,10));
			this.setSize(600,300);
			this.setLocationRelativeTo(null);
			this.setTitle("change Weather class");
		this.listV=listV;
		this.time=time;
		c=-1;
		t=-1;
		cs = new ArrayList<Pair<String,Integer>>();
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
		 ok = new JButton("Aceptar");
		ok.setVisible(true);
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				c=Integer.parseInt(contClass.getValue().toString());
				t=Integer.parseInt(ticks.getValue().toString());
				v=vehicle.getValue().toString();
				Pair<String,Integer> p= new Pair<String,Integer>(v,c);
				cs.add(p);
				t=t+time;
				NewSetContClassEvent e = new NewSetContClassEvent(t,cs);
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
		Jt=new JLabel("ticks: ", SwingConstants.CENTER);
		add(Jt);
		add(ticks);
	}
	
	public void vehicles() {
		String vehicles[]= new String[listV.size()];
		for(int i=0; i<vehicles.length; i++) {
			vehicles[i]=listV.get(i).getId();
		}
	    SpinnerModel model1 = new SpinnerListModel(vehicles);
		vehicle = new JSpinner(model1);
		vehicle.setToolTipText("Vehicle");
		vehicle.setMaximumSize(new Dimension(70, 70));
		Jv=new JLabel("V: ", SwingConstants.CENTER);
		add(Jv);
		add(vehicle);
	}
	public void contClass() {
		contClass = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
		contClass.setValue(0);
		contClass.setToolTipText("CO2 Class");
		contClass.setMaximumSize(new Dimension(70, 70));
		Jc=new JLabel("Cont: ", SwingConstants.CENTER);
		add(Jc);
		add(contClass);
}
}