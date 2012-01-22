package air;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import mpi.MpiMessage;
import mpi.RecvThread;
import mpi.SendThread;


/**
 * SimWorld is a container class with airports and aircrafts whith unique name 
 * The class allows to access the simulation objects by name 
 * @author ps
 *
 */
public class SimWorld {
	private ConcurrentHashMap<String,Airport> airports = new ConcurrentHashMap<String,Airport>();
	private ConcurrentHashMap<String,Aircraft> aircrafts = new ConcurrentHashMap<String,Aircraft>();
	static private SimWorld instance = new SimWorld(); 
	
	private Simulator simulator;
	
	private LookaheadQueue lookaheadQueue;
	
	public LookaheadQueue getLookaheadQueue() {
		return lookaheadQueue;
	}

	public void setLookaheadQueue(LookaheadQueue lookaheadQueue) {
		this.lookaheadQueue = lookaheadQueue;
	}
	
	public void addAirport(Airport ap){
		if (airports.containsKey(ap.getName())) throw new RuntimeException("Duplicate airport name");
		airports.put(ap.getName(), ap);
	}
	public void addAircraft(Aircraft ac){
		if (aircrafts.containsKey(ac.getName())) throw new RuntimeException("Duplicate aircraft name");
		aircrafts.put(ac.getName(),ac);
	}
	public Airport getAirport(String name){
		return airports.get(name);
	}
	
	public Aircraft getAircraft(String name){
		return aircrafts.get(name);
	}
	
	public void removeAircraft(Aircraft ac) {
		aircrafts.remove(ac.getName());
	}
	
	public static SimWorld getInstance(){	
		return instance;
	}
	
	public ConcurrentHashMap<String, Airport> getAirports() {
		return this.airports;
	}
	
	public synchronized ConcurrentHashMap<String, Aircraft> getAircrafts() {
		return this.aircrafts;
	}
	
	public void setSimulator(Simulator s) {
		this.simulator = s;
	}
	
	public Simulator getSimulator() {
		return this.simulator;
	}

	@Override
	public String toString() {
		return "SimWorld [airports=" + airports + ", aircrafts=" + aircrafts
				+ "]";
	}
	
//	long timeScale = 100;
//
	/**
	 * @deprecated
	 */
	public long getTimeScale() {
		return getSimulator().getClock().getScaleFactor();
	}
//
//	public void setTimeScale(long timeScale) {
//		this.timeScale = timeScale;
//	}
	
	String[] args;
	public void setArgs(String[] args) {
		this.args = args;
	}
	
	public String[] getArgs() {
		return this.args;
	}
	
	public int getRankFromString(String airport) {
		return this.airports.get(airport).getRank();
	}
	
	public Queue<MpiMessage> outgoingMessages = new LinkedList<MpiMessage>();
	
	private RecvThread recvThread;
	
	public RecvThread getRecvThread() {
		return recvThread;
	}

	public void setRecvThread(RecvThread recvThread) {
		this.recvThread = recvThread;
	}

	public SendThread getSendThread() {
		return sendThread;
	}

	public void setSendThread(SendThread sendThread) {
		this.sendThread = sendThread;
	}

	private SendThread sendThread;
}
