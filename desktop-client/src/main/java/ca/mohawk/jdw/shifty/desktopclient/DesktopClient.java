package ca.mohawk.jdw.shifty.desktopclient;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: desktop-client
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.ShiftyClient;
import ca.mohawk.jdw.shifty.clientapi.event.EventType;
import ca.mohawk.jdw.shifty.clientapi.event.response.Response;
import ca.mohawk.jdw.shifty.clientapi.event.response.ResponseCode;
import ca.mohawk.jdw.shifty.desktopclient.auth.LoginPane;
import ca.mohawk.jdw.shifty.desktopclient.main.MainPane;
import ca.mohawk.jdw.shifty.desktopclient.ui.UI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DesktopClient extends Application {

    private static Stage stage;

    public static ShiftyClient.Config config;
    public static ShiftyClient client;

    private static LoginPane loginPane;
    private static Scene loginScene;

    private static MainPane mainPane;
    private static Scene mainScene;

    @Override
    public void start(final Stage primaryStage) throws Exception{
        stage = primaryStage;
        stage.setOnCloseRequest(e -> {
            if(client != null)
                client.stop();
            System.exit(0);
        });

        config = new ShiftyClient.Config(
                getParameters().getRaw().get(0),
                Integer.parseInt(getParameters().getRaw().get(1))
        );

        loginPane = new LoginPane(config);
        loginScene = new Scene(loginPane, 500, 350);

        toLoginScene();
        stage.show();
    }

    public static void toLoginScene(){
        stage.setTitle(String.format("Shifty - %s", config.company().name()));
        stage.setScene(loginScene);
    }

    public static void toMainScene(final ShiftyClient client){
        client.eventHandler().syncWorker(EventType.UPDATE_REQUEST_RESPONSE_FROM_MY_SHIFT_OFFER, Platform::runLater);
        client.eventHandler().syncWorker(EventType.UPDATE_MY_SHIFT_OFFER_REQUEST_RESPONSE, Platform::runLater);
        client.eventHandler().syncWorker(EventType.ADD_SEARCHED_ACTIVITY_LOG, Platform::runLater);
        client.addListener(
                c -> {
                    DesktopClient.client.stop();
                    DesktopClient.client = null;
                    Platform.runLater(DesktopClient::toLoginScene);
                }
        );
        client.eventHandler().addGlobalListener(
                (c, e) -> {
                    if(e instanceof Response){
                        final Response r = (Response) e;
                        if(r.code() == ResponseCode.SUCCESS)
                            return;
                        UI.alertResponse(r);
                    }
                }
        );
        client.start();
        DesktopClient.client = client;
        mainPane = new MainPane();
        mainScene = new Scene(mainPane, 800, 600);
        stage.setTitle(String.format("Shifty - %s - %s", config.company().name(), DesktopClient.client.employee().user()));
        stage.setScene(mainScene);
    }

    public static void main(String[] args){
        launch(DesktopClient.class, args);
    }
}
