import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.Random;
import jdrasil.algorithms.ExactDecomposer;
import jdrasil.graph.*;
import jdrasil.graph.invariants.ConnectedComponents;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

public class CopsAndRobbers<T extends Comparable<T>> extends JFrame implements MouseListener
{


	// Variables declaration - do not modify

	private JButton BtnAdd;
	private JButton BtnRemove;
	private JButton BtnRestart;
	private JButton BtnReveal;

	private JLabel CopsUsed;
	private JLabel LblCopsUsed;
	private JLabel LblMaxCops;
	private JLabel LblGameStatus;
	private JLabel MaxCops;

	private SurfaceGraph<T> GraphPanel;

	private DrawGraph G;
	private int frameWidth;
	private int frameHeight;

	private int copsUsed;
	private int maxCopsNumber;

	private T fugitivePosition;
	private HashSet<T> decontaminatedVertices;

	private boolean addingCopStep;
	private boolean removingCopStep;


	// End of variables declaration

	/**
	 * Creates new form CopsAndRobbers with an empty graph
	 */

	public CopsAndRobbers()
	{
		this.G = (DrawGraph)GraphFactory.emptyGraph();
		this.decontaminatedVertices = new HashSet<>();
		initComponents();
	}

	/**
	 * Creates new CopsAndRobbers game with graph given as a parameter
	 * @param G graph on which game is going to be performed
	 */
	public CopsAndRobbers(DrawGraph G)
	{
		this.G = G;
		this.decontaminatedVertices = new HashSet<>();
		initComponents();
		initUI();
		createLayout();
		try
		{
			/// computing tree decomposition (and based on that required number of searchers - cops)
			TreeDecomposition<T> td = new ExactDecomposer<T>(G).call();
			maxCopsNumber = td.getWidth() + 1;
			MaxCops.setText(Integer.toString(maxCopsNumber));
		}
		catch(Exception e)
		{
			System.out.println("Exception during treewidth computation");
		}

		///drawing a location of fugitive (robber)
		Random random = new Random();
		int i = random.nextInt(G.getVerticesCoordinates().keySet().size());
		fugitivePosition = (T)G.getVerticesCoordinates().keySet().toArray()[i];
		CopsUsed.setText(Integer.toString(GraphPanel.cops.size()));
	}

	/**

	 UI initalization. Size of the window should be automatically adjusted to resolution of display used
	 */

	public void initUI()
	{
		GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		frameWidth = graphicsDevice.getDisplayMode().getWidth();
		frameHeight = graphicsDevice.getDisplayMode().getHeight();


		new GraphicDragController<T>(GraphPanel);
		setTitle("Graph");
		setSize(frameWidth, frameHeight);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	//@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">

	/**
	 * Swing components initialization
	 */

	private void initComponents()
	{

		BtnAdd = new JButton();
		BtnRemove = new JButton();
		BtnReveal = new JButton();
		BtnRestart = new JButton();
		LblMaxCops = new JLabel();
		MaxCops = new JLabel();
		LblCopsUsed = new JLabel();
		CopsUsed = new JLabel();
		GraphPanel = new SurfaceGraph<T>(G);
		LblGameStatus = new JLabel();
		GraphPanel.addMouseListener(this);
	}

	/**
	 * Detection of releasing of mouse button over the GraphPanel - allows to update amount of cops used after clicking
	 * one of the vertices and check if game has already been won or lost.
	 * @param e
	 */
	public void mouseReleased(MouseEvent e)
	{
		if(removingCopStep || addingCopStep)
		{
			removingCopStep = false;
			addingCopStep = false;

			CopsUsed.setText(Integer.toString(GraphPanel.cops.size()));
			copsUsed += Integer.parseInt(CopsUsed.getText()) - copsUsed;
			decontaminatedVertices.add(GraphPanel.newCop);
			if (decontaminatedVertices.contains(fugitivePosition))
			{
				displayStatus("You won!");
				BtnAdd.setEnabled(false);
				BtnRemove.setEnabled(false);
			}
			ConnectedComponents<T> connectedComponents = new ConnectedComponents<T>(G, GraphPanel.cops);
			Set<Set<T>> connectedVerticesSets = connectedComponents.getAsSets();
			Set<T> fugitiveConnectedComponent = findConnectedComponentContaining(fugitivePosition, connectedVerticesSets);

			for (T v: decontaminatedVertices)
			{
				if (fugitiveConnectedComponent.contains(v))
				{
					displayStatus("You lost!");
					BtnAdd.setEnabled(false);
					BtnRemove.setEnabled(false);
				}
			}
		}
	}

	public void mouseClicked(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{

	}

	public  void mouseEntered(MouseEvent e)
	{

	}

	public void mousePressed(MouseEvent e)
	{
	}

	/**
	 * Layout creation. Method generated by NetBeans IDE.
	 */
	private void createLayout()
	{
		/*setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setPreferredSize(new java.awt.Dimension(1366, 768));
		setSize(new java.awt.Dimension(1366, 768));
		*/

		LblGameStatus.setText("");

		BtnAdd.setText("Add a cop");
		BtnAdd.addMouseListener(new java.awt.event.MouseAdapter()
		{
			public void mouseReleased(java.awt.event.MouseEvent evt)
			{
				BtnAddMouseReleased(evt);
			}
		});

		BtnRemove.setText("Remove a cop");
		BtnRemove.addMouseListener(new java.awt.event.MouseAdapter()
		{
			public void mouseReleased(java.awt.event.MouseEvent evt)
			{
				BtnRemoveMouseReleased(evt);
			}
		});

		BtnReveal.setText("Reveal");
		BtnReveal.addMouseListener(new java.awt.event.MouseAdapter()
		{
			public void mouseReleased(java.awt.event.MouseEvent evt)
			{
				BtnRevealMouseReleased(evt);
			}
		});

		BtnRestart.setText("Restart");
		BtnRestart.addMouseListener(new java.awt.event.MouseAdapter()
		{
			public void mouseReleased(java.awt.event.MouseEvent evt)
			{
				BtnRestartMouseReleased(evt);
			}
		});

		LblMaxCops.setText("Max cops:");

		MaxCops.setText("#");

		LblCopsUsed.setText("Cops used:");

		CopsUsed.setText("#");

		javax.swing.GroupLayout GraphPanelLayout = new javax.swing.GroupLayout(GraphPanel);
		GraphPanel.setLayout(GraphPanelLayout);
		GraphPanelLayout.setHorizontalGroup(
				GraphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGap(0, 1103, Short.MAX_VALUE)
		);
		GraphPanelLayout.setVerticalGroup(
				GraphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGap(0, 0, Short.MAX_VALUE)
		);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addGap(17, 17, 17)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
												.addComponent(BtnRestart)
												.addComponent(BtnAdd)
												.addComponent(BtnRemove)
												.addComponent(BtnReveal))
										.addGroup(layout.createSequentialGroup()
												.addComponent(LblMaxCops)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(MaxCops))
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
												.addComponent(LblGameStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addGroup(layout.createSequentialGroup()
														.addComponent(LblCopsUsed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(CopsUsed))))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(GraphPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addGap(29, 29, 29)
								.addComponent(BtnAdd)
								.addGap(31, 31, 31)
								.addComponent(BtnRemove)
								.addGap(43, 43, 43)
								.addComponent(BtnReveal)
								.addGap(52, 52, 52)
								.addComponent(BtnRestart)
								.addGap(38, 38, 38)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(LblMaxCops)
										.addComponent(MaxCops))
								.addGap(18, 18, 18)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(LblCopsUsed)
										.addComponent(CopsUsed))
								.addGap(33, 33, 33)
								.addComponent(LblGameStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap(491, Short.MAX_VALUE))
						.addComponent(GraphPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		);

	}

