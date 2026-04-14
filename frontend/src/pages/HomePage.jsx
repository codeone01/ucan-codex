import { Link } from 'react-router-dom';

export default function HomePage() {
  return (
    <section className="hero container">
      <div>
        <h1>Luxury Without Limits</h1>
        <p>Explore curated premium products across nautical, aerospace, fashion, estates and more.</p>
        <Link className="cta" to="/catalog">Enter Catalog</Link>
      </div>
    </section>
  );
}
