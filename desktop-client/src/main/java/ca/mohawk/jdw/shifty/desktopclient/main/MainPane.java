package ca.mohawk.jdw.shifty.desktopclient.main;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: desktop-client
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;
import ca.mohawk.jdw.shifty.desktopclient.availableshifts.AvailableShiftsPane;
import ca.mohawk.jdw.shifty.desktopclient.employee.MyEmployeePane;
import ca.mohawk.jdw.shifty.desktopclient.myactivitylogs.MyActivityLogsPane;
import ca.mohawk.jdw.shifty.desktopclient.myshiftofferrequests.MyShiftOfferRequestsPane;
import ca.mohawk.jdw.shifty.desktopclient.myshiftoffers.MyShiftOffersPane;
import ca.mohawk.jdw.shifty.desktopclient.myshifts.MyShiftsPane;
import ca.mohawk.jdw.shifty.desktopclient.pendingshiftchanges.PendingShiftChangesPane;
import ca.mohawk.jdw.shifty.desktopclient.searchactivitylogs.SearchedActivityLogsPane;
import ca.mohawk.jdw.shifty.desktopclient.ui.UI;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;


import static ca.mohawk.jdw.shifty.desktopclient.DesktopClient.client;

public class MainPane extends BorderPane {

    private static class NavItem extends ListCell<Object> {

        @Override
        protected void updateItem(final Object value, final boolean empty){
            super.updateItem(value, empty);
            if(value == null)
                return;
            setText(null);
            if(value instanceof Tab){
                final Tab tab = (Tab) value;
                final Label label = new Label(tab.title());
                label.setGraphic(tab.graphic());
                label.getStyleClass().add("navItem");
                setGraphic(label);
            }else if(value instanceof Employee.Rank){
                final Employee.Rank rank = (Employee.Rank) value;
                final Label rankLabel = new Label(rank.name());
                rankLabel.getStyleClass().add("heading");
                final BorderPane pane = new BorderPane();
                pane.setCenter(rankLabel);
                pane.setBottom(new Separator(Orientation.HORIZONTAL));
                setGraphic(pane);
                setDisable(true);
                setDisabled(true);
            }
        }
    }

    private final MyEmployeePane myEmployeePane;

    private final ObservableList<Object> navItems;
    private final ListView<Object> nav;

    private Label headingLabel;
    private final BorderPane contentPane;

    private final MyShiftsPane myShiftsPane;
    private final AvailableShiftsPane availableShiftsPane;
    private final MyShiftOffersPane myShiftOffersPane;
    private final MyShiftOfferRequestsPane myShiftOfferRequestsPane;
    private final MyActivityLogsPane myActivityLogsPane;
    private final PendingShiftChangesPane pendingShiftChangesPane;
    private final SearchedActivityLogsPane searchedActivityLogsPane;
    private final Map<Tab, Node> contentMap;

    public MainPane(){
        getStylesheets().add(UI.css("mainPane"));
        myEmployeePane = new MyEmployeePane(client.employee());

        myShiftsPane = new MyShiftsPane();
        availableShiftsPane = new AvailableShiftsPane();
        myShiftOffersPane = new MyShiftOffersPane();
        myShiftOfferRequestsPane = new MyShiftOfferRequestsPane();
        myActivityLogsPane = new MyActivityLogsPane();
        pendingShiftChangesPane = new PendingShiftChangesPane();
        searchedActivityLogsPane = new SearchedActivityLogsPane();

        contentMap = new HashMap<>();
        contentMap.put(Tab.MY_SHIFTS, myShiftsPane);
        contentMap.put(Tab.AVAILABLE_SHIFTS, availableShiftsPane);
        contentMap.put(Tab.MY_SHIFT_OFFERS, myShiftOffersPane);
        contentMap.put(Tab.MY_SHIFT_OFFER_REQUESTS, myShiftOfferRequestsPane);
        contentMap.put(Tab.ACTIVITY_LOG, myActivityLogsPane);
        contentMap.put(Tab.PENDING_SHIFT_CHANGES, pendingShiftChangesPane);
        contentMap.put(Tab.SEARCH_ACTIVITY_LOGS, searchedActivityLogsPane);

        headingLabel = new Label(Tab.MY_SHIFTS.title());
        BorderPane.setAlignment(headingLabel, Pos.CENTER);
        headingLabel.getStyleClass().add("heading");

        contentPane = new BorderPane();
        BorderPane.setMargin(contentPane, new Insets(5));
        contentPane.setTop(headingLabel);
        contentPane.setCenter(myShiftsPane);

        navItems = FXCollections.observableArrayList();
        for(final Employee.Rank rank : Employee.Rank.values()){
            if(client.employee().rank().id() >= rank.id()){
                navItems.add(rank);
                for(final Tab tab : Tab.VALUES)
                    if(tab.rank() == rank)
                        navItems.add(tab);
            }
        }

        nav = new ListView<>(navItems);
        nav.setCellFactory(param -> new NavItem());
        nav.getSelectionModel().select(Tab.MY_SHIFTS);
        nav.getSelectionModel().selectedItemProperty()
                .addListener(
                        (ob, o, n) -> {
                            if(n != null && n instanceof Tab){
                                final Tab tab = (Tab) n;
                                headingLabel.setText(tab.title());
                                contentPane.setCenter(contentMap.get(tab));
                            }else{
                                headingLabel.setText("");
                                contentPane.setCenter(null);
                            }
                            if(n != null && n instanceof Tab)
                                headingLabel.setText(((Tab)n).title());
                            else
                                headingLabel.setText("");
                        }
                );

        final BorderPane left = new BorderPane();
        BorderPane.setMargin(left, new Insets(5));
        left.setPrefWidth(250);
        left.setTop(myEmployeePane);
        left.setCenter(nav);

        setLeft(left);
        setCenter(contentPane);
    }
}
