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
        let path;
        switch (data.role) {
          case 'ADMIN':
            path = '/create-user';
            break;
          case 'CUSTOMER':
            path = '/loan-application';
            break;
          case 'CO':
          case 'RO':
          case 'MANAGER':
            path = '/active-applications';
            break;
          default:
            path = '/';
            break;
        }
        window.location.href = path;
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
    <div className="flex flex-col items-center justify-center h-screen bg-gray-100">
      <div className="p-10 rounded-lg shadow-lg bg-white w-96 text-center">
        <h2 className="mb-8 text-gray-800">Login</h2>
        <form onSubmit={handleSubmit} className="flex flex-col gap-5">
          <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
            className="p-3 rounded border border-gray-300 text-base"
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            className="p-3 rounded border border-gray-300 text-base"
          />
          <button
            type="submit"
            className="py-3 px-5 rounded border-none bg-teal-600 text-white text-base cursor-pointer transition-colors duration-300 hover:bg-teal-700"
          >
            Login
          </button>
        </form>
        {message && <p className={`mt-5 ${message.includes('failed') || message.includes('error') ? 'text-red-500' : 'text-green-500'}`}>{message}</p>}
      </div>
    </div>
  );
};

export default LoginPage;
