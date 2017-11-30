import jdrasil.graph.Graph;

import java.awt.*;
import java.util.HashMap;
import java.util.Set;
import java.util.Random;
import java.util.Map;

import jdrasil.graph.GraphFactory;
import jdrasil.graph.invariants.ConnectedComponents;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Dimension2D;
import java.awt.event.*;
import javax.swing.event.MouseInputAdapter;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

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

	public void setX(int x)
	{
		this.x = x;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public double getLength()
	{
		return Math.sqrt(x * x + y * y);
	}

	public Point2DInt normalize()
	{
		Point2DInt result = new Point2DInt(0, 0);
		double length = this.getLength();
		result.x = (int) ((double) this.x / length);
		result.y = (int) ((double) this.y / length);

		return result;
	}

	public void add(Point2DInt p)
	{
		this.x += p.x;
		this.y += p.y;
	}

	public static Point2DInt add(Point2DInt p1, Point2DInt p2)
	{
		Point2DInt result = new Point2DInt(0, 0);
		result.x = p1.x + p2.x;
		result.y = p1.y + p2.y;

		return result;
	}

	public void subtract(Point2DInt p)
	{
		this.x -= p.x;
		this.y -= p.y;
	}

	public static Point2DInt subtract(Point2DInt p1, Point2DInt p2)
	{
		Point2DInt result = new Point2DInt(0, 0);
		result.x = p1.x - p2.x;
		result.y = p1.y - p2.y;

		return result;
	}

	public void multiply(double k)
	{
		this.x *= k;
		this.y *= k;
	}

	public static Point2DInt multiply(Point2DInt p1, double k)
	{
		Point2DInt result = new Point2DInt(0, 0);
		result.x *= k;
		result.y *= k;

		return result;
	}

	public static Point2DInt min(Point2DInt p1, Point2DInt p2)
	{
		double lengthP1 = p1.getLength();
		double lengthP2 = p2.getLength();

		if (lengthP1 > lengthP2) return p1;
		else return p2;
	}

}

class SurfaceGraph<T extends Comparable<T>> extends JPanel
{
	DrawGraph G;
	HashMap<T, Ellipse2D> circles;
	HashSet<T> cops;
	T newCop;
	Set<T> fugitiveConnectedComponent;
	private boolean addingCop = false;
	private boolean removingCop = false;
	private boolean revealStep = false;

	public SurfaceGraph(DrawGraph G)
	{
		this.G = G;
		fillCirclesList();
	}

	private void fillCirclesList()
	{
		circles = new HashMap<>();
		cops = new HashSet<>();
		fugitiveConnectedComponent = new HashSet<>();
		HashMap<T, Point2DInt> map = new HashMap<>();
		map = G.getVerticesCoordinates();
		for (Map.Entry<T, Point2DInt> entry : map.entrySet())
		{
			Point2DInt point = entry.getValue();
			circles.put(entry.getKey(), new Ellipse2D.Double(point.getX(), point.getY(), 50, 50));
		}

	}

	public void setAddingCop(boolean value)
	{
		addingCop = value;
	}

	public void setRemovingCop(boolean value)
	{
		removingCop = value;
	}

	public boolean getAddingCop()
	{
		return addingCop;
	}

	public boolean getRemovingCop()
	{
		return removingCop;
	}

	private void doDrawing(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setPaint(Color.black);

		HashMap<T, Point2DInt> verticesCoordinates = new HashMap<>();
		verticesCoordinates = G.getVerticesCoordinates();

		for (Map.Entry<T, Point2DInt> entry : verticesCoordinates.entrySet())
		{
			T v1 = entry.getKey();
			Point2DInt p1 = entry.getValue();
			Set<T> neighbors = G.getNeighborhood(v1);
			for (T v2 : neighbors)
			{
				Point2DInt p2 = verticesCoordinates.get(v2);
				g2d.setPaint(new Color(0, 0, 0));
				g2d.draw(new Line2D.Double(p1.getX() + 25, p1.getY() + 25, p2.getX() + 25, p2.getY() + 25));

			}
		}

		for (Map.Entry<T, Ellipse2D> entry : circles.entrySet())
		{
			Ellipse2D circle = entry.getValue();
			T v = entry.getKey();
			if (revealStep && fugitiveConnectedComponent.contains(v))
			{
				g2d.setPaint(new Color(0, 255, 0));
			} else
			{
				g2d.setPaint(new Color(255, 255, 255));
			}
			g2d.fill(circle);
			g2d.setPaint(new Color(0, 0, 0));
			Stroke stroke = g2d.getStroke();
			g2d.setStroke(new BasicStroke(2));
			g2d.draw(circle);
			g2d.setStroke(stroke);


			drawStringXY(g2d, String.valueOf(entry.getKey()), new Rectangle((int) circle.getX(), (int) circle.getY(), 50, 50), new Font("Purisa", Font.ROMAN_BASELINE, 40));

		}

		for (T v : cops)
		{
			Point2DInt coordinates = verticesCoordinates.get(v);
			Ellipse2D.Double circle = new Ellipse2D.Double(coordinates.getX() + 25 + G.R / 2, coordinates.getY(), G.R / 4, G.R / 4);
			g2d.setPaint(new Color(0, 0, 255));
			g2d.fill(circle);
			Stroke stroke = g2d.getStroke();
			g2d.setStroke(new BasicStroke(2));
			g2d.draw(circle);
			g2d.setStroke(stroke);

		}
	}

