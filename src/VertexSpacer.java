import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.*;
import jdrasil.graph.Graph;
import java.util.ArrayList;


class VertexVector
{
	Point2DInt position;
	Point2DInt displacement;

	public VertexVector(Point2DInt position, Point2DInt displacement)
	{
		this.position = position;
		this.displacement = displacement;
	}

	public void add(VertexVector v)
	{
		this.position.add(v.position);
		this.displacement.add(v.displacement);
	}

	public void subtract(VertexVector v)
	{
		this.position.subtract(v.position);
		this.displacement.subtract(v.displacement);
	}
}
public class VertexSpacer<T extends Comparable<T>>
{
	int frameWidth;
	int frameHeight;
	int area;
	double k;
	double C = 1;
	int temperature;

	DrawGraph G;
	HashMap<T, Point2DInt> verticesCoordinates;
	HashMap<T, VertexVector> verticesVectors;
	int numberOfIterations = 50;

	public VertexSpacer(DrawGraph<T> G)
	{
		this.frameHeight = 768;
		this.frameWidth = 1366;
		this.temperature = (int)(0.1*frameWidth);
		this.area = this.frameHeight * this.frameWidth;
		this.verticesCoordinates = G.getVerticesCoordinates();
		this.k = C*Math.sqrt(this.area/this.verticesCoordinates.size());
		this.verticesVectors = new HashMap<>();
		for (Map.Entry<T,Point2DInt> entry: verticesCoordinates.entrySet())
		{
			T vertex = entry.getKey();
			Point2DInt vertexPosition = entry.getValue();
			VertexVector vertexVector = new VertexVector(new Point2DInt(vertexPosition.getX(),vertexPosition.getY()),
					new Point2DInt(0,0));
			verticesVectors.put(vertex, vertexVector);
		}

	}
	public VertexSpacer(DrawGraph<T> G, HashMap<T, Point2DInt> verticesCoordinates, int frameWidth, int frameHeight)
	{
		this.frameHeight = frameHeight;
		this.frameWidth = frameWidth;
		this.temperature = (int)(0.1*frameWidth);
		this.area = this.frameHeight * this.frameWidth;
		this.verticesCoordinates = verticesCoordinates;
		this.k = C*Math.sqrt(this.area/this.verticesCoordinates.size());
		this.G = G;
		this.verticesVectors = new HashMap<>();
		for (Map.Entry<T,Point2DInt> entry: verticesCoordinates.entrySet())
		{
			T vertex = entry.getKey();
			Point2DInt vertexPosition = entry.getValue();
			VertexVector vertexVector = new VertexVector(new Point2DInt(vertexPosition.getX(),vertexPosition.getY()),
					new Point2DInt(0,0));
			verticesVectors.put(vertex, vertexVector);
		}
	}

	double attractiveForce(double x)
	{
		return x*x/k;
	}

	double repulsiveForce(double x)
	{
		return k*k/x;
	}

	void calculateRepulsiveForces()
	{
		for (Map.Entry<T, VertexVector> entry: verticesVectors.entrySet())
		{
			T v = entry.getKey();
			VertexVector vVector = entry.getValue();
			vVector.displacement = new Point2DInt(0,0);
			for (Map.Entry<T, VertexVector> entry2: verticesVectors.entrySet())
			{
				T u = entry2.getKey();
				VertexVector uVector = entry2.getValue();
				if (u != v)
				{
					Point2DInt difference = Point2DInt.subtract(vVector.position, uVector.position);
					vVector.displacement.add(Point2DInt.multiply(difference.normalize(),repulsiveForce(difference.getLength())));
				}
			}
			verticesVectors.put(v, vVector);
		}
	}

	void calculateAttractiveForces()
	{
		//ArrayList visitedVertices = new ArrayList();
		for (Map.Entry<T, VertexVector> entry: verticesVectors.entrySet())
		{
			T v = entry.getKey();
			VertexVector vVector = entry.getValue();
			vVector.displacement = new Point2DInt(0,0);
			Set<T> neigbours = G.getNeighborhood(v);
			for (T u:neigbours)
			{
				//if (!visitedVertices.contains(u))
				//{
					VertexVector uVector = verticesVectors.get(u);
					Point2DInt difference = Point2DInt.subtract(vVector.position, uVector.position);
					vVector.displacement = Point2DInt.subtract(vVector.displacement, Point2DInt.multiply(difference.normalize(), attractiveForce(Math.abs(difference.getLength()))));
					uVector.displacement = Point2DInt.subtract(uVector.displacement, Point2DInt.multiply(difference.normalize(), attractiveForce(Math.abs(difference.getLength()))));
					verticesVectors.put(v, vVector);
					verticesVectors.put(u, uVector);
				//}
			}
			//visitedVertices.add(v);
		}
	}

	void limitDisplacement()
	{
		for (Map.Entry<T, VertexVector> entry: verticesVectors.entrySet())
		{
			T v = entry.getKey();
			VertexVector vVector = entry.getValue();

			vVector.position = Point2DInt.add(vVector.position, Point2DInt.multiply(vVector.displacement.normalize(), Math.min(vVector.displacement.getLength(), temperature)));
			vVector.position.setX(Math.min(5*frameWidth/6, Math.max(-frameWidth/2, vVector.position.getX())));
			vVector.position.setY(Math.min(5*frameHeight/6, Math.max(-frameHeight/2, vVector.position.getY())));
		}

		if(temperature> 5)
		temperature--;
	}

	void saveCoordinates()
	{
		for (Map.Entry<T,VertexVector> entry: verticesVectors.entrySet())
		{
			T vertex = entry.getKey();
			Point2DInt vertexPosition = entry.getValue().position;
			verticesCoordinates.put(vertex, vertexPosition);
		}
	}
	public HashMap<T, Point2DInt> align()
	{
		System.out.println("Start");
		for (int i=0; i<numberOfIterations; i++)
		{
			calculateRepulsiveForces();
			calculateAttractiveForces();
			limitDisplacement();
			System.out.println("i = " + i);
		}
		saveCoordinates();
		return verticesCoordinates;
	}
}
