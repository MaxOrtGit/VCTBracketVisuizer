import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
///valorant/api.php?action=query&format=json&prop=revisions&pageids= osajofkljaklsfjkasfjlkasjklasfjklasf &rvprop=content&rvdir=older



/*


this code is bad
and inefficient
but it works


as of now


*/

public class DownloadAPI {

    public static ArrayList<Team> teams = new ArrayList<Team>();
    public static ArrayList<Match> matches = new ArrayList<Match>();

    public static ArrayList<String> allStages = new ArrayList<String>();
    public static ArrayList<String> allChallengers = new ArrayList<String>();
    public static ArrayList<String> allRegions = new ArrayList<String>();
    public static ArrayList<String> allSubRegions = new ArrayList<String>();



    public static boolean useBackup = true;



    public static void main(String[] args) throws Exception {


        int siteCount = 5;

        int less = 1;

        ArrayList<JSONObject> JSONTexts = new ArrayList<JSONObject>(); //fill
        ArrayList<String> IDSites = new ArrayList<String>(); //fill
        ArrayList<Integer> ids = new ArrayList<Integer>(); //fill

        if(!useBackup) {

            JSONObject query = (JSONObject) GetPage("https://liquipedia.net/valorant/api.php?action=query&format=json&list=categorymembers&cmpageid=810&cmlimit=max&cmstartsortkeyprefix=VALORANT%20Champions%20Tour&cmendsortkeyprefix=VALORANT%20Circuito%20de%20Elite%2F2021%2FRound%201").get("query");
            JSONArray categorymembers = new JSONArray();
            categorymembers = (JSONArray) query.get("categorymembers");
            categorymembers.remove(categorymembers.size() - 1);

            //can only pull less than 500 pages
            if(categorymembers.size() >= 495){
                System.out.println(categorymembers.size() + " game pages are full, fix it");
            }

            System.out.print("(");
            for (int i = 0; i < categorymembers.size(); i++) {
                JSONObject member = (JSONObject) categorymembers.get(i);
                String memberName = (String) member.get("title");

                if (!memberName.contains("Game Changers") && memberName.contains("VALORANT Champions Tour")) {
                    int id = (int) ((long) member.get("pageid"));
                    ids.add(id);
                    System.out.print(id + ", ");
                }
            }
            System.out.println(")");

            //System.out.println(categorymembers);

            int IDSite = 0;
            int IDSitecount = 0;

            for (int i = 0; i < ids.size() / less; i++) {
                int id = ids.get(i);
                if (IDSites.size() != IDSite) {
                    IDSites.set(IDSite, IDSites.get(IDSite) + "%7C" + id);
                } else {
                    IDSites.add(id + "");
                }
                IDSitecount++;
                if (IDSitecount >= siteCount) {
                    IDSite++;
                    IDSitecount = 0;
                }
            }

            System.out.println(ids.size());

            //TimeUnit.SECONDS.sleep(2);
            //JSONTexts.add(GetPage("https://liquipedia.net/valorant/api.php?action=query&format=json&prop=revisions&pageids=" + IDSites.get(0) +"&rvprop=content&rvsection=5&rvdir=older"));
            //TimeUnit.SECONDS.sleep(2);
            //JSONTexts.add(GetPage("https://liquipedia.net/valorant/api.php?action=query&format=json&prop=revisions&pageids=" + IDSites.get(1) +"&rvprop=content&rvsection=5&rvdir=older"));
            System.out.println("https://liquipedia.net/valorant/api.php?action=query&format=json&prop=revisions%7Cimages&pageids=" + IDSites.get(0) + "&rvprop=content&rvsection=5&rvdir=older&imlimit=max");


            System.out.println(0 + "/" + IDSites.size());
            for (int i = 0; i < IDSites.size(); i++) {
                TimeUnit.SECONDS.sleep(2);
                System.out.println((i + 1) + "/" + IDSites.size());
                JSONTexts.add(GetPage("https://liquipedia.net/valorant/api.php?action=query&format=json&prop=revisions%7Cimages&pageids=" + IDSites.get(i) + "&rvprop=content&rvsection=5&rvdir=older&imlimit=max"));
            }


            serializeDataOut(JSONTexts, "JSONTexts");
            serializeDataOut(IDSites, "IDSites");
            serializeDataOut(ids, "ids");


        } else {
            JSONTexts = serializeDataIn("JSONTexts");
            IDSites = serializeDataIn("IDSites");
            ids = serializeDataIn("ids");
        }




        int print = 2;

        for (int i = 0; i < IDSites.size(); i++){
            JSONObject sQuery = (JSONObject) JSONTexts.get(i).get("query");
            JSONObject sPages = (JSONObject) sQuery.get("pages");
            for (int j = 0; j < siteCount; j++) {
                if((i * 5) + j == ids.size()/less){
                    break;
                }
                JSONObject IDPage = (JSONObject) sPages.get((ids.get((i * 5) + j)).toString());
                JSONObject IDRevisions = (JSONObject) ((JSONArray) IDPage.get("revisions")).get(0);
                //System.out.println(IDRevisions);
                String pageText = (String) IDRevisions.get("*");

                ArrayList<Integer> indexes = indexesOf(pageText.toLowerCase(), "|team=");

                ArrayList<Integer> QIndexes = indexesOf(pageText.toLowerCase(), "|qualifier=");


                String officialTitle = (String) IDPage.get("title") + "/";
                //System.out.println(officialTitle);

                Match match = matchFromName(officialTitle, indexes, QIndexes,pageText);
                matches.add(match);

            }

        }
        for (Match match:matches) {

            boolean staBool = true;
            boolean chaBool = true;
            boolean regBool = true;
            boolean subRegBool = true;

            String sta = match.stage;
            String cha = match.challenger;
            String reg = match.region;
            String subReg = match.subRegion;

            if (allStages.size() != 0) {
                for (String stage : allStages) {
                    if(sta.equals("") || stage.equals(sta)) {
                        staBool = false;
                        break;
                    }
                }
            }
            if (staBool){
                if(!sta.equals("")) {
                    allStages.add(sta);
                }
            }

            if (allChallengers.size() != 0) {
                for (String challenger : allChallengers) {
                    if (challenger.equals(cha)) {
                        chaBool = false;
                        break;
                    }
                }
            }
            if (chaBool){
                if(!cha.equals("")) {
                    allChallengers.add(cha);
                }
            }

            if (allRegions.size() != 0) {
                for (String region : allRegions) {
                    if (reg.equals("") || region.equals(reg)) {
                        regBool = false;
                        break;
                    }
                }
            }
            if(regBool){
                if(!reg.equals("")) {
                    if(reg.equals("Asia")){
                        System.out.println(match + "ndhjasbdajhkdbsa");
                    }
                    allRegions.add(reg);
                }
            }

            if (allSubRegions.size() != 0) {
                for (String subRegion: allSubRegions) {
                    if (subReg.equals("") || subRegion.equals(subReg)) {
                        subRegBool = false;
                        break;
                    }
                }
            }
            if (subRegBool){
                if(!subReg.equals("") && !subReg.equals("Open Qualifier")) {
                    allSubRegions.add(subReg);
                }
            }
        }


        for (int i = 0; i < allStages.size(); i++) {
            if(isInteger(allStages.get(i))){
                allStages.set(i, "Stage "+ allStages.get(i));
            }
        }

        for (int i = 0; i < allChallengers.size(); i++) {
            if(isInteger(allChallengers.get(i))){
                allChallengers.set(i, "Challengers "+ allChallengers.get(i));
            }
        }

        System.out.println(allStages);
        System.out.println(allChallengers);
        System.out.println(allRegions);
        System.out.println(allSubRegions);


        //System.out.println(matches);

        System.out.println("\n\n\n\n\n");
        for (Match match:matches) {

            //gave each team a from
            if(match.teams.size() > match.fromTXT.size()){
                for(int i = match.fromTXT.size(); i < match.teams.size(); i++) {
                    match.fromTXT.add("Open Qualifiers");
                }
                if(match.teams.size() != match.fromTXT.size()){
                    System.out.println("this is broke " + match.extension);
                }
                //System.out.println("added from " + match.extension);
            } else if (match.teams.size() < match.fromTXT.size()){
                //System.out.println("not all teams qualified LOL " + match.extension);
            }

            //print from
            System.out.println("\nnew match\n" + match.extension + "\nfrom start");

            //find team from fromTXT
            for (int i = 0; i < match.teams.size(); i++) {
                String froT = match.fromTXT.get(i);
                //print from
                System.out.println(froT);

                if(froT.equals("Open Qualifiers")) {
                    match.from.add(null);
                } else if(froT.equals("First_Strike/Japan|First Strike Top 4")) {
                    match.fromTXT.set(i, "First Strike");
                    match.from.add(null);
                } else if(froT.equals("ed into Quarterfina") || froT.equals("vit")) {
                    match.fromTXT.set(i, "Seeded Into Quarterfinals");
                    match.from.add(null);
                } else if(froT.equals("vit")) {
                    match.fromTXT.set(i, "Invited");
                    match.from.add(null);
                } else if(froT.equals("/Closed Qualifier|Closed Qualifier")) {
                    match.fromTXT.set(i, "Closed Qualifiers");
                    match.from.add(null);
                } else {
                    if(froT.contains("|")) {
                        froT = froT.substring(0, froT.indexOf("|"));
                    }

                    froT = froT.replace("_", " ");
                    String newT = "VALORANT Champions Tour/2021/";
                    if(froT.contains("VALORANT Champions Tour/2021/")){
                        newT = froT + "/";
                    } else {

                        froT = " " + froT.replace(",", " ") + " ";

                        String tempReg = "";
                        for (String region : allRegions) {

                            if (froT.contains(" " + region + " ") && region.length() > tempReg.length()) {
                                tempReg = region;
                            }

                            //froT = froT.replace(region + " ", region + "/");
                        }
                        newT = newT + tempReg + "/";
                        for (String stage : allStages) {
                            froT = " " + froT + " ";
                            if (froT.contains(" " + stage + " ")) {
                                newT = newT + stage + "/";
                                break;
                            }

                            //froT = froT.replace(stage + " ", stage + "/");
                        }
                        for (String challenger : allChallengers) {

                            if (froT.contains(" " + challenger + " ")) {
                                newT = newT + challenger + "/";
                                break;
                            }

                            //froT = froT.replace(challenger + " ", challenger + "/");
                        }
                        for (String subRegions : allSubRegions) {

                            if (froT.contains(" " + subRegions + " ") && !froT.contains("North America")) {
                                newT = newT + subRegions + "/";
                                break;
                            }

                            //froT = froT.replace(subRegions + " ", subRegions + "/");
                        }
                        if(froT.contains("Open Qualifiers")){
                            newT = newT + "Open Qualifiers" + "/";
                        }
                        //froT = froT.substring(0, froT.length() - 1) + "/";

                        //froT = "VALORANT Champions Tour/2021/" + froT;
                    }

                    //print from
                    System.out.println(newT + "\n");

                    Match searchMap = matchFromName(newT, null, null, null);

                    boolean same = false;
                    for (Match sMatch : matches) {
                        if(sMatch.equals(searchMap)){
                            if(same){
                                System.out.println("2 of the same");
                                System.out.println(sMatch.solo());
                                System.out.println(match.from.get(match.from.size()-1).solo());
                            } else {
                                match.from.add(sMatch);
                                same = true;
                            }
                        }
                    }
                    if (!same){
                        System.out.println("no same");
                        System.out.println(searchMap.solo() + "\n");
                    }
                }
            }
            //System.out.println(match.from);

        }


        //cull matches

        ArrayList<Match> cMatches = new ArrayList<Match>();

        for (Match match:matches) {
            if(match.region.equals("North America") && match.stage.equals("Stage 1")){
                cMatches.add(match);
            }
        }
        System.out.println("output");
        for (Match cMatch:cMatches) {
            System.out.println(cMatch.solo());
            System.out.println();
        }

        Visualize frame = new Visualize(cMatches);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(2000, 1500);
        frame.setVisible(true);






    }






























