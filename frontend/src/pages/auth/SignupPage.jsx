import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';

export default function SignupPage() {
  const [form, setForm] = useState({ fullName: '', email: '', password: '' });
  const [message, setMessage] = useState('');
  const { signup } = useAuth();
  const navigate = useNavigate();

  return (
    <section className="container auth-box">
      <h2>Create your account</h2>
      <form onSubmit={async (e) => {
        e.preventDefault();
        await signup(form);
        setMessage('Account created. Please verify your email to unlock bonus funds.');
        setTimeout(() => navigate('/login'), 1600);
      }}>
        <input required placeholder="Full name" onChange={(e) => setForm({ ...form, fullName: e.target.value })} />
        <input type="email" required placeholder="Email" onChange={(e) => setForm({ ...form, email: e.target.value })} />
        <input minLength={8} type="password" required placeholder="Password" onChange={(e) => setForm({ ...form, password: e.target.value })} />
        {message && <p className="success">{message}</p>}
        <button type="submit">Open account</button>
      </form>
      <p>Already registered? <Link to="/login">Login</Link></p>
    </section>
  );
}
