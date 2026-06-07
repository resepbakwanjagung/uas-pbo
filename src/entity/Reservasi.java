package entity;

import java.time.LocalDate;

public class Reservasi {
    private int id;
    private String namaTamu;
    private Kamar kamar; // Asosiasi ke objek Kamar
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String status; // BOOKED, CHECK_IN, CHECK_OUT, CANCELLED

    public Reservasi() {
    }

    public Reservasi(String namaTamu, Kamar kamar, LocalDate checkIn, LocalDate checkOut, String status) {
        this.namaTamu = namaTamu;
        this.kamar = kamar;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
    }

    public Reservasi(int id, String namaTamu, Kamar kamar, LocalDate checkIn, LocalDate checkOut, String status) {
        this.id = id;
        this.namaTamu = namaTamu;
        this.kamar = kamar;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaTamu() {
        return namaTamu;
    }

    public void setNamaTamu(String namaTamu) {
        this.namaTamu = namaTamu;
    }

    public Kamar getKamar() {
        return kamar;
    }

    public void setKamar(Kamar kamar) {
        this.kamar = kamar;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reservasi [ID=" + id + ", Tamu=" + namaTamu + ", Kamar=" 
                + (kamar != null ? kamar.getNomorKamar() : "null") + ", Check-In=" + checkIn 
                + ", Check-Out=" + checkOut + ", Status=" + status + "]";
    }
}