	public void drawStringXY(Graphics g, String text, Rectangle rect, Font font)
	{
		FontMetrics metrics = g.getFontMetrics(font);
		int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
		int y = rect.y + (rect.height - metrics.getHeight()) / 2 + metrics.getAscent();
		g.setFont(font);
		g.drawString(text, x, y);
	}

	public void setCircle(T vertex, int x, int y)
	{
		Ellipse2D circle = circles.get(vertex);
		Rectangle2D rectangle = new Rectangle2D.Double(x, y, G.R, G.R);
		circle.setFrame(rectangle);
		circles.put(vertex, circle);
		repaint();
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		doDrawing(g);
	}

	public void reveal(T fugitivePosition, HashSet<T> decontaminatedVertices)
	{
		ConnectedComponents<T> connectedComponents = new ConnectedComponents<T>(G, decontaminatedVertices);
		Set<Set<T>> connectedVerticesSets = connectedComponents.getAsSets();
		for (Set<T> verticesSet : connectedVerticesSets)
		{
			if (verticesSet.contains(fugitivePosition))
			{
				fugitiveConnectedComponent = verticesSet;
				revealStep = true;
				repaint(0);
				Timer timer = new Timer(3000, new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						revealStep = false;
						repaint();
					}
				});
				timer.start();


			}
		}
	}
}
class GraphicDragController<T extends Comparable<T>> extends MouseInputAdapter
{
	SurfaceGraph component;
	Point offset = new Point();
	boolean dragging = false;
	T vertex = null;

	public GraphicDragController(SurfaceGraph surfaceGraph)
	{
		component = surfaceGraph;
		component.addMouseListener(this);
		component.addMouseMotionListener(this);
	}

	public void mousePressed(MouseEvent e)
	{
		Point p = e.getPoint();
		HashMap<T, Ellipse2D> verticesMap = component.circles;
		for (Map.Entry<T, Ellipse2D> entry : verticesMap.entrySet())
		{
			T v = entry.getKey();
			Ellipse2D circle = entry.getValue();
			if (circle.contains(p))
			{
				if (component.getAddingCop())
				{
					component.cops.add(v);
					component.newCop = v;
					component.repaint();
				} else if (component.getRemovingCop())
				{
					component.cops.remove(v);
					component.repaint();
				} else
				{
					vertex = v;
					offset.x = p.x - (int) circle.getX();
					offset.y = p.y - (int) circle.getY();
					dragging = true;
					return;
				}

			}
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		dragging = false;
		component.setAddingCop(false);
		component.setRemovingCop(false);
		vertex = null;
	}

	public void mouseDragged(MouseEvent e)
	{
		if (dragging)
		{
			int x = e.getX() - offset.x;
			int y = e.getY() - offset.y;
			component.G.moveVertex(vertex, x, y);
			component.setCircle(vertex, x, y);
		}
	}
}

class GraphWindow extends JFrame
{

	DrawGraph G;

	public GraphWindow(Graph G)
	{
		this.G = (DrawGraph) G;
		initUI();
	}

	public void initUI()
	{
		GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int frameWidth = graphicsDevice.getDisplayMode().getWidth();
		int frameHeight = graphicsDevice.getDisplayMode().getHeight();

		SurfaceGraph<Integer> surfaceGraph = new SurfaceGraph(G);
		add(surfaceGraph);
		new GraphicDragController<Integer>(surfaceGraph);
		setTitle("Graph");
		setSize(frameWidth, frameHeight);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


	}

	void execute()
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				GraphWindow graphWindow = new GraphWindow(G);
				graphWindow.setVisible(true);
			}
		});

	}
}

public class DrawGraph<T extends Comparable<T>> extends Graph<T>
{
	private HashMap<T, Point2DInt> verticesCoordinates;
	public int R = 50;
	GraphWindow window;

	public DrawGraph(Graph original)
	{
		super(original);

		GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int frameWidth = graphicsDevice.getDisplayMode().getWidth();
		int frameHeight = graphicsDevice.getDisplayMode().getHeight();
		window = null;

		verticesCoordinates = new HashMap<>();
		Set<T> vertices = original.getCopyOfVertices();
		Random random = new Random();
		for (T v : vertices)
		{
			verticesCoordinates.put(v, new Point2DInt(random.nextInt(frameWidth - R), random.nextInt(frameHeight - R)));
		}

		VertexSpacer spacer = new VertexSpacer(this, verticesCoordinates, frameWidth, frameHeight);
		verticesCoordinates = spacer.align();
	}

	public DrawGraph(Graph original, GraphWindow window)
	{
		this(original);
		this.window = window;
	}

	public DrawGraph(Graph original, int frameWidth, int frameHeight)
	{
		super(original);
		window = null;
		verticesCoordinates = new HashMap<>();
		Set<T> vertices = original.getCopyOfVertices();
		Random random = new Random();
		for (T v : vertices)
		{
			verticesCoordinates.put(v, new Point2DInt(random.nextInt(frameWidth - R / 2), random.nextInt(frameHeight - R / 2)));
		}

		VertexSpacer spacer = new VertexSpacer(this, verticesCoordinates, frameWidth, frameHeight);
		verticesCoordinates = spacer.align();
	}

	public void draw()
	{
		if (window == null) window = new GraphWindow(this);
		window.execute();
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
		verticesCoordinates.put(v, new Point2DInt(20, 20));
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
