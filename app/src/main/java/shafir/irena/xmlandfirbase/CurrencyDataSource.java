package shafir.irena.xmlandfirbase;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by irena on 02/06/2017.
 */

public class CurrencyDataSource {
    // uses-permission  INTERNET
    // listener

    public interface onCurrencyArrivedListener{
        void onCurrencyArrived(List<Currency> data, Exception e);
    }


    public static void getCurrency(final onCurrencyArrivedListener listener){
        // A synchronized method that does not  return
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // URL url = new URL("...")
                    // url = url.openConnection()
                    // in = con.getInputStream (BINARY)
                    //  String xml = IO.read(in)  // convert the binary to String
                    // Method that parses the xml into Currency. Jsoup.parse()

                    URL url = new URL("http://www.boi.org.il/currency.xml");
                    URLConnection con = url.openConnection();
                    InputStream in = con.getInputStream();
                    String xml = StreamIO.read(in);
                    List<Currency> result = parse(xml);
                    listener.onCurrencyArrived(result, null);
                }
                catch (Exception e){
                    listener.onCurrencyArrived(null, e);
                    //TODO: Handle the error with listener
                }

            }
        });
        service.shutdown();
    }

    private static List<Currency> parse (String xml){
        Document document = Jsoup.parse(xml);
        List<Currency> currencies = new ArrayList<>();

        Elements allCurrencies = document.getElementsByTag("CURRENCY");

        for (Element c : allCurrencies) {
            String name = c.getElementsByTag("NAME").get(0).text();
            int unit = Integer.parseInt(c.getElementsByTag("UNIT").get(0).text());
            String currencyCode = c.getElementsByTag("CURRENCYCODE").get(0).text();
            String country = c.getElementsByTag("COUNTRY").get(0).text();
            double rate = Double.parseDouble(c.getElementsByTag("RATE").get(0).text());
            double change = Double.parseDouble(c.getElementsByTag("CHANGE").get(0).text());

            Currency currency = new Currency(name, unit, currencyCode, country, rate, change);
            currencies.add(currency);
        }
        return currencies;
    }

    // Currency? (class)
    // it's like an outside class but it's simpler since it does not need ref to outer class
    public static class Currency{
        private final String name;
        private final int unit;
        private final String currencyCode;
        private final String country;
        private final double rate;
        private final double change;

        // Constructor
        public Currency(String name, int unit, String currencyCode, String country, double rate, double change) {
            this.name = name;
            this.unit = unit;
            this.currencyCode = currencyCode;
            this.country = country;
            this.rate = rate;
            this.change = change;
        }

        // Getters
        public String getName() {
            return name;
        }
        public int getUnit() {
            return unit;
        }
        public String getCurrencyCode() {
            return currencyCode;
        }
        public String getCountry() {
            return country;
        }
        public double getRate() {
            return rate;
        }
        public double getChange() {
            return change;
        }


        // toString
        @Override
        public String toString() {
            return "Currency{" +
                    "name='" + name + '\'' +
                    ", unit=" + unit +
                    ", currencyCode='" + currencyCode + '\'' +
                    ", country='" + country + '\'' +
                    ", rate=" + rate +
                    ", change=" + change +
                    '}';
        }
    }






}
