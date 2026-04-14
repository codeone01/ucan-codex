import { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import api from '../../services/api';

export default function VerifyEmailPage() {
  const [params] = useSearchParams();
  const [status, setStatus] = useState('Verifying your email...');

  useEffect(() => {
    const token = params.get('token');
    if (!token) {
      setStatus('Invalid verification link.');
      return;
    }
    api.post('/auth/verify-email', { token })
      .then(() => setStatus('Email verified. Bonus credited successfully.'))
      .catch(() => setStatus('Verification link invalid or already used.'));
  }, [params]);

  return <section className="container page"><h2>{status}</h2></section>;
}
