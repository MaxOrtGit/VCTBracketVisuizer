

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import org.davidmoten.text.utils.WordWrap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;


public class Visualize extends JFrame
{
    public Visualize(Match start) {
        super("Road To " + start.Shortened(true));

        String name = "Road To " + start.Shortened(true);
        mxGraph graph = new mxGraph();

        int MFontSize = 20;
        int TFontSize = 12;

        mxStylesheet stylesheet = graph.getStylesheet();
        Hashtable<String, Object> style = new Hashtable<String, Object>();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        style.put(mxConstants.STYLE_FILLCOLOR, "white");
        style.put(mxConstants.STYLE_FONTCOLOR, "black");
        style.put(mxConstants.STYLE_FONTFAMILY, "Arial");
        style.put(mxConstants.STYLE_STROKEWIDTH, 3);
        style.put(mxConstants.STYLE_FONTSIZE, TFontSize);
        style.put(mxConstants.STYLE_VERTICAL_ALIGN, "ALIGN_TOP");

        stylesheet.putCellStyle("teamS", style);


        mxStylesheet Msheet = graph.getStylesheet();
        Hashtable<String, Object> Mstyle = new Hashtable<String, Object>();
        Mstyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        Mstyle.put(mxConstants.STYLE_FILLCOLOR, "white");
        Mstyle.put(mxConstants.STYLE_FONTCOLOR, "black");
        Mstyle.put(mxConstants.STYLE_FONTFAMILY, "Arial");
        Mstyle.put(mxConstants.STYLE_STROKECOLOR, "black");
        Mstyle.put(mxConstants.STYLE_STROKEWIDTH, 1);
        Mstyle.put(mxConstants.STYLE_ROUNDED, "1");
        Mstyle.put(mxConstants.STYLE_ARCSIZE, "5");
        Mstyle.put(mxConstants.STYLE_FONTSIZE, MFontSize);
        Mstyle.put(mxConstants.STYLE_VERTICAL_ALIGN, "ALIGN_TOP");

        Msheet.putCellStyle("matchS", Mstyle);

        mxStylesheet Hsheet = graph.getStylesheet();
        Hashtable<String, Object> Hstyle = new Hashtable<String, Object>();
        Hstyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        Hstyle.put(mxConstants.STYLE_FILLCOLOR, "white");
        Hstyle.put(mxConstants.STYLE_STROKECOLOR, "white");
        Hstyle.put(mxConstants.STYLE_VERTICAL_ALIGN, "ALIGN_TOP");

        Msheet.putCellStyle("holderS", Hstyle);

        Cell graphPath = new Cell("", graph.getDefaultParent());

        Cell startCell;

        graph.getModel().beginUpdate();
        try {
            startCell = subSection(start, graphPath, graph);
        } finally {
            graph.getModel().endUpdate();
        }


        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);

        //print image
        try {
            System.out.println("Saving Photo");
            BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, 1, Color.WHITE, true, null);

