package co.edu.javeriana.bikewars.Items;

import java.util.Date;

/**
 * Created by Todesser on 04/09/2017.
 */

public class RaceItem {

    private String name;
    private Date date;

    public RaceItem(String name, Date date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "RaceItem{" +
                "name='" + name + '\'' +
                ", date=" + date +
                '}';
    }
}
