import jdrasil.graph.Graph;
import java.util.HashMap;
import java.util.Set;
import java.util.Random;
import java.util.Map;
import jdrasil.graph.GraphFactory;
import java.io.File;
import java.io.IOException;

class Point2DInt
{
	private int x;
	private int y;
	public Point2DInt(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}

}
public class DrawGraph<T extends Comparable<T>> extends Graph<T>
{
	private HashMap<T, Point2DInt> verticesCoordinates;
	public int R = 50;
	public DrawGraph(Graph original)
	{
		super(original);
		verticesCoordinates = new HashMap<>();
		Set<T> vertices = original.getCopyOfVertices();
		Random random = new Random();
		for (T v : vertices)
		{
			verticesCoordinates.put(v,new Point2DInt(random.nextInt(1360),random.nextInt(500)));
		}
	}

	void draw()
	{

	}

	public HashMap<T, Point2DInt> getVerticesCoordinates()
	{
		return verticesCoordinates;
	}
	public void moveVertex(T v, int x, int y)
	{
		verticesCoordinates.put(v, new Point2DInt(x, y));
	}
	@Override
	public void addVertex(T v)
	{
		super.addVertex(v);
		verticesCoordinates.put(v,new Point2DInt(20,20));
	}

	public void addVertex(T v, int x, int y)
	{
		super.addVertex(v);
		verticesCoordinates.put(v, new Point2DInt(x, y));
	}

	@Override
	public void removeVertex(T v)
	{
		super.removeVertex(v);
		verticesCoordinates.remove(v);
	}
}
