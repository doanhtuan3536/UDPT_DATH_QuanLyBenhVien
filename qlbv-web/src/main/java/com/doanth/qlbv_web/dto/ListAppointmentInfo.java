package com.doanth.qlbv_web.dto;

import org.springframework.hateoas.PagedModel.PageMetadata;

import java.util.List;

public class ListAppointmentInfo {
    private List<AppointmentInfo> listAppointments;
    private List<DoctorDTO> listDoctors;
    private PageMetadata pageMetadata;

    public List<AppointmentInfo> getListAppointments() {
        return listAppointments;
    }

    public void setListAppointments(List<AppointmentInfo> listAppointments) {
        this.listAppointments = listAppointments;
    }

    public List<DoctorDTO> getListDoctors() {
        return listDoctors;
    }

    public void setListDoctors(List<DoctorDTO> listDoctors) {
        this.listDoctors = listDoctors;
    }

    public PageMetadata getPageMetadata() {
        return pageMetadata;
    }

    public void setPageMetadata(PageMetadata pageMetadata) {
        this.pageMetadata = pageMetadata;
    }

    @Override
    public String toString() {
        return "ListAppointmentInfo{" +
                "listAppointments=" + listAppointments +
                ", listDoctors=" + listDoctors +
                ", pageMetadata=" + pageMetadata +
                '}';
    }
}
