package picklenostra.picklebankapp.Model;

import java.io.Serializable;

/**
 * Created by Edwin on 5/14/2016.
 */
public class ItemWithdrawalModel implements Serializable {
    private int id;
    private int nominalWithdrawal;
    private long waktu;
    private String namaNasabah;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNominalWithdrawal() {
        return nominalWithdrawal;
    }

    public void setNominalWithdrawal(int nominalWithdrawal) {
        this.nominalWithdrawal = nominalWithdrawal;
    }

    public long getWaktu() {
        return waktu;
    }

    public void setWaktu(long waktu) {
        this.waktu = waktu;
    }

    public String getNamaNasabah() {
        return namaNasabah;
    }

    public void setNamaNasabah(String namaNasabah) {
        this.namaNasabah = namaNasabah;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
