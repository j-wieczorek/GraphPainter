# GraphPainter
Visualization utility for Jdrasil library enabling user to display processed graphs 
## About
GraphPainter is a plugin for [Jdrasil](https://github.com/maxbannach/Jdrasil) (library for computing tree decompositions of graphs by Max Bannach, Sebastian Berndt, and Thorsten Ehlers). Having the graph as an object of Jdrasil's class `Graph` drawing this graph requires only 2 lines of code:

```
DrawGraph G = new DrawGraph(g);
G.draw();
```

For drawing the graph, automatic alignment algorithm was implemented (class VertexSpacer), although it's performance still needs improvement. However, interface of graph drawing function enables moving vertices by dragging and dropping, so that display can be adjusted manually.

Class CopsAndRobbers allows user to play the graph-based game called 'Cops And Robbers'. The objective of this game is to find a robber hiding in one of the vertices of the graph with limited amount of cops. You win when you find a vertex in which robber is hiding, and you lose when robber gets access to the vertex previously occupied by a cop (so if robber is in the same connected component as previously 'cleared' vertex). When you run out of cops you can perform a reveal step, in which connected component in which robber is hiding will be marked green for some moment. Cops And Robbers game was developed based on the description given in: `'A Simple Bottom-Up Approach to Tree Width: Understanding Tamaki's Algorithm'` by Max Bannach and Sebastian Berndt.

## Prerequisities

* [Jdrasil](https://github.com/maxbannach/Jdrasil)
* Java 8
