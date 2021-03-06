package extraction;

import java.awt.Point;
import java.util.ArrayList;

import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import tools.MathTools;

public class ConnectedComponent{
	private ArrayList<Point> points;
	
	//constructors
	public ConnectedComponent(){
		this(new ArrayList<Point>());
	}
	
	public ConnectedComponent(int x, int y){
		this(new ArrayList<Point>());
		points.add(new Point(x, y));
	}

	public ConnectedComponent(ArrayList<Point> points) {
		this.points = points;
	}
	
	/**
	 * @return a deep clone of the shape
	 */
	public ConnectedComponent deepClone(){
		ArrayList<Point> pointsCopy = new ArrayList<>();
		for (Point point : points) {
			pointsCopy.add(new Point(point));
		}
		return new ConnectedComponent(pointsCopy);
	}

	//operations
	/**
	 * @return the points making up the shape
	 */
	public ArrayList<Point> getPoints(){
		return points;
	}

	/**
	 * @param p : a point that you want to add to the shape
	 */
	public void addPoint(Point p){
		points.add(p);
	}

	/**
	 * @param x : x coordinate of a point that you want to add to the shape
	 * @param y : y coordinate of a point that you want to add to the shape
	 */
	public void addPoint(int x, int y){
		addPoint(new Point(x, y));
	}

	public boolean contains(Point p){
		return contains(p.getX(), p.getY());
	}
	
	public boolean contains(double x, double y) {
		for (Point point : points) {
			if((point.getX()==x)&&(point.getY()==y))
				return true;
		}
		return false;
	}
	
	/**
	 * @param cc : ConnectedComponent that will be compared
	 * @return true if all the points in cc are identical to all the points in this and there are the same nuber of points in cc and this, false otherwise.
	 */
	public boolean isIdenticalTo(ConnectedComponent cc){
		if(points.size()!=cc.getPoints().size())
			return false;
		
		for (int i = 0; i < points.size(); i++) {
			if((points.get(i).getX()!=cc.getPoints().get(i).getX()) || (points.get(i).getY()!=cc.getPoints().get(i).getY()))
				return false;
		}
		
		return true;
	}
	
	/**
	 * @return A list of x coordinates of all the points in the shape
	 */
	public ArrayList<Integer> getXs(){
		ArrayList<Integer> xs = new ArrayList<Integer>();
		for(Point point:points){
			xs.add((int)point.getX());
		}
		return xs;
	}

	/**
	 * @return A list of y coordinates of all the points in the shape
	 */
	public ArrayList<Integer> getYs(){
		ArrayList<Integer> ys = new ArrayList<Integer>();
		for(Point point:points){
			ys.add((int)point.getY());
		}
		return ys;
	}
	
	/**
	 * @return the proportion of pixels that are part of the CC within the circumscribed rectangle.
	 * Example : if there are 2 pixels contained in the CC for each pixel not contained in the CC,
	 * this method will return 2/3, as 2/3 of the circumscribed rectangle will be occupied by the CC.
	 */
	public double computeCoverage(){
		Point topLeft = getTopLeft(), bottomRight = getBottomRight();
		double area = (bottomRight.getX()-topLeft.getX())*(bottomRight.getY()-topLeft.getY());
		return points.size()/area;
	}
	
	// get Corners
	/**
	 * @return The top-left corner of the circumscribed rectangle of the shape
	 */
	public Point getTopLeft(){
		return new Point(MathTools.min(getXs()), MathTools.min(getYs()));
	}
	
	/**
	 * @return The top-right corner of the circumscribed rectangle of the shape
	 */
	public Point getTopRight(){
		return new Point(MathTools.max(getXs()), MathTools.min(getYs()));
	}
	
	/**
	 * @return The bottom-left corner of the circumscribed rectangle of the shape
	 */
	public Point getBottomLeft(){
		return new Point(MathTools.min(getXs()), MathTools.max(getYs()));
	}
	
	/**
	 * @return The bottom-right corner of the circumscribed rectangle of the shape
	 */
	public Point getBottomRight(){
		return new Point(MathTools.min(getXs()), MathTools.max(getYs()));
	}
	
	/**
	 * @return The area of the circumscribed rectangle of the shape
	 */
	public double getRectArea(){
		return getWidth()*getHeight();
	}
	
	/**
	 * @return The area of the circumscribed square of the shape
	 */
	public double getSquArea(){
		return Math.pow(Math.max(getWidth(), getHeight()), 2);
	}
	
	/**
	 * @return the width of the shape
	 */
	public int getWidth(){
		return (int) (getTopRight().getX() - getTopLeft().getX());
	}
	
	/**
	 * @return the height of the shape
	 */
	public int getHeight(){
		return (int) (getBottomLeft().getY() - getTopLeft().getY());
	}
	
	/**
	 * @return the center pixel of the shape (may not be contained within the actual shape if, for example, the shape is in the form of a ring)
	 */
	public Point getCenter(){
		Point topLeft = getTopLeft();
		return new Point((int) (topLeft.getX()+(getWidth()/2)), (int) (topLeft.getY()+(getHeight()/2)));
	}
	
	/**
	 * @return the centroid of the shape : the point of average coordinates (may not be contained within the actual shape if, for example, the shape is in the form of a ring)
	 */
	public Point getCentroid(){
		return new Point(MathTools.average(getXs()), MathTools.average(getYs()));
	}
	
	/**
	 * @return a square image containing only the connected component in black on a white background
	 */
	public ImageProcessor createImage(){
		int cote = (this.getWidth()>this.getHeight())?this.getWidth():this.getHeight();
		ImageProcessor result = new ByteProcessor(cote,cote);
		result.setColor(255);
		result.fill();
		
		double minX = MathTools.min(getXs()), minY = MathTools.min(getYs());
		
		for(Point point:this.getPoints()){
			int x = (int) (point.getX() - minX), y = (int) (point.getY() - minY);
			result.putPixel(x, y, 0);
		}
		
		return result;
	}
}