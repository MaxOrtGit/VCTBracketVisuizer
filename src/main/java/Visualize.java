

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxCellEditor;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import org.davidmoten.text.utils.WordWrap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Random;


public class Visualize extends JFrame
{

    ArrayList<Cell> allCells = new ArrayList<>();

    public Visualize(Match start) {
        super("Road To " + start.Shortened(true));



        String name = "Road To " + start.Shortened(true);
        System.out.println("start point " + name);
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
        Hstyle.put(mxConstants.STYLE_OPACITY, "0");
        Mstyle.put(mxConstants.STYLE_STROKECOLOR, "black");
        Mstyle.put(mxConstants.STYLE_STROKE_OPACITY, 50f);

        Hstyle.put(mxConstants.STYLE_STROKEWIDTH, 1);
        Hstyle.put(mxConstants.STYLE_VERTICAL_ALIGN, "ALIGN_TOP");

        Hsheet.putCellStyle("holderS", Hstyle);

        Cell graphPath = new Cell("", graph.getDefaultParent());

        Cell startCell;

        try {
            startCell = subSection(start, graphPath, graph, null);
        } finally {
            graph.getModel().endUpdate();
        }


        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);

        //print image
        try {
            System.out.println("Saving Photo");
            BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, 1, Color.WHITE, true, null);

