package entity;

import java.math.BigDecimal;

public class Kamar {
    private int id;
    private String nomorKamar;
    private String tipeKamar;
    private BigDecimal hargaPerMalam;
    private String status; // TERSEDIA, TERISI, MAINTENANCE

    public Kamar() {
    }

    public Kamar(String nomorKamar, String tipeKamar, BigDecimal hargaPerMalam, String status) {
        this.nomorKamar = nomorKamar;
        this.tipeKamar = tipeKamar;
        this.hargaPerMalam = hargaPerMalam;
        this.status = status;
    }

    public Kamar(int id, String nomorKamar, String tipeKamar, BigDecimal hargaPerMalam, String status) {
        this.id = id;
        this.nomorKamar = nomorKamar;
        this.tipeKamar = tipeKamar;
        this.hargaPerMalam = hargaPerMalam;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomorKamar() {
        return nomorKamar;
    }

    public void setNomorKamar(String nomorKamar) {
        this.nomorKamar = nomorKamar;
    }

    public String getTipeKamar() {
        return tipeKamar;
    }

    public void setTipeKamar(String tipeKamar) {
        this.tipeKamar = tipeKamar;
    }

    public BigDecimal getHargaPerMalam() {
        return hargaPerMalam;
    }

    public void setHargaPerMalam(BigDecimal hargaPerMalam) {
        this.hargaPerMalam = hargaPerMalam;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Kamar [ID=" + id + ", Nomor=" + nomorKamar + ", Tipe=" + tipeKamar + ", Harga/Malam=" + hargaPerMalam
                + ", Status=" + status + "]";
    }
}
