package com.redsponge.mycoolapp.project.event;

public class Event {

    private int id;
    private int projectId;
    private String name;
    private int status;
    private int deadline;

    public Event(int id, int projectId, String name, int status, int deadline) {
        this.id = id;
        this.projectId = projectId;
        this.name = name;
        this.status = status;
        this.deadline = deadline;
    }

    public Event(int projectId, String name, int status, int deadline) {
        this(-1, projectId, name, status, deadline);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", deadline=" + deadline +
                '}';
    }
}
