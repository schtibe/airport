package mpi;

import p2pmpi.mpi.MPI;
import p2pmpi.mpi.Status;
import utils.AirportLogger;
import air.SimWorld;

public class RecvThread extends Thread {
	public void run() {
		while(true) {
			MpiMessage[] data = new MpiMessage[1];
			Status status = MPI.COMM_WORLD.Recv(data, 0, 1, MPI.OBJECT, MPI.ANY_SOURCE, 1);
			
			MpiEvent.createEventFromMessage(data[0]);

			int sender = status.source;
			long lookahead = data[0].getLookAhead();
			long timestamp = data[0].getTimeStamp();
			AirportLogger.getLogger().debug("Received lookahead" + lookahead + " with TS " + timestamp + " from " + sender);
			SimWorld.getInstance().getLookaheadQueue().addLookahead(sender, timestamp + lookahead);
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
