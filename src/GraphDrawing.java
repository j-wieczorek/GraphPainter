import jdrasil.graph.*;

public class GraphDrawing
{
	public static void main(String[] args)
	{
		Graph g = GraphFactory.emptyGraph();
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

		DrawGraph G = new DrawGraph(g);
		G.draw();
	}

}
