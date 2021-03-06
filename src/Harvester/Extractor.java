    package Harvester;

    import Clustering.HierarchicalClustering;
    import org.webharvest.definition.ScraperConfiguration;
    import org.webharvest.runtime.Scraper;
    import org.webharvest.runtime.variables.NodeVariable;
    import org.webharvest.runtime.variables.Variable;

    import java.io.FileNotFoundException;
    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.List;


    /**
     * Created by IntelliJ IDEA.
     * User: john
     * Date: 07.03.11
     * Time: 4:26
     */

    //This class will use external library "Web Harvester" (WH) sources to deal with html-pages
    //You can find it here: http://web-harvest.sourceforge.net/
    //Input: WH XML-based configuration file
    //Output: populated MySQL database file with parsed entities

    public class Extractor {


        enum Months {
            января(1),февраля(2), марта (3), апреля (4), мая (5), июня (6),
            июля (7), августа(8),сентября(9), октября(10), ноября(11),декабря(12);
            private final int id;
            Months(int i) {
                this.id = i;
            }
        };

        public static void main(String[] args) {
        List<CarEntity> cars = new ArrayList();
        try{
            String curDir = System.getProperty("user.dir");
            //ScraperConfiguration config = new ScraperConfiguration(curDir + "/src/Harvester/config.xml");
            ScraperConfiguration config = new ScraperConfiguration(curDir + "/config.xml");
            Scraper scraper = new Scraper(config, curDir);

            scraper.setDebug(true);
            scraper.execute();
            //Unwrapping all variables that scraper constructed after execution
            List<String> rawYears = UnwrapToList((List<NodeVariable>) ((Variable) scraper.getContext().get("yearList")).getWrappedObject());
            List<String> rawPrices = UnwrapToList((List<NodeVariable>) ((Variable) scraper.getContext().get("priceList")).getWrappedObject());
            List<String> rawDescriptions = UnwrapToList((List<NodeVariable>) ((Variable) scraper.getContext().get("descriptionList")).getWrappedObject());
            List<String> rawCdates = UnwrapToList((List<NodeVariable>) ((Variable) scraper.getContext().get("cityDateList")).getWrappedObject());

            List<Integer> prices = FilterPrices(rawPrices);
            List<Integer> years = FilterYears(rawYears);
            List<String> cities = GetCities(rawCdates);
            List<int[]> dates = GetDates(rawCdates);     //[0] is Year;[1] is Month;[2] is Day
            for(int i = 0,j = 0; i < prices.size();i++,j+=2)
            {
                int price = prices.get(i);
                String sDescription = rawDescriptions.get(j);
                String lDescription = rawDescriptions.get(j+1);
                String city = cities.get(i);
                int year = years.get(i);
                cars.add(new CarEntity(price,sDescription,lDescription,city,year,0));
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        HierarchicalClustering processor = new HierarchicalClustering(cars);
        List<Integer> indices = processor.process();
        DatabaseAdapter db = new DatabaseAdapter();
        for(int i=0;i<cars.size();i++) {
            if(indices.get(i)<0)
                indices.set(i,i);
            db.InsertRow(cars.get(i).getPrice(),
                        cars.get(i).getShortDesc(),
                        cars.get(i).getLongDesc(),
                        cars.get(i).getCity(),
                        cars.get(i).getYear(),
                        indices.get(i));
            //TODO make bath insert
        }
    }

        private static List<String> GetCities(List<String> rawCdates) {
            List<String> result = new ArrayList<String>();
            String temp="";
            int position=0;
            try{
                for(int i = 0; i < rawCdates.size();i++)
                {
                    position = rawCdates.get(i).indexOf(",");
                    temp = rawCdates.get(i).substring(0,position);
                    result.add(i,temp);
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
            return result;

        }
        private static List<int[]> GetDates(List<String> rawCdates) {
            List<int[]> result = new ArrayList<int[]>();
            String temp="";
            int position=0;
            try{
                for(int i = 0; i < rawCdates.size();i++)
                {
                    position = rawCdates.get(i).indexOf(",");
                    temp = rawCdates.get(i).substring(position + 1);
                    temp = temp.replaceAll(",\n","").replaceFirst(" ", "");
                    result.add(i, CastToDate(temp.split(" ", 0)));
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
            return result;

        }

        private static int[] CastToDate(String[] split) {
            Calendar cal = Calendar.getInstance();
            int year=cal.get(Calendar.YEAR);
            int month=cal.get(Calendar.MONTH)+1; //January field is '0'
            int day=cal.get(Calendar.DATE);
            if(split.length==2)
            {
                //So we have day and month
                day = Integer.parseInt(split[0]);
                for(Months m : Months.values())
                    if(split[1].equals(m.name()))
                    {
                        month=m.id;
                        break;
                    }
            }
            if(split.length==1){
                //It could be today and yesterday
                if(split[0].equals("вчера"))
                {
                    cal.add(Calendar.DATE,-1);
                    day = cal.get(Calendar.DATE);
                }
            }
            return new int[]{year,month,day};
        }

        private static List<Integer> FilterYears(List<String> rawYears) {
            List<Integer> result = new ArrayList<Integer>();
            try{
            for(int i = 0; i< rawYears.size(); i++)
            {
                result.add(i,Integer.parseInt(rawYears.get(i).substring(0,4)));
            }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
            return result;

        }

        private static List<Integer> FilterPrices(List<String> rawPrices) {
            //We need to remove all spaces in strings and then cast them to Integers
            List<Integer> result = new ArrayList<Integer>();
            try
            {
                for(int i = 0; i< rawPrices.size();i++)
                    result.add(i,Integer.parseInt(rawPrices.get(i).replace(" ","")));
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
            return result;
        }

        private static List<String> UnwrapToList(List<NodeVariable> wrappedObject) {
            //Due to structure of Variable (Wrappers, wrappers ... wrappers) used in Web Harvester
            // we need to use this ugly function to get back to Java's String List
            List<String> result = new ArrayList<String>();
            for(int i =0;i< wrappedObject.size();i++)
                result.add(wrappedObject.get(i).getWrappedObject().toString());
            return result;
        }
    }