	/**
	 * Handling adding a cop action. Method comunicates with GraphPanel, which is responsible for drawing the graph and
	 * recognizing vertices pressed
	 * @param evt
	 */
	private void BtnAddMouseReleased(java.awt.event.MouseEvent evt)
	{
		if ((maxCopsNumber - GraphPanel.cops.size()) > 0)
		{
			GraphPanel.setAddingCop(true);
			addingCopStep = true;
		}
	}

	/**
	 * Handling removing a cop action. Method communicates with GraphPanel, which is responsible for drawing the graph
	 * and recognizing vertices pressed
	 * @param evt
	 */
	private void BtnRemoveMouseReleased(java.awt.event.MouseEvent evt)
	{
		if (GraphPanel.cops.size()>0)
		{
			GraphPanel.setRemovingCop(true);
			removingCopStep = true;
		}
	}

	/**
	 * Handling reveal action. Method executes specific method from GraphPanel which causes highlighting connected
	 * component containing fugitive location in the green color for few seconds. There is no limit for reveal actions
	 * @param evt
	 */
	private void BtnRevealMouseReleased(java.awt.event.MouseEvent evt)
	{
		GraphPanel.reveal(fugitivePosition, decontaminatedVertices);
	}

	/**
	 * Handling restart action. It cleans all cops and causes game to return to initial state. At this point
	 * after restart action fugitive location does not change.
	 * @param evt
	 */
	private void BtnRestartMouseReleased(java.awt.event.MouseEvent evt)
	{
		this.decontaminatedVertices = new HashSet<>();
		GraphPanel.clean();
		//createLayout();
		copsUsed = 0;
		CopsUsed.setText("0");
		LblGameStatus.setText("");
		BtnAdd.setEnabled(true);
		BtnRemove.setEnabled(true);
		this.repaint(0);
	}

	/**
	 * Displaying string in @param status on the status bar
	 * @param status
	 */

	public void displayStatus(String status)
	{
		LblGameStatus.setText(status);
	}

	/**
	 * Given set of sets of vertices, which are connected components of certain graph find the one that contains certain
	 * vertex. If vertex was not found, empty Set is returned
	 * @param vertex - vertex to find
	 * @param connectedComponents - set of sets of vertices which are connected components of the graph
	 * @return
	 */

	public Set<T> findConnectedComponentContaining(T vertex, Set<Set<T>> connectedComponents)
	{
		for (Set<T> connectedComponent: connectedComponents)
		{
			if(connectedComponent.contains(vertex))
			{
				return connectedComponent;
			}
		}
		return new HashSet<T>();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[])
	{
	    /* Set the Nimbus look and feel */

		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
		try
		{
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
			{
				if ("Nimbus".equals(info.getName()))
				{
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex)
		{
			java.util.logging.Logger.getLogger(CopsAndRobbers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex)
		{
			java.util.logging.Logger.getLogger(CopsAndRobbers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex)
		{
			java.util.logging.Logger.getLogger(CopsAndRobbers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex)
		{
			java.util.logging.Logger.getLogger(CopsAndRobbers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

        /* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable()
		{
			public void run()
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
				CopsAndRobbers<Integer> game = new CopsAndRobbers(G);
				game.setVisible(true);
			}
		});
	}


}
