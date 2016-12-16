package ca.mohawk.jdw.shifty.companyapi.model;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: company-api
  
  Developed By: Josh Maione (000320309)
*/

import java.util.Comparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

public class ModelManager<M extends Model> {

    private final ObservableList<M> list;

    public ModelManager(){
        list = FXCollections.observableArrayList();
    }

    public ObservableList<M> list(){
        return list;
    }

    public SortedList<M> sorted(final Comparator<M> sort){
        return new SortedList<>(list, sort);
    }

    public void add(final M m){
        if(!list.contains(m))
            list.add(m);
    }

    public void remove(final M m){
        list.remove(m);
    }

    public M forId(final long id){
        return list.stream()
                .filter(m -> m.id() == id)
                .findFirst()
                .orElse(null);
    }
}
