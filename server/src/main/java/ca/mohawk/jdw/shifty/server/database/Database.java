package ca.mohawk.jdw.shifty.server.database;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.server.database.activity.ActivityLogDao;
import ca.mohawk.jdw.shifty.server.database.shift.ShiftOfferDao;
import ca.mohawk.jdw.shifty.server.database.shift.ShiftOfferRequestDao;
import ca.mohawk.jdw.shifty.server.utils.Utils;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import org.skife.jdbi.v2.DBI;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;

public class Database {

    public static class Config {

        private final String url;

        public Config(final String url){
            this.url = url;
        }

        public String url(){
            return url;
        }

        public static Config parse(final Path path) throws IOException{
            final Properties props = Utils.props(path);
            return new Config(
                    props.getProperty("url")
            );
        }
    }

    private final Config config;

    private DBI dbi;

    private ShiftOfferDao shiftOffers;
    private ShiftOfferRequestDao shiftOfferRequests;

    private ActivityLogDao activityLogs;

    public Database(final Config config){
        this.config = config;
    }

    public void init(){
        final SQLiteConnectionPoolDataSource ds = new SQLiteConnectionPoolDataSource();
        ds.setUrl(config.url);

        dbi = new DBI(ds);

        shiftOffers = dbi.open(ShiftOfferDao.class);
        shiftOfferRequests = dbi.open(ShiftOfferRequestDao.class);

        activityLogs = dbi.open(ActivityLogDao.class);
    }

    public ShiftOfferDao shiftOffers(){
        return shiftOffers;
    }

    public ShiftOfferRequestDao shiftOfferRequests(){
        return shiftOfferRequests;
    }

    public ActivityLogDao activityLogs(){
        return activityLogs;
    }
}
