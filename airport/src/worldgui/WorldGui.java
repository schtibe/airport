package worldgui;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import air.SimWorld;

public class WorldGui extends BasicGame implements Runnable {
	
	private int mapHeight = 500;
	private int mapWidth = 500;
	private int mapScale = 150;
		
	public WorldGui(){
		// set nice title
		super("LP: " + SimWorld.getInstance().getSimulator().getMpiRank() +  ", AP: " + SimWorld.getInstance().getSimulator().getHomeAirport().getName());
	}
	
	public void run() {
		AppGameContainer app;
		try {
			app = new AppGameContainer(this);
			
			// we don't need that much FPS
			app.setTargetFrameRate(10);
			app.setShowFPS(false);

			app.setAlwaysRender(true);
			app.setUpdateOnlyWhenVisible(false);
			
			app.setMaximumLogicUpdateInterval(200);
			
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
	public void render(GameContainer container, Graphics g) throws SlickException {
		
		// display some status variables
		String safeTSStatus = "";
		
		if(SimWorld.getInstance().getLookaheadQueue().hasEmptyQueues()){
			safeTSStatus = "<none>";
		} else {
			safeTSStatus = "" + SimWorld.getInstance().getLookaheadQueue().getSmallestLookahead();
		}
		
		g.drawString(
				"LP: " + SimWorld.getInstance().getSimulator().getMpiRank() +  
				" CST: " + SimWorld.getInstance().getSimulator().getCurrentSimulationTime() + 
				" Safe TS: " + safeTSStatus,	
				10, 10);	
		
		// draw home airport
		air.Airport ap = SimWorld.getInstance().getSimulator().getHomeAirport();
		g.drawString("Airport: " + ap.getName(), 10, 30);
		
		g.setLineWidth(4);
		//g.setColor(new Color(255, 255, 255));
		g.drawLine(
				(float)getXPos(ap.getX1()), 
				(float)getYPos(ap.getY1()), 
				(float)getXPos(ap.getX2()), 
				(float)getYPos(ap.getY2())
				);
		//g.resetLineWidth();

//		g.drawString("X1:" + getXPos(ap.getX1()) + ",Y1:" + getYPos(ap.getY1()), 30, 30);
//		g.drawString("X2:" + getXPos(ap.getX2()) + ",Y2:" + getYPos(ap.getY2()), 30, 50);
		
	
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		// TODO Auto-generated method stub
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		synchronized (this) {
			this.notify();	
		}
	}
}
