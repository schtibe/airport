package air;

import p2pmpi.mpi.MPI;
import mpi.MpiEvent;
import mpi.MpiMessage;
import utils.AirportLogger;


public class Aircraft implements EventHandler{
	// states
	public static final int ON_GROUND = 0;
	public static final int WAITING_FOR_TAKE_OFF = 1;
	public static final int TAKING_OFF = 2;
	public static final int ON_FLIGHT = 3;
	public static final int ARRIVING = 4;	
	public static final int ON_HOLDING_LOOP = 5;
	public static final int LANDING = 6;
	public static final String [] stateStrings = {"ON_GROUND","WAITING_FOR_TAKE_OFF",
		"TAKING_OFF","ON_FLIGHT","ARRIVING","ON_HOLDING_LOOP","LANDING"};

	
	private String name;
	private Airport currentAirPort;
	private double lastX;
	private double lastY;
	private long lastTime;	
	private int state;
	private Airport origin;
	private Airport destination;
	private FlightPlan flightPlan=new FlightPlan();
	private long maxSpeed=100;
	private long maxAcceleration=5;
	
	public Aircraft(String name,Airport ap){
		this.name = name;
		origin = ap;
		currentAirPort = ap;
		ap.subscribeAircraft(this);
		lastX = ap.getX1();
		lastY = ap.getY1();
		state = ON_GROUND;
	}
	
	public Airport getCurrentAirPort() {
		return currentAirPort;
	}
	public void setCurrentAirPort(Airport currentAirPort) {
		this.currentAirPort = currentAirPort;
	}
	public double getLastX() {
		return lastX;
	}
	public void setLastX(double lastX) {
		this.lastX = lastX;
	}
	public double getLastY() {
		return lastY;
	}
	public void setLastY(double lastY) {
		this.lastY = lastY;
	}
	public long getLastTime() {
		return lastTime;
	}
	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}
	public int getState() {
		synchronized (this) {
			return state;
		}
	}
	public void setState(int state) {
		synchronized (this) {
			this.state = state;
		}
	}
	public Airport getOrigin() {
		return origin;
	}
	public void setOrigin(Airport origin) {
		this.origin = origin;
	}
	public Airport getDestination() {
		return destination;
	}
	public void setDestination(Airport destination) {
		this.destination = destination;
	}
	public long getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(long maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	@Override
	public void processEvent(Event e, EventScheduler sched) {
		Aircraft ac = e.getAirCraft();
		Airport ap = e.getAirPort();
		
		if (e.getType()==Event.START_TAKE_OFF){
			ac.setState(Aircraft.TAKING_OFF);
			ac.setLastTime(e.getTimeStamp());
			// we assume a constant acceleration maxAcceleration
			long takeOffDuration = maxSpeed/maxAcceleration;
			// distance for the accelerating part:
			double dist = maxAcceleration*takeOffDuration*takeOffDuration/2.0;
			// the remaining part 
			double remainingDist = ap.getRunwayLength()-dist;
			// the remaining distance of the runway we have constant speed
			if (remainingDist > 0){
				takeOffDuration += remainingDist/maxSpeed;
			}
			else {
				throw new RuntimeException("runway too short!!");
			}
			// schedule next event
			Event eNew = new Event(Event.END_TAKE_OFF,ac,e.getTimeStamp()+takeOffDuration,ap,ac); // to do!
			sched.scheduleEvent(eNew);			
		}
		else if (e.getType()==Event.END_TAKE_OFF){
			ac.setState(ON_FLIGHT);
			ac.lastX = ap.getX2();
			ac.lastY = ap.getY2();
			ac.lastTime = e.getTimeStamp();
			ap.setRunWayFree(true);
			ap.unscribeAircraft(ac);
			long duration = (long) (ap.getDistanceTo(ac.destination)/ac.maxSpeed);
	
			Event e1 = new Event(Event.ARRIVAL,ac.getDestination(),e.getTimeStamp()+duration,ac.getDestination(),ac); // to do!
			sched.scheduleEvent(e1);
			Event e2 = new Event(Event.PROCESS_QUEUES,ap,e.getTimeStamp(),ap,null);
			sched.scheduleEvent(e2);	
			
			// if the target airport is not the same rank as this LP, send a message
			if (SimWorld.getInstance().getRankFromString(ac.getDestination().getName()) != MPI.COMM_WORLD.Rank()) {
				MpiMessage msg = new MpiMessage(
						e.getTimeStamp(), 
						MpiEvent.getDefaultLookAhead(), 
						ac.getName(), 
						ac.getCurrentAirPort().getName(), 
						ac.getDestination().getName()
				);
				AirportLogger.getLogger().debug("Put the arrival event in the send queue");
				SimWorld.getInstance().outgoingMessages.add(msg);
			}
		}
		else if (e.getType()==Event.START_LANDING){
			ac.setState(Aircraft.LANDING);
			ac.lastX = ap.getX2();
			ac.lastY = ap.getY2();
			ac.lastTime = e.getTimeStamp();	
			// we assume that the aircraft is landing with a constant negative acceleration
			long landingDuration =(long)( 2*destination.getRunwayLength()/maxSpeed);
			Event eNew = new Event(Event.END_LANDING,ac,e.getTimeStamp()+landingDuration,ap,ac); // to do!
			sched.scheduleEvent(eNew);			
		}
		
		else if (e.getType()==Event.END_LANDING){
			ac.setState(ON_GROUND);
			ac.lastX = ap.getX1();
			ac.lastY = ap.getY1();
			ac.lastTime = e.getTimeStamp();
			ap.setRunWayFree(true);
			// do we have another flight?
			if (ac.flightPlan.size()>0){
				Flight f = ac.getFlightPlan().removeNextFlight();
				ac.setDestination(f.getDestination());
				Event e2 = new Event(Event.READY_FOR_DEPARTURE,ap,e.getTimeStamp()+f.getTimeGap(),ap,ac);
				sched.scheduleEvent(e2);
			}
			Event e2 = new Event(Event.PROCESS_QUEUES,ap,e.getTimeStamp(),ap,null);
			sched.scheduleEvent(e2);			
		}
	}
	
	public void setMaxAcceleration(long maxAcceleration) {
		this.maxAcceleration = maxAcceleration;
	}
	public long getMaxAcceleration() {
		return maxAcceleration;
	}
	public void setFlightPlan(FlightPlan flightPlan) {
		this.flightPlan = flightPlan;
	}
	public FlightPlan getFlightPlan() {
		return flightPlan;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public String toString() {
	String dest = null;
	if (destination!=null) dest = destination.getName();
	else dest = null;
	return "Aircraft name=" + name + ", state=" + stateStrings[state] 
			+ "\n  lastX=" + lastX + ", lastY=" + lastY + ", lastTime=" + lastTime
			+ "\n  currentAirPort=" + currentAirPort.getName()
			+ "\n  origin=" + origin.getName() 
			+ "\n  destination=" + dest 
			+ "\n  maxSpeed=" + maxSpeed
			+ "\n  maxAcceleration=" + maxAcceleration; 
	}
}

