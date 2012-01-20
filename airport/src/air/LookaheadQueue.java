package air;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import p2pmpi.mpi.MPI;
import utils.AirportLogger;

public class LookaheadQueue {
	/**
	 * Use a fixed array to be faster
	 */
	List<Long>[] queues;
	
	/**
	 * The amount of processes (and therefore incoming queues)
	 */
	int LPcount = 0;
	
	public LookaheadQueue() {
		this.LPcount = MPI.COMM_WORLD.Size();
		
		this.queues = new ArrayList[LPcount];
		
		for (int i = 0; i < this.LPcount; i++) {
			this.queues[i] = new ArrayList<Long>();
		}
	}
	
	/**
	 * Add lookahead to the corresponding queue
	 * 
	 * @param sender Who sent the lookahead 
	 * @param lookahead The lookahead value
	 */
	public synchronized void addLookahead(int sender, Long lookahead) {
		AirportLogger.getLogger().debug("Adding a lookahead from " + sender + " with TS " + lookahead);
		synchronized (this) {
			this.queues[sender].add(lookahead);
			Collections.sort(this.queues[sender]);	
		}
	}
	
	/**
	 * Return whether one of the queues is empty
	 * 
	 * This results in halting the simulation because
	 * we must not proceed until we know of all other
	 * processes the safe timestamp
	 */
	public boolean hasEmptyQueues() {
		synchronized (this) {
			for (int i = 0; i < this.LPcount; i++) {
				if (i == MPI.COMM_WORLD.Rank()) {
					continue;
				}
				if (this.queues[i].size() == 0) {
					return true;
				} 
			}
			return false;	
		}
	}
	
	/**
	 * Return the smallest lookahead
	 * 
	 * Exclude the queue with the number of the
	 * current rank. Since the queues are sorted,
	 * we can just take the 0 positioned value.
	 */
	public synchronized Long getSmallestLookahead() {
		// we are now sure we can do this
		Long smallestLookahead = null;
		for (int i = 1; i < this.LPcount; i++) {
			if (i == MPI.COMM_WORLD.Rank()) {
				continue;
			}
			if (smallestLookahead == null) {
				smallestLookahead = this.queues[i].get(0);
			} else {
				smallestLookahead = Math.min(this.queues[i].get(0), smallestLookahead);
			}
		}
		
		return smallestLookahead;
	}
	
	/**
	 * Remove the lookahead
	 * 
	 * Remove the lookahead from all the queues (in case
	 * the lookahead is present in several queues).
	 * This is a bit dodgy since we cannot alter the 
	 * queue directly, we first have to save the positions where 
	 * the elements to remove are and the remove them
	 */
	public synchronized void removeLookeahead(Long lookahead) {
		for (int i = 0; i < this.LPcount; i++) {
			ArrayList<Long> removalElements = new ArrayList<Long>();
			Iterator<Long> itr = this.queues[i].iterator();
			outerloop:
			while (itr.hasNext()) {
				Long elem = itr.next();
				if (elem.compareTo(lookahead) > 0) {
					// since it is sorted we're sure it won't be in here anymore
					break outerloop;
				} else {
					removalElements.add(elem);
				}
			}
			
			itr = removalElements.iterator();
			while (itr.hasNext()) {
				Long elem = itr.next();
				this.queues[i].remove(elem);
			}
		}
	}
}
