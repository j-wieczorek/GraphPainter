import jdrasil.graph.GraphFactory;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Dimension2D;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import jdrasil.graph.*;
class SurfaceGraph<T extends Comparable<T>> extends JPanel
{
	DrawGraph G;
	HashMap<T, Ellipse2D> circles;

	public SurfaceGraph(DrawGraph G)
	{
		this.G = G;
		fillCirclesList();
	}

	private void fillCirclesList()
	{
		circles = new HashMap<>();
		HashMap<T, Point2DInt> map = new HashMap<>();
		map = G.getVerticesCoordinates();
		for(Map.Entry<T, Point2DInt> entry : map.entrySet())
		{
			Point2DInt point = entry.getValue();
			circles.put(entry.getKey(),new Ellipse2D.Double(point.getX(), point.getY(), 50,50));
		}

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
				g2d.setPaint(new Color(0,0,0));
				g2d.draw(new Line2D.Double(p1.getX()+25,p1.getY()+25, p2.getX()+25, p2.getY()+25));

			}
		}

		for (Map.Entry<T,Ellipse2D> entry : circles.entrySet())
		{
			Ellipse2D circle = entry.getValue();
			g2d.setPaint(new Color(255,255,255));
			g2d.fill(circle);
			g2d.setPaint(new Color(0,0,0));
			Stroke stroke = g2d.getStroke();
			g2d.setStroke(new BasicStroke(2));
			g2d.draw(circle);
			g2d.setStroke(stroke);


			drawStringXY(g2d, String.valueOf(entry.getKey()), new Rectangle((int)circle.getX(), (int)circle.getY(), 50, 50), new Font("Purisa",Font.ROMAN_BASELINE,40));

		}
	}

	public void drawStringXY(Graphics g, String text, Rectangle rect, Font font)
	{
		FontMetrics metrics = g.getFontMetrics(font);
		int x = rect.x + (rect.width - metrics.stringWidth(text))/2;
		int y = rect.y + (rect.height - metrics.getHeight() )/2 + metrics.getAscent();
		g.setFont(font);
		g.drawString(text, x, y);
	}

	public void setCircle(T vertex, int x, int y)
	{
		Ellipse2D circle = circles.get(vertex);
		Rectangle2D rectangle = new Rectangle2D.Double(x, y, G.R, G.R);
		circle.setFrame(rectangle);
		circles.put(vertex,circle);
		repaint();
	}
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		doDrawing(g);
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
				vertex = v;
				offset.x = p.x - (int)circle.getX();
				offset.y = p.y - (int)circle.getY();
				dragging = true;
				return;
			}
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		dragging = false;
		vertex = null;
	}

	public void mouseDragged(MouseEvent e)
	{
		if(dragging)
		{
			int x = e.getX() - offset.x;
			int y = e.getY() - offset.y;
			component.G.moveVertex(vertex, x, y);
			component.setCircle(vertex, x, y);


		}
	}
}
public class GraphPainter extends JFrame
{

	DrawGraph G;
	public GraphPainter()
	{
		Random randomGenerator = new Random();
		Graph<Integer> g = GraphFactory.emptyGraph();

		/*for (int v = 1; v<=5; v++)
		{
			g.addVertex(v);
		}
		g.addEdge(1,3);
		g.addEdge(1,4);
		g.addEdge(1,5);

		g.addEdge(2,3);
		g.addEdge(2,4);
		g.addEdge(2,5);

		g.addEdge(3,5);
		*/

		for (int v = 1; v<=13; v++)
		{
			g.addVertex(v);
		}
		g.addEdge(1,2);
		g.addEdge(1,3);
		g.addEdge(2,3);

		g.addEdge(2,4);

		g.addEdge(4,5);
		g.addEdge(4,6);
		g.addEdge(5,6);

		g.addEdge(3,7);

		g.addEdge(7,8);
		g.addEdge(7,9);
		g.addEdge(8,9);

		g.addEdge(8,10);

		g.addEdge(10,11);
		g.addEdge(10,12);
		g.addEdge(11,12);

		g.addEdge(9,13);

		g.addEdge(13,14);
		g.addEdge(13,15);
		g.addEdge(14,15);
		G = new DrawGraph(g);
		initUI();


	}

	public void initUI()
	{
		SurfaceGraph<Integer> surfaceGraph = new SurfaceGraph(G);
		add(surfaceGraph);
		new GraphicDragController<Integer>(surfaceGraph);
		setTitle("Graph");
		setSize(1366, 768);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				GraphPainter graphPainter = new GraphPainter();
				graphPainter.setVisible(true);
			}
		});

	}
}
