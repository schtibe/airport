package utils;

/**
 * implementation of a 2D Vector
 */
public class Vector {

	/**
	 * The vectors components
	 */
	private double[] components;

	/**
	 * Is the vectors length already calculated
	 */
	private boolean lengthCalculated = false;

	/**
	 * The vectors length
	 */
	private double length;

	/**
	 * @param array of doubles, the vectors components
	 */
	public Vector(double[] components) {
		if (components != null) {
			if (components.length == 2) {
				this.components = components.clone();
			} else {
				this.components = new double[2];
				int i;
				for (i = 0; i < components.length; i++) {
					if (i < 2) {
						this.components[i] = components[i];
					} else {
						break;
					}
				}
				if (i < 2) {
					for (int j = i; j < 2; j++) {
						this.components[j] = 0;
					}
				}
			}
		} else {
			this.components = new double[] { 0, 0 };
		}
	}
	
	/**
	 * adding two vectors
	 * 
	 * @param the vector to be added to this vector
	 * @return a vector as a result of the operation
	 */
	public Vector add(Vector vector) {
		return new Vector(new double[] { components[0] + vector.getComponent(0),
				components[1] + vector.getComponent(1) });
	}

	/**
	 * the cross product of two vectors if defined
	 * 
	 * @param the vector to calculate the cross product with this vector
	 * @return a vector as a result of the operation
	 */
	public Vector cross(Vector vector) {
		return new Vector(new double[] { 0, 0 });
	}
	
	/**
	 * the dot product of two vectors
	 * 
	 * @param the vector to calculate the dotproduct with this vector
	 * @return a float as a result of the operation
	 */
	public double dot(Vector vector) {
		double scalar = 0;
		for (int i = 0; i < this.components.length; i++) {
			scalar += this.components[i] * vector.getComponent(i);
		}
		return scalar;
	}

	/**
	 * returns the component at index [0,1,..]
	 * 
	 * @param index of a component of the vector
	 * @return the components value
	 */
	public double getComponent(int index) {
		if (index >= this.components.length) {
			return 0;
		} else {
			return this.components[index];
		}
	}

	/**
	 * the norm of this vector
	 * 
	 * @return The length of the vector
	 */
	public double norm() {
		if (!lengthCalculated) {
			for (int i = 0; i < this.components.length; i++) {
				length += this.components[i] * this.components[i];
			}
			this.length = (double) Math.sqrt(length);
			lengthCalculated = true;
		}
		return this.length;
	}
	/**
	 * subtracting two vectors
	 * 
	 * @param the vector to be subtracted from this vector
	 * @return a vector as a result of the operation
	 */
	public Vector sub(Vector vector) {
		return new Vector(new double[] { components[0] - vector.getComponent(0),
				components[1] - vector.getComponent(1) });
	}
	/**
	 * normalise the vector
	 * 
	 * @return a vector with length 1.0 in the same direction
	 */
	public Vector normalize() {
		double[] newComponents = new double[this.components.length];
		for (int i = 0; i < this.components.length; i++) {
			newComponents[i] = this.components[i] / this.norm();
		}
		return new Vector(newComponents);
	}

	/**
	 * returns a duplicate of this vector
	 * 
	 * @return
	 */
	public Vector clone() {
		return new Vector(this.components);
	}

	/**
	 * returns a multiple of the factor (stretched)
	 * 
	 * @param factor to multiply this vector with
	 * @return a scaled copy of the actual vector
	 */
	public Vector multiply(double factor) {
		double[] newComponents = new double[this.components.length];
		for (int i = 0; i < this.components.length; i++) {
			newComponents[i] = this.components[i] * factor;
		}
		return new Vector(newComponents);
	}

	/**
	 * returns x: value, y:value
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return "x: " + this.components[0] + ", y: " + this.components[1];
	}

	/**
	 * returns the vector rotated by angle, angle is measured in radians
	 * 
	 * @param an angle in the radiant notation [0,2*PI]
	 * @return a vector rotated by the input angle
	 */
	public Vector rotate(double angle) {
		double newAngle = this.getAngle() + angle;
		return new Vector(new double[] {
				(double) Math.cos(newAngle) * this.norm(),
				(double) Math.sin(newAngle) * this.norm() });
	}

	/**
	 * the result is the angle between this vector and the x axis in radians
	 * 
	 * @return an angle in radiant
	 */
	public double getAngle() {
		Vector newThis = this.normalize();
		Vector xAxis = new Vector(new double[] { 1, 0 });
		double cosAlpha = newThis.dot(xAxis);
		double direction = (double) Math.atan2(newThis.getComponent(1), newThis
				.getComponent(0));
		double angle = (double) Math.acos(cosAlpha);
		if (direction >= 0) {
			return angle;
		} else {
			return (double) (Math.PI * 2) - angle;
		}
	}

	public int compareTo(Vector arg0) {
		Boolean isEqual = true;
		for (int i = 0; i < this.components.length; i++) {
			if (this.components[i] != arg0.getComponent(i)) {
				isEqual = false;
			}
		}
		if (isEqual) {
			return 0;
		} else if (this.norm() > arg0.norm()) {
			return 1;
		} else {
			return -1;
		}
	}
	
	/**
	 * Return the first component
	 * @return Vector's first component
	 */
	public double getX() {
		return this.components[0];
	}
	
	/**
	 * Return the second component
	 * @return Vector's second component
	 */
	public double getY() {
		return this.components[1];
	}
	
//	/**
//	 * Return the result of a linear combination
//	 * 
//	 * @param center 
//	 * @param directionA
//	 * @param directionB
//	 * @param point
//	 * @return The result of the linear combination
//	 */
//	public static LinearCombination getLinearCombination (Vector center, Vector directionA, Vector directionB, Vector point){
//		Vector position = point.sub(center);	
//		double denominator = 
//			directionA.getComponent(0) * directionB.getComponent(1)
//			- directionA.getComponent(1) * directionB.getComponent(0);
//		
//		double lambda = -(directionB.getComponent(0) * position.getComponent(1)
//				- directionB.getComponent(1) * position.getComponent(0)) /
//				denominator;
//		
//		double mu = (directionA.getComponent(0) * position.getComponent(1)
//				- directionA.getComponent(1) * position.getComponent(0)) /
//				denominator;
//		
//		return new LinearCombination (mu, lambda);
//	}
}


