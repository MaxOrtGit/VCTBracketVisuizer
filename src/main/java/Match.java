import java.util.ArrayList;
import java.util.Objects;

public class Match {
    boolean openQualifier;

    String stage;
    String challenger;
    String region;
    String subRegion;
    String extension;
    ArrayList<Team> teams;
    ArrayList<String> fromTXT;
    ArrayList<Match> from = new ArrayList<Match>();



    public Match(String stage, String challenger, String region, String subRegion, String extension, boolean open, ArrayList<Team> teams, ArrayList<String> fromTXT) {
        this.openQualifier = open;
        this.stage = stage;
        this.challenger = challenger;
        this.region = region;
        this.subRegion = subRegion;
        this.extension = extension.substring(0, extension.length() - 1);

        ArrayList<Team> cull = new ArrayList<>();
        if(teams.size() != 0) {
            for (int i = 0; i < teams.size(); i++) {
                if (teams.get(i) != null && !teams.get(i).name.equals("")) {
                    cull.add(teams.get(i));
                }
            }
        }
        //System.out.println(fromTXT);
        ArrayList<String> qCull = new ArrayList<String>();
        if(fromTXT.size() != 0) {
            for (int i = 0; i < fromTXT.size(); i++) {
                if (fromTXT.get(i) != null && !fromTXT.get(i).equals("")) {
                    qCull.add(fromTXT.get(i));
                }
            }
        }

        this.teams = cull;
        this.fromTXT = qCull;
    }

    public Match(String stage, String challenger, String region, String subRegion, String extension, boolean open){
        //never save
        this.openQualifier = open;
        this.stage = stage;
        this.challenger = challenger;
        this.region = region;
        this.subRegion = subRegion;
        this.extension = extension.substring(0, extension.length() - 1);

    }


    public String GetName(){
        String start = "Stage: " + stage + ", Challenger: " + challenger;
        if(openQualifier){
            start += " Open Qualifier";
        }
        start += ", Region: " + region;
        if(!subRegion.equals("")){
            start += ", " + subRegion;
        }
        return start;
    }

    @Override
    public String toString() {

        return extension;
    }
    public String full() {
        String start = "Stage:" + stage + ", Challenger:" + challenger;
        if(openQualifier){
            start += " Open Qualifier";
        }
        start += ", region:" + region;
        if(!subRegion.equals("")){
            start += ", " + subRegion;
        }
        start += "\n" + teams + "\n" + from + "\n" + fromTXT;
        return start;
    }

    public String solo() {
        String start = "Stage:" + stage + ", Challenger:" + challenger;
        if(openQualifier){
            start += " Open Qualifier";
        }
        start += ", region:" + region;
        if(!subRegion.equals("")){
            start += ", " + subRegion;
        }
        start += "\n" + extension;
        return start;
    }


    public String Shortened() {
        String start = region;
        if(!subRegion.equals("")){
            start += ", " + subRegion;
        }
        start += " " + stage + " " + challenger;
        if(openQualifier){
            start += " Open Qualifier";
        }
        return start;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return openQualifier == match.openQualifier && Objects.equals(stage, match.stage) && Objects.equals(challenger, match.challenger) && Objects.equals(region, match.region) && Objects.equals(subRegion, match.subRegion);
    }

}
