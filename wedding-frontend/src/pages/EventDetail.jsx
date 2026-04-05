import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../api/axios';
import { useAuth } from '../context/AuthContext';

export default function EventDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const [event, setEvent] = useState(null);
  const [expenses, setExpenses] = useState([]);
  const [tasks, setTasks] = useState([]);
  const [total, setTotal] = useState(0);
  const [tab, setTab] = useState('expenses');
  const [expenseForm, setExpenseForm] = useState({
    title: '', amount: '', category: '', eventId: id });
  const [taskForm, setTaskForm] = useState({
    title: '', description: '', deadline: '',
    assignedToEmail: '', eventId: id });
  const [memberEmail, setMemberEmail] = useState('');

  useEffect(() => {
    fetchAll();
  }, [id]);

  const fetchAll = async () => {
    const [ev, exp, tsk, tot] = await Promise.all([
      api.get(`/events/${id}`),
      api.get(`/expenses/event/${id}`),
      api.get(`/tasks/event/${id}`),
      api.get(`/expenses/event/${id}/total`),
    ]);
    setEvent(ev.data);
    setExpenses(exp.data);
    setTasks(tsk.data);
    setTotal(tot.data);
  };

  const addExpense = async (e) => {
    e.preventDefault();
    await api.post('/expenses', expenseForm);
    setExpenseForm({ title: '', amount: '', category: '', eventId: id });
    fetchAll();
  };

  const deleteExpense = async (expId) => {
    await api.delete(`/expenses/${expId}`);
    fetchAll();
  };

  const addTask = async (e) => {
    e.preventDefault();
    await api.post('/tasks', taskForm);
    setTaskForm({ title: '', description: '', deadline: '', assignedToEmail: '', eventId: id });
    fetchAll();
  };

  const updateTaskStatus = async (taskId, status) => {
    await api.patch(`/tasks/${taskId}/status`, { status });
    fetchAll();
  };

  const addMember = async (e) => {
    e.preventDefault();
    await api.post(`/events/${id}/members`, { email: memberEmail });
    setMemberEmail('');
    fetchAll();
  };

  if (!event) return <p style={{ padding: '2rem' }}>Loading...</p>;

  return (
    <div style={styles.page}>
      <button style={styles.back} onClick={() => navigate('/')}>← Back</button>

      <div style={styles.header}>
        <div>
          <h1 style={styles.title}>{event.name}</h1>
          {event.weddingDate && <p style={styles.date}>Wedding: {event.weddingDate}</p>}
          <p style={styles.sub}>{event.memberEmails?.length} members · by {event.createdByName}</p>
        </div>
        <div style={styles.totalBox}>
          <p style={styles.totalLabel}>Total Spent</p>
          <p style={styles.totalAmt}>₹{Number(total).toLocaleString()}</p>
        </div>
      </div>

      {/* Add Member */}
      {user.role === 'ADMIN' && (
        <form onSubmit={addMember} style={styles.inlineForm}>
          <input style={styles.inlineInput} placeholder="Add member by email"
            value={memberEmail} onChange={e => setMemberEmail(e.target.value)} required />
          <button style={styles.inlineBtn} type="submit">Add Member</button>
        </form>
      )}

      {/* Tabs */}
      <div style={styles.tabs}>
        <button style={tab === 'expenses' ? styles.tabActive : styles.tab}
          onClick={() => setTab('expenses')}>Expenses ({expenses.length})</button>
        <button style={tab === 'tasks' ? styles.tabActive : styles.tab}
          onClick={() => setTab('tasks')}>Tasks ({tasks.length})</button>
      </div>

      {/* Expenses Tab */}
      {tab === 'expenses' && (
        <div>
          <form onSubmit={addExpense} style={styles.form}>
            <div style={styles.formRow}>
              <input style={styles.input} placeholder="Title"
                value={expenseForm.title}
                onChange={e => setExpenseForm({...expenseForm, title: e.target.value})} required />
              <input style={styles.input} type="number" placeholder="Amount (₹)"
                value={expenseForm.amount}
                onChange={e => setExpenseForm({...expenseForm, amount: e.target.value})} required />
              <select style={styles.input} value={expenseForm.category}
                onChange={e => setExpenseForm({...expenseForm, category: e.target.value})}>
                <option value="">Category</option>
                {['Catering','Decor','Photography','Venue','Music','Attire','Transport','Other']
                  .map(c => <option key={c} value={c}>{c}</option>)}
              </select>
              <button style={styles.addBtn} type="submit">Add</button>
            </div>
          </form>

          <table style={styles.table}>
            <thead>
              <tr style={styles.thead}>
                <th style={styles.th}>Title</th>
                <th style={styles.th}>Category</th>
                <th style={styles.th}>Amount</th>
                <th style={styles.th}>Added By</th>
                <th style={styles.th}>Action</th>
              </tr>
            </thead>
            <tbody>
              {expenses.map(exp => (
                <tr key={exp.id} style={styles.tr}>
                  <td style={styles.td}>{exp.title}</td>
                  <td style={styles.td}>
                    <span style={styles.badge}>{exp.category || '—'}</span>
                  </td>
                  <td style={styles.td}>₹{Number(exp.amount).toLocaleString()}</td>
                  <td style={styles.td}>{exp.addedByName}</td>
                  <td style={styles.td}>
                    <button style={styles.deleteBtn}
                      onClick={() => deleteExpense(exp.id)}>Delete</button>
                  </td>
                </tr>
              ))}
              {expenses.length === 0 && (
                <tr><td colSpan={5} style={styles.empty}>No expenses yet</td></tr>
              )}
            </tbody>
          </table>
        </div>
      )}

      {/* Tasks Tab */}
      {tab === 'tasks' && (
        <div>
          <form onSubmit={addTask} style={styles.form}>
            <div style={styles.formRow}>
              <input style={styles.input} placeholder="Task title"
                value={taskForm.title}
                onChange={e => setTaskForm({...taskForm, title: e.target.value})} required />
              <input style={styles.input} placeholder="Assign to (email)"
                value={taskForm.assignedToEmail}
                onChange={e => setTaskForm({...taskForm, assignedToEmail: e.target.value})} />
              <input style={styles.input} type="date"
                value={taskForm.deadline}
                onChange={e => setTaskForm({...taskForm, deadline: e.target.value})} />
              <button style={styles.addBtn} type="submit">Add</button>
            </div>
          </form>

          <div style={styles.taskList}>
            {tasks.map(task => (
              <div key={task.id} style={styles.taskCard}>
                <div style={styles.taskLeft}>
                  <p style={styles.taskTitle}>{task.title}</p>
                  {task.description && <p style={styles.taskDesc}>{task.description}</p>}
                  <p style={styles.taskMeta}>
                    {task.assignedToName && `Assigned: ${task.assignedToName} · `}
                    {task.deadline && `Due: ${task.deadline}`}
                  </p>
                </div>
                <select
                  style={{...styles.statusSelect,
                    background: task.status === 'DONE' ? '#e6f7ee'
                      : task.status === 'IN_PROGRESS' ? '#fff7e6' : '#f5f5f5'}}
                  value={task.status}
                  onChange={e => updateTaskStatus(task.id, e.target.value)}>
                  <option value="PENDING">Pending</option>
                  <option value="IN_PROGRESS">In Progress</option>
                  <option value="DONE">Done</option>
                </select>
              </div>
            ))}
            {tasks.length === 0 && <p style={styles.empty}>No tasks yet</p>}
          </div>
        </div>
      )}
    </div>
  );
}

