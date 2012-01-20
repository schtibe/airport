package mpi;

import air.Aircraft;
import air.Airport;
import air.Event;
import air.EventScheduler;
import air.SimWorld;

public class MpiEvent {
	
	/**
	 * Events received from MPI are always new aircrafts, 
	 * we need to calculate the arrival date and add it to the 
	 * local event queue 
	 * 
	 * @param msg
	 */
	public static void createEventFromMessage(MpiMessage msg){
		// process only events, not null messages
		if(!msg.isEvent()){
			return;
		}		
		
		String aircraftName = msg.getAircraft();
		String fromAirportName = msg.getFromAirport();
		String toAirportName = msg.getToAirport();
		
		// simworld instance
		SimWorld world = SimWorld.getInstance();
		
		// event scheduler
		EventScheduler sched = world.getSimulator();
		
		// find airports
		Airport fromAp = world.getAirport(fromAirportName);
		Airport toAp = world.getAirport(toAirportName);
		
		// create new aircraft, set departing airport as from, but immediately 
		// remove it again
		Aircraft ac = new Aircraft(aircraftName, fromAp);
		ac.setState(Aircraft.ON_FLIGHT);
		ac.setLastX(fromAp.getX2());
		ac.setLastY(fromAp.getY2());
		ac.setLastTime(msg.getTimeStamp());
		ac.setDestination(toAp);
		fromAp.unscribeAircraft(ac);
		
		// add aircraft to world 
		world.addAircraft(ac);
				
		// add arrival event to the local airport
		long duration = (long) (fromAp.getDistanceTo(toAp)/ac.getMaxSpeed());
		
		Event e1 = new Event(
				Event.ARRIVAL,
				ac.getDestination(),
				msg.getTimeStamp() + duration,
				ac.getDestination(),
				ac
				); // to do!
		sched.scheduleEvent(e1);

		// process queue event
		Event e2 = new Event(
				Event.PROCESS_QUEUES,
				ac.getDestination(),
				msg.getTimeStamp() + duration - 1,  // TODO: really needed?
				ac.getDestination(),
				null);
		sched.scheduleEvent(e2);	
	}

	/**
	 * Return some default lookahead to constantly send null msgs
	 * 
	 * @todo This should somewhat be a reasonable value. Probably 
	 * use the start-time of an aircraft
	 */
	public static long getDefaultLookAhead() {
		return 10;
	}
}
