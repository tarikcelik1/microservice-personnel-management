package com.example.personal_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class PersonelUpdateDTO {

    @Size(min = 2, max = 50, message = "Ad 2-50 karakter arasında olmalıdır")
    private String ad;

    @Size(min = 2, max = 50, message = "Soyad 2-50 karakter arasında olmalıdır")
    private String soyad;

    @Email(message = "Geçerli bir email adresi giriniz")
    private String email;

    @Size(min = 10, max = 15, message = "Telefon numarası 10-15 karakter arasında olmalıdır")
    private String telefon;

    private String departman;
    private String pozisyon;
    private LocalDate iseBaslamaTarihi;
    private Double maas;
    private Boolean aktif;

    // Constructors
    public PersonelUpdateDTO() {}

    // Getters and Setters
    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getDepartman() {
        return departman;
    }

    public void setDepartman(String departman) {
        this.departman = departman;
    }

    public String getPozisyon() {
        return pozisyon;
    }

    public void setPozisyon(String pozisyon) {
        this.pozisyon = pozisyon;
    }

    public LocalDate getIseBaslamaTarihi() {
        return iseBaslamaTarihi;
    }

    public void setIseBaslamaTarihi(LocalDate iseBaslamaTarihi) {
        this.iseBaslamaTarihi = iseBaslamaTarihi;
    }

    public Double getMaas() {
        return maas;
    }

    public void setMaas(Double maas) {
        this.maas = maas;
    }

    public Boolean getAktif() {
        return aktif;
    }

    public void setAktif(Boolean aktif) {
        this.aktif = aktif;
    }
}
