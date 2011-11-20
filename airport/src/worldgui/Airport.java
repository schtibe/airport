package worldgui;

import com.trolltech.qt.gui.QGraphicsScene;
import com.trolltech.qt.gui.QGraphicsSimpleTextItem;
import com.trolltech.qt.gui.QGraphicsTextItem;

public class Airport implements WorldObjects {
	air.Airport airport;
	
	public Airport(air.Airport airport) {
		this.airport = airport;
	}
	

	@Override
	public void drawObject(QGraphicsScene scene) {
		scene.addRect(
				this.airport.getX1() / 1000,
				this.airport.getY1() / 1000, 
				this.airport.getRunwayLength() / 1000, 
				20
		);
		
		QGraphicsSimpleTextItem text = scene.addSimpleText(this.airport.getName());
		text.setX(this.airport.getX1() / 1000);
		text.setY(this.airport.getY1() / 1000);
	}

}
