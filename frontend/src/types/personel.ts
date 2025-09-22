/**
 * Personel bilgilerini temsil eden ana interface
 * Frontend'de personel verilerini tipli olarak kullanmak için
 */
export interface Personel {
  id?: number;                    // Personel ID'si (opsiyonel - yeni kayıtlarda olmaz)
  ad: string;                     // Personelin adı
  soyad: string;                  // Personelin soyadı
  email: string;                  // Email adresi (benzersiz olmalı)
  telefon: string;                // Telefon numarası
  departman: string;              // Çalıştığı departman
  pozisyon: string;               // İş pozisyonu/ünvanı
  maas: number;                   // Maaş bilgisi
  iseBaslamaTarihi: string;       // İşe başlama tarihi (ISO string format)
  aktif: boolean;                 // Personelin aktif durumu
  olusturmaTarihi?: string;       // Kayıt oluşturma zamanı (opsiyonel)
  guncellemeTarihi?: string;      // Son güncelleme zamanı (opsiyonel)
}

/**
 * Yeni personel oluştururken kullanılan DTO
 * Backend'e POST request'inde gönderilen veri yapısı
 */
export interface PersonelCreateDTO {
  ad: string;                     // Zorunlu - Personelin adı
  soyad: string;                  // Zorunlu - Personelin soyadı
  email: string;                  // Zorunlu - Email adresi
  telefon: string;                // Zorunlu - Telefon numarası
  departman: string;              // Zorunlu - Departman bilgisi
  pozisyon: string;               // Zorunlu - İş pozisyonu
  maas: number;                   // Zorunlu - Maaş bilgisi
  iseBaslamaTarihi: string;       // Zorunlu - İşe başlama tarihi
  aktif: boolean;                 // Zorunlu - Aktiflik durumu
}

/**
 * Personel güncellerken kullanılan DTO
 * Tüm alanlar opsiyonel - sadece değişen alanlar gönderilir
 */
export interface PersonelUpdateDTO {
  ad?: string;                    // Opsiyonel - Personelin adı
  soyad?: string;                 // Opsiyonel - Personelin soyadı
  email?: string;                 // Opsiyonel - Email adresi
  telefon?: string;               // Opsiyonel - Telefon numarası
  departman?: string;             // Opsiyonel - Departman bilgisi
  pozisyon?: string;              // Opsiyonel - İş pozisyonu
  maas?: number;                  // Opsiyonel - Maaş bilgisi
  iseBaslamaTarihi?: string;      // Opsiyonel - İşe başlama tarihi
  aktif?: boolean;                // Opsiyonel - Aktiflik durumu
}

/**
 * Backend'den dönen personel response yapısı
 * Tüm alanlar zorunlu ve tam veri içerir
 */
export interface PersonelResponse {
  id: number;                     // Personel ID'si
  ad: string;                     // Personelin adı
  soyad: string;                  // Personelin soyadı
  email: string;                  // Email adresi
  telefon: string;                // Telefon numarası
  departman: string;              // Departman bilgisi
  pozisyon: string;               // İş pozisyonu
  maas: number;                   // Maaş bilgisi
  iseBaslamaTarihi: string;       // İşe başlama tarihi
  aktif: boolean;                 // Aktiflik durumu
  olusturmaTarihi: string;        // Kayıt oluşturma zamanı
  guncellemeTarihi: string;       // Son güncelleme zamanı
}

/**
 * API hatalarını temsil eden interface
 * Backend'den dönen hata yapısını tipli olarak kullanmak için
 */
export interface ApiError {
  message: string;                // Hata mesajı
  status: number;                 // HTTP status kodu
  timestamp: string;              // Hata zamanı
  path: string;                   // Hata oluşan endpoint path'i
}

/**
 * Genel API response wrapper'ı
 * Backend'den dönen tüm response'ları sarmalamak için
 */
export interface ApiResponse<T> {
  data: T;                        // Asıl veri (generic tip)
  message?: string;               // Opsiyonel mesaj
  success: boolean;               // İşlem başarı durumu
}
