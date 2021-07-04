package com.example.lab.entities.book.request.misc;

public enum RequestState {
    SENT, IN_PROGRESS, PROCESSED, REJECTED, RETURNED;

    @Override
    public String toString() {
        return switch (this) {
            case SENT -> "sent";
            case PROCESSED -> "processed";
            case IN_PROGRESS -> "in_progress";
            case REJECTED -> "rejected";
            case RETURNED -> "returned";
        };
    }
}