            Random rand = new Random();
            ImageIO.write(image, "png", new File("/Users/Mortman2299/Documents/Images/" + name + " " + rand.nextInt()+ ".png"));
        } catch (Exception exception) {
            System.out.println("WHY");
        }


        graph.getModel().endUpdate();
    }

    public Cell subSection(Match match, Cell path, mxGraph graph, ArrayList<Match> fromsToAdd) {

        System.out.println("\n" + match.extension);
        Cell holder = Create(null, path, true, graph);

        ArrayList<Cell> cellsInSection = new ArrayList<Cell>();
        cellsInSection.add(Create(match, holder, false, graph));

        ArrayList<Match> froms = getFroms(match, false);

        if(fromsToAdd != null){
            froms = addFroms(froms, fromsToAdd);
        }
        froms = compressFroms(froms);

        if(fromsToAdd != null){
            froms.remove(match);
        }
        System.out.println(froms);

        if(froms.size() >= 1) {
            for (int i = 0; i < froms.size(); i++) {
                if(froms.get(i).stage.equals(match.stage)) {
                    if (froms.get(i).region.equals(match.region) && froms.get(i).subRegion.equals(match.subRegion)) {
                        //create normal match
                        Cell cell = Create(froms.get(i), holder, false, graph);
                        cellsInSection.add(cell);
                        allCells.add(cell);
                        //graph.insertEdge(graph.getDefaultParent(), null, "", cellsInSection.get(cellsInSection.size()-1).cell, cellsInSection.get(0).cell);
                        froms = addFroms(froms, getFroms(froms.get(i), false));
                        if(fromsToAdd != null){
                            froms.remove(match);
                        }
                    } else {
                        //create new subsection
                        ArrayList<Match> FTA = new ArrayList<>();
                        for (int j = i + 1; j < froms.size(); j++) {
                            if (froms.get(j).region.equals(froms.get(i).region) && froms.get(j).subRegion.equals(froms.get(i).subRegion)) {
                                FTA.add(froms.get(j));
                                froms.remove(j);
                                j--;
                            }
                        }
                        cellsInSection.add(subSection(froms.get(i), holder, graph, FTA));
                    }
                }
            }
        }
        Organize(holder, cellsInSection, graph);
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

    public ArrayList<ArrayList<Cell>> Organize(Cell parent, ArrayList<Cell> cells, mxGraph graph){

        int cellSpacing = 30;
        ArrayList<ArrayList<Cell>> newOrder = new ArrayList<>();
        newOrder.add(new ArrayList<Cell>());
        newOrder.get(0).add(cells.get(0));
        if(cells.size() > 1) {
            for (int i = 1; i < cells.size(); i++) {
                Cell cell = cells.get(i);
                if(cell.type == 1){
                    boolean added = false;
                    for (int j = 0; j < newOrder.get(0).size(); j++) {
                        Cell sCell = newOrder.get(0).get(j);
                        if(cell.type == 1){
                            if(cell.match.challenger.length() == 13 && cell.match.challenger.contains("Challengers")){
                                if(sCell.match.challenger.length() == 13 && cell.match.challenger.contains("Challengers")){
                                    if(Integer.parseInt(cell.match.challenger.substring(12)) < Integer.parseInt(sCell.match.challenger.substring(12))){
                                        //System.out.println(sCell);
                                        newOrder.get(0).add(newOrder.get(0).indexOf(sCell), cell);
                                        added = true;
                                        break;
                                    }
                                } else {
                                    newOrder.get(0).add(j, cell);
                                    added = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (!added){
                        newOrder.get(0).add(cell);
                    }
                } else if (cell.type == 2){
                    if(newOrder.size() == 1) {
                        newOrder.add(new ArrayList<Cell>());
                    }
                    newOrder.get(1).add(cell);
                } else {
                    System.out.println("ERROR, not a match or holder");
                }
            }
        }




        if (newOrder.size() == 1) {
            double totalWidthOver = cellSpacing;

            for (Cell cell : newOrder.get(0)) {
                //System.out.println("breaker " + cell.type);
                if (cell.match == null) {
                    System.out.println("error 1 Vowep" + cell.match.extension);
                }
                mxGeometry cellGeo = graph.getCellGeometry(cell.cell);
                double width = cellGeo.getWidth();
                totalWidthOver += width + cellSpacing;
            }

            //System.out.println(totalWidthOver + "widthOver");
            mxGeometry parentGeo = graph.getCellGeometry(parent.cell);
            parentGeo.setWidth(totalWidthOver);
            parentGeo.setHeight(parentGeo.getHeight() + cellSpacing * 2);

            graph.updateCellSize(parent);
            double widthOver = cellSpacing;

            for (Cell cell : newOrder.get(0)) {
                mxGeometry cellGeo = graph.getCellGeometry(cell.cell);
                double width = cellGeo.getWidth();
                Object[] cellz = new Object[1];
                cellz[0] = cell.cell;
                graph.moveCells(cellz, widthOver, cellSpacing);
                widthOver += width + cellSpacing;
                //System.out.println(widthOver + " " + width);
                //System.out.println(cell.cell);
            }

            graph.updateCellSize(parent);

            //System.out.println(parent);

            return newOrder;
        } else {
            double totalWidthOver = cellSpacing;
            double totalHeightOver = cellSpacing;

            double currentOver = 0;
            for (Cell cell : newOrder.get(1)) {
                if (cell.match != null) {
                    System.out.println("error 2 Vsjsa" + cell.match.extension);
                }
                mxGeometry cellGeo = graph.getCellGeometry(cell.cell);
                if(currentOver < cellGeo.getWidth() + cellSpacing) {
                    currentOver = cellGeo.getWidth() + cellSpacing;
                }
                //System.out.println(currentOver);
                totalHeightOver += cellGeo.getHeight() + cellSpacing;
            }
            totalWidthOver += currentOver;
            for (Cell cell : newOrder.get(0)) {
                //System.out.println("breaker " + cell.type);
                if (cell.match == null) {
                    System.out.println("error 1 Vowep" + cell.match.extension);
                }
                mxGeometry cellGeo = graph.getCellGeometry(cell.cell);
                double width = cellGeo.getWidth();
                totalWidthOver += width + cellSpacing;
            }
            mxGeometry parentGeo = graph.getCellGeometry(parent.cell);
            parentGeo.setWidth(totalWidthOver);
            parentGeo.setHeight(totalHeightOver);

            graph.updateCellSize(parent);

            double heightOver = cellSpacing;

            for (Cell cell : newOrder.get(1)) {
                mxGeometry cellGeo = graph.getCellGeometry(cell.cell);
                double height = cellGeo.getHeight();
                Object[] cellz = new Object[1];
                cellz[0] = cell.cell;
                graph.moveCells(cellz, 0, heightOver);
                heightOver += height + cellSpacing;
                //System.out.println(widthOver + " " + width);
                //System.out.println(cell.cell);
            }

            double widthOver = cellSpacing;

            for (Cell cell : newOrder.get(0)) {
                mxGeometry cellGeo = graph.getCellGeometry(cell.cell);
                double width = cellGeo.getWidth();
                Object[] cellz = new Object[1];
                cellz[0] = cell.cell;
                graph.moveCells(cellz, widthOver + currentOver - cellSpacing, totalHeightOver/2 - cellGeo.getHeight()/2);
                widthOver += width + cellSpacing;
                //System.out.println(widthOver + " " + width);
                //System.out.println(cell.cell);
            }

            graph.updateCellSize(parent);

            return newOrder;
        }
    }

    public ArrayList<Cell> Organize(Cell parent, ArrayList<Cell> cells, mxGraph graph, boolean f){

        int cellSpacing = 30;


        ArrayList<Cell> newOrder = new ArrayList<>();
        newOrder.add(cells.get(0));
        if(cells.size() > 1) {
            for (int i = 1; i < cells.size(); i++) {
                Cell cell = cells.get(i);
                boolean added = false;
                for (int j = 0; j < newOrder.size(); j++) {
                    Cell sCell = newOrder.get(j);
                    if(cell.type == 1){
                        if(cell.match.challenger.length() == 13 && cell.match.challenger.contains("Challengers")){
                            if(sCell.match.challenger.length() == 13 && cell.match.challenger.contains("Challengers")){
                                if(Integer.parseInt(cell.match.challenger.substring(12)) < Integer.parseInt(sCell.match.challenger.substring(12))){
                                    newOrder.add(newOrder.indexOf(sCell), cell);
                                    added = true;
                                    break;
                                }
                            } else {
                                newOrder.add(j, cell);
                                added = true;
                                break;
                            }
                        }
                    }
                }
                if (!added){
                    newOrder.add(cell);
                }
            }
        }



        double totalWidthOver = cellSpacing;

        for (Cell cell : newOrder) {
            //System.out.println("breaker " + cell.type);
            if(cell.match != null) {
                System.out.println("error" + cell.match.extension);
            }
            mxGeometry cellGeo = graph.getCellGeometry(cell.cell);
            double width = cellGeo.getWidth();
            totalWidthOver += width + cellSpacing;
        }

        //System.out.println(totalWidthOver + "widthOver");
        mxGeometry parentGeo = graph.getCellGeometry(parent.cell);
        parentGeo.setWidth(totalWidthOver);

        graph.updateCellSize(parent);
        double widthOver = cellSpacing;

        for (Cell cell : newOrder) {
            mxGeometry cellGeo = graph.getCellGeometry(cell.cell);
            double width = cellGeo.getWidth();
            Object[] cellz = new Object[1];
            cellz[0] = cell.cell;
            graph.moveCells(cellz, widthOver,0);
            widthOver += width + cellSpacing;
            //System.out.println(widthOver + " " + width);
            //System.out.println(cell.cell);
        }

        graph.updateCellSize(parent);

        //System.out.println(parent);

        return newOrder;
    }

    public ArrayList<Match> getFroms(Match match, Boolean duplicates){
        ArrayList<Match> froms = match.from;
        if (!duplicates){
            froms = compressFroms(froms);
        }
        return froms;
    }

    public ArrayList<Match> addFroms(ArrayList<Match> original, ArrayList<Match> second){
        ArrayList<Match> froms = original;
        for (int i = 0; i < second.size(); i++) {
            boolean safe = true;
            for (int j = 0; j < original.size(); j++) {
                if(second.get(i).equals(original.get(j))){
                    safe = false;
                }
            }
            if(safe){
                froms.add(second.get(i));
            }
        }
        return froms;
    }

    public ArrayList<Match> compressFroms(ArrayList<Match> array){
        ArrayList<Match> newArray = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            if (newArray.size() != 0) {
                boolean safe = true;
                for (int j = 0; j < newArray.size(); j++) {
                    if (array.get(i) == newArray.get(j)) {
                        safe = false;
                    }
                }
                if (safe && array.get(i) != null){
                    newArray.add(array.get(i));
                }
            } else {
                if (array.get(i) != null) {
                    newArray.add(array.get(i));
                }
            }
        }
        return newArray;
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
                        //System.out.println(lowestNext);
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



    private static String[] splitLines(String str){
        return str.split("\r\n|\r|\n");
    }

}