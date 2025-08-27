package com.ivoovi.meter.initializer;


import com.ivoovi.meter.domain.Subscription;
import com.ivoovi.meter.repository.SubscriptionRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class IcaoCodePopulateDatabase implements CommandLineRunner {

    public static final String METER_DATA_URL ="https://tgftp.nws.noaa.gov/data/observations/metar/stations/";

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public void run(String... args) throws Exception {

        long count = subscriptionRepository.count();

        if(count >0){
            System.out.println("Database already populated");
        }else {
            System.out.println("Populating database with ICAO codes");

            Document doc = Jsoup.connect(METER_DATA_URL).get();
            Elements elements = doc.select("a[href$=.TXT]");

            for(Element element : elements){
                String linkName = element.text();
                String icaoCode = linkName.replace(".TXT","");

                Subscription subscription = new Subscription();
                subscription.setIcaoCode(icaoCode);

                subscriptionRepository.save(subscription);
            }
        }
    }
}
