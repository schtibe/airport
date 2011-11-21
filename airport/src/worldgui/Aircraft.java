package worldgui;

import utils.Vector;
import air.SimWorld;

import com.trolltech.qt.core.QRectF;
import com.trolltech.qt.gui.QGraphicsEllipseItem;
import com.trolltech.qt.gui.QGraphicsScene;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QStyleOptionGraphicsItem;
import com.trolltech.qt.gui.QWidget;

public class Aircraft extends QGraphicsEllipseItem {

	private air.Aircraft aircraft;
	
	public Aircraft(air.Aircraft a) {
		super();
		this.aircraft = a;
		
		//this.setRect(this.x, this.y, 10, 10);
	}
	
	public void paint(QPainter p, QStyleOptionGraphicsItem s, QWidget w) {
		Vector pos = this.getPosition();
		
		double xPos = WorldGui.getXPos(pos.getX());
		double yPos = WorldGui.getYPos(pos.getY());
		this.setRect(xPos, yPos, 10, 10);
		p.drawEllipse((int)xPos, (int)yPos, 10, 10);
	}
	
	public void draw(QGraphicsScene scene) {
		Vector pos = this.getPosition();
		double xPos = WorldGui.getXPos(pos.getX());
		double yPos = WorldGui.getYPos(pos.getY());
		scene.addEllipse((int)xPos, (int)yPos, 10, 10);
	}

	protected Vector getPosition() {
		int state = this.aircraft.getState();
		
		switch (state) {
			case air.Aircraft.ON_FLIGHT:
				Vector position =  this.getFlightPosition();
				System.err.println(position);
				return position;
			case air.Aircraft.ON_GROUND:
			case air.Aircraft.WAITING_FOR_TAKE_OFF:
			case air.Aircraft.TAKING_OFF:
				return this.getGroundPosition();
		}
		
		return new Vector(new double[] {500000, 200000});
	}
	
	private Vector getGroundPosition() {
		double xPos = this.aircraft.getCurrentAirPort().getX1();
		double yPos = this.aircraft.getCurrentAirPort().getY1();
		
		return new Vector(new double[] {xPos, yPos});
	}

	protected Vector getFlightPosition() {
		double lastX   = this.aircraft.getLastX();
		double lastY   = this.aircraft.getLastY();
		long t         = SimWorld.getInstance().getSimulator().getCurrentSimulationTime();
		long t0        = this.aircraft.getLastTime();
		long speed     = this.aircraft.getMaxSpeed();
		double targetX = this.aircraft.getDestination().getX1();
		double targetY = this.aircraft.getDestination().getY1();
		
		Vector n       = new Vector(new double[] {targetX - lastX, targetY - lastY});
		n = n.normalize();
	
		Vector tail = n.multiply((t - t0) * (speed));
		return new Vector(new double[] {
				lastX + tail.getComponent(0),
				lastY + tail.getComponent(1)
		});
	}
}
