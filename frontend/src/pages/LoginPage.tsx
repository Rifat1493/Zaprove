import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { LOGIN_URL } from '../constants/api';

const LoginPage: React.FC = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    // If user is already logged in, redirect to home
    if (localStorage.getItem('token')) {
      navigate('/');
    }
  }, [navigate]);

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setMessage('Logging in...');

    try {
      const response = await fetch(LOGIN_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      });

      if (response.ok) {
        const data = await response.json();
        setMessage(`Login successful!`);
        localStorage.setItem('token', data.token);
        localStorage.setItem('role', data.role); 
        localStorage.setItem('userId', data.userId);
        
        // Conditional routing based on role
        switch (data.role) {
          case 'ADMIN':
            navigate('/create-user');
            break;
          case 'CUSTOMER':
            navigate('/loan-application');
            break;
          case 'CO':
            navigate('/active-applications');
            break;
          case 'RO':
            navigate('/active-applications');
            break;
          case 'MANAGER':
            navigate('/active-applications');
            break;
          default:
            navigate('/');
            break;
        }
      } else {
        const errorData = await response.json();
        setMessage(`Login failed: ${errorData.message || response.statusText}`);
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        localStorage.removeItem('userId');
      }
    } catch (error) {
      setMessage(`An error occurred: ${error instanceof Error ? error.message : String(error)}`);
      localStorage.removeItem('token');
      localStorage.removeItem('role');
      localStorage.removeItem('userId');
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', height: '100vh', backgroundColor: '#f0f2f5' }}>
      <div style={{ padding: '40px', borderRadius: '8px', boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)', backgroundColor: '#ffffff', width: '350px', textAlign: 'center' }}>
        <h2 style={{ marginBottom: '30px', color: '#333' }}>Login</h2>
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
          <button
            type="submit"
            style={{
              padding: '12px 20px',
              borderRadius: '4px',
              border: 'none',
              backgroundColor: '#007bff',
              color: 'white',
              fontSize: '16px',
              cursor: 'pointer',
              transition: 'background-color 0.3s ease'
            }}
            onMouseOver={(e) => (e.currentTarget.style.backgroundColor = '#0056b3')}
            onMouseOut={(e) => (e.currentTarget.style.backgroundColor = '#007bff')}
          >
            Login
          </button>
        </form>
        {message && <p style={{ marginTop: '20px', color: message.includes('failed') || message.includes('error') ? '#dc3545' : '#28a745' }}>{message}</p>}
      </div>
    </div>
  );
};

export default LoginPage;
