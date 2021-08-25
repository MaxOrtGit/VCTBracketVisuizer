

import javax.swing.JFrame;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import java.util.ArrayList;
import java.util.Hashtable;


public class test extends JFrame
{


    public test()
    {
        super("Stage One NA");

        mxGraph graph = new mxGraph();

        Object parent = graph.getDefaultParent();

        mxStylesheet stylesheet = graph.getStylesheet();
        Hashtable<String, Object> style = new Hashtable<String, Object>();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        style.put(mxConstants.STYLE_FILLCOLOR, "white");
        style.put(mxConstants.STYLE_STROKECOLOR, "black");

        stylesheet.putCellStyle("teamS", style);

        mxStylesheet Msheet = graph.getStylesheet();
        Hashtable<String, Object> Mstyle = new Hashtable<String, Object>();
        Mstyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        Mstyle.put(mxConstants.STYLE_FILLCOLOR, "white");
        Mstyle.put(mxConstants.STYLE_STROKECOLOR, "black");
        Mstyle.put(mxConstants.STYLE_ROUNDED, "1");

        Msheet.putCellStyle("matchS", Mstyle);

        graph.getModel().beginUpdate();

        try
        {

            Object base = graph.insertVertex(parent, null, "null", 200, 20, 100, 120,"matchS;spacingTop=-47.5");


            Object v1 = graph.insertVertex(base, null, "one", 0, 20, 50, 50,"teamS");
            Object v2 = graph.insertVertex(base, null, "two", 50, 20, 50, 50, "teamS");
            Object v3 = graph.insertVertex(base, null, "three", 0, 70, 50, 50,"teamS");
            Object v4 = graph.insertVertex(base, null, "four", 50, 70, 50, 50,"teamS");

            ArrayList<Object> CTC = new ArrayList<>();
            CTC.add(v1);
            graph.toggleCells(false, CTC.toArray());
            graph.moveCells(CTC.toArray(), 10, 10);
            graph.toggleCells(true, CTC.toArray());

            //graph.insertEdge(parent, null, "Edge", v1, v2);
        }
        finally
        {
            graph.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);
    }

    public static void main(String[] args)
    {
        test frame = new test();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 320);
        frame.setVisible(true);
    }

}