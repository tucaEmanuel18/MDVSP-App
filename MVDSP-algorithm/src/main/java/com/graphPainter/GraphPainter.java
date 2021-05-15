package com.graphPainter;


import com.graph.Edge;
import com.graph.Node;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GraphPainter {


    public static void paint(Graph<Node, Edge> graph) throws IOException {
        JGraphXAdapter<Node, Edge> graphAdapter =
                new JGraphXAdapter<Node, Edge>(graph);

        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);

        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image =
                mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File("src/test/network.jpg");
        ImageIO.write(image, "JPG", imgFile);
    }
}
