

import javax.swing.JFrame;

import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxIGraphLayout;
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

            Object parejnt = graph.getDefaultParent();

            Object v1 = graph.insertVertex(base, null, "one", 12, 23, 50, 50,"teamS");
            Object v2 = graph.insertVertex(base, null, "two", 64, 23, 50, 50, "teamS");
            Object v3 = graph.insertVertex(base, null, "three", 2, 64, 50, 50,"teamS");
            Object v4 = graph.insertVertex(base, null, "four", 23, 64, 50, 50,"teamS");


            Object base2 = graph.insertVertex(parent, null, "null", 57, 34, 100, 120,"matchS;spacingTop=-47.5");


            Object v21 = graph.insertVertex(base2, null, "one", 78, 14, 50, 50,"teamS");
            Object v22 = graph.insertVertex(base2, null, "two", 64, 53, 50, 50, "teamS");
            Object v23 = graph.insertVertex(base2, null, "three", 43, 70, 50, 50,"teamS");
            Object v24 = graph.insertVertex(base2, null, "four", 12, 23, 50, 50,"teamS");

            ArrayList<Object> CTC = new ArrayList<>();
            CTC.add(v1);
            graph.toggleCells(false, CTC.toArray());
            graph.moveCells(CTC.toArray(), 10, 16);
            graph.toggleCells(true, CTC.toArray());

            graph.insertEdge(parent, null, "", base, base2);
            mxIGraphLayout layout = new mxFastOrganicLayout(graph);
            layout.execute(parejnt);
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