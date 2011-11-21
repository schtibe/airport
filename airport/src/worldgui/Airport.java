package worldgui;

import air.Config;

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
				this.airport.getX1() / Config.getInstance().getWorldScale(),
				this.airport.getY1() / Config.getInstance().getWorldScale(), 
				this.airport.getRunwayLength() / Config.getInstance().getWorldScale(), 
				20
		);
		
		QGraphicsSimpleTextItem text = scene.addSimpleText(this.airport.getName());
		text.setX(this.airport.getX1() / Config.getInstance().getWorldScale());
		text.setY(this.airport.getY1() / Config.getInstance().getWorldScale());
	}

}