const styles = {
  page: { maxWidth: '960px', margin: '0 auto', padding: '2rem' },
  back: { background: 'none', border: 'none', cursor: 'pointer',
    color: '#6c63ff', fontSize: '0.95rem', marginBottom: '1rem', padding: 0 },
  header: { display: 'flex', justifyContent: 'space-between',
    alignItems: 'flex-start', marginBottom: '1.5rem' },
  title: { margin: '0 0 0.25rem', fontSize: '1.6rem', fontWeight: 700 },
  date: { margin: '0 0 0.25rem', color: '#6c63ff', fontSize: '0.9rem' },
  sub: { margin: 0, color: '#888', fontSize: '0.85rem' },
  totalBox: { background: '#6c63ff', color: '#fff', padding: '1rem 1.5rem',
    borderRadius: '12px', textAlign: 'right' },
  totalLabel: { margin: '0 0 0.25rem', fontSize: '0.8rem', opacity: 0.85 },
  totalAmt: { margin: 0, fontSize: '1.4rem', fontWeight: 700 },
  inlineForm: { display: 'flex', gap: '0.5rem', marginBottom: '1.5rem' },
  inlineInput: { flex: 1, padding: '0.6rem', border: '1px solid #ddd',
    borderRadius: '8px', fontSize: '0.9rem' },
  inlineBtn: { padding: '0.6rem 1rem', background: '#6c63ff', color: '#fff',
    border: 'none', borderRadius: '8px', cursor: 'pointer', whiteSpace: 'nowrap' },
  tabs: { display: 'flex', gap: '0.5rem', marginBottom: '1.5rem',
    borderBottom: '2px solid #eee', paddingBottom: '0' },
  tab: { padding: '0.6rem 1.25rem', border: 'none', background: 'none',
    cursor: 'pointer', fontSize: '0.95rem', color: '#666', borderBottom: '2px solid transparent' },
  tabActive: { padding: '0.6rem 1.25rem', border: 'none', background: 'none',
    cursor: 'pointer', fontSize: '0.95rem', color: '#6c63ff', fontWeight: 600,
    borderBottom: '2px solid #6c63ff' },
  form: { background: '#f9f9f9', padding: '1rem', borderRadius: '8px', marginBottom: '1rem' },
  formRow: { display: 'flex', gap: '0.5rem', flexWrap: 'wrap', alignItems: 'flex-end' },
  input: { flex: 1, minWidth: '140px', padding: '0.65rem', border: '1px solid #ddd',
    borderRadius: '8px', fontSize: '0.9rem' },
  addBtn: { padding: '0.65rem 1.25rem', background: '#6c63ff', color: '#fff',
    border: 'none', borderRadius: '8px', cursor: 'pointer' },
  table: { width: '100%', borderCollapse: 'collapse' },
  thead: { background: '#f5f5f5' },
  th: { padding: '0.75rem 1rem', textAlign: 'left', fontSize: '0.85rem',
    fontWeight: 600, color: '#555' },
  tr: { borderBottom: '1px solid #f0f0f0' },
  td: { padding: '0.75rem 1rem', fontSize: '0.9rem' },
  badge: { background: '#f0edff', color: '#6c63ff', padding: '0.2rem 0.6rem',
    borderRadius: '20px', fontSize: '0.8rem' },
  deleteBtn: { padding: '0.3rem 0.75rem', background: '#fff0f0', color: '#e53e3e',
    border: '1px solid #fcc', borderRadius: '6px', cursor: 'pointer', fontSize: '0.8rem' },
  empty: { textAlign: 'center', color: '#aaa', padding: '2rem' },
  taskList: { display: 'flex', flexDirection: 'column', gap: '0.75rem' },
  taskCard: { display: 'flex', justifyContent: 'space-between', alignItems: 'center',
    background: '#fff', padding: '1rem 1.25rem', borderRadius: '10px',
    border: '1px solid #eee', boxShadow: '0 1px 4px rgba(0,0,0,0.05)' },
  taskLeft: { flex: 1 },
  taskTitle: { margin: '0 0 0.2rem', fontWeight: 600, fontSize: '0.95rem' },
  taskDesc: { margin: '0 0 0.2rem', fontSize: '0.85rem', color: '#666' },
  taskMeta: { margin: 0, fontSize: '0.8rem', color: '#999' },
  statusSelect: { padding: '0.4rem 0.75rem', border: '1px solid #ddd',
    borderRadius: '8px', fontSize: '0.85rem', cursor: 'pointer' },
};