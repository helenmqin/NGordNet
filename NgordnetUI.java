package ngordnet;
import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.In;
import java.lang.IllegalArgumentException;

/** Provides a simple user interface for exploring WordNet and NGram data.
 *  @author Helen Qin
 */
public class NgordnetUI {
    public static void main(String[] args) {
        In in = new In("./ngordnet/ngordnetui.config");
        System.out.println("Reading ngordnetui.config...");

        String wordFile = in.readString();
        String countFile = in.readString();
        String synsetFile = in.readString();
        String hyponymFile = in.readString();
        WordNet wn = new WordNet(synsetFile, hyponymFile);
        NGramMap ngm = new NGramMap(wordFile, countFile);
        int year;
        int startDate = 1500; 
        int endDate = 2000;
        String word;

        System.out.println("\nBased on ngordnetui.config, using the following: "
                           + wordFile + ", " + countFile + ", " + synsetFile +
                           ", and " + hyponymFile + ".");

        System.out.println("\nFor tips on implementing NgordnetUI, see ExampleUI.java.");

        while (true) {
            System.out.print("> ");
            String line = StdIn.readLine();
            String[] rawTokens = line.split(" ");
            String command = rawTokens[0];
            String[] tokens = new String[rawTokens.length - 1];
            System.arraycopy(rawTokens, 1, tokens, 0, rawTokens.length - 1);
            switch (command) {
                case "quit": 
                    return;
                case "help":
                    In in2 = new In("help.txt");
                    String helpStr = in2.readAll();
                    System.out.println(helpStr);
                    break;  
                case "range": 
                    startDate = Integer.parseInt(tokens[0]); 
                    endDate = Integer.parseInt(tokens[1]);
                    System.out.println("Start date: " + startDate);
                    System.out.println("End date: " + endDate);
                    break;
                case "count":
                    word = tokens[0];
                    year = Integer.parseInt(tokens[1]);
                    YearlyRecord yr = ngm.getRecord(year);
                    System.out.println("Word count for " + word + " in " + year
                        + " : " + yr.count(word));
                    break;
                case "hyponyms":
                    System.out.println(wn.hyponyms(tokens[0]));
                    break;
                case "history":
                    Plotter.plotAllWords(ngm, tokens, startDate, endDate);
                    break;
                case "hypohist":
                    Plotter.plotCategoryWeights(ngm, wn, tokens, startDate, endDate);
                    break;
                case "wordlength":
                    WordLengthProcessor wlp = new WordLengthProcessor();
                    Plotter.plotProcessedHistory(ngm, startDate, endDate, wlp);
                case "zipf year":
                    year = Integer.parseInt(tokens[0]);
                    Plotter.plotZipfsLaw(ngm, year);               
                    break;
                default:
                    System.out.println("Invalid command.");  
                    break;
            }
        }
    }
} 
