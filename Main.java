import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static final Scanner input = new Scanner(System.in);
    private static final ArrayList<Kendaraan> daftarKendaraan = new ArrayList<>();

    public static void main(String[] args) {
        int pilihan;

        do {
            tampilkanMenu();
            pilihan = bacaInt("Pilih menu: ");

            switch (pilihan) {
                case 1:
                    tambahKendaraan();
                    break;
                case 2:
                    tampilkanKendaraan();
                    break;
                case 3:
                    ubahData();
                    break;
                case 4:
                    hitungBiaya();
                    break;
                case 5:
                    detailKendaraan();
                    break;
                case 0:
                    System.out.println("Terima kasih. Program selesai.");
                    break;
                default:
                    System.out.println("Pilihan tidak tersedia.");
            }
            System.out.println();
        } while (pilihan != 0);
    }

    private static void tampilkanMenu() {
        System.out.println("===== RENTAL KENDARAAN =====");
        System.out.println("1. Tambah Kendaraan");
        System.out.println("2. Tampilkan Kendaraan");
        System.out.println("3. Ubah Data");
        System.out.println("4. Hitung Biaya");
        System.out.println("5. Detail Kendaraan");
        System.out.println("0. Keluar");
    }

    private static void tambahKendaraan() {
        System.out.println("\n--- TAMBAH KENDARAAN ---");
        System.out.println("1. Mobil");
        System.out.println("2. Motor");
        int jenis = bacaInt("Pilih jenis kendaraan: ");

        if (jenis != 1 && jenis != 2) {
            System.out.println("Jenis kendaraan tidak tersedia.");
            return;
        }

        String nama = bacaString("Nama kendaraan: ");
        String plat = bacaString("Nomor plat: ");
        double harga = bacaDouble("Harga sewa per hari (Rp): ");
        int lama = bacaInt("Lama sewa (hari): ");

        try {
            if (jenis == 1) {
                int kursi = bacaInt("Jumlah kursi: ");
                daftarKendaraan.add(new Mobil(nama, plat, harga, lama, kursi));
            } else {
                String tipe = bacaString("Jenis motor (Matic/Manual/Sport): ");
                daftarKendaraan.add(new Motor(nama, plat, harga, lama, tipe));
            }
            System.out.println("Kendaraan berhasil ditambahkan.");
        } catch (IllegalArgumentException e) {
            System.out.println("Data tidak valid: " + e.getMessage());
        }
    }

    private static void tampilkanKendaraan() {
        System.out.println("\n--- DAFTAR KENDARAAN ---");
        if (daftarKendaraan.isEmpty()) {
            System.out.println("Belum ada kendaraan yang tersimpan.");
            return;
        }

        for (int i = 0; i < daftarKendaraan.size(); i++) {
            Kendaraan k = daftarKendaraan.get(i);
            String jenis = (k instanceof Mobil) ? "Mobil" : "Motor";

            System.out.println((i + 1) + ". " + k.getNamaKendaraan()
                + " | " + k.getNomorPlat()
                + " | " + jenis
                + " | " + formatRupiah(k.getHargaSewa()) + "/hari");
        }
    }

    private static void ubahData() {
        System.out.println("\n--- UBAH DATA KENDARAAN ---");
        Kendaraan k = pilihKendaraan();
        if (k == null) return;

        System.out.println("Kosongkan input jika data tidak ingin diubah.");
        String nama = bacaStringBolehKosong("Nama baru [" + k.getNamaKendaraan() + "]: ");
        String plat = bacaStringBolehKosong("Plat baru [" + k.getNomorPlat() + "]: ");
        String hargaText = bacaStringBolehKosong(
            "Harga sewa baru [" + k.getHargaSewa() + "]: ");
        String lamaText = bacaStringBolehKosong(
            "Lama sewa baru [" + k.getLamaSewa() + "]: ");

        try {
            if (!nama.isEmpty()) k.setNamaKendaraan(nama);
            if (!plat.isEmpty()) k.setNomorPlat(plat);
            if (!hargaText.isEmpty()) k.setHargaSewa(Double.parseDouble(hargaText));
            if (!lamaText.isEmpty()) k.setLamaSewa(Integer.parseInt(lamaText));

            if (k instanceof Mobil) {
                Mobil m = (Mobil) k;
                String kursiText = bacaStringBolehKosong(
                    "Jumlah kursi baru [" + m.getJumlahKursi() + "]: ");
                if (!kursiText.isEmpty()) {
                    m.setJumlahKursi(Integer.parseInt(kursiText));
                }
            } else if (k instanceof Motor) {
                Motor m = (Motor) k;
                String jenisText = bacaStringBolehKosong(
                    "Jenis motor baru [" + m.getJenisMotor() + "]: ");
                if (!jenisText.isEmpty()) {
                    m.setJenisMotor(jenisText);
                }
            }
            System.out.println("Data kendaraan berhasil diperbarui.");
        } catch (NumberFormatException e) {
            System.out.println("Data angka tidak valid. Perubahan yang sudah dilakukan "
                + "sebelum kesalahan mungkin sudah tersimpan.");
        } catch (IllegalArgumentException e) {
            System.out.println("Data tidak valid: " + e.getMessage());
        }
    }

    private static void hitungBiaya() {
        System.out.println("\n--- HITUNG BIAYA SEWA ---");
        Kendaraan k = pilihKendaraan();
        if (k == null) return;

        System.out.println("Nama Kendaraan : " + k.getNamaKendaraan());
        System.out.println("Harga per Hari : " + formatRupiah(k.getHargaSewa()));
        System.out.println("Lama Sewa      : " + k.getLamaSewa() + " hari");
        System.out.println("Total Biaya    : " + formatRupiah(k.hitungBiaya()));
    }

    private static void detailKendaraan() {
        System.out.println("\n--- DETAIL KENDARAAN ---");
        Kendaraan k = pilihKendaraan();
        if (k != null) {
            k.tampilkanInfo();
        }
    }

    private static Kendaraan pilihKendaraan() {
        if (daftarKendaraan.isEmpty()) {
            System.out.println("Belum ada kendaraan yang tersimpan.");
            return null;
        }

        tampilkanKendaraan();
        int nomor = bacaInt("Pilih nomor kendaraan: ");

        if (nomor < 1 || nomor > daftarKendaraan.size()) {
            System.out.println("Nomor kendaraan tidak ditemukan.");
            return null;
        }
        return daftarKendaraan.get(nomor - 1);
    }

    private static String bacaString(String pesan) {
        String hasil;
        do {
            System.out.print(pesan);
            hasil = input.nextLine().trim();
            if (hasil.isEmpty()) System.out.println("Input tidak boleh kosong.");
        } while (hasil.isEmpty());
        return hasil;
    }

    private static String bacaStringBolehKosong(String pesan) {
        System.out.print(pesan);
        return input.nextLine().trim();
    }

    private static int bacaInt(String pesan) {
        while (true) {
            System.out.print(pesan);
            try {
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Masukkan bilangan bulat yang benar.");
            }
        }
    }

    private static double bacaDouble(String pesan) {
        while (true) {
            System.out.print(pesan);
            try {
                // Gunakan titik untuk angka desimal, misalnya 350000 atau 350000.5.
                return Double.parseDouble(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Masukkan angka yang benar.");
            }
        }
    }

    public static String formatRupiah(double nilai) {
        NumberFormat format = NumberFormat.getCurrencyInstance(
            Locale.forLanguageTag("id-ID")
        );
        format.setMaximumFractionDigits(0);
        format.setMinimumFractionDigits(0);
        return format.format(nilai);
    }
}
