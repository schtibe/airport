package mpi;

import p2pmpi.mpi.MPI;
import utils.AirportLogger;
import air.SimWorld;
import air.Simulator;

public class SendThread extends Thread {
	/**
	 * The last lookahead value that was used
	 */
	long lookAhead = 0;
	
	long defaultLookAhead = MpiEvent.getDefaultLookAhead();
	
	Simulator simulator;
	
	public SendThread(Simulator simulator) {
		this.simulator = simulator;
	}
	
	public void run() {
		while(true) {
			MpiMessage data = SimWorld.getInstance().outgoingMessages.poll();
			if (data != null) {
				this.sendMessage(data);
			} else {
				long time = simulator.getClock().getCurrentSimulationTime();
				if (time >= this.lookAhead) {
					// time to send another null message
					this.sendNullMessage(time, this.defaultLookAhead, -1);
				}
			}
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * Send a event message to a certain other process 
	 * @param data
	 */
	private void sendMessage(MpiMessage data) {
		int dest = SimWorld.getInstance().getRankFromString(data.getToAirport());
		
		MpiMessage[] dataBuf = new MpiMessage[1];
		dataBuf[0] = data;
		MPI.COMM_WORLD.Send(dataBuf, 0, 1, MPI.OBJECT, dest, 1);
		
		this.sendNullMessage(data.getTimeStamp(), data.getLookAhead(), dest);
	}
	
	/**
	 * Send null message to all processes except the excludeRank
	 * @param timeStamp
	 * @param lookAhead
	 * @param excludeRank
	 */
	private void sendNullMessage(long timeStamp, Long lookAhead, int excludeRank) {
		int LPcount = MPI.COMM_WORLD.SizeTotal();
		int selfRank = MPI.COMM_WORLD.Rank();
		
		AirportLogger.getLogger().debug("Sending null message with TS " + timeStamp + " and lookahead " + lookAhead);
		for (int lp = 0; lp < LPcount; lp++) {
			if (lp != excludeRank && lp != selfRank) {
				MpiMessage[] dataBuf = new MpiMessage[1];
				dataBuf[0] = new MpiMessage(timeStamp, lookAhead);
				MPI.COMM_WORLD.Send(dataBuf, 0, 1, MPI.OBJECT, lp, 1);
			}
		}
		
		this.lookAhead = timeStamp + lookAhead;
	}
}
