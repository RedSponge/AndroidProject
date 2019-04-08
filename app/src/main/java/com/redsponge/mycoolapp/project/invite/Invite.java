package com.redsponge.mycoolapp.project.invite;

/**
 * Represents an invitation
 */
public class Invite {

    private int idFrom;
    private int idTo;
    private int projectId;
    private int shouldBeAdmin;

    public Invite(int idFrom, int idTo, int projectId, boolean shouldBeAdmin) {
        this(idFrom, idTo, projectId, shouldBeAdmin ? 1 : 0);
    }

    public Invite(int idFrom, int idTo, int projectId, int shouldBeAdmin) {
        this.idFrom = idFrom;
        this.idTo = idTo;
        this.projectId = projectId;
        this.shouldBeAdmin = shouldBeAdmin;
    }

    public int getIdFrom() {
        return idFrom;
    }

    public int getIdTo() {
        return idTo;
    }

    public int getProjectId() {
        return projectId;
    }

    public int getShouldBeAdmin() {
        return shouldBeAdmin;
    }
}
