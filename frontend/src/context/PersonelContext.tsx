import React, { createContext, useContext, useReducer, ReactNode } from 'react';
import { PersonelResponse } from '../types/personel';

// State interface
interface PersonelState {
  personeller: PersonelResponse[];
  loading: boolean;
  error: string | null;
  selectedPersonel: PersonelResponse | null;
}

// Action types
type PersonelAction =
  | { type: 'SET_LOADING'; payload: boolean }
  | { type: 'SET_ERROR'; payload: string | null }
  | { type: 'SET_PERSONELLER'; payload: PersonelResponse[] }
  | { type: 'ADD_PERSONEL'; payload: PersonelResponse }
  | { type: 'UPDATE_PERSONEL'; payload: PersonelResponse }
  | { type: 'DELETE_PERSONEL'; payload: number }
  | { type: 'SET_SELECTED_PERSONEL'; payload: PersonelResponse | null };

// Initial state
const initialState: PersonelState = {
  personeller: [],
  loading: false,
  error: null,
  selectedPersonel: null,
};

// Reducer
const personelReducer = (state: PersonelState, action: PersonelAction): PersonelState => {
  switch (action.type) {
    case 'SET_LOADING':
      return { ...state, loading: action.payload };
    case 'SET_ERROR':
      return { ...state, error: action.payload, loading: false };
    case 'SET_PERSONELLER':
      return { ...state, personeller: action.payload, loading: false, error: null };
    case 'ADD_PERSONEL':
      return { 
        ...state, 
        personeller: [...state.personeller, action.payload],
        loading: false,
        error: null 
      };
    case 'UPDATE_PERSONEL':
      return {
        ...state,
        personeller: state.personeller.map(p => 
          p.id === action.payload.id ? action.payload : p
        ),
        selectedPersonel: state.selectedPersonel?.id === action.payload.id 
          ? action.payload 
          : state.selectedPersonel,
        loading: false,
        error: null
      };
    case 'DELETE_PERSONEL':
      return {
        ...state,
        personeller: state.personeller.filter(p => p.id !== action.payload),
        selectedPersonel: state.selectedPersonel?.id === action.payload 
          ? null 
          : state.selectedPersonel,
        loading: false,
        error: null
      };
    case 'SET_SELECTED_PERSONEL':
      return { ...state, selectedPersonel: action.payload };
    default:
      return state;
  }
};

// Context interface
interface PersonelContextType {
  state: PersonelState;
  dispatch: React.Dispatch<PersonelAction>;
}

// Create context
const PersonelContext = createContext<PersonelContextType | undefined>(undefined);

// Provider component
interface PersonelProviderProps {
  children: ReactNode;
}

export const PersonelProvider: React.FC<PersonelProviderProps> = ({ children }) => {
  const [state, dispatch] = useReducer(personelReducer, initialState);

  return (
    <PersonelContext.Provider value={{ state, dispatch }}>
      {children}
    </PersonelContext.Provider>
  );
};

// Custom hook
export const usePersonel = () => {
  const context = useContext(PersonelContext);
  if (context === undefined) {
    throw new Error('usePersonel must be used within a PersonelProvider');
  }
  return context;
};

export default PersonelContext;
