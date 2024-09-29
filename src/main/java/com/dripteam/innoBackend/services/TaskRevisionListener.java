package com.dripteam.innoBackend.services;

import org.hibernate.envers.RevisionListener;

public class TaskRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        // отправляем уведу
    }
}
