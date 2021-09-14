import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

class DebugInput {
    public DebugInput(DownloadAPI parent) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        String name = reader.readLine();
        for (Match match : parent.matches) {
            if(match.Shortened(true).equals(name) || match.extension.equals(name)){
                System.out.println(match.full());
            }
        }
        new DebugInput(parent);
    }
}