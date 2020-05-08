package simulator.view;

import javax.swing.JDialog;

import simulator.control.Controller;

public class ChangeWeatherDialog extends JDialog {
	Controller ctr;
	ChangeWeatherDialog(Controller ctr){
		this.ctr=ctr;
	}
}
