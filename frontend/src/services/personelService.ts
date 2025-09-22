import axios from 'axios';
import { PersonelCreateDTO, PersonelUpdateDTO, PersonelResponse } from '../types/personel';

// Backend API'nin base URL'i
const API_BASE_URL = 'http://localhost:8080/api';

/**
 * Axios client instance'ı - tüm API istekleri için ortak yapılandırma
 * Base URL ve ortak header'ları içerir
 */
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json', // JSON formatında veri gönderimi
  },
});

/**
 * Request interceptor - Giden istekleri yakalar ve loglar
 * Her API isteği öncesinde çalışır
 */
apiClient.interceptors.request.use(
  (config) => {
    // Konsola istek detaylarını yazdır (debug amaçlı)
    console.log(`Making ${config.method?.toUpperCase()} request to ${config.url}`);
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

/**
 * Response interceptor - Gelen yanıtları yakalar ve hataları işler
 * Her API yanıtı sonrasında çalışır
 */
apiClient.interceptors.response.use(
  (response) => {
    // Başarılı yanıtları olduğu gibi döndür
    return response;
  },
  (error) => {
    // API hatalarını konsola yazdır ve yeniden fırlat
    console.error('API Error:', error.response?.data || error.message);
    return Promise.reject(error);
  }
);

/**
 * Personel işlemleri için API servis fonksiyonları
 * Backend CRUD operasyonlarına karşılık gelen client-side metodlar
 */
export const personelService = {
  /**
   * Tüm aktif personelleri getirir
   * @returns Promise<PersonelResponse[]> - Personel listesi
   */
  getAll: async (): Promise<PersonelResponse[]> => {
    const response = await apiClient.get<PersonelResponse[]>('/personel');
    return response.data;
  },

  /**
   * Belirtilen ID'ye sahip personeli getirir
   * @param id - Personel ID'si
   * @returns Promise<PersonelResponse> - Personel bilgileri
   */
  getById: async (id: number): Promise<PersonelResponse> => {
    const response = await apiClient.get<PersonelResponse>(`/personel/${id}`);
    return response.data;
  },

  /**
   * Yeni personel kaydı oluşturur
   * @param personel - Oluşturulacak personel bilgileri
   * @returns Promise<PersonelResponse> - Oluşturulan personel bilgileri
   */
  create: async (personel: PersonelCreateDTO): Promise<PersonelResponse> => {
    const response = await apiClient.post<PersonelResponse>('/personel', personel);
    return response.data;
  },

  /**
   * Mevcut personel bilgilerini günceller
   * @param id - Güncellenecek personel ID'si
   * @param personel - Güncellenecek bilgiler
   * @returns Promise<PersonelResponse> - Güncellenmiş personel bilgileri
   */
  update: async (id: number, personel: PersonelUpdateDTO): Promise<PersonelResponse> => {
    const response = await apiClient.put<PersonelResponse>(`/personel/${id}`, personel);
    return response.data;
  },

  /**
   * Personeli siler (soft delete)
   * @param id - Silinecek personel ID'si
   * @returns Promise<void> - Yanıt verisi yok
   */
  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`/personel/${id}`);
  },

  /**
   * Belirtilen departmandaki personelleri getirir
   * @param departman - Departman adı
   * @returns Promise<PersonelResponse[]> - Departmandaki personel listesi
   */
  getByDepartman: async (departman: string): Promise<PersonelResponse[]> => {
    const response = await apiClient.get<PersonelResponse[]>(`/personel/departman/${departman}`);
    return response.data;
  },

  /**
   * Sadece aktif personelleri getirir
   * @returns Promise<PersonelResponse[]> - Aktif personel listesi
   */
  getAktif: async (): Promise<PersonelResponse[]> => {
    const response = await apiClient.get<PersonelResponse[]>('/personel/aktif');
    return response.data;
  },

  /**
   * Ad ve soyada göre personel arar
   * @param ad - Aranacak ad
   * @param soyad - Aranacak soyad
   * @returns Promise<PersonelResponse[]> - Arama sonuçları
   */
  searchByName: async (ad: string, soyad: string): Promise<PersonelResponse[]> => {
    const response = await apiClient.get<PersonelResponse[]>(`/personel/search`, {
      params: { ad, soyad } // Query parametreleri olarak gönder
    });
    return response.data;
  }
};

// Diğer servislerin kullanabileceği axios client instance'ını export et
export default apiClient;
