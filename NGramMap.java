package ngordnet;
import java.util.Collection;
import edu.princeton.cs.introcs.In;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.List;


public class NGramMap {
   
    private HashMap<String, TimeSeries> ngrammap;
    private HashMap<Integer, YearlyRecord> getrecord;
    private TimeSeries<Long> timeseries;
    
    /** Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME. */
    public NGramMap(String wordsFilename, String countsFilename) {

        ngrammap = new HashMap<String, TimeSeries>();
        TimeSeries<Integer> temp = new TimeSeries<Integer>();
        YearlyRecord temp2 = new YearlyRecord();
        timeseries = new TimeSeries<Long>();
        getrecord = new HashMap<Integer, YearlyRecord>();
        
        In file = new In(wordsFilename);
        while (!file.isEmpty()) {
            String[] columns = file.readLine().split("\t");
            String word = columns[0];
            int year = Integer.parseInt(columns[1]);
            int count = Integer.parseInt(columns[2]);

            if (ngrammap.containsKey(word)) {
                ngrammap.get(word).put(year, count);
            } else {
                temp.put(year, count);
                ngrammap.put(word, temp);
                temp = new TimeSeries<Integer>();
            }

            if (getrecord.containsKey(year)) {
                getrecord.get(year).put(word, count);
            } else {
                temp2.put(word, count);
                getrecord.put(year, temp2);
                temp2 = new YearlyRecord();
            }
                
        }

        file = new In(countsFilename);
        while (!file.isEmpty()) {
            String[] columns = file.readLine().split(",");
            int year = Integer.parseInt(columns[0]);
            long count = Long.parseLong(columns[1]);
            timeseries.put(year, count);
        }
        
    }
    
    /** Returns the absolute count of WORD in the given YEAR. If the word
      * did not appear in the given year, return 0. */
    public int countInYear(String word, int year) {
        if (ngrammap.containsKey(word)) {
            TimeSeries<Integer> temp = ngrammap.get(word);
            return (temp.get(year));
        }
        return 0;
    }

    /** Returns a defensive copy of the YearlyRecord of WORD. */
    public YearlyRecord getRecord(int year) {
        YearlyRecord newrec = getrecord.get(year);
        Collection<Number> counts = newrec.counts();
        Collection<String> words = newrec.words();
        List countslist = new ArrayList(counts);
        List wordslist = new ArrayList(words);
        YearlyRecord ret = new YearlyRecord();
        for (int i = 0; i < countslist.size(); i++) {
            String word = (String) wordslist.get(i);
            int count = (int) countslist.get(i);
            ret.put(word, count);
        }
        return ret;
    }

    /** Returns the total number of words recorded in all volumes. */
    public TimeSeries<Long> totalCountHistory() {
        return timeseries;
    }

    /** Provides the history of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Integer> countHistory(String word, int startYear, int endYear) {
        TimeSeries<Integer> ret = countHistory(word);
        return new TimeSeries(ret, startYear, endYear);
    }

    /** Provides a defensive copy of the history of WORD. */
    public TimeSeries<Integer> countHistory(String word) {
        return (ngrammap.get(word));
    }

    /** Provides the relative frequency of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> weightHistory(String word, int startYear, int endYear) {
        TimeSeries<Double> ret = weightHistory(word);
        return new TimeSeries(ret, startYear, endYear);
    }

    /** Provides the relative frequency of WORD. */
    public TimeSeries<Double> weightHistory(String word) {
        TimeSeries<Integer> ret = countHistory(word);
        return (ret.dividedBy(timeseries));
    }

    /** Provides the summed relative frequency of all WORDS between
      * STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words, 
                              int startYear, int endYear) {
        TimeSeries<Double> ret = summedWeightHistory(words);
        return new TimeSeries(ret, startYear, endYear);
    }

    /** Returns the summed relative frequency of all WORDS. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words) {
        TimeSeries<Double> ret = new TimeSeries<Double>();
        for (String w : words) {
            ret = ret.plus(new TimeSeries(weightHistory(w)));          
        }
        return ret;
    }

    /** Provides processed history of all words between STARTYEAR and ENDYEAR as processed
      * by YRP. */
    public TimeSeries<Double> processedHistory(int startYear, int endYear,
                                               YearlyRecordProcessor yrp) {
        TimeSeries<Double> ret = processedHistory(yrp);
        return new TimeSeries(ret, startYear, endYear);
    }

    /** Provides processed history of all words ever as processed by YRP. */
    public TimeSeries<Double> processedHistory(YearlyRecordProcessor yrp) {
        TimeSeries<Double> ret = new TimeSeries<Double>();
        for (Entry<Integer, YearlyRecord> e : getrecord.entrySet()) {
            ret.put(e.getKey(), yrp.process(e.getValue()));   
        }
        return ret;
    }
}



