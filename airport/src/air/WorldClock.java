package air;

public class WorldClock {
	private long currentSimulationTime = 0;

	private static final int SCALE_FACTOR = 30000;

	public long getCurrentSimulationTime() {
		synchronized(this) {
			return this.currentSimulationTime;
		}
	}

	/**
	 * Sleep until a certain time stamp is reached
	 * @param targetTime
	 */
	public void sleepUntil(long targetTime) {
		final long diff = targetTime - this.currentSimulationTime;
		try {
			final long sleepTime = diff * 1000 / SCALE_FACTOR;
			Thread.sleep(sleepTime);
			synchronized(this) {
				this.currentSimulationTime = targetTime;
			}
			
		} catch (InterruptedException e) {
		}
	}
}
