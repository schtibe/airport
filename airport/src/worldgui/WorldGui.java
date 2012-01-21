package worldgui;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import air.SimWorld;

public class WorldGui extends BasicGame implements Runnable {
	
	public WorldGui(String airportName){
		super(airportName);
	}
	
	
	public void run() {
		AppGameContainer app;
		try {
			app = new AppGameContainer(this);
			app.setAlwaysRender(true);
			app.setTargetFrameRate(10);
			app.setUpdateOnlyWhenVisible(false);
			app.setMaximumLogicUpdateInterval(200);
			app.setShowFPS(false);
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
	public static double getXPos(double xVal) {
		return xVal  / SimWorld.getInstance().getWorldScale() - SimWorld.getInstance().getXOffset();
	}
	
	/**
	 * Calculate the Y position scaled and relative to the world
	 * @param yVal
	 * @return
	 */
	public static double getYPos(double yVal) {
		return SimWorld.getInstance().getWorldHeight() - 
				(yVal  / SimWorld.getInstance().getWorldScale() - SimWorld.getInstance().getYOffset());
	}



	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		g.drawString("CST: " + SimWorld.getInstance().getSimulator().getCurrentSimulationTime(), 10, 10);
		
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
