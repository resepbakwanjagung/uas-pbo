package main;

import dao.KamarDAOImpl;
import dao.ReservasiDAOImpl;
import entity.Kamar;
import entity.Reservasi;
import service.KamarService;
import service.ReservasiService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MainApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Injeksi dependensi (DIP - Dependency Inversion Principle)
    private static final KamarService kamarService = new KamarService(new KamarDAOImpl());
    private static final ReservasiService reservasiService = new ReservasiService(new ReservasiDAOImpl(), new KamarDAOImpl());

    public static void main(String[] args) {
        int pilihanUtama;
        do {
            tampilkanMenuUtama();
            pilihanUtama = bacaInt("Pilih menu: ");
            switch (pilihanUtama) {
                case 1 -> menuKamar();
                case 2 -> menuReservasi();
                case 0 -> {
                    printSeparatedLine();
                    System.out.println("   Terima kasih telah menggunakan sistem kami!");
                    System.out.println("             Sampai jumpa kembali!");
                    printSeparatedLine();
                }
                default -> printError("Pilihan tidak valid.");
            }
        } while (pilihanUtama != 0);

        scanner.close();
    }

    // ====================================================================
    // MENU UTAMA & SUB-MENU
    // ====================================================================

    private static void tampilkanMenuUtama() {
        System.out.println();
        System.out.println("┌────────────────────────────────────────────────────────┐");
        System.out.println("│          HOTEL RESERVATION MANAGEMENT SYSTEM           │");
        System.out.println("├────────────────────────────────────────────────────────┤");
        System.out.println("│  [1] Kelola Data Kamar                                 │");
        System.out.println("│  [2] Kelola Data Reservasi                             │");
        System.out.println("│  [0] Keluar Aplikasi                                   │");
        System.out.println("└────────────────────────────────────────────────────────┘");
    }

    private static void menuKamar() {
        int pilihan;
        do {
            System.out.println();
            System.out.println("┌────────────────────────────────────────────────────────┐");
            System.out.println("│                  MENU KELOLA DATA KAMAR                │");
            System.out.println("├────────────────────────────────────────────────────────┤");
            System.out.println("│  [1] Tambah Kamar Baru                                 │");
            System.out.println("│  [2] Lihat Semua Kamar                                 │");
            System.out.println("│  [3] Update Data Kamar                                 │");
            System.out.println("│  [4] Hapus Kamar                                       │");
            System.out.println("│  [5] Cari Kamar Berdasarkan Nomor                      │");
            System.out.println("│  [0] Kembali ke Menu Utama                             │");
            System.out.println("└────────────────────────────────────────────────────────┘");
            pilihan = bacaInt("Pilih menu Kelola Kamar: ");

            switch (pilihan) {
                case 1 -> tambahKamar();
                case 2 -> lihatSemuaKamar();
                case 3 -> updateKamar();
                case 4 -> hapusKamar();
                case 5 -> cariKamarNomor();
                case 0 -> System.out.println("\nKembali...");
                default -> printError("Pilihan tidak valid.");
            }
        } while (pilihan != 0);
    }

    private static void menuReservasi() {
        int pilihan;
        do {
            System.out.println();
            System.out.println("┌────────────────────────────────────────────────────────┐");
            System.out.println("│                MENU KELOLA DATA RESERVASI              │");
            System.out.println("├────────────────────────────────────────────────────────┤");
            System.out.println("│  [1] Tambah Reservasi Baru                             │");
            System.out.println("│  [2] Lihat Semua Reservasi                             │");
            System.out.println("│  [3] Cari Reservasi Berdasarkan ID                     │");
            System.out.println("│  [4] Update Data Reservasi                             │");
            System.out.println("│  [5] Hapus Reservasi                                   │");
            System.out.println("│  [6] Cari Reservasi Berdasarkan Nama Tamu              │");
            System.out.println("│  [0] Kembali ke Menu Utama                             │");
            System.out.println("└────────────────────────────────────────────────────────┘");
            pilihan = bacaInt("Pilih menu Kelola Reservasi: ");

            switch (pilihan) {
                case 1 -> tambahReservasi();
                case 2 -> lihatSemuaReservasi();
                case 3 -> cariReservasiId();
                case 4 -> updateReservasi();
                case 5 -> hapusReservasi();
                case 6 -> cariReservasiNama();
                case 0 -> System.out.println("\nKembali...");
                default -> printError("Pilihan tidak valid.");
            }
        } while (pilihan != 0);
    }

    // ====================================================================
    // FITUR KAMAR
    // ====================================================================

    private static void tambahKamar() {
        System.out.println("\n--- Tambah Kamar Baru ---");
        String nomor = bacaString("Masukkan Nomor Kamar: ");
        String tipe = bacaString("Masukkan Tipe Kamar (e.g. Standard, Deluxe, Suite): ");
        BigDecimal harga = bacaBigDecimal("Masukkan Harga per Malam: ");
        
        System.out.println("Pilihan Status Kamar:");
        System.out.println(" 1. TERSEDIA");
        System.out.println(" 2. TERISI");
        System.out.println(" 3. MAINTENANCE");
        int optStatus = bacaInt("Pilih Status (1-3): ");
        String status = switch (optStatus) {
            case 2 -> "TERISI";
            case 3 -> "MAINTENANCE";
            default -> "TERSEDIA";
        };

        try {
            Kamar kamar = new Kamar(nomor, tipe, harga, status);
            kamarService.tambahKamar(kamar);
            printSuccess("Kamar berhasil ditambahkan dengan ID: " + kamar.getId());
        } catch (Exception e) {
            printError("Gagal menambahkan kamar: " + e.getMessage());
        }
    }

    private static void lihatSemuaKamar() {
        System.out.println("\n--- Daftar Semua Kamar ---");
        List<Kamar> list = kamarService.semuaKamar();
        if (list.isEmpty()) {
            System.out.println("Belum ada data kamar terdaftar.");
            return;
        }
        printTabelKamar(list);
    }

    private static void updateKamar() {
        System.out.println("\n--- Update Data Kamar ---");
        int id = bacaInt("Masukkan ID Kamar yang ingin diupdate: ");
        try {
            Kamar kamar = kamarService.cariKamarById(id);
            System.out.println("Data Kamar Saat Ini: " + kamar);
            System.out.println("(Tekan Enter untuk mempertahankan nilai lama)");

            String nomor = bacaStringOpsional("Nomor Kamar baru [" + kamar.getNomorKamar() + "]: ");
            String tipe = bacaStringOpsional("Tipe Kamar baru [" + kamar.getTipeKamar() + "]: ");
            
            String hargaInput = bacaStringOpsional("Harga baru [" + kamar.getHargaPerMalam() + "]: ");
            BigDecimal harga = kamar.getHargaPerMalam();
            if (!hargaInput.isBlank()) {
                harga = new BigDecimal(hargaInput);
            }

            System.out.println("Pilihan Status Kamar baru [" + kamar.getStatus() + "]:");
            System.out.println(" 1. TERSEDIA");
            System.out.println(" 2. TERISI");
            System.out.println(" 3. MAINTENANCE");
            String status = kamar.getStatus();
            String optInput = bacaStringOpsional("Pilihan (1-3) / Kosongkan: ");
            if (!optInput.isBlank()) {
                status = switch (optInput.trim()) {
                    case "2" -> "TERISI";
                    case "3" -> "MAINTENANCE";
                    default -> "TERSEDIA";
                };
            }

            if (!nomor.isBlank()) kamar.setNomorKamar(nomor);
            if (!tipe.isBlank()) kamar.setTipeKamar(tipe);
            kamar.setHargaPerMalam(harga);
            kamar.setStatus(status);

            kamarService.ubahKamar(kamar);
            printSuccess("Data kamar berhasil diperbarui.");
        } catch (Exception e) {
            printError("Gagal mengupdate kamar: " + e.getMessage());
        }
    }

    private static void hapusKamar() {
        System.out.println("\n--- Hapus Kamar ---");
        int id = bacaInt("Masukkan ID Kamar yang ingin dihapus: ");
        try {
            Kamar kamar = kamarService.cariKamarById(id);
            System.out.println("Data Kamar yang akan dihapus: " + kamar);
            String konfirmasi = bacaString("Apakah Anda yakin ingin menghapus? (y/n): ");
            if (konfirmasi.equalsIgnoreCase("y")) {
                kamarService.hapusKamar(id);
                printSuccess("Kamar berhasil dihapus.");
            } else {
                System.out.println("Penghapusan dibatalkan.");
            }
        } catch (Exception e) {
            printError("Gagal menghapus kamar: " + e.getMessage());
        }
    }

    private static void cariKamarNomor() {
        System.out.println("\n--- Cari Kamar Berdasarkan Nomor ---");
        String nomor = bacaString("Masukkan Nomor Kamar: ");
        try {
            Kamar kamar = kamarService.cariKamarByNomor(nomor);
            System.out.println("\nDetail Kamar Ditemukan:");
            printTabelKamar(List.of(kamar));
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    // ====================================================================
    // FITUR RESERVASI
    // ====================================================================

    private static void tambahReservasi() {
        System.out.println("\n--- Tambah Reservasi Baru ---");
        String nama = bacaString("Masukkan Nama Tamu: ");
        int kamarId = bacaInt("Masukkan ID Kamar yang dipesan: ");
        LocalDate checkIn = bacaDate("Masukkan Tanggal Check-In (yyyy-MM-dd): ");
        LocalDate checkOut = bacaDate("Masukkan Tanggal Check-Out (yyyy-MM-dd): ");

        System.out.println("Pilihan Status Reservasi:");
        System.out.println(" 1. BOOKED");
        System.out.println(" 2. CHECK_IN");
        int optStatus = bacaInt("Pilih (1-2): ");
        String status = optStatus == 2 ? "CHECK_IN" : "BOOKED";

        try {
            Kamar kamar = kamarService.cariKamarById(kamarId);
            Reservasi reservasi = new Reservasi(nama, kamar, checkIn, checkOut, status);
            reservasiService.tambahReservasi(reservasi);
            printSuccess("Reservasi berhasil dibuat dengan ID: " + reservasi.getId());
        } catch (Exception e) {
            printError("Gagal membuat reservasi: " + e.getMessage());
        }
    }

    private static void lihatSemuaReservasi() {
        System.out.println("\n--- Daftar Semua Reservasi ---");
        List<Reservasi> list = reservasiService.semuaReservasi();
        if (list.isEmpty()) {
            System.out.println("Belum ada data reservasi terdaftar.");
            return;
        }
        printTabelReservasi(list);
    }

    private static void cariReservasiId() {
        System.out.println("\n--- Cari Reservasi Berdasarkan ID ---");
        int id = bacaInt("Masukkan ID Reservasi: ");
        try {
            Reservasi res = reservasiService.cariReservasiById(id);
            System.out.println("\nDetail Reservasi Ditemukan:");
            printTabelReservasi(List.of(res));
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    private static void updateReservasi() {
        System.out.println("\n--- Update Data Reservasi ---");
        int id = bacaInt("Masukkan ID Reservasi yang ingin diupdate: ");
        try {
            Reservasi res = reservasiService.cariReservasiById(id);
            System.out.println("Reservasi saat ini: " + res);
            System.out.println("(Tekan Enter untuk mempertahankan nilai lama)");

            String nama = bacaStringOpsional("Nama Tamu baru [" + res.getNamaTamu() + "]: ");
            
            String kamarIdStr = bacaStringOpsional("Kamar ID baru [" + res.getKamar().getId() + "]: ");
            Kamar kamar = res.getKamar();
            if (!kamarIdStr.isBlank()) {
                int newKamarId = Integer.parseInt(kamarIdStr);
                kamar = kamarService.cariKamarById(newKamarId);
            }

            String checkInStr = bacaStringOpsional("Check-In baru [" + res.getCheckIn() + "] (yyyy-MM-dd): ");
            LocalDate checkIn = res.getCheckIn();
            if (!checkInStr.isBlank()) {
                checkIn = LocalDate.parse(checkInStr, dateFormatter);
            }

            String checkOutStr = bacaStringOpsional("Check-Out baru [" + res.getCheckOut() + "] (yyyy-MM-dd): ");
            LocalDate checkOut = res.getCheckOut();
            if (!checkOutStr.isBlank()) {
                checkOut = LocalDate.parse(checkOutStr, dateFormatter);
            }

            System.out.println("Pilihan Status Reservasi baru [" + res.getStatus() + "]:");
            System.out.println(" 1. BOOKED");
            System.out.println(" 2. CHECK_IN");
            System.out.println(" 3. CHECK_OUT");
            System.out.println(" 4. CANCELLED");
            String status = res.getStatus();
            String optInput = bacaStringOpsional("Pilihan (1-4) / Kosongkan: ");
            if (!optInput.isBlank()) {
                status = switch (optInput.trim()) {
                    case "1" -> "BOOKED";
                    case "2" -> "CHECK_IN";
                    case "3" -> "CHECK_OUT";
                    case "4" -> "CANCELLED";
                    default -> res.getStatus();
                };
            }

            if (!nama.isBlank()) res.setNamaTamu(nama);
            res.setKamar(kamar);
            res.setCheckIn(checkIn);
            res.setCheckOut(checkOut);
            res.setStatus(status);

            reservasiService.ubahReservasi(res);
            printSuccess("Data reservasi berhasil diupdate.");
        } catch (Exception e) {
            printError("Gagal mengupdate reservasi: " + e.getMessage());
        }
    }

    private static void hapusReservasi() {
        System.out.println("\n--- Hapus Reservasi ---");
        int id = bacaInt("Masukkan ID Reservasi yang ingin dihapus: ");
        try {
            Reservasi res = reservasiService.cariReservasiById(id);
            System.out.println("Data Reservasi yang akan dihapus: " + res);
            String konfirmasi = bacaString("Apakah Anda yakin ingin menghapus? (y/n): ");
            if (konfirmasi.equalsIgnoreCase("y")) {
                reservasiService.hapusReservasi(id);
                printSuccess("Reservasi berhasil dihapus.");
            } else {
                System.out.println("Penghapusan dibatalkan.");
            }
        } catch (Exception e) {
            printError("Gagal menghapus reservasi: " + e.getMessage());
        }
    }

    private static void cariReservasiNama() {
        System.out.println("\n--- Cari Reservasi Berdasarkan Nama Tamu ---");
        String nama = bacaString("Masukkan Nama Tamu: ");
        try {
            List<Reservasi> list = reservasiService.cariReservasiByNamaTamu(nama);
            if (list.isEmpty()) {
                System.out.println("Tidak ada reservasi ditemukan untuk nama: " + nama);
                return;
            }
            printTabelReservasi(list);
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    // ====================================================================
    // INPUT UTILITIES
    // ====================================================================

    private static int bacaInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("[!] Input harus berupa angka bulat. Silakan coba lagi.");
            }
        }
    }

    private static String bacaString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("[!] Input tidak boleh kosong.");
        }
    }

    private static String bacaStringOpsional(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static BigDecimal bacaBigDecimal(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("[!] Input nominal harga tidak valid. Contoh: 350000.00");
            }
        }
    }

    private static LocalDate bacaDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("[!] Format tanggal salah. Gunakan format yyyy-MM-dd (Contoh: 2026-06-07).");
            }
        }
    }

    // ====================================================================
    // FORMATTING UTILITIES & PRINT TABLES
    // ====================================================================

    private static void printTabelKamar(List<Kamar> list) {
        System.out.println("┌──────┬──────────────┬──────────────────────┬──────────────────────┬──────────────┐");
        System.out.printf("│ %-4s │ %-12s │ %-20s │ %-20s │ %-12s │%n", "ID", "Nomor Kamar", "Tipe Kamar", "Harga/Malam (Rp)", "Status");
        System.out.println("├──────┼──────────────┼──────────────────────┼──────────────────────┼──────────────┤");
        for (Kamar k : list) {
            System.out.printf("│ %-4d │ %-12s │ %-20s │ %-20.2f │ %-12s │%n",
                    k.getId(), k.getNomorKamar(), k.getTipeKamar(), k.getHargaPerMalam(), k.getStatus());
        }
        System.out.println("└──────┴──────────────┴──────────────────────┴──────────────────────┴──────────────┘");
    }

    private static void printTabelReservasi(List<Reservasi> list) {
        System.out.println("┌──────┬──────────────────────┬──────┬─────────────┬─────────────┬─────────────┬─────────────┐");
        System.out.printf("│ %-4s │ %-20s │ %-4s │ %-11s │ %-11s │ %-11s │ %-11s │%n", 
                "ID", "Nama Tamu", "RmID", "No. Kamar", "Check In", "Check Out", "Status");
        System.out.println("├──────┼──────────────────────┼──────┼─────────────┼─────────────┼─────────────┼─────────────┤");
        for (Reservasi r : list) {
            Kamar k = r.getKamar();
            String noKamar = k != null ? k.getNomorKamar() : "-";
            String roomId = k != null ? String.valueOf(k.getId()) : "-";
            System.out.printf("│ %-4d │ %-20s │ %-4s │ %-11s │ %-11s │ %-11s │ %-11s │%n",
                    r.getId(), r.getNamaTamu(), roomId, noKamar, 
                    r.getCheckIn().format(dateFormatter), r.getCheckOut().format(dateFormatter), r.getStatus());
        }
        System.out.println("└──────┴──────────────────────┴──────┴─────────────┴─────────────┴─────────────┴─────────────┘");
    }

    private static void printSuccess(String message) {
        System.out.println("\n[OK] " + message + "\n");
    }

    private static void printError(String message) {
        System.out.println("\n[!] Error: " + message + "\n");
    }

    private static void printSeparatedLine() {
        System.out.println("==========================================================");
    }
}
