package worldgui;

import com.trolltech.qt.gui.QGraphicsEllipseItem;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QStyleOptionGraphicsItem;
import com.trolltech.qt.gui.QWidget;

public class Aircraft extends QGraphicsEllipseItem {
	// temporary for prototyping
	int counter = 0;
	int x = 100;
	int y = 100;
	
	private air.Aircraft aircraft;
	
	public Aircraft(air.Aircraft a) {
		super();
		this.aircraft = a;
		
		this.setRect(this.x, this.y, 10, 10);
	}
	
	public void paint(QPainter p, QStyleOptionGraphicsItem s, QWidget w) {
		this.x++;
		this.y++;
		System.out.println("x: " + this.x + ", y: " + this.y);
		this.setRect(this.x, this.y, 10, 10);
		p.drawEllipse(this.x, this.y, 10, 10);
	}

}
