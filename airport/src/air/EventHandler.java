package air;

/**
 * @author ps
 * Processes events and eventually schedules new events 
 */
public interface EventHandler {
	public void processEvent(Event e, EventScheduler s);// new Event are scheduled by 's'
}
