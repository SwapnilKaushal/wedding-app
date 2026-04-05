import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../api/axios';

export default function Dashboard() {
  const [events, setEvents] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ name: '', weddingDate: '', description: '' });
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  useEffect(() => { fetchEvents(); }, []);

  const fetchEvents = async () => {
    const { data } = await api.get('/events');
    setEvents(data);
  };

  const createEvent = async (e) => {
    e.preventDefault();
    await api.post('/events', form);
    setForm({ name: '', weddingDate: '', description: '' });
    setShowForm(false);
    fetchEvents();
  };

  return (
    <div style={styles.page}>
      <div style={styles.header}>
        <div>
          <h1 style={styles.title}>Wedding App</h1>
          <p style={styles.sub}>Welcome, {user.name}</p>
        </div>
        <button style={styles.logoutBtn} onClick={logout}>Logout</button>
      </div>

      <div style={styles.section}>
        <div style={styles.sectionHeader}>
          <h2 style={styles.sectionTitle}>My Events</h2>
          {user.role === 'ADMIN' && (
            <button style={styles.addBtn} onClick={() => setShowForm(!showForm)}>
              + New Event
            </button>
          )}
        </div>

        {showForm && (
          <form onSubmit={createEvent} style={styles.form}>
            <input style={styles.input} placeholder="Event name"
              value={form.name} onChange={e => setForm({...form, name: e.target.value})} required />
            <input style={styles.input} type="date"
              value={form.weddingDate} onChange={e => setForm({...form, weddingDate: e.target.value})} />
            <input style={styles.input} placeholder="Description (optional)"
              value={form.description} onChange={e => setForm({...form, description: e.target.value})} />
            <button style={styles.addBtn} type="submit">Create Event</button>
          </form>
        )}

        <div style={styles.grid}>
          {events.map(event => (
            <div key={event.id} style={styles.card}
              onClick={() => navigate(`/events/${event.id}`)}>
              <h3 style={styles.cardTitle}>{event.name}</h3>
              {event.weddingDate && (
                <p style={styles.cardDate}>Date: {event.weddingDate}</p>
              )}
              <p style={styles.cardMeta}>{event.memberEmails?.length} members</p>
              <p style={styles.cardSub}>by {event.createdByName}</p>
            </div>
          ))}
          {events.length === 0 && (
            <p style={styles.empty}>No events yet. Create one to get started!</p>
          )}
        </div>
      </div>
    </div>
  );
}

const styles = {
  page: { maxWidth: '960px', margin: '0 auto', padding: '2rem' },
  header: { display: 'flex', justifyContent: 'space-between',
    alignItems: 'center', marginBottom: '2rem' },
  title: { margin: 0, fontSize: '1.8rem', fontWeight: 700 },
  sub: { margin: '4px 0 0', color: '#666' },
  logoutBtn: { padding: '0.5rem 1rem', border: '1px solid #ddd',
    borderRadius: '8px', cursor: 'pointer', background: '#fff' },
  section: { marginBottom: '2rem' },
  sectionHeader: { display: 'flex', justifyContent: 'space-between',
    alignItems: 'center', marginBottom: '1rem' },
  sectionTitle: { margin: 0, fontSize: '1.2rem' },
  addBtn: { padding: '0.5rem 1rem', background: '#6c63ff', color: '#fff',
    border: 'none', borderRadius: '8px', cursor: 'pointer' },
  form: { background: '#f9f9f9', padding: '1rem', borderRadius: '8px',
    marginBottom: '1rem' },
  input: { width: '100%', padding: '0.65rem', marginBottom: '0.75rem',
    border: '1px solid #ddd', borderRadius: '8px', fontSize: '0.95rem',
    boxSizing: 'border-box' },
  grid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))',
    gap: '1rem' },
  card: { background: '#fff', padding: '1.25rem', borderRadius: '12px',
    boxShadow: '0 2px 8px rgba(0,0,0,0.07)', cursor: 'pointer',
    transition: 'transform 0.15s', border: '1px solid #eee' },
  cardTitle: { margin: '0 0 0.5rem', fontSize: '1rem', fontWeight: 600 },
  cardDate: { margin: '0 0 0.25rem', fontSize: '0.85rem', color: '#6c63ff' },
  cardMeta: { margin: '0 0 0.25rem', fontSize: '0.85rem', color: '#555' },
  cardSub: { margin: 0, fontSize: '0.8rem', color: '#999' },
  empty: { color: '#999', gridColumn: '1/-1' }
};