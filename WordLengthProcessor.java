package ngordnet;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;

public class WordLengthProcessor implements YearlyRecordProcessor {
    public double process(YearlyRecord yearlyRecord) {
        Collection<Number> counts = yearlyRecord.counts();
        Collection<String> words = yearlyRecord.words();
        List countslist = new ArrayList(counts);
        List wordslist = new ArrayList(words);
        double num = 0.0;
        for (int i = 0; i < yearlyRecord.size(); i++) {
            String word = (String) wordslist.get(i);
            Integer wordl = word.length();
            double length = wordl.doubleValue();
            Integer countl = (Integer) countslist.get(i);
            double count = countl.doubleValue();
            num = num + (count * length);
        }
        double ret = 0.0;
        for (Number d : counts) {
            ret += d.doubleValue();
        }
        return (num / ret);
    }        
}
