package ca.mohawk.jdw.shifty.clientapi;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.auth.LoginData;
import ca.mohawk.jdw.shifty.clientapi.auth.LoginResponse;
import ca.mohawk.jdw.shifty.clientapi.event.Event;
import ca.mohawk.jdw.shifty.clientapi.event.EventHandler;
import ca.mohawk.jdw.shifty.clientapi.event.response.ResponseCode;
import ca.mohawk.jdw.shifty.clientapi.model.ActivityLog;
import ca.mohawk.jdw.shifty.clientapi.model.PendingShiftChange;
import ca.mohawk.jdw.shifty.clientapi.model.RequestAnswer;
import ca.mohawk.jdw.shifty.clientapi.model.ShiftOffer;
import ca.mohawk.jdw.shifty.clientapi.net.Opcode;
import ca.mohawk.jdw.shifty.clientapi.net.Packet;
import ca.mohawk.jdw.shifty.clientapi.utils.NetUtils;
import ca.mohawk.jdw.shifty.companyapi.Company;
import ca.mohawk.jdw.shifty.companyapi.model.ModelManager;
import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;
import ca.mohawk.jdw.shifty.companyapi.model.shift.Shift;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShiftyClient {

    public interface Listener {

        void onDisconnect(final ShiftyClient client);
    }

    public static class Config {

        private final String host;
        private final int port;

        private final Company company;

        public Config(final String host,
                      final int port) throws IOException {
            this.host = host;
            this.port = port;

            final Connection con = new Connection(host, port);
            con.send(new Packet(Opcode.REQUEST_COMPANY_INFO));
            final Packet pkt = con.readForOpcode(Opcode.INIT_COMPANY_INFO);
            con.disconnect();

            company = NetUtils.deserializeCompany(pkt);
            pkt.buf().release();
        }

        public String host(){
            return host;
        }

        public int port(){
            return port;
        }

        public Company company(){
            return company;
        }
    }

    private final Config config;
    private final Connection con;
    private final Employee employee;

    private final ModelManager<Shift> myShifts;
    private final ModelManager<ShiftOffer> availableShifts;
    private final ModelManager<ShiftOffer> myShiftOffers;
    private final ModelManager<ShiftOffer.Request> myShiftOfferRequests;
    private final ModelManager<ActivityLog> myActivityLogs;
    private final ModelManager<ActivityLog> searchedActivityLogs;
    private final ModelManager<PendingShiftChange> pendingShiftChanges;

    private final List<Listener> listeners;

    private EventHandler eventHandler;

    private ShiftyClient(final Config config,
                         final Connection con,
                         final Employee employee){
        this.config = config;
        this.con = con;
        this.employee = employee;

        myShifts = new ModelManager<>();
        availableShifts = new ModelManager<>();
        myShiftOffers = new ModelManager<>();
        myShiftOfferRequests = new ModelManager<>();
        myActivityLogs = new ModelManager<>();
        searchedActivityLogs = new ModelManager<>();
        pendingShiftChanges = new ModelManager<>();

        listeners = new ArrayList<>();

        eventHandler = new EventHandler();
    }

    public Config config(){
        return config;
    }

    public Employee employee(){
        return employee;
    }

    public void addListener(final Listener listener){
        listeners.add(listener);
    }

    public void removeListener(final Listener listener){
        listeners.remove(listener);
    }

    public List<Listener> listeners(){
        return listeners;
    }

    public void start(){
        con.listener = new Connection.Listener() {
            @Override
            public void onRead(final Event e){
                fireEvent(e);
            }

            @Override
            public void onDisconnect(){
                listeners.forEach(l -> l.onDisconnect(ShiftyClient.this));
            }
        };
        con.startThread();
    }

    public void stop(){
        con.disconnect();
    }

    public ModelManager<Shift> myShifts(){
        return myShifts;
    }

    public ModelManager<ShiftOffer> availableShifts(){
        return availableShifts;
    }

    public ModelManager<ShiftOffer> myShiftOffers(){
        return myShiftOffers;
    }

    public ModelManager<ShiftOffer.Request> myShiftOfferRequests(){
        return myShiftOfferRequests;
    }

    public ModelManager<ActivityLog> myActivityLogs(){
        return myActivityLogs;
    }

    public ModelManager<ActivityLog> searchedActivityLogs(){
        return searchedActivityLogs;
    }

    public ModelManager<PendingShiftChange> pendingShiftChanges(){
        return pendingShiftChanges;
    }

    public EventHandler eventHandler(){
        return eventHandler;
    }

    public void eventHandler(final EventHandler eventHandler){
        this.eventHandler = eventHandler;
    }

    public void fireEvent(final Event e){
        eventHandler.fire(this, e);
    }

    private void send(final Packet pkt) throws IOException {
        con.send(pkt);
    }

    private boolean trySend(final Packet pkt){
        try{
            send(pkt);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public boolean offerMyShift(final Shift shift){
        return trySend(
                new Packet(Opcode.OFFER_MY_SHIFT)
                    .writeLong(shift.id())
        );
    }

    public boolean requestToTakeShift(final ShiftOffer availableShift){
        return trySend(
                new Packet(Opcode.REQUEST_TO_TAKE_SHIFT)
                    .writeLong(availableShift.id())
        );
    }

    public boolean cancelMyShiftOffer(final ShiftOffer offer){
        return trySend(
                new Packet(Opcode.CANCEL_MY_SHIFT_OFFER)
                    .writeLong(offer.id())
        );
    }

    public boolean answerRequestFromMyShiftOffer(final ShiftOffer.Request request,
                                                 final RequestAnswer answer){
        return trySend(
                new Packet(Opcode.ANSWER_REQUEST_FROM_MY_SHIFT_OFFER)
                    .writeLong(request.offer().id())
                    .writeLong(request.employee().id())
                    .writeByte(answer.id())
        );
    }

    public boolean cancelRequestToTakeShift(final ShiftOffer.Request request){
        return trySend(
                new Packet(Opcode.CANCEL_REQUEST_TO_TAKE_SHIFT)
                    .writeLong(request.offer().id())
        );
    }
    
    public boolean answerPendingShiftChange(final PendingShiftChange pendingShiftChange,
                                            final RequestAnswer answer){
        return trySend(
                new Packet(Opcode.ANSWER_PENDING_SHIFT_CHANGE)
                    .writeLong(pendingShiftChange.id())
                    .writeLong(pendingShiftChange.requestingEmployee().id())
                    .writeByte(answer.id())
        );
    }

    public boolean searchActivityLogsByEmployee(final String employeeUser){
        return trySend(
                new Packet(Opcode.SEARCH_ACTIVITY_LOGS_BY_EMPLOYEE)
                    .writeString(employeeUser)
        );
    }

    public static LoginResponse login(final Config config,
                                      final LoginData data) throws IOException{
        final Connection con = new Connection(config.host, config.port);
        con.send(
                new Packet(Opcode.LOGIN)
                    .writeString(data.user())
                    .writeString(data.pass())
        );
        Packet pkt = con.readForOpcode(Opcode.LOGIN_RESPONSE);
        final ResponseCode code = LoginResponse.Code.forId(pkt.readUnsignedByte());
        pkt.buf().release();
        if(code != ResponseCode.SUCCESS){
            con.disconnect();
            return new LoginResponse(config, data, code, null);
        }
        pkt = con.readForOpcode(Opcode.INIT);
        final Employee employee = NetUtils.deserializeEmployee(pkt);
        pkt.buf().release();
        final ShiftyClient client = new ShiftyClient(config, con, employee);
        con.client = client;
        return new LoginResponse(config, data, code, client);
    }
}
