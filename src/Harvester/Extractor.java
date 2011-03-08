    package Harvester;

    import org.webharvest.definition.ScraperConfiguration;
    import org.webharvest.runtime.Scraper;
    import org.webharvest.runtime.ScraperContext;
    import org.webharvest.runtime.variables.Variable;

    import java.io.FileNotFoundException;

    /**
     * Created by IntelliJ IDEA.
     * User: john
     * Date: 07.03.11
     * Time: 4:26
     * To change this template use File | Settings | File Templates.
     */
    //This class will use external library "Web Harvester" (WH) sources to deal with html-pages
    //You can find it here: http://web-harvest.sourceforge.net/
    //Input: WH XML-based configuration file
    //Output: MySQL data base file with parsed entities

    public class Extractor {
        public static void main(String[] args) {

        try{
        String curDir = System.getProperty("user.dir");
        ScraperConfiguration config = new ScraperConfiguration(curDir + "/src/Harvester/config.xml");
        Scraper scraper = new Scraper(config, curDir);

        scraper.setDebug(true);
        scraper.execute();
        // takes variable created during execution
        Variable prices = (Variable) scraper.getContext().get("priceList");
        Variable descriptions = (Variable) scraper.getContext().get("descriptionList");
        Variable cdates = (Variable) scraper.getContext().get("cityDateList");
        Variable years = (Variable) scraper.getContext().get("yearList");
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e.getMessage());
        }

    }
}
