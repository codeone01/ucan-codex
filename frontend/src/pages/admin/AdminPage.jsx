import { useEffect, useState } from 'react';
import api from '../../services/api';

export default function AdminPage() {
  const [metrics, setMetrics] = useState({});
  useEffect(() => {
    api.get('/admin/metrics').then((res) => setMetrics(res.data));
  }, []);

  return (
    <section className="container page">
      <h2>Operations Console</h2>
      <div className="metrics-grid">
        <article><h4>Revenue (fictional)</h4><p>R$ {Number(metrics.fictionalRevenue || 0).toLocaleString('pt-BR')}</p></article>
        <article><h4>Orders</h4><p>{metrics.totalOrders || 0}</p></article>
        <article><h4>Users</h4><p>{metrics.totalUsers || 0}</p></article>
      </div>
      <p>Use backend endpoints to manage products, categories, users, banners and featured sections.</p>
    </section>
  );
}
