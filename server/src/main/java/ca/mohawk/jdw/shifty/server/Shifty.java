package ca.mohawk.jdw.shifty.server;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.Company;
import ca.mohawk.jdw.shifty.server.database.Database;
import ca.mohawk.jdw.shifty.server.net.client.ClientManager;
import ca.mohawk.jdw.shifty.server.utils.CompanyLoader;
import java.io.File;
import java.nio.file.Paths;

public class Shifty {

    public static ClientManager clients;

    public static Company company;

    public static Database.Config databaseConfig;
    public static Database database;

    public static Server.Config serverConfig;
    public static Server server;

    public static void main(String[] args) throws Exception {
        clients = new ClientManager();

        company = CompanyLoader.load(new File(args[0]));
//        company = CompanyLoader.load(new File("test-company.jar"));

        databaseConfig = Database.Config.parse(Paths.get(args[1]));
//        databaseConfig = Database.Config.parse(Paths.get("database.properties"));
        database = new Database(databaseConfig);

        serverConfig = Server.Config.parse(Paths.get(args[2]));
//        serverConfig = Server.Config.parse(Paths.get("server.properties"));
        server = new Server(serverConfig);

        database.init();
        server.start();
    }
}
