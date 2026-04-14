import { useEffect, useMemo, useState } from 'react';
import $ from 'jquery';
import { useCart } from '../../contexts/CartContext';
import api from '../../services/api';

const initialFilters = {
  q: '',
  category: '',
  subcategory: '',
  brand: '',
  type: '',
  color: '',
  sort: 'featured'
};

export default function CatalogPage() {
  const [products, setProducts] = useState([]);
  const [meta, setMeta] = useState({ categories: [], subcategories: [], brands: [], types: [], colors: [] });
  const [filters, setFilters] = useState(initialFilters);
  const { addItem } = useCart();

  useEffect(() => {
    api.get('/catalog/metadata').then((res) => setMeta(res.data));
  }, []);

  useEffect(() => {
    api.get('/catalog/products', { params: filters }).then((res) => setProducts(res.data));
  }, [filters]);

  useEffect(() => {
    $('.catalog-grid').hide().fadeIn(220);
  }, [products]);

  const subcategoryOptions = useMemo(() => (
    meta.subcategories.filter((s) => !filters.category || s.categorySlug === filters.category)
  ), [meta.subcategories, filters.category]);

  return (
    <section className="container page">
      <h2>Luxury Catalog</h2>
      <div className="filters">
        <input placeholder="Search by product name" value={filters.q} onChange={(e) => setFilters({ ...filters, q: e.target.value })} />
        {['category', 'subcategory', 'brand', 'type', 'color', 'sort'].map((key) => (
          <select key={key} value={filters[key]} onChange={(e) => setFilters({ ...filters, [key]: e.target.value })}>
            <option value="">{key === 'sort' ? 'Sort' : `All ${key}s`}</option>
            {(key === 'subcategory' ? subcategoryOptions : meta[`${key}s`] || []).map((item) => (
              <option key={item.slug} value={item.slug}>{item.name}</option>
            ))}
            {key === 'sort' && (
              <>
                <option value="featured">Featured</option>
                <option value="price_asc">Price: Low to High</option>
                <option value="price_desc">Price: High to Low</option>
                <option value="newest">Newest</option>
              </>
            )}
          </select>
        ))}
      </div>

      <div className="catalog-grid">
        {products.map((product) => (
          <article key={product.id} className="product-card">
            <div className="thumb">{product.featured ? '★ Featured' : 'Luxury'}</div>
            <h3>{product.name}</h3>
            <p>{product.shortDescription}</p>
            <strong>R$ {Number(product.price).toLocaleString('pt-BR')}</strong>
            <button type="button" onClick={() => addItem(product)}>Add to cart</button>
          </article>
        ))}
      </div>
    </section>
  );
}
