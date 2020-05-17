package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class MapByRoadComponent extends JPanel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;
	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;

	private RoadMap _map;

	private Image _car;

	MapByRoadComponent(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
		setPreferredSize(new Dimension(300,200));
	}

	private void initGUI() {
		_car = loadImage("car_front.png");
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			updatePrefferedSize();
			drawMap(g);
		}
	}

	private void drawMap(Graphics g) {
		drawRoads(g);
	}

	private void drawRoads(Graphics g) {
		int i=0;
		for (Road r : _map.getRoads()) {

			// donde ​x1=50​, ​x2=getWidth()-100​ y ​y=(i+1)*50​
			int x1 = 50;
			int y = (i+1)*100;
			int x2 = getWidth()-100;
			// choose a color for the arrow depending on the traffic light of the road
			Color arrowColor = _RED_LIGHT_COLOR;
			int idx = r.getFin().getGreenLightIndex();
			if (idx != -1 && r.equals(r.getFin().getInRoads().get(idx))) {
				arrowColor = _GREEN_LIGHT_COLOR;
			}

			// choose a color for the road depending on the total contamination, the darker
			// the
			// more contaminated (wrt its co2 limit)
			int roadColorValue = 200 - (int) (200.0 * Math.min(1.0, (double) r.getTotalC() / (1.0 + (double) r.getCO2())));
			Color roadColor = new Color(roadColorValue, roadColorValue, roadColorValue);

			// draw line from (x1,y1) to (x2,y2) with arrow of color arrowColor and line of
			// color roadColor. The size of the arrow is 15px length and 5 px width
			
			g.drawLine(x1, y, x2, y);
			g.setColor(Color.BLUE);
			g.fillOval(x1, y, 10, 10);
			g.setColor(arrowColor);
			g.fillOval(x2, y, 10, 10);
			g.setColor(Color.BLUE);
			// crear vehiculo
			for(Vehicle v : r.getV()) {
				
				int x=x1 + (int) ((x2 - x1) * ((double) v.getL() / (double) r.getLongitud()));	
				g.drawString(v.getId(), x, y - 6);
				ImageIcon Img= new ImageIcon("resources/icons/car.png");
				g.drawImage(Img.getImage(), x, y,16, 16, null);
				
				 
			}
			g.drawString(r.getId(), x1-20, y);
			g.drawString(r.getini().getId(), x1, y-20);
			g.drawString(r.getFin().getId(), x2, y-20);
			
			// imagen segun el tiempo
			ImageIcon Img2 = null;
			if(r.getWeather()=="SUNNY")Img2= new ImageIcon("resources/icons/sun.png");
			else if(r.getWeather()=="CLOUDY")Img2= new ImageIcon("resources/icons/cloud.png");
			else if(r.getWeather()=="RAINY")Img2= new ImageIcon("resources/icons/rain.png"); 
			else if(r.getWeather()=="WINDY")Img2= new ImageIcon("resources/icons/wind.png");
			else if(r.getWeather()=="STORM")Img2= new ImageIcon("resources/icons/storm.png"); 
			g.drawImage(Img2.getImage(), x2+6, y,32, 32, null);
			  
			
			// imagen segun la contaminacion
			ImageIcon Img3 = null;
			int c = ( int ) Math.floor(Math.min(( double ) r.getTotalC()/(1.0 + ( double ) r.getCO2()),1.0) / 0.19);
			if(c==0) Img3= new ImageIcon("resources/icons/cont_0.png"); 
			else if(c==1) Img3= new ImageIcon("resources/icons/cont_1.png");  
			else if(c==2) Img3= new ImageIcon("resources/icons/cont_2.png");  
			else if(c==3)Img3= new ImageIcon("resources/icons/cont_3.png"); 
			else if(c==4)Img3= new ImageIcon("resources/icons/cont_4.png");  
			else if(c==5)Img3= new ImageIcon("resources/icons/cont_5.png"); 
			g.drawImage(Img3.getImage(), x2+40, y,32, 32, null);
			
			i++;
		}

	}


	
	private void updatePrefferedSize() {
		int maxW = 200;
		int maxH = 200;
		for (Junction j : _map.getJunctions()) {
			maxW = Math.max(maxW, j.getX());
			maxH = Math.max(maxH, j.getY());
		}
		maxW += 20;
		maxH += 20;
		setPreferredSize(new Dimension(maxW, maxH));
		setSize(new Dimension(maxW, maxH));
	}

	// This method draws a line from (x1,y1) to (x2,y2) with an arrow.
	// The arrow is of height h and width w.
	// The last two arguments are the colors of the arrow and the line
	
	// loads an image from a file
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}

	public void update(RoadMap map) {
		_map = map;
		repaint();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {
	}

}
