package picklenostra.picklebankapp.Model;

import java.io.Serializable;

/**
 * Created by Edwin on 4/14/2016.
 */
public class NasabahModel implements Serializable {
    private String id;
    private String nama;
    private String email;
    private String telepon;
    private String lokasi;
    private String photoUrl;
    private int jumlahSampah, jumlahSaldo;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public int getJumlahSampah() {
        return jumlahSampah;
    }

    public void setJumlahSampah(int jumlahSampah) {
        this.jumlahSampah = jumlahSampah;
    }

    public int getJumlahSaldo() {
        return jumlahSaldo;
    }

    public void setJumlahSaldo(int jumlahSaldo) {
        this.jumlahSaldo = jumlahSaldo;
    }
}
