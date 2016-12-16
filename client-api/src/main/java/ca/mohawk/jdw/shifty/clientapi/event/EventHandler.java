package ca.mohawk.jdw.shifty.clientapi.event;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.ShiftyClient;
import ca.mohawk.jdw.shifty.clientapi.event.response.Response;
import ca.mohawk.jdw.shifty.clientapi.event.type.AddSearchedActivityLogEvent;
import ca.mohawk.jdw.shifty.clientapi.event.type.AddAvailableShiftEvent;
import ca.mohawk.jdw.shifty.clientapi.event.type.AddMyActivityLogEvent;
import ca.mohawk.jdw.shifty.clientapi.event.type.AddMyShiftEvent;
import ca.mohawk.jdw.shifty.clientapi.event.type.AddMyShiftOfferEvent;
import ca.mohawk.jdw.shifty.clientapi.event.type.AddMyShiftOfferRequestEvent;
import ca.mohawk.jdw.shifty.clientapi.event.type.AddPendingShiftChangeEvent;
import ca.mohawk.jdw.shifty.clientapi.event.type.AddRequestToMyShiftOfferEvent;
import ca.mohawk.jdw.shifty.clientapi.event.type.RemoveAvailableShiftEvent;
import ca.mohawk.jdw.shifty.clientapi.event.type.RemoveMyShiftEvent;
import ca.mohawk.jdw.shifty.clientapi.event.type.RemoveMyShiftOfferEvent;
import ca.mohawk.jdw.shifty.clientapi.event.type.RemoveMyShiftOfferRequestEvent;
import ca.mohawk.jdw.shifty.clientapi.event.type.RemovePendingShiftChangeEvent;
import ca.mohawk.jdw.shifty.clientapi.event.type.RemoveRequestFromMyShiftOfferEvent;
import ca.mohawk.jdw.shifty.clientapi.event.type.UpdateMyShiftOfferRequestResponseEvent;
import ca.mohawk.jdw.shifty.clientapi.event.type.UpdateRequestResponseFromMyShiftOfferEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class EventHandler {

    public interface SyncWorker {

        void sync(final Runnable task);
    }

    private final List<EventListener> globalListeners;
    private final Map<EventType, List<EventListener>> listeners;
    private final Map<EventType, EventListener> syncListeners;

    private final Map<EventType, SyncWorker> syncWorkers;

    public EventHandler(){
        globalListeners = new ArrayList<>();
        listeners = new HashMap<>();

        syncWorkers = new HashMap<>();

        syncListeners = new HashMap<>();
        syncListeners.put(EventType.ADD_MY_SHIFT,
                (EventListener<AddMyShiftEvent>) (c, e) -> {
                    syncWorker(EventType.ADD_MY_SHIFT).sync(() -> {
                        c.myShifts().add(e.shift());
                    });
                });
        syncListeners.put(EventType.REMOVE_MY_SHIFT,
                (EventListener<RemoveMyShiftEvent>) (c, e) -> {
                    syncWorker(EventType.REMOVE_MY_SHIFT).sync(() -> c.myShifts().remove(e.shift()));
                });
        syncListeners.put(EventType.ADD_AVAILABLE_SHIFT,
                (EventListener<AddAvailableShiftEvent>) (c, e) -> {
                    syncWorker(EventType.ADD_AVAILABLE_SHIFT).sync(() -> {
                        if(c.availableShifts().list().stream().noneMatch(s -> s.id() == e.offer().id()))
                            c.availableShifts().add(e.offer());
                    });
                });
        syncListeners.put(EventType.ADD_MY_SHIFT_OFFER,
                (EventListener<AddMyShiftOfferEvent>) (c, e) -> {
                    syncWorker(EventType.ADD_MY_SHIFT_OFFER).sync(() -> c.myShiftOffers().add(e.offer()));
                });
        syncListeners.put(EventType.ADD_REQUEST_TO_MY_SHIFT_OFFER,
                (EventListener<AddRequestToMyShiftOfferEvent>) (c, e) -> {
                    syncWorker(EventType.ADD_REQUEST_TO_MY_SHIFT_OFFER).sync(() -> e.request().offer().requests().add(e.request()));
                });
        syncListeners.put(EventType.ADD_MY_SHIFT_OFFER_REQUEST,
                (EventListener<AddMyShiftOfferRequestEvent>) (c, e) -> {
                    syncWorker(EventType.ADD_MY_SHIFT_OFFER_REQUEST).sync(() -> c.myShiftOfferRequests().add(e.request()));
                });
        syncListeners.put(EventType.REMOVE_AVAILABLE_SHIFT,
                (EventListener<RemoveAvailableShiftEvent>) (c, e) -> {
                    syncWorker(EventType.REMOVE_AVAILABLE_SHIFT).sync(() -> c.availableShifts().remove(e.shift()));
                });
        syncListeners.put(EventType.REMOVE_MY_SHIFT_OFFER,
                (EventListener<RemoveMyShiftOfferEvent>) (c, e) -> {
                    syncWorker(EventType.REMOVE_MY_SHIFT_OFFER).sync(() -> c.myShiftOffers().remove(e.offer()));
                });
        syncListeners.put(EventType.REMOVE_REQUEST_FROM_MY_SHIFT_OFFER,
                (EventListener<RemoveRequestFromMyShiftOfferEvent>) (c, e) -> {
                    syncWorker(EventType.REMOVE_REQUEST_FROM_MY_SHIFT_OFFER).sync(() -> e.request().offer().requests().remove(e.request()));
                });
        syncListeners.put(EventType.REMOVE_MY_SHIFT_OFFER_REQUEST,
                (EventListener<RemoveMyShiftOfferRequestEvent>) (c, e) -> {
                    syncWorker(EventType.REMOVE_MY_SHIFT_OFFER_REQUEST).sync(() -> c.myShiftOfferRequests().remove(e.request()));
                });
        syncListeners.put(EventType.ADD_MY_ACTIVITY_LOG,
                (EventListener<AddMyActivityLogEvent>) (c, e) -> {
                    syncWorker(EventType.ADD_MY_ACTIVITY_LOG).sync(() -> c.myActivityLogs().add(e.log()));
                });
        syncListeners.put(EventType.ADD_SEARCHED_ACTIVITY_LOG,
                (EventListener<AddSearchedActivityLogEvent>) (c, e) -> {
                    syncWorker(EventType.ADD_SEARCHED_ACTIVITY_LOG).sync(() -> c.searchedActivityLogs().add(e.log()));
                });
        syncListeners.put(EventType.UPDATE_MY_SHIFT_OFFER_REQUEST_RESPONSE,
                (EventListener<UpdateMyShiftOfferRequestResponseEvent>) (c, e) -> {
                    syncWorker(EventType.UPDATE_MY_SHIFT_OFFER_REQUEST_RESPONSE).sync(() -> e.request().answer(e.answer()));
                });
        syncListeners.put(EventType.UPDATE_REQUEST_RESPONSE_FROM_MY_SHIFT_OFFER,
                (EventListener<UpdateRequestResponseFromMyShiftOfferEvent>) (c, e) -> {
                    syncWorker(EventType.UPDATE_REQUEST_RESPONSE_FROM_MY_SHIFT_OFFER).sync(() -> e.request().answer(e.answer()));
                });
        syncListeners.put(EventType.ADD_PENDING_SHIFT_CHANGE,
                (EventListener<AddPendingShiftChangeEvent>) (c, e) -> {
                    syncWorker(EventType.ADD_PENDING_SHIFT_CHANGE).sync(() -> c.pendingShiftChanges().add(e.pendingShiftChange()));
                });
        syncListeners.put(EventType.REMOVE_PENDING_SHIFT_CHANGE,
                (EventListener<RemovePendingShiftChangeEvent>) (c, e) -> {
                    syncWorker(EventType.REMOVE_PENDING_SHIFT_CHANGE).sync(() -> c.pendingShiftChanges().remove(e.pendingShiftChange()));
                });
    }

    public void syncWorker(final EventType type,
                           final SyncWorker syncWorker){
        syncWorkers.put(type, syncWorker);
    }

    public SyncWorker syncWorker(final EventType type){
        return syncWorkers.getOrDefault(type, Runnable::run);
    }

    public void addGlobalListener(final EventListener listener){
        globalListeners.add(listener);
    }

    public void removeGlobalListener(final EventListener listener){
        globalListeners.remove(listener);
    }

    public List<EventListener> globalListeners(){
        return globalListeners;
    }

    public void addListener(final EventType type,
                            final EventListener listener){
        if(!listeners.containsKey(type))
            listeners.put(type, new ArrayList<>());
        listeners.get(type).add(listener);
    }

    public void onAddMyShiftEvent(final EventListener<AddMyShiftEvent> listener){
        addListener(EventType.ADD_MY_SHIFT, listener);
    }

    public void onAddAvailableShift(final EventListener<AddAvailableShiftEvent> listener){
        addListener(EventType.ADD_AVAILABLE_SHIFT, listener);
    }

    public void onAddMyShiftOfferEvent(final EventListener<AddMyShiftOfferEvent> listener){
        addListener(EventType.ADD_MY_SHIFT_OFFER, listener);
    }

    public void onAddRequestToMyShiftOfferEvent(final EventListener<AddRequestToMyShiftOfferEvent> listener){
        addListener(EventType.ADD_REQUEST_TO_MY_SHIFT_OFFER, listener);
    }

    public void onAddMyShiftOfferRequestEvent(final EventListener<AddMyShiftOfferRequestEvent> listener){
        addListener(EventType.ADD_MY_SHIFT_OFFER_REQUEST, listener);
    }

    public void onRemoveAvailableShiftEvent(final EventListener<RemoveAvailableShiftEvent> listener){
        addListener(EventType.REMOVE_AVAILABLE_SHIFT, listener);
    }

    public void onAddMyActivityLogEvent(final EventListener<AddMyActivityLogEvent> listener){
        addListener(EventType.ADD_MY_ACTIVITY_LOG, listener);
    }

    public void onAddSearchedActivityLogEvent(final EventListener<AddSearchedActivityLogEvent> listener){
        addListener(EventType.ADD_SEARCHED_ACTIVITY_LOG, listener);
    }

    public void onUpdateMyShiftOfferRequestResponseEvent(final EventListener<UpdateMyShiftOfferRequestResponseEvent> listener){
        addListener(EventType.UPDATE_MY_SHIFT_OFFER_REQUEST_RESPONSE, listener);
    }

    public void onUpdateRequestResponseFromMyShiftOfferEvent(final EventListener<UpdateRequestResponseFromMyShiftOfferEvent> listener){
        addListener(EventType.UPDATE_REQUEST_RESPONSE_FROM_MY_SHIFT_OFFER, listener);
    }

    public void onCancelMyShiftOfferResponse(final EventListener<Response> listener){
        addListener(EventType.CANCEL_MY_SHIFT_OFFER_RESPONSE, listener);
    }

    public void onOfferMyShiftOfferResponse(final EventListener<Response> listener){
        addListener(EventType.OFFER_MY_SHIFT_RESPONSE, listener);
    }

    public void onRequestToTakeMyShiftResponse(final EventListener<Response> listener){
        addListener(EventType.REQUEST_TO_TAKE_SHIFT_RESPONSE, listener);
    }

    public void onAnswerRequestFromMyShiftOfferResponse(final EventListener<Response> listener){
        addListener(EventType.ANSWER_REQUEST_FROM_MY_SHIFT_OFFER_RESPONSE, listener);
    }

    public void onCancelRequestToTakeShiftResponse(final EventListener<Response> listener){
        addListener(EventType.CANCEL_REQUEST_TO_TAKE_SHIFT_RESPONSE, listener);
    }

    public void onAnswerPendingShiftChangeResponse(final EventListener<Response> listener){
        addListener(EventType.ANSWER_PENDING_SHIFT_CHANGE_RESPONSE, listener);
    }

    public void onSearchActivityLogsByEmployeeResponse(final EventListener<Response> listener){
        addListener(EventType.SEARCH_ACTIVITY_LOGS_BY_EMPLOYEE_RESPONSE, listener);
    }

    public void removeListener(final EventType type,
                               final EventListener listener){
        if(listeners.containsKey(type))
            listeners.get(type).remove(listener);
    }

    public void fire(final ShiftyClient client,
                     final Event e){
        final EventListener syncListener = syncListeners.get(e.type());
        if(syncListener != null)
            syncListener.onEvent(client, e);
        Stream.concat(globalListeners.stream(), listeners.getOrDefault(e.type(), Collections.emptyList()).stream())
                .forEach(l -> l.onEvent(client, e));
    }
}
