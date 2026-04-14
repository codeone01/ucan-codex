import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCart } from '../../contexts/CartContext';
import api from '../../services/api';

export default function CheckoutPage() {
  const { items, total, clearCart } = useCart();
  const [payload, setPayload] = useState({
    addressLine: '', city: '', state: '', zipCode: '', country: 'BR',
    cardPrintedName: '', cardNumber: '', expiration: '', cvv: '', nickname: '', brand: ''
  });
  const [reuseCardId, setReuseCardId] = useState('');
  const [savedCards, setSavedCards] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    api.get('/checkout/cards').then((res) => setSavedCards(res.data));
  }, []);

  const submit = async (e) => {
    e.preventDefault();
    await api.post('/checkout/orders', {
      items,
      shipping: payload,
      card: reuseCardId ? { cardId: reuseCardId } : payload,
      paymentMethod: 'fake_credit_card'
    });
    clearCart();
    navigate('/dashboard');
  };

  return (
    <section className="container page">
      <h2>Checkout</h2>
      <form className="checkout-grid" onSubmit={submit}>
        <input required placeholder="Address line" onChange={(e) => setPayload({ ...payload, addressLine: e.target.value })} />
        <input required placeholder="City" onChange={(e) => setPayload({ ...payload, city: e.target.value })} />
        <input required placeholder="State" onChange={(e) => setPayload({ ...payload, state: e.target.value })} />
        <input required placeholder="ZIP" onChange={(e) => setPayload({ ...payload, zipCode: e.target.value })} />

        <select value={reuseCardId} onChange={(e) => setReuseCardId(e.target.value)}>
          <option value="">Use new fake card</option>
          {savedCards.map((card) => <option key={card.id} value={card.id}>{card.nickname} •••• {card.last4}</option>)}
        </select>

        {!reuseCardId && (
          <>
            <input required placeholder="Printed name" onChange={(e) => setPayload({ ...payload, cardPrintedName: e.target.value })} />
            <input required pattern="[0-9]{16}" placeholder="Card number (16 digits)" onChange={(e) => setPayload({ ...payload, cardNumber: e.target.value })} />
            <input required pattern="(0[1-9]|1[0-2])/[0-9]{2}" placeholder="MM/YY" onChange={(e) => setPayload({ ...payload, expiration: e.target.value })} />
            <input required pattern="[0-9]{3,4}" placeholder="CVV" onChange={(e) => setPayload({ ...payload, cvv: e.target.value })} />
            <input required placeholder="Nickname" onChange={(e) => setPayload({ ...payload, nickname: e.target.value })} />
            <input required placeholder="Brand" onChange={(e) => setPayload({ ...payload, brand: e.target.value })} />
          </>
        )}

        <h3>Total: R$ {total().toLocaleString('pt-BR')}</h3>
        <button type="submit">Place fictional order</button>
      </form>
    </section>
  );
}
