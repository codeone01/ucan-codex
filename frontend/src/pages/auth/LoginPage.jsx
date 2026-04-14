import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';

export default function LoginPage() {
  const [form, setForm] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();

  return (
    <section className="container auth-box">
      <h2>Login</h2>
      <form onSubmit={async (e) => {
        e.preventDefault();
        try {
          await login(form);
          navigate('/dashboard');
        } catch (err) {
          setError(err.response?.data?.message || 'Invalid credentials');
        }
      }}>
        <input type="email" required placeholder="Email" onChange={(e) => setForm({ ...form, email: e.target.value })} />
        <input type="password" required placeholder="Password" onChange={(e) => setForm({ ...form, password: e.target.value })} />
        {error && <p className="error">{error}</p>}
        <button type="submit">Enter</button>
      </form>
      <p>No account? <Link to="/signup">Create one</Link></p>
    </section>
  );
}
