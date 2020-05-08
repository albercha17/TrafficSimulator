package simulator.view;

import javax.swing.JDialog;

import simulator.control.Controller;

public class ChangeCO2ClassDialog extends JDialog{
	Controller ctr;
	ChangeCO2ClassDialog(Controller ctr){
		this.ctr=ctr;
	}
}
