import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { useCart } from '../contexts/CartContext';

export default function Header() {
  const { user, logout, isAdmin } = useAuth();
  const { items } = useCart();
  const navigate = useNavigate();

  return (
    <header className="header">
      <div className="container nav-bar">
        <Link to="/" className="brand">UCAN</Link>
        <nav>
          <Link to="/catalog">Catalog</Link>
          <Link to="/cart">Cart ({items.length})</Link>
          {user && <Link to="/dashboard">My Area</Link>}
          {!user && <Link to="/login">Login</Link>}
          {!user && <Link to="/signup">Sign up</Link>}
          {isAdmin && <Link className="hidden-admin-link" to="/ops-console-7f9x">Ops</Link>}
          {user && (
            <button
              type="button"
              onClick={async () => {
                await logout();
                navigate('/');
              }}
            >
              Logout
            </button>
          )}
        </nav>
      </div>
    </header>
  );
}
