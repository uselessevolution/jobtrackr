package com.jobtrackr.backend.dashboard.dto;

public class DashboardFunnelResponse {

    private long applied;
    private long interviewed;
    private long offered;
    private long accepted;

    private double interviewRate;
    private double offerRate;
    private double acceptedRate;

    private double interviewToOfferRate;
    private double offerToAcceptedRate;

    public DashboardFunnelResponse() {
    }

    public DashboardFunnelResponse(
            long applied,
            long interviewed,
            long offered,
            long accepted,
            double interviewRate,
            double offerRate,
            double acceptedRate,
            double interviewToOfferRate,
            double offerToAcceptedRate) {

        this.applied = applied;
        this.interviewed = interviewed;
        this.offered = offered;
        this.accepted = accepted;
        this.interviewRate = interviewRate;
        this.offerRate = offerRate;
        this.acceptedRate = acceptedRate;
        this.interviewToOfferRate = interviewToOfferRate;
        this.offerToAcceptedRate = offerToAcceptedRate;
    }

    public long getApplied() {
        return applied;
    }

    public void setApplied(long applied) {
        this.applied = applied;
    }

    public long getInterviewed() {
        return interviewed;
    }

    public void setInterviewed(long interviewed) {
        this.interviewed = interviewed;
    }

    public long getOffered() {
        return offered;
    }

    public void setOffered(long offered) {
        this.offered = offered;
    }

    public long getAccepted() {
        return accepted;
    }

    public void setAccepted(long accepted) {
        this.accepted = accepted;
    }

    public double getInterviewRate() {
        return interviewRate;
    }

    public void setInterviewRate(double interviewRate) {
        this.interviewRate = interviewRate;
    }

    public double getOfferRate() {
        return offerRate;
    }

    public void setOfferRate(double offerRate) {
        this.offerRate = offerRate;
    }

    public double getAcceptedRate() {
        return acceptedRate;
    }

    public void setAcceptedRate(double acceptedRate) {
        this.acceptedRate = acceptedRate;
    }

    public double getInterviewToOfferRate() {
        return interviewToOfferRate;
    }

    public void setInterviewToOfferRate(double interviewToOfferRate) {
        this.interviewToOfferRate = interviewToOfferRate;
    }

    public double getOfferToAcceptedRate() {
        return offerToAcceptedRate;
    }

    public void setOfferToAcceptedRate(double offerToAcceptedRate) {
        this.offerToAcceptedRate = offerToAcceptedRate;
    }
}