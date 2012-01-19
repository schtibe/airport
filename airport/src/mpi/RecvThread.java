package mpi;

import p2pmpi.mpi.MPI;
import p2pmpi.mpi.Status;
import air.SimWorld;

public class RecvThread extends Thread {
	public void run() {
		while(true) {
			MpiMessage[] data = { null };
			Status status = MPI.COMM_WORLD.Recv(data, 0, 1, MPI.OBJECT, MPI.ANY_SOURCE, 1);
			System.err.println("Received something");
			
			MpiEvent.createEventFromMessage(data[0]);

			int sender = status.source;
			long timestamp = data[0].getTimeStamp();
			SimWorld.getInstance().getLookaheadQueue().addLookahead(sender, timestamp);
		}
	}
}
