package com.doanth.appointment_service.dto;

public class SpecialtyInforForListAppointments {
    private String name;
    private String room;

    public SpecialtyInforForListAppointments(String room, String name) {
        this.room = room;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "SpecialtyInforForListAppointments{" +
                "name='" + name + '\'' +
                ", room='" + room + '\'' +
                '}';
    }
}
