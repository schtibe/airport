package air;

public class Event {
	// Event - Types
	public static final int READY_FOR_DEPARTURE = 0;	
	public static final int START_TAKE_OFF = 1;
	public static final int END_TAKE_OFF = 2;
	public static final int ARRIVAL = 3;
	public static final int START_LANDING = 4;
	public static final int END_LANDING = 5;
	public static final int ENTER_START_QUEUE = 6;
	public static final int ENTER_LANDING_QUEUE = 7;
	public static final int PROCESS_QUEUES = 8;
	public static final int LEAVE_MAP = 9;
	
	public String [] typeStrings = {
			"READY_FOR_DEPARTURE",	
			"START_TAKE_OFF",
			"END_TAKE_OFF",
			"ARRIVAL",
			"START_LANDING",
			"END_LANDING",
			"ENTER_START_QUEUE",
			"ENTER_LANDING_QUEUE",
			"PROCESS_QUEUES",
			"LEAVE_MAP"};	
	
	private long timeStamp;
	private int type;
	private EventHandler eventHandler;
	private Airport airPort;
	private Aircraft airCraft;
	
	
	public Airport getAirPort() {
		return airPort;
	}

	public void setAirPort(Airport airPort) {
		this.airPort = airPort;
	}

	public Aircraft getAirCraft() {
		return airCraft;
	}

	public void setAirCraft(Aircraft airCraft) {
		this.airCraft = airCraft;
	}

	public EventHandler getEventHandler() {
		return eventHandler;
	}

	public Event(int type, EventHandler h,  long time, Airport ap, Aircraft ac){
		this.type = type;
		eventHandler = h;
		timeStamp = time;
		airPort = ap;
		airCraft = ac;
	}	

	public int getType() {
		return type;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp){
		this.timeStamp = timeStamp;
	}

	public void testType(){
		if (type == READY_FOR_DEPARTURE ) return;
		else if (type == START_TAKE_OFF ) return;
		else if (type == END_TAKE_OFF ) return;
		else if (type == ARRIVAL ) return;
		else if (type == START_LANDING ) return;
		else if (type == END_LANDING ) return;
		else if (type == ENTER_START_QUEUE ) return;
		else if (type == ENTER_LANDING_QUEUE) return;
		else if (type == PROCESS_QUEUES) return;
		else if (type == LEAVE_MAP) return;
		else throw new RuntimeException("invalid event type: "+type);
	}
	
	@Override
	public String toString() {
		String s = "Event "+"T=" + 
		timeStamp +" "+typeStrings[type]+", eventHandler=";	
		if (eventHandler instanceof Aircraft) s = s +" Aircraft "+((Aircraft)eventHandler).getName();
		else if (eventHandler instanceof Airport) s = s +" Airport "+((Airport)eventHandler).getName();		
		else s= s + " OTHER "+eventHandler; 
		s=s+", ap: ";
		if (airPort!=null) s=s+airPort.getName();
		else s=s+"null";
		s=s+", ac: ";
		if (airCraft!=null) s=s+airCraft.getName();
		else s=s+"null";
		return s;
	}
}
