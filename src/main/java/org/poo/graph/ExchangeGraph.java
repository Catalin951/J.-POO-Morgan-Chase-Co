package org.poo.graph;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.poo.exchange.Exchange;

public class ExchangeGraph {
    private static ExchangeGraph instance;
    private final Graph<String, DefaultWeightedEdge> exchangeGraph;

    public ExchangeGraph(final Exchange[] exchanges) {
        exchangeGraph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        for (Exchange exchange : exchanges) {
            String from = exchange.getFrom();
            String to = exchange.getTo();
            double rate = exchange.getRate();
            exchangeGraph.addVertex(from);
            exchangeGraph.addVertex(to);
            DefaultWeightedEdge edge = exchangeGraph.addEdge(from, to);
            if (edge != null) {
                exchangeGraph.setEdgeWeight(edge, rate);
            }
            DefaultWeightedEdge reverseEdge = exchangeGraph.addEdge(to, from);
            if (reverseEdge != null) {
                exchangeGraph.setEdgeWeight(reverseEdge, 1.0 / rate);
            }
        }
    }

    public double convertCurrency(String fromCurrency, String toCurrency, double amount) {
        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(exchangeGraph);
        GraphPath<String, DefaultWeightedEdge> path = dijkstra.getPath(fromCurrency, toCurrency);

        if (path == null) {
            throw new IllegalArgumentException("No path found for " + fromCurrency + " and " + toCurrency);
        }

        double totalRate = 1.0;
        for (DefaultWeightedEdge edge : path.getEdgeList()) {
            double weight = exchangeGraph.getEdgeWeight(edge);
            totalRate *= weight;
        }
        return amount * totalRate;
    }

    public void printGraph() {
        System.out.println("Graph Vertices: " + exchangeGraph.vertexSet());
        System.out.println("Graph Edges: " + exchangeGraph.edgeSet());
    }
}
