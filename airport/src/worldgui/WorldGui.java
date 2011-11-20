package worldgui;

import java.util.Map.Entry;

import air.Config;
import air.SimWorld;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QGraphicsRectItem;
import com.trolltech.qt.gui.QGraphicsScene;
import com.trolltech.qt.gui.QGraphicsView;

public class WorldGui extends Thread {
	
	
	public void run() {

        QApplication.initialize(Config.getInstance().getArgs());
   	 
        QGraphicsScene scene = new QGraphicsScene();
        Config.getInstance().setScene(scene);
        //QGraphicsRectItem rect = scene.addRect(0,0,100,100);
        QGraphicsView view = new QGraphicsView(scene);
        view.show();
        
        this.initObjects();
 
        QApplication.exec();
	}
	
	private void initObjects() {
		for (Entry<String, air.Airport> a : SimWorld.getInstance().getAirports().entrySet()) {
			Airport airport = new Airport(a.getValue());
			airport.drawObject(Config.getInstance().getScene());
		}
	}
}
