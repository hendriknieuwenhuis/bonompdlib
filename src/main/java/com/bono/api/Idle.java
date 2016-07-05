package com.bono.api;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by hendriknieuwenhuis on 09/05/16.
 */
public class Idle {

    private ArrayList<ChangeListener> changeListeners = new ArrayList<>();

    private Status status;

    private DBExecutor dbExecutor;

    public Idle(Status status) {
        this.status = status;
    }

    public Idle(DBExecutor dbExecutor) {
        this.dbExecutor = dbExecutor;
    }

    public void addListener(ChangeListener listener) {
        changeListeners.add(listener);
    }

    public void removeListeners() {
        changeListeners.clear();
    }

    public void runIdle() throws Exception {
        String feedback = null;

        while (true) {
            //feedback = status.idle(null);
            feedback = dbExecutor.execute(new DefaultCommand(Status.IDLE));

            Reply reply = new Reply(feedback);
            Iterator<String> i = reply.iterator();
            while (i.hasNext()) {
                String line = i.next();
                String[] lines = line.split(" ");
                for (ChangeListener l : changeListeners) {
                    l.stateChanged(new ChangeEvent(lines[1]));
                }
            }
        }
    }
}
