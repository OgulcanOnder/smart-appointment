package com.ogulcanonder.smart_appointment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Table(name = "working_hours")
public class WorkingHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private int appointmentDurationMinutes;

    @OneToOne
    private Employee employee;

    @ElementCollection(targetClass = DayOfWeek.class)
    @JoinTable(name = "working_days", joinColumns = @JoinColumn(name = "working_hours_id"))
    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> workingDays;

    public WorkingHour() {
    }

    public WorkingHour(Long id, LocalTime startTime, LocalTime endTime, int appointmentDurationTime,
                       Employee employee, Set<DayOfWeek> workingDays) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.appointmentDurationMinutes = appointmentDurationTime;
        this.employee = employee;
        this.workingDays = workingDays;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getAppointmentDurationMinutes() {
        return appointmentDurationMinutes;
    }

    public void setAppointmentDurationMinutes(int appointmentDurationMinutes) {
        this.appointmentDurationMinutes = appointmentDurationMinutes;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Set<DayOfWeek> getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(Set<DayOfWeek> workingDays) {
        this.workingDays = workingDays;
    }

    public static Builder builder() {
        return new Builder();
    }

    public WorkingHour(Builder builder) {
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.appointmentDurationMinutes = builder.appointmentDurationMinutes;
        this.employee = builder.employee;
        this.workingDays = builder.workingDays;
    }

    public static class Builder {
        private LocalTime startTime;
        private LocalTime endTime;
        private int appointmentDurationMinutes;
        private Employee employee;
        private Set<DayOfWeek> workingDays;


        public Builder startTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder appointmentDurationMinutes(int appointmentDurationMinutes) {
            this.appointmentDurationMinutes = appointmentDurationMinutes;
            return this;
        }

        public Builder employee(Employee employee) {
            this.employee = employee;
            return this;
        }

        public Builder workingDays(Set<DayOfWeek> workingDays) {
            this.workingDays = workingDays;
            return this;
        }

        public WorkingHour build() {
            return new WorkingHour(this);
        }
    }
}
