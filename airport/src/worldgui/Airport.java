package worldgui;

import air.SimWorld;

import com.trolltech.qt.gui.QGraphicsScene;
import com.trolltech.qt.gui.QGraphicsSimpleTextItem;

public class Airport implements WorldObject {
	air.Airport airport;
	
	public Airport(air.Airport airport) {
		this.airport = airport;
	}

	public void draw(QGraphicsScene scene) {
		SimWorld confInst = SimWorld.getInstance();
		/*
		scene.addRect(
				WorldGui.getXPos(this.airport.getX1()),
				WorldGui.getYPos(this.airport.getY1()), 
				this.airport.getRunwayLength() / confInst.getWorldScale(), 
				20
		);
		*/
		
		QGraphicsSimpleTextItem text = scene.addSimpleText(this.airport.getName());
		text.setX(WorldGui.getXPos(this.airport.getX1()));
		text.setY(WorldGui.getYPos(this.airport.getY1()));
	}

}
