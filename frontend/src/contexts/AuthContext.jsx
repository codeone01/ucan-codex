import { createContext, useContext, useEffect, useMemo, useState } from 'react';
import api from '../services/api';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [sessionToken, setSessionToken] = useState(localStorage.getItem('ucan_token'));

  useEffect(() => {
    if (!sessionToken) return;
    api.get('/auth/me').then((res) => setUser(res.data)).catch(() => {
      setUser(null);
      localStorage.removeItem('ucan_token');
      setSessionToken(null);
    });
  }, [sessionToken]);

  const value = useMemo(() => ({
    user,
    isAdmin: user?.role === 'admin',
    async login(payload) {
      const res = await api.post('/auth/login', payload);
      localStorage.setItem('ucan_token', res.data.token);
      setSessionToken(res.data.token);
      setUser(res.data.user);
    },
    async signup(payload) {
      return api.post('/auth/signup', payload);
    },
    async logout() {
      localStorage.removeItem('ucan_token');
      setSessionToken(null);
      setUser(null);
    }
  }), [user]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export const useAuth = () => useContext(AuthContext);
