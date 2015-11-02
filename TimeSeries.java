package ngordnet;
import java.util.Collection;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

public class TimeSeries<T extends Number> extends TreeMap<Integer, T> {    
    /** Constructs a new empty TimeSeries. */

    public TimeSeries() {
        super();
    }

    /** Returns the years in which this time series is valid. Doesn't really
      * need to be a NavigableSet. This is a private method and you don't have 
      * to implement it if you don't want to. */
    private NavigableSet<Integer> validYears(int startYear, int endYear) {
        return null;
    }

    /** Creates a copy of TS, but only between STARTYEAR and ENDYEAR. 
     * inclusive of both end points. */
    public TimeSeries(TimeSeries<T> ts, int startYear, int endYear) {
        Set<Integer> boundyears = ts.keySet();
        for (Integer y : boundyears) {
            if (y >= startYear && y <= endYear) {
                T data = ts.get(y);
                this.put(y, data);
            }
        }
    }

    /** Creates a copy of TS. */
    public TimeSeries(TimeSeries<T> ts) {
        Set<Integer> boundyears = ts.keySet();
        for (Integer y : boundyears) {
            T data = ts.get(y);
            this.put(y, data);
        }
    }

    /** Returns the quotient of this time series divided by the relevant value in ts.
      * If ts is missing a key in this time series, return an IllegalArgumentException. */
    public TimeSeries<Double> dividedBy(TimeSeries<? extends Number> ts) {
        TimeSeries<Double> totalquotient = new TimeSeries<Double>();
        Collection<Integer> ts1 = ts.keySet();
        Collection<Integer> ts2 = this.keySet();

        if (!ts1.containsAll(ts2)) {
            throw new IllegalArgumentException("Does not exist");
        }

        for (Integer y : ts2) {
            double quotient = 0;
            double top = this.get(y).doubleValue();
            double bottom = ts.get(y).doubleValue();
            quotient = top / bottom;

            totalquotient.put(y, quotient);
        }
        return totalquotient;         
    }



    /** Returns the sum of this time series with the given ts. The result is a 
      * a Double time series (for simplicity). */
    public TimeSeries<Double> plus(TimeSeries<? extends Number> ts) {
        TimeSeries<Double> total = new TimeSeries<Double>();
        Collection<Integer> ts1 = ts.keySet();
        Collection<Integer> ts2 = this.keySet();
        Set<Integer> combine = new HashSet<Integer>();
        combine.addAll(ts1);
        combine.addAll(ts2);
        for (Integer y : combine) {
            double sum = 0;
            if (ts.containsKey(y)) {
                sum += ts.get(y).doubleValue();
            }
            if (this.containsKey(y)) {
                sum += this.get(y).doubleValue();
            }
            total.put(y, sum);
        }
        return total;         
    }

    /** Returns all years for this time series (in any order). */
    public Collection<Number> years() {
        Collection<Number> ret = new ArrayList<Number>();
        Collection<Integer> year = this.keySet();
        for (Integer y : year) {
            ret.add(y);
        }
        return ret;    
    }

    /** Returns all data for this time series (in any order). */
    public Collection<Number> data() {
        Collection<Number> ret = new ArrayList<Number>();
        Collection<Integer> year = this.keySet();
        for (Integer y : year) {
            ret.add(this.get(y));
        }
        return ret;   
    } 
}

