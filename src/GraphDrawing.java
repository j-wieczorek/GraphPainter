import jdrasil.graph.*;

public class GraphDrawing
{
	public static void main(String[] args)
	{
		Graph g = GraphFactory.emptyGraph();
		for (int v = 1; v<=7; v++)
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



		DrawGraph G = new DrawGraph(g);
		System.out.println(G);
		G.draw();

	}

}
