import React, { useState } from 'react';
import { CREATE_USER_URL } from '../constants/api';

const CreateUserPage: React.FC = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [email, setEmail] = useState('');
  const [fullName, setFullName] = useState('');
  const [role, setRole] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setMessage('Creating user...');

    // Retrieve the token from localStorage
    const token = localStorage.getItem('token');
    if (!token) {
      setMessage('Authentication token not found. Please log in.');
      return;
    }

    try {
      const response = await fetch(CREATE_USER_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({ username, password, email, role }),
      });

      if (response.ok) {
        setMessage('User created successfully!');
        // Clear form fields
        setUsername('');
        setPassword('');
        setEmail('');
        setFullName('');
        setRole('');
      } else {
        const errorData = await response.json();
        setMessage(`User creation failed: ${errorData.message || response.statusText}`);
      }
    } catch (error) {
      setMessage(`An error occurred: ${error instanceof Error ? error.message : String(error)}`);
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', minHeight: '100vh', backgroundColor: '#f0f2f5' }}>
      <div style={{ padding: '40px', borderRadius: '8px', boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)', backgroundColor: '#ffffff', width: '400px', textAlign: 'center' }}>
        <h2 style={{ marginBottom: '30px', color: '#333' }}>Create User</h2>
        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
            style={{ padding: '12px', borderRadius: '4px', border: '1px solid #ddd', fontSize: '16px' }}
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            style={{ padding: '12px', borderRadius: '4px', border: '1px solid #ddd', fontSize: '16px' }}
          />
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            style={{ padding: '12px', borderRadius: '4px', border: '1px solid #ddd', fontSize: '16px' }}
          />
          {/* <input
            type="text"
            placeholder="Full Name"
            value={fullName}
            onChange={(e) => setFullName(e.target.value)}
            required
            style={{ padding: '12px', borderRadius: '4px', border: '1px solid #ddd', fontSize: '16px' }}
          /> */}
          <select
            value={role}
            onChange={(e) => setRole(e.target.value)}
            required
            style={{ padding: '12px', borderRadius: '4px', border: '1px solid #ddd', fontSize: '16px' }}
          >
            <option value="USER">CUSTOMER</option>
            <option value="ADMIN">CO</option>
            <option value="ADMIN">RO</option>
            <option value="ADMIN">MANAGER</option>
          </select>
          <button
            type="submit"
            style={{
              padding: '12px 20px',
              borderRadius: '4px',
              border: 'none',
              backgroundColor: '#28a745',
              color: 'white',
              fontSize: '16px',
              cursor: 'pointer',
              transition: 'background-color 0.3s ease'
            }}
            onMouseOver={(e) => (e.currentTarget.style.backgroundColor = '#218838')}
            onMouseOut={(e) => (e.currentTarget.style.backgroundColor = '#28a745')}
          >
            Create User
          </button>
        </form>
        {message && <p style={{ marginTop: '20px', color: message.includes('failed') || message.includes('error') ? '#dc3545' : '#28a745' }}>{message}</p>}
      </div>
    </div>
  );
};

export default CreateUserPage;
