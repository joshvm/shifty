package ca.mohawk.jdw.shifty.server.net.client;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;
import ca.mohawk.jdw.shifty.companyapi.model.shift.Shift;
import ca.mohawk.jdw.shifty.server.model.activity.ActivityLog;
import ca.mohawk.jdw.shifty.server.model.shift.RequestResponse;
import ca.mohawk.jdw.shifty.server.model.shift.ShiftOffer;
import ca.mohawk.jdw.shifty.server.model.shift.ShiftOfferRequest;
import ca.mohawk.jdw.shifty.server.net.Opcode;
import ca.mohawk.jdw.shifty.server.net.Packet;
import ca.mohawk.jdw.shifty.server.net.utils.NetUtils;
import ca.mohawk.jdw.shifty.server.utils.Utils;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


import static ca.mohawk.jdw.shifty.server.Shifty.company;
import static ca.mohawk.jdw.shifty.server.Shifty.database;

public class Client {

    public static final AttributeKey<Client> CLIENT_KEY = AttributeKey.valueOf("client");

    public enum Tab {

        MY_SHIFTS(1),
        AVAILABLE_SHIFTS(2),
        MY_OFFERS(3),
        MY_OFFER_REQUESTS(4),
        ACTIVITY(5),
        PENDING_SHIFT_CHANGES(6);

        private static final Map<Integer, Tab> MAP = Utils.map(values(), Tab::id);

        private final int id;

        Tab(final int id){
            this.id = id;
        }

        public int id(){
            return id;
        }

        public static Tab forId(final int id){
            return MAP.get(id);
        }
    }

    private final ChannelHandlerContext ctx;
    private final Employee employee;

    private final Map<Tab, Boolean> loadedTabs;

    public Client(final ChannelHandlerContext ctx,
                  final Employee employee){
        this.ctx = ctx;
        this.employee = employee;

        loadedTabs = new HashMap<>();
    }

    public ChannelHandlerContext ctx(){
        return ctx;
    }

    public Employee employee(){
        return employee;
    }

    public ChannelFuture write(final Packet pkt){
        return NetUtils.write(ctx, pkt);
    }

    public ChannelFuture writeLater(final Packet pkt){
        return NetUtils.writeLater(ctx, pkt);
    }

    public ChannelFuture writeResponse(final Opcode opcode,
                                       final int response){
        return NetUtils.writeResponse(ctx, opcode, response);
    }

    public void flush(){
        ctx.flush();
    }

    public void init(){
        write(new Packet(Opcode.INIT, NetUtils.serialize(employee)));
    }

    public void addMyShift(final Shift shift){
        writeLater(new Packet(Opcode.ADD_MY_SHIFT, NetUtils.serialize(shift)));
    }

    public void addMyShifts(){
        company.shifts().between(employee, Utils.timestamp(), Utils.timestampPlus(21, TimeUnit.DAYS))
                .stream()
                .forEach(this::addMyShift);
        flush();
    }

    public void removeMyShift(final Shift shift){
        writeLater(
                new Packet(Opcode.REMOVE_MY_SHIFT)
                    .writeLong(shift.id())
        );
    }

    public void addAvailableShift(final ShiftOffer offer){
        writeLater(
                new Packet(Opcode.ADD_AVAILABLE_SHIFT)
                    .writeBuffer(NetUtils.serialize(offer.employee()))
                    .writeBuffer(NetUtils.serialize(offer.shift()))
        );
    }

    public void addAvailableShifts(){
        company.shifts().between(Utils.timestamp(), Utils.timestampPlus(21, TimeUnit.DAYS))
                .stream()
                .map(s -> database.shiftOffers().forId(s.id()))
                .filter(o -> o != null && !Objects.equals(o.employee(), employee) && company.shiftValidator().canRequest(o.employee(), o.shift(), employee))
                .forEach(this::addAvailableShift);
        flush();
    }

    public void removeAvailableShift(final ShiftOffer offer){
        writeLater(
                new Packet(Opcode.REMOVE_AVAILABLE_SHIFT)
                    .writeLong(offer.id())
        );
    }

    public void addMyShiftOffer(final ShiftOffer offer){
        writeLater(
                new Packet(Opcode.ADD_MY_SHIFT_OFFER)
                    .writeLong(offer.id())
        );
        addRequestsToMyShiftOffers(offer);
    }

    public void addMyShiftOffers(){
        company.shifts().between(employee, Utils.timestamp(), Utils.timestampPlus(21, TimeUnit.DAYS))
                .stream()
                .map(s -> database.shiftOffers().forId(s.id()))
                .filter(Objects::nonNull)
                .forEach(this::addMyShiftOffer);
        flush();
    }

    public void removeMyShiftOffer(final ShiftOffer offer){
        writeLater(
                new Packet(Opcode.REMOVE_MY_SHIFT_OFFER)
                    .writeLong(offer.id())
        );
    }

