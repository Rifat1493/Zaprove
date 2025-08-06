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
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <div className="p-10 rounded-lg shadow-lg bg-white w-96 text-center">
        <h2 className="mb-8 text-gray-800">Create User</h2>
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
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            className="p-3 rounded border border-gray-300 text-base"
          />
          {/* <input
            type="text"
            placeholder="Full Name"
            value={fullName}
            onChange={(e) => setFullName(e.target.value)}
            required
            className="p-3 rounded border border-gray-300 text-base"
          /> */}
          <select
            value={role}
            onChange={(e) => setRole(e.target.value)}
            required
            className="p-3 rounded border border-gray-300 text-base"
          >
            <option value="CUSTOMER">CUSTOMER</option>
            <option value="CO">CO</option>
            <option value="RO">RO</option>
            <option value="MANAGER">MANAGER</option>
          </select>
          <button
            type="submit"
            className="py-3 px-5 rounded border-none bg-teal-600 text-white text-base cursor-pointer transition-colors duration-300 hover:bg-teal-700"
          >
            Create User
          </button>
        </form>
        {message && <p className={`mt-5 ${message.includes('failed') || message.includes('error') ? 'text-red-500' : 'text-green-500'}`}>{message}</p>}
      </div>
    </div>
  );
};

export default CreateUserPage;
