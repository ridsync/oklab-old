package com.okitoki.checklist.okperm;

import java.util.ArrayList;

import lombok.ToString;

/**
 * Created by andman on 2016-04-20.
 */
@ToString
public class OKPermEvent {

    public boolean permission;
    public ArrayList<String> returnPermissions;

    public OKPermEvent(boolean permission, ArrayList<String> returnPermissions
    ) {
        this.permission = permission;
        this.returnPermissions = returnPermissions;
    }

    public boolean hasPermissionAll() {
        return permission;
    }


    public ArrayList<String> getReturnPermissions() {
        return returnPermissions;
    }
}
