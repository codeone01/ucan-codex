import { Link } from 'react-router-dom';
import { useCart } from '../../contexts/CartContext';

export default function CartPage() {
  const { items, removeItem, total } = useCart();

  return (
    <section className="container page">
      <h2>Your Cart</h2>
      {items.map((item) => (
        <div className="line-item" key={item.id}>
          <span>{item.name} x {item.qty}</span>
          <span>R$ {(item.qty * item.price).toLocaleString('pt-BR')}</span>
          <button type="button" onClick={() => removeItem(item.id)}>Remove</button>
        </div>
      ))}
      <h3>Total: R$ {total().toLocaleString('pt-BR')}</h3>
      <Link className="cta" to="/checkout">Proceed to checkout</Link>
    </section>
  );
}
