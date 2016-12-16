package ca.mohawk.jdw.shifty.desktopclient.log;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: desktop-client
  
  Developed By: Josh Maione (000320309)
*/

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

public class ActivityLogPane extends BorderPane {

    private final ObservableList<String> list;
    private final ListView<String> listView;

    public ActivityLogPane(){
        list = FXCollections.observableArrayList();
        list.add(String.format("[%s] You offered to give your shift on Saturday at 07:00", LocalDate.now().format(DateTimeFormatter.ISO_DATE)));
        list.add(String.format("[%s] Mohammed offered to take your shift on Saturday at 07:00", LocalDate.now().format(DateTimeFormatter.ISO_DATE)));
        list.add(String.format("[%s] You cancelled your shift offer on Saturday at 07:00",  LocalDate.now().format(DateTimeFormatter.ISO_DATE)));

        listView = new ListView<>(list);

        setCenter(listView);
    }
}
