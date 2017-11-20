package co.edu.javeriana.bikewars.Logic.Entities;

import java.util.List;

/**
 * Created by Todesser on 29/10/2017.
 */

public class dbGroup {
    private String groupID;
    private String name;
    private List<String> members;

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public dbGroup(String groupID, List<String> members) {

        this.groupID = groupID;
        this.members = members;
    }

    public dbGroup() {

    }
}