    public static ArrayList<Integer> indexesOf(String textString, String word){
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        String lowerCaseTextString = textString.toLowerCase();

        String lowerCaseWord = word.toLowerCase();

        int index = 0;
        //System.out.println("jwefis" + index + " i " + i + " j " + j);
        while(index != -1){
            index = lowerCaseTextString.indexOf(lowerCaseWord, index);
            if (index != -1) {
                indexes.add(index);
                index++;
            }
        }
        return indexes;
    }



    public static JSONObject GetPage(String urlName) throws Exception {
        URL url = new URL(urlName);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Accept-Encoding", "gzip");

        Reader reader = null;
        if ("gzip".equals(con.getContentEncoding())) {
            reader = new InputStreamReader(new GZIPInputStream(con.getInputStream()));
        }
        else {
            reader = new InputStreamReader(con.getInputStream());
        }

        String str = "";
        while (true) {
            int ch = reader.read();
            if (ch==-1) {
                break;
            } //(char)ch String.valueOf((char)ch) //"revisions"
            str = str + (char)ch;

        }
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(str);
        return json;
    }
    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    public static void serializeDataOut(ArrayList toSave, String name)throws IOException {
        String fileName= name + ".txt";
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(toSave);
        oos.close();
    }

    public static ArrayList serializeDataIn(String name) throws IOException, ClassNotFoundException {
        String fileName= name + ".txt";
        FileInputStream fin = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fin);
        ArrayList toReturn = (ArrayList) ois.readObject();
        ois.close();
        return toReturn;
    }






    // only need officaialTitle
    public static Match matchFromName(String officialTitle, ArrayList<Integer> teamIndexes, ArrayList<Integer> qualifierIndexes, String pageText){


        ArrayList<Integer> indexOfSlash = indexesOf(officialTitle, "/");

        Boolean open = false;
        String region = "";
        String subRegion = "";
        String stage = "";
        String challenger = "";


        String P1 = officialTitle.substring(indexOfSlash.get(1) + 1, indexOfSlash.get(2));
        if(P1.contains("Champions")){
            region = "World";
            stage = "Champions";
            challenger = "";
        } else if (P1.contains("Stage")) {
            region = "World";
            stage = P1;
            challenger = "Masters";
        } else {
            region = P1;
        }


        if(indexOfSlash.size() >= 4) {
            String P2 = officialTitle.substring(indexOfSlash.get(2) + 1, indexOfSlash.get(3));
            if (P2.contains("Qualifier")) {
                stage = "Last Chance Qualifier";
                challenger = "";
            } else if(!challenger.equals("Masters")) {
                stage = P2;
            }
        }


        if(indexOfSlash.size() >= 5) {
            String P3 = officialTitle.substring(indexOfSlash.get(3) + 1, indexOfSlash.get(4));
            if (P3.equals("Vietnam")) {
                challenger = "1";
                subRegion = "Vietnam";
            } else {
                challenger = officialTitle.substring(indexOfSlash.get(3) + 1, indexOfSlash.get(4));
                if(indexOfSlash.size() == 6){
                    String P4 = officialTitle.substring(indexOfSlash.get(4) + 1, indexOfSlash.get(5));
                    if(P4.equals("Open Qualifier")){
                        open = true;
                    } else {
                        subRegion = P4;
                    }
                }
                if(indexOfSlash.size() == 7){
                    open = true;
                    subRegion = officialTitle.substring(indexOfSlash.get(4) + 1, indexOfSlash.get(5));
                }
            }
        }



        /*
        if(P1.contains("Champions")){
            region = "World";
            stage = "Champions";
            challenger = "";
        } else if (P1.contains("Stage")) {
            region = "World";
            stage = officialTitle.substring(indexOfSlash.get(1) + 7, indexOfSlash.get(2));
            challenger = "";
        } else {
            region = P1;

            String P2 = officialTitle.substring(indexOfSlash.get(2) + 7, indexOfSlash.get(3));

            if(P2.contains("Qualifier")) {
                stage = "Last Chance Qualifier";
                challenger = "";
            } else {
                stage = P2;

                String P3 = officialTitle.substring(indexOfSlash.get(3) + 1, indexOfSlash.get(4));
                if(P3.contains("Masters")){
                    challenger = "Masters";
                } else if (P3.contains("Final")) {
                    challenger = "Final";
                } else if (P3.contains("Preliminary Round")) {
                    challenger = "Final";
                } else if (P3.equals("Challengers")) {
                    challenger = "1";
                } else if (P3.equals("Vietnam")) {
                    challenger = "1";
                    subRegion = "Vietnam";
                } else {
                    challenger = officialTitle.substring(indexOfSlash.get(3) + 13, indexOfSlash.get(4));
                    if(indexOfSlash.size() >= 6){
                        subRegion = officialTitle.substring(indexOfSlash.get(4) + 1, indexOfSlash.get(5));
                    }
                }

            }

        }

         */

        if(teamIndexes != null) {

            ArrayList<Team> matchTeams = new ArrayList<Team>();

            for (int k = 0; k < teamIndexes.size(); k++) {
                //System.out.println();
                //System.out.print("team name: ");
                int offset = 0;
                StringBuilder fullT = new StringBuilder();
                while (true) {
                    //System.out.print(indexes.get(k)+offset);
                    if (pageText.charAt(teamIndexes.get(k) + offset) != '\n') {
                        offset++;
                    } else {
                        break;
                    }

                    fullT.append(pageText.charAt(teamIndexes.get(k) + offset - 1));
                }
                //System.out.print(teamName);

                String teamName = fullT.substring(fullT.indexOf("=") + 1);

                Team unique = null;
                for (Team team : teams) {
                    if (team.name.equals(teamName)) {
                        unique = team;
                        break;
                    }
                }

                if (unique == null) {
                    Team tea = new Team(teamName);
                    teams.add(tea);
                    matchTeams.add(tea);
                } else {
                    matchTeams.add(unique);
                }

            }

            //get from

            ArrayList<String> matchFrom = new ArrayList<String>();

            for (int k = 0; k < qualifierIndexes.size(); k++) {
                //System.out.println();
                //System.out.print("team name: ");
                int offset = 0;
                StringBuilder fullQ = new StringBuilder();
                while (true) {
                    //System.out.print(indexes.get(k)+offset);
                    if (pageText.charAt(qualifierIndexes.get(k) + offset) != '\n') {
                        offset++;
                    } else {
                        break;
                    }

                    fullQ.append(pageText.charAt(qualifierIndexes.get(k) + offset - 1));
                }

                //get fromTXT
                String from = fullQ.substring(fullQ.indexOf("=") + 1);
                if (from.length() > 0) {
                    from = from.substring(2, from.length() - 2);
                    if (from.contains("Open Qualifier")) {
                        from = region;
                        if (!subRegion.equals("")) {
                            from += ", " + subRegion;
                        }
                        from += " Stage " + stage + " Open Qualifier " + challenger;
                    }
                    matchFrom.add(from);
                }// else {
                //   System.out.println(fullQ + "\nhshfdsshsh\n\n");
                // }
            }
            return new Match(stage, challenger, region, subRegion, officialTitle, open, matchTeams, matchFrom);
        } else {
            return new Match(stage, challenger, region, subRegion, officialTitle, open);
        }
    }
}