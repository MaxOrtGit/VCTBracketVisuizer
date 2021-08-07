import java.awt.*;
import java.util.ArrayList;

public class Team {
    String name;
    Image logo;

    public Team(String nam) {
        name = nam;
    }

    @Override
    public String toString() {
        return name;
    }
}
