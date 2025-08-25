package com.doanth.appointment_service.dto;

import java.util.List;

public class ListAppointmentInfoDTO {
    private List<AppointmentInfoDTO> listAppointmentsResovled;
    private List<AppointmentInfoDTO> listAppointmentsNotResovled;
    private List<DoctorInfoDTO> listDoctors;

    public List<AppointmentInfoDTO> getListAppointmentsResovled() {
        return listAppointmentsResovled;
    }

    public void setListAppointmentsResovled(List<AppointmentInfoDTO> listAppointmentsResovled) {
        this.listAppointmentsResovled = listAppointmentsResovled;
    }

    public List<AppointmentInfoDTO> getListAppointmentsNotResovled() {
        return listAppointmentsNotResovled;
    }

    public void setListAppointmentsNotResovled(List<AppointmentInfoDTO> listAppointmentsNotResovled) {
        this.listAppointmentsNotResovled = listAppointmentsNotResovled;
    }

    public List<DoctorInfoDTO> getListDoctors() {
        return listDoctors;
    }

    public void setListDoctors(List<DoctorInfoDTO> listDoctors) {
        this.listDoctors = listDoctors;
    }

    @Override
    public String toString() {
        return "ListAppointmentInfoDTO{" +
                "listAppointmentsResovled=" + listAppointmentsResovled +
                ", listAppointmentsNotResovled=" + listAppointmentsNotResovled +
                ", listDoctors=" + listDoctors +
                '}';
    }
}
