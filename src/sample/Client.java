package sample;

import java.util.Date;

public class Client {

    String clientName;
    String incidentNumber;
    Date dateIncident;
    String problemName;
    int empoyerTime;
    int employerCounter;
    String carDrivingToIncident;
    int mileage;
    String FixProblem;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getIncidentNumber() {
        return incidentNumber;
    }

    public void setIncidentNumber(String incidentNumber) {
        this.incidentNumber = incidentNumber;
    }

    public Date getDateIncident() {
        return dateIncident;
    }

    public void setDateIncident(Date dateIncident) {
        this.dateIncident = dateIncident;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public int getEmpoyerTime() {
        return empoyerTime;
    }

    public void setEmpoyerTime(int empoyerTime) {
        this.empoyerTime = empoyerTime;
    }

    public int getEmployerCounter() {
        return employerCounter;
    }

    public void setEmployerCounter(int employerCounter) {
        this.employerCounter = employerCounter;
    }

    public String getCarDrivingToIncident() {
        return carDrivingToIncident;
    }

    public void setCarDrivingToIncident(String carDrivingToIncident) {
        this.carDrivingToIncident = carDrivingToIncident;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String getFixProblem() {
        return FixProblem;
    }

    public void setFixProblem(String fixProblem) {
        FixProblem = fixProblem;
    }

}
