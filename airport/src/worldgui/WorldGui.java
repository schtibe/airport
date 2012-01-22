package worldgui;
import java.util.Iterator;
import java.util.Map.Entry;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Ellipse;

import utils.Vector;
import air.Aircraft;
import air.Airport;
import air.SimWorld;

public class WorldGui extends BasicGame implements Runnable {
	
	private int mapHeight = 500;
	private int mapWidth = 500;
	private int mapScale = 150;
	
	private AircraftPosition acPos = new AircraftPosition(); 
	
	private Airport homeAirport;
	
	private long lastUpdateSimulationTime = 0;
	
	private String statusLine = "";
	private String airportStatus = "";
	
	private UnicodeFont aircraftFont; 
		
	public WorldGui(){
		// set nice title
		super("LP: " + SimWorld.getInstance().getSimulator().getMpiRank() +  ", AP: " + SimWorld.getInstance().getSimulator().getHomeAirport().getName());
	}
	
	public void run() {
		AppGameContainer app;
		try {
			app = new AppGameContainer(this);
			
			// we don't need that much FPS
			app.setTargetFrameRate(15);
			app.setShowFPS(false);

			app.setAlwaysRender(true);
			app.setUpdateOnlyWhenVisible(false);
			
			app.setMinimumLogicUpdateInterval(50);
			//app.setMaximumLogicUpdateInterval(200);
			
			app.setDisplayMode(mapWidth, mapHeight, false);
			app.start();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Calculate the X position scaled and relative to the world
	 *
	 * @param xVal
	 * @return
	 */
	public double getXPos(double xVal) {
		// home airport's end of runway is the center of the map
		double centerX = mapWidth / 2;
		double scaledX = SimWorld.getInstance().getSimulator().getHomeAirport().getX2() / mapScale;
		double deltaX = scaledX - centerX;
		return xVal  / mapScale - deltaX;
	}
	
	/**
	 * Calculate the Y position scaled and relative to the world
	 * @param yVal
	 * @return
	 */
	public double getYPos(double yVal) {
		// home airport's end of runway is the center of the map
		double centerY = mapHeight / 2;
		double scaledY = SimWorld.getInstance().getSimulator().getHomeAirport().getY2() / mapScale;
		double deltaY = scaledY - centerY;
		return mapHeight - (yVal  / mapScale - deltaY);
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		
		// we don't need to update anything if simulation time didn't advance
		if(lastUpdateSimulationTime >= SimWorld.getInstance().getSimulator().getClock().getCurrentSimulationTime()){
			return;
		}

		lastUpdateSimulationTime = SimWorld.getInstance().getSimulator().getClock().getCurrentSimulationTime();
		
		// display some status variables
		String safeTSStatus = "";
		
		if(SimWorld.getInstance().getLookaheadQueue().hasEmptyQueues()){
			safeTSStatus = "<none>";
		} else {
			safeTSStatus = "" + SimWorld.getInstance().getLookaheadQueue().getSmallestLookahead();
		}
		
		statusLine = "LP: " + SimWorld.getInstance().getSimulator().getMpiRank() +  
				" CST: " + SimWorld.getInstance().getSimulator().getCurrentSimulationTime() + 
				" Safe TS: " + safeTSStatus;
				
	}
	
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		
		// 
		g.drawString(statusLine, 10, 10);
		g.drawString(airportStatus, 10, 30);
			
		
		g.setColor(new Color(200, 200, 200));
		g.setLineWidth(5);
		g.drawLine(
				(float)getXPos(homeAirport.getX1()), 
				(float)getYPos(homeAirport.getY1()), 
				(float)getXPos(homeAirport.getX2()), 
				(float)getYPos(homeAirport.getY2())
				);
		g.resetLineWidth();
		g.setColor(new Color(255, 255, 255));

//		g.drawString("X1:" + getXPos(ap.getX1()) + ",Y1:" + getYPos(ap.getY1()), 30, 30);
//		g.drawString("X2:" + getXPos(ap.getX2()) + ",Y2:" + getYPos(ap.getY2()), 30, 50);
		
		// draw airplanes
		g.setColor(new Color(255, 0, 0));
		
		// creates new Ellipse instance for each render run 
		// TODO this is pretty inefficient and unclean... but works
		Iterator<Entry<String, Aircraft>> itr = SimWorld.getInstance().getAircrafts().entrySet().iterator();
		
		float x, y;
		//g.setFont(aircraftFont);
		
		while (itr.hasNext()) {
		    Aircraft ac = itr.next().getValue();
			
		    if(ac.getState() == Aircraft.ON_GROUND || ac.getState() == Aircraft.WAITING_FOR_TAKE_OFF){
		    	continue;		    	
		    }
		    
			acPos.setAircraft(ac);
		    Vector v = acPos.getPosition();
		    x = (float)getXPos(v.getX());
		    y = (float)getYPos(v.getY());
		    g.draw(new Ellipse(x-2, y-2, 5, 5));
		    
		    // display aircraft name, but not in holding loop, it gets messy
		    if(ac.getState() != Aircraft.ON_HOLDING_LOOP){		    
		    	g.drawString(ac.getName(), x, y);
		    }
		}
		
		g.setColor(new Color(255, 255, 255));
		g.resetFont();
		
	
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		
		// init home airport
		homeAirport = SimWorld.getInstance().getSimulator().getHomeAirport();
		airportStatus = "Airport: " + homeAirport.getName();
		
//		Font font = new Font("Arial", Font.BOLD, 10);
//		aircraftFont = new UnicodeFont(font, font.getSize(), font.isBold(), font.isItalic());

				
		synchronized (this) {
			this.notify();	
		}
	}
}
