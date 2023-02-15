package com.question.app.graph;

import java.util.*;

/**
 * 有向有环无权图
 * @author Dioxide.CN
 * @date 2023/2/13 21:11
 * @since 1.0
 */
@SuppressWarnings("all")
public class UnweightedGraph<V> extends AbstractGraph<V> {
    /** Construct an empty graph */
    public UnweightedGraph() {
    }

    /** Construct a graph from vertices and edges stored in arrays */
    public UnweightedGraph(V[] vertices, int[][] edges) {
        super(vertices, edges);
    }

    /** Construct a graph from vertices and edges stored in List */
    public UnweightedGraph(List<V> vertices, List<Edge> edges) {
        super(vertices, edges);
    }

    /** Construct a graph for integer vertices 0, 1, 2 and edge list */
    public UnweightedGraph(List<Edge> edges, int numberOfVertices) {
        super(edges, numberOfVertices);
    }

    /** Construct a graph from integer vertices 0, 1, and edge array */
    public UnweightedGraph(int[][] edges, int numberOfVertices) {
        super(edges, numberOfVertices);
    }


}

