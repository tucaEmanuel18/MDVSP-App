package com.graphPainter;


import com.graph.Node;
import com.repair.RouteNode;
import com.graph.WeightEdge;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GraphPainter {


    public static void paint(Graph<Node, WeightEdge> graph, String pictureName) throws IOException {
        JGraphXAdapter<Node, WeightEdge> graphAdapter =
                new JGraphXAdapter<Node, WeightEdge>(graph);

        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);

        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image =
                mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File("src/test/" + pictureName +".jpg");
        ImageIO.write(image, "JPG", imgFile);
    }

    public static void paint(Graph<Node, WeightEdge> graph) throws IOException {
        JGraphXAdapter<Node, WeightEdge> graphAdapter =
                new JGraphXAdapter<Node, WeightEdge>(graph);

        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);

        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image =
                mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File("src/test/network.jpg");
        ImageIO.write(image, "JPG", imgFile);
    }

    public static void routePaint(Graph<RouteNode, WeightEdge> graph, String pictureName) throws IOException {
        JGraphXAdapter<RouteNode, WeightEdge> graphAdapter =
                new JGraphXAdapter<RouteNode, WeightEdge>(graph);

        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);

        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image =
                mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File("src/test/" + pictureName +".jpg");
        ImageIO.write(image, "JPG", imgFile);
    }

    public static void routePaint(Graph<RouteNode, WeightEdge> graph) throws IOException {
        JGraphXAdapter<RouteNode, WeightEdge> graphAdapter =
                new JGraphXAdapter<RouteNode, WeightEdge>(graph);

        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);

        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image =
                mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File("src/test/network.jpg");
        ImageIO.write(image, "JPG", imgFile);
    }


}
