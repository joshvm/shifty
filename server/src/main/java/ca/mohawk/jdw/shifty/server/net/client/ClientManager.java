package ca.mohawk.jdw.shifty.server.net.client;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ClientManager {

    private final Map<Long, Client> map;

    public ClientManager(){
        map = new HashMap<>();
    }

    public void add(final Client client){
        map.put(client.employee().id(), client);
    }

    public void remove(final Client client){
        map.remove(client.employee().id());
    }

    public Collection<Client> values(){
        return map.values();
    }

    public Stream<Client> stream(){
        return values().stream();
    }

    public Stream<Client> byRank(final Employee.Rank rank){
        return stream()
                .filter(c -> c.employee().rank() == rank);
    }

    public Client forId(final long id){
        return map.get(id);
    }

}
