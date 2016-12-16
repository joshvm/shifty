package ca.mohawk.jdw.shifty.desktopclient.main;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: desktop-client
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;
import javafx.scene.Node;

public enum Tab {

    MY_SHIFTS("My Shifts", null, Employee.Rank.EMPLOYEE),
    AVAILABLE_SHIFTS("Available Shifts", null, Employee.Rank.EMPLOYEE),
    MY_SHIFT_OFFERS("My Shift Offers", null, Employee.Rank.EMPLOYEE),
    MY_SHIFT_OFFER_REQUESTS("My Shift Offer Requests", null, Employee.Rank.EMPLOYEE),
    ACTIVITY_LOG("Activity Log", null, Employee.Rank.EMPLOYEE),
    PENDING_SHIFT_CHANGES("Pending Shift Changes", null, Employee.Rank.MANAGER),
    SEARCH_ACTIVITY_LOGS("Search Activity Logs", null, Employee.Rank.MANAGER);

    public static final Tab[] VALUES = values();

    private final String title;
    private final Node graphic;
    private final Employee.Rank rank;

    Tab(final String title,
        final Node graphic,
        final Employee.Rank rank){
        this.title = title;
        this.graphic = graphic;
        this.rank = rank;
    }

    public String title(){
        return title;
    }

    public Node graphic(){
        return graphic;
    }

    public Employee.Rank rank(){
        return rank;
    }
}
