package co.edu.javeriana.bikewars.Interfaces;

import co.edu.javeriana.bikewars.Logic.Entities.dbObservable;

/**
 * Created by Todesser on 30/10/2017.
 */

public interface NewGroupListener {
    void friendAdded(dbObservable added);
    void friendRemoved(dbObservable removed);
}
