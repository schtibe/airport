package worldgui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import air.Config;
import air.SimWorld;

import com.trolltech.qt.core.QRectF;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QGraphicsScene;
import com.trolltech.qt.gui.QGraphicsView;

public class WorldGui extends Thread {
	QGraphicsScene scene;
	QGraphicsView view;
	private List<QRectF> aircrafts = new ArrayList<QRectF>();
	
	
	public void run() {
        QApplication.initialize(Config.getInstance().getArgs());
   	 
        this.scene = new QGraphicsScene(
        		10, 
        		10, 
        		Config.getInstance().getWorldWidth(), 
        		Config.getInstance().getWorldHeight()
        );
        Config.getInstance().setScene(this.scene);
        //QGraphicsRectItem rect = scene.addRect(0,0,100,100);
        this.view = new QGraphicsView(this.scene);
        view.show();
        this.initObjects();
        
        setTimer();
 
        QApplication.exec();
	}

	/**
	 * Create a timer that emits a signal to redraw the world
	 */
	private void setTimer() {
		QTimer repaint = new QTimer();        
        repaint.timeout.connect(this, "updateWorld()");
        repaint.start(100);
	}
	
	/**
	 * Redraw the scene
	 * 
	 * Passes a list of the aircrafts to redraw
	 */
	public void updateWorld() {
		this.view.updateScene(this.aircrafts);
	}
	
	/**
	 * Create the wrapper objects
	 */
	private void initObjects() {
		for (Entry<String, air.Airport> a : SimWorld.getInstance().getAirports().entrySet()) {
			Airport airport = new Airport(a.getValue());
			airport.draw(Config.getInstance().getScene());
		}
		
		for (Entry<String, air.Aircraft> a: SimWorld.getInstance().getAircrafts().entrySet()) {
			Aircraft aircraft = new Aircraft(a.getValue());
			this.scene.addItem(aircraft);
			this.aircrafts.add(aircraft.rect());
		}
	}
	
	/**
	 * Calculate the X position scaled and relative to the world
	 *
	 * @param xVal
	 * @return
	 */
	public static double getXPos(double xVal) {
		return xVal  / Config.getInstance().getWorldScale() - Config.getInstance().getXOffset();
	}
	
	/**
	 * Calculate the Y position scaled and relative to the world
	 * @param yVal
	 * @return
	 */
	public static double getYPos(double yVal) {
		return Config.getInstance().getWorldHeight() - 
				(yVal  / Config.getInstance().getWorldScale() - Config.getInstance().getYOffset());
	}
}
