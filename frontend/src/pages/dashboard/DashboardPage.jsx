import { useEffect, useState } from 'react';
import api from '../../services/api';

export default function DashboardPage() {
  const [data, setData] = useState({ profile: {}, orders: [], cards: [], addresses: [], favorites: [] });

  useEffect(() => {
    api.get('/users/me/dashboard').then((res) => setData(res.data));
  }, []);

  return (
    <section className="container page">
      <h2>My Luxury Area</h2>
      <p><strong>{data.profile.fullName}</strong> · Balance: R$ {Number(data.profile.balance || 0).toLocaleString('pt-BR')}</p>
      <h3>Orders</h3>
      {data.orders.map((o) => <p key={o.id}>#{o.id} · {o.status} · R$ {Number(o.totalAmount).toLocaleString('pt-BR')}</p>)}
      <h3>Saved Fake Cards</h3>
      {data.cards.map((c) => <p key={c.id}>{c.nickname} •••• {c.last4}</p>)}
      <h3>Saved Addresses</h3>
      {data.addresses.map((a) => <p key={a.id}>{a.addressLine}, {a.city}</p>)}
      <h3>Favorites</h3>
      {data.favorites.map((f) => <p key={f.productId}>{f.productName}</p>)}
    </section>
  );
}
