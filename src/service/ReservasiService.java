package service;

import dao.KamarDAO;
import dao.ReservasiDAO;
import entity.Kamar;
import entity.Reservasi;

import java.util.List;


public class ReservasiService {

    private final ReservasiDAO reservasiDAO;
    private final KamarDAO kamarDAO;

    public ReservasiService(ReservasiDAO reservasiDAO, KamarDAO kamarDAO) {
        this.reservasiDAO = reservasiDAO;
        this.kamarDAO = kamarDAO;
    }

    public void tambahReservasi(Reservasi reservasi) {
        validateReservasi(reservasi);

        // Ambil data kamar terbaru untuk mengecek status
        Kamar kamar = kamarDAO.findById(reservasi.getKamar().getId());
        if (kamar == null) {
            throw new IllegalArgumentException("Kamar yang dipilih tidak terdaftar.");
        }

        // Kamar tidak boleh dipesan jika status TERISI atau MAINTENANCE
        if ("TERISI".equals(kamar.getStatus())) {
            throw new IllegalArgumentException("Kamar dengan nomor " + kamar.getNomorKamar() + " sedang TERISI.");
        }
        if ("MAINTENANCE".equals(kamar.getStatus())) {
            throw new IllegalArgumentException("Kamar dengan nomor " + kamar.getNomorKamar() + " sedang dalam MAINTENANCE.");
        }

        // Jika status reservasi baru adalah BOOKED atau CHECK_IN, ubah status kamar menjadi TERISI
        if ("BOOKED".equals(reservasi.getStatus()) || "CHECK_IN".equals(reservasi.getStatus())) {
            kamar.setStatus("TERISI");
            kamarDAO.update(kamar);
        }

        reservasiDAO.insert(reservasi);
    }

    public List<Reservasi> semuaReservasi() {
        return reservasiDAO.findAll();
    }

    public Reservasi cariReservasiById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID Reservasi tidak valid.");
        }
        Reservasi reservasi = reservasiDAO.findById(id);
        if (reservasi == null) {
            throw new IllegalArgumentException("Reservasi dengan ID " + id + " tidak ditemukan.");
        }
        return reservasi;
    }

    public List<Reservasi> cariReservasiByNamaTamu(String namaTamu) {
        if (namaTamu == null || namaTamu.isBlank()) {
            throw new IllegalArgumentException("Nama tamu pencarian tidak boleh kosong.");
        }
        return reservasiDAO.findByNamaTamu(namaTamu);
    }

    public void ubahReservasi(Reservasi reservasi) {
        if (reservasi.getId() <= 0) {
            throw new IllegalArgumentException("ID Reservasi tidak valid untuk diupdate.");
        }
        validateReservasi(reservasi);

        // Ambil data reservasi lama sebelum diupdate
        Reservasi oldReservasi = reservasiDAO.findById(reservasi.getId());
        if (oldReservasi == null) {
            throw new IllegalArgumentException("Reservasi tidak ditemukan.");
        }

        Kamar oldKamar = oldReservasi.getKamar();
        Kamar newKamar = kamarDAO.findById(reservasi.getKamar().getId());

        if (newKamar == null) {
            throw new IllegalArgumentException("Kamar yang dipilih tidak valid.");
        }

        // Kasus 1: Ganti kamar dalam reservasi
        if (oldKamar.getId() != newKamar.getId()) {
            // Kamar baru tidak boleh terisi
            if ("TERISI".equals(newKamar.getStatus())) {
                throw new IllegalArgumentException("Kamar baru dengan nomor " + newKamar.getNomorKamar() + " sedang TERISI.");
            }
            if ("MAINTENANCE".equals(newKamar.getStatus())) {
                throw new IllegalArgumentException("Kamar baru dengan nomor " + newKamar.getNomorKamar() + " sedang dalam MAINTENANCE.");
            }

            // Kembalikan status kamar lama ke TERSEDIA
            oldKamar.setStatus("TERSEDIA");
            kamarDAO.update(oldKamar);

            // Set kamar baru menjadi TERISI jika status reservasi aktif
            if ("BOOKED".equals(reservasi.getStatus()) || "CHECK_IN".equals(reservasi.getStatus())) {
                newKamar.setStatus("TERISI");
                kamarDAO.update(newKamar);
            }
        } else {
            // Kasus 2: Kamar yang sama tetapi status reservasi berubah
            // Jika berubah ke CANCELLED atau CHECK_OUT, bebaskan kamar
            if ("CANCELLED".equals(reservasi.getStatus()) || "CHECK_OUT".equals(reservasi.getStatus())) {
                newKamar.setStatus("TERSEDIA");
                kamarDAO.update(newKamar);
            } else if ("BOOKED".equals(reservasi.getStatus()) || "CHECK_IN".equals(reservasi.getStatus())) {
                newKamar.setStatus("TERISI");
                kamarDAO.update(newKamar);
            }
        }

        reservasiDAO.update(reservasi);
    }

    public void hapusReservasi(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID Reservasi tidak valid.");
        }

        Reservasi res = reservasiDAO.findById(id);
        if (res == null) {
            throw new IllegalArgumentException("Reservasi tidak ditemukan.");
        }

        // Bebaskan kamar jika reservasi yang dihapus bertatus aktif (BOOKED/CHECK_IN)
        if ("BOOKED".equals(res.getStatus()) || "CHECK_IN".equals(res.getStatus())) {
            Kamar kamar = res.getKamar();
            if (kamar != null) {
                kamar.setStatus("TERSEDIA");
                kamarDAO.update(kamar);
            }
        }

        reservasiDAO.delete(id);
    }

    // Validasi Bisnis Reservasi
    private void validateReservasi(Reservasi reservasi) {
        if (reservasi.getNamaTamu() == null || reservasi.getNamaTamu().isBlank()) {
            throw new IllegalArgumentException("Nama tamu tidak boleh kosong.");
        }
        if (reservasi.getKamar() == null || reservasi.getKamar().getId() <= 0) {
            throw new IllegalArgumentException("Kamar yang valid wajib ditentukan.");
        }
        if (reservasi.getCheckIn() == null || reservasi.getCheckOut() == null) {
            throw new IllegalArgumentException("Tanggal Check-In dan Check-Out tidak boleh kosong.");
        }
        if (!reservasi.getCheckOut().isAfter(reservasi.getCheckIn())) {
            throw new IllegalArgumentException("Tanggal Check-Out harus setelah tanggal Check-In.");
        }

        String status = reservasi.getStatus();
        if (status == null || (!status.equals("BOOKED") && !status.equals("CHECK_IN") && 
                               !status.equals("CHECK_OUT") && !status.equals("CANCELLED"))) {
            throw new IllegalArgumentException("Status reservasi tidak valid. Harus BOOKED, CHECK_IN, CHECK_OUT, atau CANCELLED.");
        }
    }
}
