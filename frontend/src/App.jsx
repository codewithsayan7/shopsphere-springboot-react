import React, { useEffect, useMemo, useState } from 'react';

const API = 'http://localhost:8081/api';

const emptyProduct = {
  name: '',
  description: '',
  category: 'General',
  price: 1,
  stock: 0,
  imageUrl: ''
};

async function readError(response, fallback) {
  const body = await response.json().catch(() => ({}));
  return body.error || fallback;
}

export default function App() {
  const [products, setProducts] = useState([]);
  const [query, setQuery] = useState('');
  const [cart, setCart] = useState([]);
  const [form, setForm] = useState(emptyProduct);
  const [editingId, setEditingId] = useState(null);
  const [isAdmin, setIsAdmin] = useState(false);
  const [customerName, setCustomerName] = useState('');
  const [customerEmail, setCustomerEmail] = useState('');
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [checkingOut, setCheckingOut] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  async function loadProducts(search = query) {
    setLoading(true);
    try {
      const suffix = search.trim() ? `?q=${encodeURIComponent(search.trim())}` : '';
      const response = await fetch(`${API}/products${suffix}`);
      if (!response.ok) throw new Error('Could not load the product catalogue.');
      setProducts(await response.json());
      setError('');
    } catch (requestError) {
      setError(requestError.message || 'The shop service is unavailable.');
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    const timer = setTimeout(() => loadProducts(query), 250);
    return () => clearTimeout(timer);
  }, [query]);

  const cartCount = cart.reduce((count, item) => count + item.quantity, 0);
  const cartTotal = useMemo(
    () => cart.reduce((total, item) => total + item.price * item.quantity, 0),
    [cart]
  );

  function addToCart(product) {
    setMessage('');
    setError('');
    setCart(current => {
      const existing = current.find(item => item.id === product.id);
      if (existing) {
        return current.map(item => item.id === product.id
          ? { ...item, quantity: Math.min(item.quantity + 1, product.stock) }
          : item
        );
      }
      return [...current, { ...product, quantity: 1 }];
    });
  }

  function updateProductField(event) {
    const { name, value } = event.target;
    setForm(current => ({ ...current, [name]: value }));
  }

  function resetProductForm() {
    setForm(emptyProduct);
    setEditingId(null);
  }

  async function saveProduct(event) {
    event.preventDefault();
    setSaving(true);
    setMessage('');
    setError('');

    try {
      const editing = editingId !== null;
      const response = await fetch(
        editing ? `${API}/products/${editingId}` : `${API}/products`,
        {
          method: editing ? 'PUT' : 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            ...form,
            price: Number(form.price),
            stock: Number(form.stock)
          })
        }
      );

      if (!response.ok) {
        throw new Error(await readError(response, 'The product could not be saved.'));
      }

      resetProductForm();
      setMessage(editing ? 'Product details updated.' : 'Product added to the catalogue.');
      await loadProducts();
    } catch (requestError) {
      setError(requestError.message || 'Something went wrong while saving the product.');
    } finally {
      setSaving(false);
    }
  }

  async function checkout() {
    if (!cart.length || !customerName.trim() || !customerEmail.trim()) return;

    setCheckingOut(true);
    setMessage('');
    setError('');

    try {
      const response = await fetch(`${API}/orders`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          customerName: customerName.trim(),
          customerEmail: customerEmail.trim(),
          items: cart.map(item => ({
            productId: item.id,
            quantity: item.quantity
          }))
        })
      });

      if (!response.ok) {
        throw new Error(await readError(response, 'Checkout could not be completed.'));
      }

      const order = await response.json();
      setMessage(`Demo order #${order.id} has been placed.`);
      setCart([]);
      await loadProducts();
    } catch (requestError) {
      setError(requestError.message || 'Something went wrong during checkout.');
    } finally {
      setCheckingOut(false);
    }
  }

  async function showOrders() {
    setError('');
    try {
      const response = await fetch(`${API}/orders`);
      if (!response.ok) throw new Error('Could not load orders.');
      setOrders(await response.json());
    } catch (requestError) {
      setError(requestError.message || 'Orders are unavailable right now.');
    }
  }

  async function deleteProduct(id) {
    if (!window.confirm('Remove this product from the catalogue?')) return;

    setError('');
    setMessage('');
    try {
      const response = await fetch(`${API}/products/${id}`, { method: 'DELETE' });
      if (!response.ok) throw new Error('The product could not be removed.');
      setMessage('Product removed from the catalogue.');
      await loadProducts();
    } catch (requestError) {
      setError(requestError.message || 'Something went wrong while deleting the product.');
    }
  }

  function editProduct(product) {
    setEditingId(product.id);
    setForm({
      name: product.name,
      description: product.description,
      category: product.category,
      price: product.price,
      stock: product.stock,
      imageUrl: product.imageUrl || ''
    });
    setMessage('');
    setError('');
  }

  return (
    <main>
      <nav>
        <div className="brand">SHOP<span>SPHERE</span></div>
        <div>
          <button className={!isAdmin ? 'active' : ''} onClick={() => setIsAdmin(false)}>Store</button>
          <button className={isAdmin ? 'active' : ''} onClick={() => setIsAdmin(true)}>Admin</button>
        </div>
      </nav>

      <section className="hero">
        <div>
          <small>USEFUL THINGS FOR EVERYDAY LIFE</small>
          <h1>Good finds.<br />Better living.</h1>
          <p>A small storefront demo built around a simple shopping flow.</p>
        </div>
        <div className="heroMark" aria-hidden="true">S.</div>
      </section>

      {error && <p className="notice error" role="alert">{error}</p>}
      {message && <p className="notice" role="status">{message}</p>}

      {!isAdmin ? (
        <>
          <div className="tools">
            <h2>Explore the catalogue</h2>
            <input
              aria-label="Search products"
              placeholder="Search by product or category…"
              value={query}
              onChange={event => setQuery(event.target.value)}
            />
          </div>

          <div className="layout">
            <section className="products" aria-live="polite">
              {products.map(product => (
                <article className="product" key={product.id}>
                  <div className="photo">
                    {product.imageUrl
                      ? <img src={product.imageUrl} alt={product.name} />
                      : <span>{product.name.slice(0, 1).toUpperCase()}</span>}
                  </div>
                  <div className="category">{product.category}</div>
                  <h3>{product.name}</h3>
                  <p>{product.description}</p>
                  <div className="buy">
                    <b>₹{Number(product.price).toFixed(2)}</b>
                    <button disabled={product.stock < 1} onClick={() => addToCart(product)}>
                      {product.stock > 0 ? 'Add to bag' : 'Out of stock'}
                    </button>
                  </div>
                  <small className="stock-note">
                    {product.stock > 0 ? `${product.stock} available` : 'Currently unavailable'}
                  </small>
                </article>
              ))}
              {loading && <p className="empty">Loading products…</p>}
              {!loading && products.length === 0 && (
                <p className="empty">
                  {query ? 'No products match that search.' : 'The catalogue is empty for now.'}
                </p>
              )}
            </section>

            <aside className="cart">
              <h2>Your bag <span>{cartCount}</span></h2>
              {cart.length === 0 && <p className="empty">Your bag is empty. Add something you like.</p>}
              {cart.map(item => (
                <div className="cartrow" key={item.id}>
                  <div>
                    <b>{item.name}</b>
                    <small>₹{Number(item.price).toFixed(2)} × {item.quantity}</small>
                  </div>
                  <button
                    className="remove"
                    aria-label={`Remove ${item.name} from bag`}
                    onClick={() => setCart(current => current.filter(row => row.id !== item.id))}
                  >×</button>
                </div>
              ))}
              <div className="total"><span>Total</span><b>₹{cartTotal.toFixed(2)}</b></div>
              <input placeholder="Your name" value={customerName} onChange={event => setCustomerName(event.target.value)} />
              <input type="email" placeholder="Email address" value={customerEmail} onChange={event => setCustomerEmail(event.target.value)} />
              <button
                className="checkout"
                disabled={!cart.length || !customerName.trim() || !customerEmail.trim() || checkingOut}
                onClick={checkout}
              >
                {checkingOut ? 'Placing order…' : 'Place demo order'}
              </button>
              <small className="hint">This is a demo checkout. No payment is collected.</small>
            </aside>
          </div>
        </>
      ) : (
        <section className="admin">
          <div className="tools">
            <h2>Manage products</h2>
            <button className="outline" onClick={showOrders}>Load orders</button>
          </div>

          <form className="adminform" onSubmit={saveProduct}>
            <input name="name" required maxLength="120" placeholder="Product name" value={form.name} onChange={updateProductField} />
            <input name="category" required placeholder="Category" value={form.category} onChange={updateProductField} />
            <input name="price" required type="number" min="0.01" step="0.01" placeholder="Price" value={form.price} onChange={updateProductField} />
            <input name="stock" required type="number" min="0" step="1" placeholder="Stock" value={form.stock} onChange={updateProductField} />
            <input name="imageUrl" placeholder="Image URL (optional)" value={form.imageUrl} onChange={updateProductField} />
            <input name="description" required maxLength="2000" placeholder="Short description" value={form.description} onChange={updateProductField} />
            <button disabled={saving}>{saving ? 'Saving…' : editingId ? 'Save product' : 'Add product'}</button>
            {editingId && <button type="button" className="outline" onClick={resetProductForm}>Cancel</button>}
          </form>

          <div className="adminlist">
            {products.map(product => (
              <div key={product.id}>
                <span>
                  <b>{product.name}</b>
                  <small>{product.category} · ₹{Number(product.price).toFixed(2)} · {product.stock} in stock</small>
                </span>
                <button className="outline" onClick={() => editProduct(product)}>Edit</button>
                <button className="delete" onClick={() => deleteProduct(product.id)}>Delete</button>
              </div>
            ))}
            {!loading && products.length === 0 && <p className="empty">No products to manage yet.</p>}
          </div>

          {orders.length > 0 && (
            <>
              <h2>Orders</h2>
              {orders.map(order => (
                <p className="order" key={order.id}>
                  Order #{order.id} · {order.customerName} · ₹{Number(order.total).toFixed(2)} · {order.status}
                </p>
              ))}
            </>
          )}
        </section>
      )}

      <footer>SHOPSPHERE · A FULL-STACK COMMERCE PRACTICE PROJECT</footer>
    </main>
  );
}
