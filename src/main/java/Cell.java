import org.davidmoten.text.utils.WordWrap;

import java.util.ArrayList;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import org.davidmoten.text.utils.WordWrap;

public class Cell {

    Cell parent;
    Object cell;
    Match match;
    ArrayList<Cell> cells = new ArrayList<Cell>();

    //0 is team, 1 is match, 2 is holder, 3 is main path
    int type;

    //team
    Cell(Object cell, Cell holder){
        this.cell = cell;
        parent = holder;
        type = 0;
    }

    //match
    Cell(Cell holder, Match match, mxGraph graph){
        this.match = match;
        parent = holder;
        type = 1;
        int MFontSize = 20;
        int TFontSize = 12;

        int cellSize = 75;
        int vCellSpace = 35;
        int hCellSpace = 20;

        int totalTeams = match.teams.size();
        int rows = (int) (0.708 * Math.pow(totalTeams, 0.5));

        if (rows != 0) {
            String matchName = match.Shortened(false);

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




            Object base = graph.insertVertex(holder.cell, null, matchWrapped, 0, 0, cellLength, cellHeight, "matchS;spacingTop=" + 5);
            holder.cells.add(this);

            for (int j = 0; j < match.teams.size(); j++) {
                String teamName = match.teams.get(j).name;
                String teamWrapped = WordWrap.from(teamName).maxWidth(cellSize/(TFontSize/1.9)).insertHyphens(true).wrap();
                int cellX = (hCellSpace / 2) + (j / rows * (cellSize + hCellSpace));
                int cellY = (titleSize) + ((j % rows) * (cellSize + vCellSpace));

                //String cellStyle = "teamS;spacingTop=" + ((cellSize / 2) + ((countLines(teamWrapped) - 1) * (Math.round(TFontSize * .783621) / 2)) + Math.round(TFontSize * .783621) + 10);
                //String cellStyle = "teamS;spacingTop=" + (((countLines(teamWrapped) - 1) * TFontSize));
                String cellStyle = "teamS;strokeColor=green;spacingTop=" + (cellSize + 3);

                Object cell = graph.insertVertex(base, null, teamWrapped, cellX, cellY, cellSize, cellSize, cellStyle);
                Cell newCell = new Cell(cell, this);
                cells.add(newCell);
            }
        } else {
            System.out.println(match + " has no teams");
        }
    }

    //holder
    Cell(Cell holder, mxGraph graph){
        parent = holder;
        Object base = graph.insertVertex(holder.cell, null, "", 0, 0, 0, 0, "holderS");
        cell = base;
        type = 2;
    }

    //main path
    Cell(String dummy, Object holder){
        cell = holder;
        type = 3;
    }


    private static String[] splitLines(String str){
        return str.split("\r\n|\r|\n");
    }
}
