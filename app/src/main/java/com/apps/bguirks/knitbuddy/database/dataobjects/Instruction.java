package com.apps.bguirks.knitbuddy.database.dataobjects;

public class Instruction implements Comparable<Instruction> {

    // Instruction ID - PRIMARY KEY
    private long _id;
    // Project ID - INT NON-NULL
    private long projectId;
    // Step Number - INT NON-NULL
    private long stepNumber;
    // Instruction - STRING NON-NULL
    private String instruction;
    // Completed - INT NON-NULL
    private long completed;
    // Counter - INT NON-NULL
    private long counter;

    public Instruction() {}
    public Instruction(long projectId, long stepNumber) {
        this.projectId = projectId;
        this.stepNumber = stepNumber;
        this.instruction = "";
        this.completed = 0;
        this.counter = -999;
    }
    public Instruction(long id, long projectId, long stepNumber, String instruction, long completed, long counter) {
        this._id = id;
        this.projectId = projectId;
        this.stepNumber = stepNumber;
        this.instruction = instruction;
        this.completed = completed;
        this.counter = counter;
    }

    @Override
    public int compareTo(Instruction o) {
        return Long.compare(stepNumber, o.stepNumber);
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(long stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public long getCompleted() {
        return completed;
    }

    public void setCompleted(long completed) {
        this.completed = completed;
    }

    public long getCounter() {
        return counter;
    }

    public void setCounter(long counter) {
        this.counter = counter;
    }
}
