import React, { useState, useEffect } from 'react';
import { PersonelResponse } from '../types/personel';
import { personelService } from '../services/personelService';
import { usePersonel } from '../context/PersonelContext';
import toast from 'react-hot-toast';

interface PersonelListProps {
  onEdit: (personel: PersonelResponse) => void;
  onView: (personel: PersonelResponse) => void;
}

const PersonelList: React.FC<PersonelListProps> = ({ onEdit, onView }) => {
  const { state, dispatch } = usePersonel();
  const [searchTerm, setSearchTerm] = useState('');
  const [departmanFilter, setDepartmanFilter] = useState('');
  const [aktifFilter, setAktifFilter] = useState<string>('all');

  const departmanlar = [
    'İnsan Kaynakları', 'Bilgi İşlem', 'Muhasebe', 'Pazarlama', 
    'Satış', 'Üretim', 'Kalite Kontrol', 'Lojistik'
  ];

  useEffect(() => {
    loadPersoneller();
  }, []);

  const loadPersoneller = async () => {
    try {
      dispatch({ type: 'SET_LOADING', payload: true });
      const data = await personelService.getAll();
      dispatch({ type: 'SET_PERSONELLER', payload: data });
    } catch (error: any) {
      dispatch({ type: 'SET_ERROR', payload: 'Personeller yüklenirken hata oluştu' });
      toast.error('Personeller yüklenirken hata oluştu');
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Bu personeli silmek istediğinizden emin misiniz?')) {
      try {
        dispatch({ type: 'SET_LOADING', payload: true });
        await personelService.delete(id);
        dispatch({ type: 'DELETE_PERSONEL', payload: id });
        toast.success('Personel başarıyla silindi');
      } catch (error: any) {
        dispatch({ type: 'SET_ERROR', payload: 'Personel silinirken hata oluştu' });
        toast.error('Personel silinirken hata oluştu');
      }
    }
  };

  const filteredPersoneller = state.personeller.filter(personel => {
    const searchMatch = searchTerm === '' || 
      personel.ad.toLowerCase().includes(searchTerm.toLowerCase()) ||
      personel.soyad.toLowerCase().includes(searchTerm.toLowerCase()) ||
      personel.email.toLowerCase().includes(searchTerm.toLowerCase());
    
    const departmanMatch = departmanFilter === '' || personel.departman === departmanFilter;
    
    const aktifMatch = aktifFilter === 'all' || 
      (aktifFilter === 'aktif' && personel.aktif) ||
      (aktifFilter === 'pasif' && !personel.aktif);

    return searchMatch && departmanMatch && aktifMatch;
  });

  if (state.loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-primary-500"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Arama ve Filtreler */}
      <div className="card">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Arama
            </label>
            <input
              type="text"
              placeholder="Ad, soyad veya email ile ara..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="input-field"
            />
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Departman
            </label>
            <select
              value={departmanFilter}
              onChange={(e) => setDepartmanFilter(e.target.value)}
              className="input-field"
            >
              <option value="">Tüm Departmanlar</option>
              {departmanlar.map(dept => (
                <option key={dept} value={dept}>{dept}</option>
              ))}
            </select>
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Durum
            </label>
            <select
              value={aktifFilter}
              onChange={(e) => setAktifFilter(e.target.value)}
              className="input-field"
            >
              <option value="all">Tümü</option>
              <option value="aktif">Aktif</option>
              <option value="pasif">Pasif</option>
            </select>
          </div>
          
          <div className="flex items-end">
            <button
              onClick={loadPersoneller}
              className="btn-secondary w-full"
            >
              Yenile
            </button>
          </div>
        </div>
      </div>

      {/* Personel Listesi */}
      <div className="card">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-semibold text-gray-800">
            Personel Listesi ({filteredPersoneller.length})
          </h2>
        </div>

        {filteredPersoneller.length === 0 ? (
          <div className="text-center py-8 text-gray-500">
            Personel bulunamadı
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Ad Soyad
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Email
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Departman
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Pozisyon
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Maaş
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Durum
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    İşlemler
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {filteredPersoneller.map((personel) => (
                  <tr key={personel.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm font-medium text-gray-900">
                        {personel.ad} {personel.soyad}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-500">{personel.email}</div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-900">{personel.departman}</div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-900">{personel.pozisyon}</div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-900">
                        ₺{personel.maas.toLocaleString()}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                        personel.aktif 
                          ? 'bg-green-100 text-green-800' 
                          : 'bg-red-100 text-red-800'
                      }`}>
                        {personel.aktif ? 'Aktif' : 'Pasif'}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                      <button
                        onClick={() => onView(personel)}
                        className="text-blue-600 hover:text-blue-900"
                      >
                        Görüntüle
                      </button>
                      <button
                        onClick={() => onEdit(personel)}
                        className="text-indigo-600 hover:text-indigo-900"
                      >
                        Düzenle
                      </button>
                      <button
                        onClick={() => handleDelete(personel.id!)}
                        className="text-red-600 hover:text-red-900"
                      >
                        Sil
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default PersonelList;
