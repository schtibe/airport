package air;

public class WorldClock {
	private long currentSimulationTime = 0;
	private long rtStartTime = 0;

	private static final int SCALE_FACTOR = 50;

	public long getCurrentSimulationTime() {
		synchronized(this) {
			return this.currentSimulationTime;
		}
	}
	
	public long getRtStartTime(){
		return this.rtStartTime;
	}
	
	public void setRtStartTime(long rts){
		this.rtStartTime = rts;
	}
	
	public int getScaleFactor(){
		return SCALE_FACTOR;
	}

	/**
	 * Sleep until a certain time stamp is reached
	 * @param targetTime
	 */
	public void sleepUntil(long targetTime) {
		while(targetTime - this.currentSimulationTime > 0){
			try {
				final long sleepTime = 1000 / SCALE_FACTOR;
				Thread.sleep(sleepTime);
				synchronized(this) {
					this.currentSimulationTime++;
				}
				
			} catch (InterruptedException e) {
			}
		}
	}
}
