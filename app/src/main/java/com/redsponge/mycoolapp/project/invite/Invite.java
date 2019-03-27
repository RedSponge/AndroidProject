package com.redsponge.mycoolapp.project.invite;

/**
 * Represents an invitation
 */
public class Invite {

    public final int idFrom;
    public final int idTo;
    public final int projectId;
    public final int shouldBeAdmin;

    public Invite(int idFrom, int idTo, int projectId, boolean shouldBeAdmin) {
        this(idFrom, idTo, projectId, shouldBeAdmin ? 1 : 0);
    }

    public Invite(int idFrom, int idTo, int projectId, int shouldBeAdmin) {
        this.idFrom = idFrom;
        this.idTo = idTo;
        this.projectId = projectId;
        this.shouldBeAdmin = shouldBeAdmin;
    }
}
