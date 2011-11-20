package air;

import com.trolltech.qt.gui.QGraphicsRectItem;
import com.trolltech.qt.gui.QGraphicsScene;

public class Config {
	private static Config instance;

	private Config() {}
	
	public static Config getInstance() {
		if (Config.instance == null) {
			Config.instance = new Config();
		}
		
		return Config.instance;
	}
	
	
	String[] args;
	public void setArgs(String[] args) {
		this.args = args;
	}
	
	public String[] getArgs() {
		return this.args;
	}
	
	QGraphicsScene scene;
	public void setScene(QGraphicsScene scene) {
		this.scene = scene;
	}
	
	public QGraphicsScene getScene() {
		return this.scene;
	}
}
