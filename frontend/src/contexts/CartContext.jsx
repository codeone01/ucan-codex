import { createContext, useContext, useMemo, useState } from 'react';

const CartContext = createContext(null);

export function CartProvider({ children }) {
  const [items, setItems] = useState([]);

  const value = useMemo(() => ({
    items,
    addItem(product) {
      setItems((prev) => {
        const found = prev.find((p) => p.id === product.id);
        if (found) return prev.map((p) => (p.id === product.id ? { ...p, qty: p.qty + 1 } : p));
        return [...prev, { ...product, qty: 1 }];
      });
    },
    removeItem(productId) {
      setItems((prev) => prev.filter((p) => p.id !== productId));
    },
    clearCart() {
      setItems([]);
    },
    total() {
      return items.reduce((sum, i) => sum + i.price * i.qty, 0);
    }
  }), [items]);

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>;
}

export const useCart = () => useContext(CartContext);
