package worldgui;

import utils.Vector;
import air.Aircraft;
import air.SimWorld;

public class AircraftPosition  {

	private air.Aircraft aircraft;
	
	public AircraftPosition() {	}

	public void setAircraft(Aircraft a){
		this.aircraft = a;
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
				//System.err.println(position);
				return position;
			case air.Aircraft.ON_HOLDING_LOOP:
				return this.getHoldingLoopPosition();
			case air.Aircraft.LANDING:
			case air.Aircraft.ARRIVING: 
				return this.getLandingPosition();
		}
		
		return this.getGroundPosition();
	}
	
	private Vector getGroundPosition() {
		double xPos = this.aircraft.getCurrentAirPort().getX1();
		double yPos = this.aircraft.getCurrentAirPort().getY1();
		
		return new Vector(new double[] {xPos, yPos});
	}
	
	private Vector getTakeoffPosition() {
		
		double xPos = this.aircraft.getCurrentAirPort().getX1();
		double yPos = this.aircraft.getCurrentAirPort().getY1();
		
		long t0        = this.aircraft.getLastTime();
		long t         = SimWorld.getInstance().getSimulator().getClock().getCurrentSimulationTime();
		
		double runwayLength = this.aircraft.getCurrentAirPort().getRunwayLength();
		double maxSpeed     = this.aircraft.getMaxSpeed();
		double breakAcc     = Math.pow(maxSpeed, 2) / (2 * runwayLength);
		
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
		runway = runway.normalize();
		Vector head = runway.multiply((t - t0) * maxSpeed);
		Vector tail = runway.multiply(0.5 * Math.pow((t - t0), 2) * -breakAcc);
		//System.out.println("V_max: " + maxSpeed + ", break_acc: " + breakAcc);
		//System.out.println("head: " + head + ", tail: "+tail);
		
		return new Vector(new double[] {
				xPos + head.getX() - tail.getX(),
				yPos + head.getY() - tail.getY() 
		});
			
	}
	
	private double getAcceleration() {
		return this.aircraft.getMaxAcceleration();
	}
	
	private double getSpeed() {
		return this.aircraft.getMaxSpeed();
	}

	private Vector getFlightPosition() {
		double lastX   = this.aircraft.getLastX();
		double lastY   = this.aircraft.getLastY();
		
		long t0        = this.aircraft.getLastTime();
		long t         = SimWorld.getInstance().getSimulator().getClock().getCurrentSimulationTime();
		double speed   = this.getSpeed();
		double targetX = this.aircraft.getDestination().getX2();
		double targetY = this.aircraft.getDestination().getY2();
		
		Vector n       = new Vector(new double[] {targetX - lastX, targetY - lastY});
		n = n.normalize();
	
		Vector tail = n.multiply((t - t0) * (speed));
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
		long t         = SimWorld.getInstance().getSimulator().getClock().getCurrentSimulationTime();
				
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
		double w = 0.02;

		Vector n = runwayBegin.add(runway.multiply(Math.cos((t - t0) * w)));
		n = n.add(runwayRotated.multiply(Math.sin((t - t0) * w)));	
		return n;
	}
	
	private Vector getLandingPosition(){
		double xPos = this.aircraft.getCurrentAirPort().getX2();
		double yPos = this.aircraft.getCurrentAirPort().getY2();
		
		long t0        = this.aircraft.getLastTime();
		long t         = SimWorld.getInstance().getSimulator().getClock().getCurrentSimulationTime();
		
		double runwayLength = this.aircraft.getCurrentAirPort().getRunwayLength();
		double maxSpeed     = this.aircraft.getMaxSpeed();
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
		Vector head = runway.multiply((t - t0) * maxSpeed);
		Vector tail = runway.multiply(0.5 * Math.pow((t - t0), 2) * breakAcc);
		//System.out.println("V_max: " + maxSpeed + ", break_acc: " + breakAcc);
		//System.out.println("head: " + head + ", tail: "+tail);
		
		return new Vector(new double[] {
				xPos + head.getX() - tail.getX(),
				yPos + head.getY() - tail.getY() 
		});
	}
	
	
}