    public void addRequestToMyShiftOffer(final ShiftOfferRequest request){
        writeLater(
                new Packet(Opcode.ADD_REQUEST_TO_MY_SHIFT_OFFER)
                    .writeLong(request.offer().id())
                    .writeBuffer(NetUtils.serialize(request.employee()))
                    .writeByte(request.response().id())
        );
    }

    public void addRequestsToMyShiftOffers(final ShiftOffer offer){
        database.shiftOfferRequests().forOffer(offer.id())
                .forEach(this::addRequestToMyShiftOffer);
    }

    public void removeRequestFromMyShiftOffer(final ShiftOfferRequest request){
        writeLater(
                new Packet(Opcode.REMOVE_REQUEST_FROM_MY_SHIFT_OFFER)
                    .writeLong(request.id())
                    .writeLong(request.employee().id())
        );
    }

    public void updateRequestResponseFromMyShiftOffer(final ShiftOfferRequest request,
                                                     final RequestResponse response){
        writeLater(
                new Packet(Opcode.UPDATE_REQUEST_RESPONSE_FROM_MY_SHIFT_OFFER)
                    .writeLong(request.offer().id())
                    .writeLong(request.employee().id())
                    .writeByte(response.id())
        );
    }

    public void addMyShiftOfferRequest(final ShiftOfferRequest request){
        writeLater(
                new Packet(Opcode.ADD_MY_SHIFT_OFFER_REQUEST)
                    .writeLong(request.offer().id())
                    .writeByte(request.response().id())
        );
    }

    public void addMyShiftOfferRequests(){
        company.shifts().between(Utils.timestamp(), Utils.timestampPlus(21, TimeUnit.DAYS))
                .stream()
                .flatMap(s -> database.shiftOfferRequests().forOffer(s.id()).stream())
                .filter(sor -> sor != null && Objects.equals(sor.employee(), employee))
                .forEach(this::addMyShiftOfferRequest);
        flush();
    }

    public void removeMyShiftOfferRequest(final ShiftOfferRequest request){
        writeLater(
                new Packet(Opcode.REMOVE_MY_SHIFT_OFFER_REQUEST)
                    .writeLong(request.id())
        );
    }

    public void updateMyShiftOfferRequestResponse(final ShiftOfferRequest request,
                                                  final RequestResponse response){
        writeLater(
                new Packet(Opcode.UPDATE_MY_SHIFT_OFFER_REQUEST_RESPONSE)
                    .writeLong(request.offer().id())
                    .writeByte(response.id())
        );
    }

    public void addMyActivityLog(final ActivityLog log){
        writeLater(new Packet(Opcode.ADD_MY_ACTIVITY_LOG, NetUtils.serialize(log)));
    }

    public void addMyActivityLogs(){
        database.activityLogs().forEmployee(employee.id())
                .forEach(this::addMyActivityLog);
        flush();
    }

    public void addSearchedActivityLog(final ActivityLog log){
        writeLater(
                new Packet(Opcode.ADD_SEARCHED_ACTIVITY_LOG)
                    .writeBuffer(NetUtils.serialize(log.employee()))
                    .writeBuffer(NetUtils.serialize(log))
        );
    }

    public void addSearchedActivityLogs(final Collection<ActivityLog> logs){
        logs.forEach(this::addSearchedActivityLog);
        flush();
    }

    public void addPendingShiftChange(final ShiftOfferRequest request){
        writeLater(
                new Packet(Opcode.ADD_PENDING_SHIFT_CHANGE)
                    .writeBuffer(NetUtils.serialize(request.offer().employee()))
                    .writeBuffer(NetUtils.serialize(request.offer().shift()))
                    .writeBuffer(NetUtils.serialize(request.employee()))
        );
    }

    public void addPendingShiftChanges(){
        database.shiftOfferRequests().forResponse(RequestResponse.ACCEPTED)
                .stream()
                .filter(r -> Utils.between(r.timestamp(), Utils.timestamp(), Utils.timestampPlus(21, TimeUnit.DAYS)))
                .forEach(this::addPendingShiftChange);
        flush();
    }

    public void removePendingShiftChange(final ShiftOfferRequest request){
        writeLater(
                new Packet(Opcode.REMOVE_PENDING_SHIFT_CHANGE)
                    .writeLong(request.id())
        );
    }

    @Override
    public boolean equals(final Object obj){
        if(!(obj instanceof Client))
            return false;
        final Client c = (Client) obj;
        return employee.equals(c.employee);
    }

    public static Client register(final ChannelHandlerContext ctx,
                                  final Employee employee){
        final Client client = new Client(ctx, employee);
        ctx.channel().attr(CLIENT_KEY).set(client);
        return client;
    }
}
