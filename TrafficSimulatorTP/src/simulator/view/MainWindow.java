package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import extra.jtable.EventsTableModel;
import extra.jtable.JunctionsTableModel;
import extra.jtable.RoadsTableModel;
import extra.jtable.VehiclesTableModel;
import simulator.control.Controller;

public class MainWindow extends JFrame {
	private Controller _ctrl;
	public MainWindow(Controller ctrl) {
		super("Traffic Simulator");
		_ctrl = ctrl;
		initGUI();
}
	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		mainPanel.add(new ControlPanel(_ctrl), BorderLayout.PAGE_START);
		mainPanel.add(new StatusBar(_ctrl),BorderLayout.PAGE_END);
		JPanel viewsPanel = new JPanel(new GridLayout(1, 2));
		mainPanel.add(viewsPanel, BorderLayout.CENTER);
		JPanel tablesPanel = new JPanel();
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(tablesPanel);
		JPanel mapsPanel = new JPanel();
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(mapsPanel);
		Border _defaultBorder = BorderFactory.createLineBorder(Color.black, 1);
		// tables
		JPanel eventsView =createViewPanel(new JTable(new EventsTableModel(_ctrl)), "Events");
		eventsView.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Events", TitledBorder.LEFT,TitledBorder.TOP));
		eventsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(eventsView);
		
		JPanel vehiclesView =createViewPanel(new JTable(new VehiclesTableModel(_ctrl)), "Vehicles");
		vehiclesView.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Vehicles", TitledBorder.LEFT,TitledBorder.TOP));
		vehiclesView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(vehiclesView);
		
		JPanel junctionsView =createViewPanel(new JTable(new JunctionsTableModel(_ctrl)), "Junctions");
		junctionsView.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Junctions", TitledBorder.LEFT,TitledBorder.TOP));
		junctionsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(junctionsView);
		
		JPanel roadsView =createViewPanel(new JTable(new RoadsTableModel(_ctrl)), "Roads");
		roadsView.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Roads", TitledBorder.LEFT,TitledBorder.TOP));
		roadsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(roadsView);
		// TODO add other tables
		// ...
		// maps
		JPanel mapView = createViewPanel(new MapComponent(_ctrl), "Map");
		mapView.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Map", TitledBorder.LEFT,TitledBorder.TOP));
		mapView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapView);
		
		JPanel mapView2 = createViewPanel(new MapByRoadComponent(_ctrl), "Map");
		mapView2.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Map by Road", TitledBorder.LEFT,TitledBorder.TOP));
		mapView2.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapView2);
		// TODO add a map for MapByRoadComponent
		// ...
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		}
		private JPanel createViewPanel(JComponent c, String title) {
		JPanel p = new JPanel( new BorderLayout() );
		// TODO add a framed border to p with title
		p.add(new JScrollPane(c));
		return p;
		}
}

