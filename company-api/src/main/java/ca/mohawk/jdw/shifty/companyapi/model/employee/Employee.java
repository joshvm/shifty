package ca.mohawk.jdw.shifty.companyapi.model.employee;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: company-api
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.Model;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Employee implements Model {

    public enum Rank {

        EMPLOYEE(1),
        MANAGER(2);

        private static final Map<Integer, Rank> MAP = Stream.of(values())
                .collect(Collectors.toMap(Rank::id, Function.identity()));

        private final int id;

        Rank(final int id){
            this.id = id;
        }

        public int id(){
            return id;
        }

        public static Rank forId(final int id){
            return MAP.get(id);
        }
    }

    private final long id;
    private final String user;
    private final Rank rank;
    private final String firstName;
    private final String lastName;

    private final String description;

    public Employee(final long id,
                    final String user,
                    final Rank rank,
                    final String firstName,
                    final String lastName,
                    final String description){
        this.id = id;
        this.user = user;
        this.rank = rank;
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
    }

    public long id(){
        return id;
    }

    public String user(){
        return user;
    }

    public Rank rank(){
        return rank;
    }

    public boolean isManager(){
        return rank == Rank.MANAGER;
    }

    public String firstName(){
        return firstName;
    }

    public String lastName(){
        return lastName;
    }

    public String description(){
        return description;
    }

    @Override
    public boolean equals(final Object obj){
        if(!(obj instanceof Employee))
            return false;
        final Employee e = (Employee) obj;
        return id == e.id
                && user.equals(e.user);
    }
}
