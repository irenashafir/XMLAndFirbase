package shafir.irena.xmlandfirbase;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by irena on 02/06/2017.
 */

public class YnetDataSource {

    public interface onYnetArrivedListener{
        public void onYnetArrived(List <Ynet> data, Exception e);
    }

    public static void getYnet(final onYnetArrivedListener listener){
        // service = Executor.newSingleThread
        // service -> run
        // URL url = new url ("...");
        // con = url.openConnection():
        // InputStream in = StreamIO.read(in);
        //List<Ynet> data = parse (xml);
        //listener.notify

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.ynet.co.il/Integration/StoryRss2.xml");
                    URLConnection con = url.openConnection();
                    InputStream in = con.getInputStream();
                    String xml = StreamIO.read(in, "Windows-1255");
                    List<Ynet> parse = parse(xml);
                    listener.onYnetArrived(parse, null);

                } catch (Exception e) {
                listener.onYnetArrived(null, e);
                }
            }


            private List<Ynet> parse(String xml) {
                List<Ynet> data = new ArrayList<>();
                Document document = Jsoup.parse(xml);
                // from the doc we get all the "children" that has the TAG "item"
                Elements items = document.getElementsByTag("item");

                for (Element item : items) {

                     String title = item.getElementsByTag("title").get(0).text().
                             replace("<![CDATA[", "").replace("]]>", "");

                     String link = item.getElementsByTag("link").get(0).text();
                     String description= item.getElementsByTag("description").get(0).text();

                    Document descriptionElement = Jsoup.parse(description);
                    String href = descriptionElement.getElementsByTag("a").get(0).attr("href");
                    String src = descriptionElement.getElementsByTag("img").get(0).attr("src");
                    description = descriptionElement.text();

                    Ynet ynet = new Ynet(title, href, description, src);
                    data.add(ynet);
                    Log.d("Ness", ynet.toString());
                }
                return data;
            }
        });
    }



    public static class Ynet {
        private String title;
        private String link;
        private String description;
        private String image;

        public Ynet(String title, String link, String description, String image) {
            this.title = title;
            this.link = link;
            this.description = description;
            this.image = image;
        }

        public String getTitle() {
            return title;
        }
        public String getLink() {
            return link;
        }
        public String getDescription() {
            return description;
        }
        public String getImage() {
            return image;
        }

        @Override
        public String toString() {
            return "Ynet{" +
                    "title='" + title + '\'' +
                    ", link='" + link + '\'' +
                    ", description='" + description + '\'' +
                    ", image='" + image + '\'' +
                    '}';
        }
    }



}
