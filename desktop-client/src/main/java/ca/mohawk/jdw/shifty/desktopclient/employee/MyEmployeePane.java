package ca.mohawk.jdw.shifty.desktopclient.employee;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: desktop-client
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;
import ca.mohawk.jdw.shifty.desktopclient.ui.UI;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;


import static ca.mohawk.jdw.shifty.desktopclient.DesktopClient.client;

public class MyEmployeePane extends BorderPane {

    private final Employee employee;

    private final Button logoutButton;
    private final Label nameLabel;

    private final TextArea descArea;

    public MyEmployeePane(final Employee employee){
        this.employee = employee;
        getStylesheets().add(UI.css("myEmployeePane"));

        logoutButton = new Button("<");
        logoutButton.setOnAction(
                e -> {
                    if(client != null)
                        client.stop();
                }
        );

        nameLabel = new Label(String.format("%s %s", employee.firstName(), employee.lastName()));
        nameLabel.setTooltip(new Tooltip(String.format("ID: %s%nUser: %s%nRank: %s", employee.id(), employee.user(), employee.rank())));
        BorderPane.setAlignment(nameLabel, Pos.CENTER);

        final BorderPane top = new BorderPane();
        top.setCenter(nameLabel);
        top.setLeft(logoutButton);

        descArea = new TextArea(employee.description());
        descArea.setEditable(false);
        descArea.setWrapText(true);
        descArea.setPrefRowCount(2);

        setTop(top);
        setCenter(descArea);
    }
}
