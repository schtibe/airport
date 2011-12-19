package worldgui;

import utils.Vector;
import air.SimWorld;

import com.trolltech.qt.gui.QGraphicsEllipseItem;
import com.trolltech.qt.gui.QGraphicsScene;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QStyleOptionGraphicsItem;
import com.trolltech.qt.gui.QWidget;

public class Aircraft extends QGraphicsEllipseItem implements WorldObject {

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
				return this.getFlightPosition();
			case air.Aircraft.ON_GROUND:
			case air.Aircraft.WAITING_FOR_TAKE_OFF:
				return this.getGroundPosition();
			case air.Aircraft.TAKING_OFF:
				Vector position = this.getTakeoffPosition();
				System.err.println(position);
				return position;
			case air.Aircraft.ON_HOLDING_LOOP:
				return this.getHoldingLoopPosition();
			case air.Aircraft.LANDING:
			case air.Aircraft.ARRIVING: 
				return this.getLandingPosition();
		}
		
		return new Vector(new double[] {500000, 200000});
	}
	
	private Vector getGroundPosition() {
		double xPos = this.aircraft.getCurrentAirPort().getX1();
		double yPos = this.aircraft.getCurrentAirPort().getY1();
		
		return new Vector(new double[] {xPos, yPos});
	}
	
	private Vector getTakeoffPosition() {
		double startX   = this.aircraft.getCurrentAirPort().getX1();
		double startY   = this.aircraft.getCurrentAirPort().getY1();
		double endX     = this.aircraft.getCurrentAirPort().getX2();
		double endY     = this.aircraft.getCurrentAirPort().getY2();
		
		long t0        = this.aircraft.getLastTime() * SimWorld.getInstance().getTimeScale();
		long t         = System.currentTimeMillis() - 
							SimWorld.getInstance().getSimulator().getRtStartTime();
		
		double speed     = this.getSpeed();
		double acc       = this.getAcceleration();
		double t_acc     = speed / acc;
		
		Vector n       = new Vector(new double[] {endX - startX, endY - startY});
		n = n.normalize();
		
		System.out.println("t: " + t + " diff " + t0 + t_acc);
		if (t < t0 + t_acc) {
			Vector dir = n.multiply(0.5 * Math.pow(t - t0, 2) * acc);
			System.out.println("Dir: " + dir);
			return new Vector(new double[] {
					startX + dir.getX(),
					startY + dir.getY()
			});
		} else {
			
		}
		
		// accelerate 
		
		// constant speed
		
		
		
		return this.getGroundPosition();		
	}
	
	private double getAcceleration() {
		double acc = this.aircraft.getMaxAcceleration();
		return acc / SimWorld.getInstance().getTimeScale();
	}
	
	private double getSpeed() {
		double speed = this.aircraft.getMaxSpeed();
		return speed / SimWorld.getInstance().getTimeScale();
	}

	private Vector getFlightPosition() {
		double lastX   = this.aircraft.getLastX();
		double lastY   = this.aircraft.getLastY();
		
		long t0        = this.aircraft.getLastTime();
		long t         = System.currentTimeMillis() - 
							SimWorld.getInstance().getSimulator().getRtStartTime();
		double speed   = this.getSpeed();
		double targetX = this.aircraft.getDestination().getX2();
		double targetY = this.aircraft.getDestination().getY2();
		
		Vector n       = new Vector(new double[] {targetX - lastX, targetY - lastY});
		n = n.normalize();
	
		Vector tail = n.multiply((t - t0 * SimWorld.getInstance().getTimeScale()) * (speed));
		return new Vector(new double[] {
				lastX + tail.getComponent(0),
				lastY + tail.getComponent(1)
		});
	}
	
	private Vector getHoldingLoopPosition() {
		
		// end of the runway of destination airport
		double lastX   = this.aircraft.getLastX();
		double lastY   = this.aircraft.getLastY();
		
		long t0        = this.aircraft.getLastTime();
		long t         = System.currentTimeMillis() - 
							SimWorld.getInstance().getSimulator().getRtStartTime();
		
		long speed     = this.aircraft.getMaxSpeed() / SimWorld.getInstance().getTimeScale();
		
		Vector runwayBegin = new Vector(
				new double[] {
						this.aircraft.getCurrentAirPort().getX1(),
						this.aircraft.getCurrentAirPort().getY1()
						}
				);
		
		Vector runwayEnd = new Vector(
				new double[] {
						this.aircraft.getCurrentAirPort().getX2(),
						this.aircraft.getCurrentAirPort().getY2()
						}
				);
		
		Vector runway = runwayEnd.sub(runwayBegin);
		Vector runwayRotated = runway.rotate(Math.PI /  2);
		
		// angular velocity
		double w = 0.008;

		Vector n = runwayBegin.add(runway.multiply(Math.cos((t - t0 * 
				SimWorld.getInstance().getTimeScale()) * w)));
		n = n.add(runwayRotated.multiply(Math.sin((t - t0 * 
				SimWorld.getInstance().getTimeScale()) * w)));	
		return n;		
	}
	
	private Vector getLandingPosition(){
		double xPos = this.aircraft.getCurrentAirPort().getX2();
		double yPos = this.aircraft.getCurrentAirPort().getY2();
		
		long t0        = this.aircraft.getLastTime();
		long t         = System.currentTimeMillis() - 
							SimWorld.getInstance().getSimulator().getRtStartTime();
		
		double runwayLength = this.aircraft.getCurrentAirPort().getRunwayLength();
		double maxSpeed     = this.aircraft.getMaxSpeed() / SimWorld.getInstance().getTimeScale();
		double breakAcc     = Math.pow(maxSpeed, 2) / (2 * runwayLength);
		
		Vector runwayBegin = new Vector(
				new double[] {
						this.aircraft.getCurrentAirPort().getX2(),
						this.aircraft.getCurrentAirPort().getY2()
						}
				);
		
		Vector runwayEnd = new Vector(
				new double[] {
						this.aircraft.getCurrentAirPort().getX1(),
						this.aircraft.getCurrentAirPort().getY1()
						}
				);
		
		Vector runway = runwayEnd.sub(runwayBegin);
		runway = runway.normalize();
		Vector head = runway.multiply((t - t0 * SimWorld.getInstance().getTimeScale()) * maxSpeed);
		Vector tail = runway.multiply(0.5 * Math.pow((t - t0 * SimWorld.getInstance().getTimeScale()), 2) * breakAcc);
		System.out.println("V_max: " + maxSpeed + ", break_acc: " + breakAcc);
		System.out.println("head: " + head + ", tail: "+tail);
		
		return new Vector(new double[] {
				xPos + head.getX() - tail.getX(),
				yPos + head.getY() - tail.getY() 
		});
	}
	
	
}
