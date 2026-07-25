// Given a file of currency conversion rates, 
// write a function that converts one currency to another. 
// Sample log file entries: 
// {“from”: “USD”, “to”: “EUR” “rate”:1.1} (“from”: “EUR”, “to”: “GBP” “rate” :1.2}
import java.util.*;
public class CurrencyConversion {
    class RateLog{
        String from; String to; double rate;
        public RateLog(String from, String to, double rate) {
            this.from = from;
            this.to = to;
            this.rate = rate;
        }
    }
    
    public double calRate(String start, String end, List<RateLog> logs){
        // 1. build the graph
        Map<String, Map<String,Double>> graph = new HashMap<>();
        for(RateLog log: logs){
            String from = log.from; String to = log.to; double rate = log.rate;
            graph.putIfAbsent(from, new HashMap<>()); graph.putIfAbsent(to,  new HashMap<>());
            graph.get(from).put(to,rate);graph.get(to).put(from,1.0/rate);
        }
        // 2.calculate the conversion
        if(!graph.containsKey(start) || !graph.containsKey(end)) return -1;
        if(start.equals(end)) return 1.0;
        return calConversion(start,end,graph,new HashSet<>(),1.0);
    }
    public double calConversion(String cur, String end, Map<String, Map<String,Double>> graph, Set<String> visited, double val){
        if(cur.equals(end)) return val;
        visited.add(cur);
        for(String next: graph.get(cur).keySet()){
            if(visited.contains(next) || !graph.containsKey(next)) continue;
            double factor = graph.get(cur).get(next);
            double res = calConversion(next, end, graph, visited, val*factor);
            if(res != -1.0) return res;
        }
        return -1.0;
    }
    public static void main(String[] args) {
        currencyConversion converter = new currencyConversion();

        List<RateLog> logs = new ArrayList<>();

        logs.add(new RateLog("USD", "EUR", 1.1));
        logs.add(new RateLog("EUR", "GBP", 1.2));
        logs.add(new RateLog("GBP", "JPY", 190.0));

        // USD -> EUR
        System.out.println(
                "USD to EUR rate: "
                        + converter.calRate("USD", "EUR", logs)
        );
        // Expected: 1.1

        // USD -> GBP
        System.out.println(
                "USD to GBP rate: "
                        + converter.calRate("USD", "GBP", logs)
        );
        // Expected: 1.1 * 1.2 = 1.32

        // GBP -> USD
        System.out.println(
                "GBP to USD rate: "
                        + converter.calRate("GBP", "USD", logs)
        );
        // Expected: 1 / 1.32

        // USD -> JPY
        System.out.println(
                "USD to JPY rate: "
                        + converter.calRate("USD", "JPY", logs)
        );
        // Expected: 1.1 * 1.2 * 190 = 250.8

        // Same currency
        System.out.println(
                "USD to USD rate: "
                        + converter.calRate("USD", "USD", logs)
        );
        // Expected: 1.0

        // Currency does not exist
        System.out.println(
                "USD to CAD rate: "
                        + converter.calRate("USD", "CAD", logs)
        );
        // Expected: -1.0

        // Convert an actual amount
        double amount = 100.0;
        double rate = converter.calRate("USD", "GBP", logs);

        if (rate != -1.0) {
            System.out.println(
                    amount + " USD = "
                            + amount * rate + " GBP"
            );
        } else {
            System.out.println("Conversion path not found");
        }
    }
}