            ImageIO.write(image, "png", new File("/Users/Mortman2299/Documents/Images/"+name+".png"));
        } catch (Exception exception) {
            System.out.println("WHY");
        }


        graph.getModel().endUpdate();
    }

    public Cell subSection(Match match, Cell path, mxGraph graph) {
        Cell holder = Create(null, path, true, graph);

        ArrayList<Cell> cellsInSection = new ArrayList<Cell>();
        cellsInSection.add(Create(match, holder, false, graph));

        ArrayList<Match> froms = getFroms(match, false);
        System.out.println(froms);

        if(froms.size() >= 1) {
            for (int i = 0; i < froms.size(); i++) {
                if(froms.get(i).region.equals(match.region)){
                    //create normal match
                    cellsInSection.add(Create(froms.get(i), holder, false, graph));
                } else {
                    //create new subsection
                    cellsInSection.add(subSection(froms.get(i), holder, graph));
                }
            }
        }

        Organize(cellsInSection);
        return holder;
    }

    public Cell Create(Match match, Cell holder, boolean isHolder, mxGraph graph){
        if(isHolder){
            //make clear cell
            return new Cell(holder, graph);
        } else {
            //make match cell
            return new Cell(holder, match, graph);
        }
    }

    public void Organize(ArrayList<Cell> cells){
        System.out.println("LOL you haven't coded it");
    }

    public ArrayList<Match> getFroms(Match match, Boolean duplicates){
        ArrayList<Match> froms = new ArrayList<Match>();
        if (duplicates){
            froms = match.from;
        } else {
            for (int i = 0; i < match.from.size(); i++) {
                if (froms.size() != 0) {
                    boolean safe = true;
                    for (int j = 0; j < froms.size(); j++) {
                        if (!(match.from.get(i) != null && match.from.get(i) != froms.get(j))) {
                            safe = false;
                        }
                    }
                    if (safe){
                        froms.add(match.from.get(i));
                    }
                } else {
                    if (match.from.get(i) != null) {
                        froms.add(match.from.get(i));
                    }
                }
            }
        }
        return froms;
    }

    public Visualize(ArrayList<Match> matches)
    {
        super("Stage One NA");
        String name = "Stage One NA";

        mxGraph graph = new mxGraph();

        Object parent = graph.getDefaultParent();

        int MFontSize = 20;
        int TFontSize = 12;

        int cellSize = 75;
        int cellSpacing = 30;
        int vCellSpace = 35;
        int hCellSpace = 20;

        mxStylesheet stylesheet = graph.getStylesheet();
        Hashtable<String, Object> style = new Hashtable<String, Object>();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        style.put(mxConstants.STYLE_FILLCOLOR, "white");
        style.put(mxConstants.STYLE_FONTCOLOR, "black");
        style.put(mxConstants.STYLE_FONTFAMILY, "Arial");
        style.put(mxConstants.STYLE_STROKEWIDTH, 3);
        style.put(mxConstants.STYLE_FONTSIZE, TFontSize);
        style.put(mxConstants.STYLE_VERTICAL_ALIGN, "ALIGN_TOP");

        stylesheet.putCellStyle("teamS", style);


        mxStylesheet Msheet = graph.getStylesheet();
        Hashtable<String, Object> Mstyle = new Hashtable<String, Object>();
        Mstyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        Mstyle.put(mxConstants.STYLE_FILLCOLOR, "white");
        Mstyle.put(mxConstants.STYLE_FONTCOLOR, "black");
        Mstyle.put(mxConstants.STYLE_FONTFAMILY, "Arial");
        Mstyle.put(mxConstants.STYLE_STROKECOLOR, "black");
        Mstyle.put(mxConstants.STYLE_STROKEWIDTH, 1);
        Mstyle.put(mxConstants.STYLE_ROUNDED, "1");
        Mstyle.put(mxConstants.STYLE_ARCSIZE, "5");
        Mstyle.put(mxConstants.STYLE_FONTSIZE, MFontSize);
        Mstyle.put(mxConstants.STYLE_VERTICAL_ALIGN, "ALIGN_TOP");

        Msheet.putCellStyle("matchS", Mstyle);

        graph.getModel().beginUpdate();


        try
        {
            int lengthOver = 20;

            for (int i = 0; i < matches.size(); i++) {

                int totalTeams = matches.get(i).teams.size();
                int rows = (int) (0.708 * Math.pow(totalTeams, 0.5));

                if (rows != 0) {
                    String matchName = matches.get(i).Shortened(false);

                    int cellLength = (cellSize + hCellSpace) * (totalTeams / rows);
                    String matchWrapped = WordWrap.from(matchName).maxWidth(cellLength / (MFontSize/2.075)).insertHyphens(true).wrap();

                    String[] splittedLine = splitLines(matchWrapped);

                    int lowestNext = 4;
                    for (int l = 1; l < splittedLine.length; l++) {
                        int untilNext = 0;
                        for (int s = 0; s < splittedLine[l].length(); s++) {
                            if(splittedLine[l].charAt(s) == ' '){
                                untilNext = s;
                                break;
                            }
                        }
                        if (untilNext < lowestNext){
                            lowestNext = untilNext;
                        }
                    }
                    if (lowestNext <= 3){
                        System.out.println(lowestNext);
                        matchWrapped = WordWrap.from(matchName).maxWidth(cellLength / (MFontSize/1.5)).insertHyphens(true).wrap();

                    }

                    int titleSize = 10 + (MFontSize * splittedLine.length * 2);
                    int cellHeight = titleSize + rows * (vCellSpace + cellSize);


                    Object base = graph.insertVertex(parent, null, matchWrapped, lengthOver, 50, cellLength, cellHeight, "matchS;spacingTop=" + 5);


                    lengthOver += cellLength + cellSpacing;

                    for (int j = 0; j < matches.get(i).teams.size(); j++) {
                        String teamName = matches.get(i).teams.get(j).name;
                        String teamWrapped = WordWrap.from(teamName).maxWidth(cellSize/(TFontSize/1.9)).insertHyphens(true).wrap();
                        int cellX = (hCellSpace / 2) + (j / rows * (cellSize + hCellSpace));
                        int cellY = (titleSize) + ((j % rows) * (cellSize + vCellSpace));

                        //String cellStyle = "teamS;spacingTop=" + ((cellSize / 2) + ((countLines(teamWrapped) - 1) * (Math.round(TFontSize * .783621) / 2)) + Math.round(TFontSize * .783621) + 10);
                        //String cellStyle = "teamS;spacingTop=" + (((countLines(teamWrapped) - 1) * TFontSize));
                        String cellStyle = "teamS;strokeColor=green;spacingTop=" + (cellSize + 3);

                        Object cell = graph.insertVertex(base, null, teamWrapped, cellX, cellY, cellSize, cellSize, cellStyle);
                    }
                } else {
                    System.out.println(matches.get(i) + " has no teams");
                }
            }
        }
        finally
        {
            graph.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);


        //print image
        try {
            System.out.println("Saving Photo");
            BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, 1, Color.WHITE, true, null);

            ImageIO.write(image, "png", new File("/Users/Mortman2299/Documents/Images/"+name+".png"));
        } catch (Exception exception) {
            System.out.println("WHY");
        }

    }


    public static void main(String[] args)
    {
        test frame = new test();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 320);
        frame.setVisible(true);
    }


    private static String[] splitLines(String str){
        return str.split("\r\n|\r|\n");
    }

}