import React, { useState } from 'react';
import { LOAN_APPLICATION_URL } from '../constants/api';

const LoanApplicationPage: React.FC = () => {
  const [amount, setAmount] = useState('');
  const [purpose, setPurpose] = useState('');
  const [term, setTerm] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setMessage('Submitting loan application...');

    const token = localStorage.getItem('token');
    if (!token) {
      setMessage('Authentication token not found. Please log in.');
      return;
    }

    try {
      const response = await fetch(LOAN_APPLICATION_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({
          amount: parseFloat(amount),
          purpose,
          term: parseInt(term, 10),
        }),
      });

      if (response.ok) {
        setMessage('Loan application submitted successfully!');
        setAmount('');
        setPurpose('');
        setTerm('');
      } else {
        const errorData = await response.json();
        setMessage(`Loan application failed: ${errorData.message || response.statusText}`);
      }
    } catch (error) {
      setMessage(`An error occurred: ${error instanceof Error ? error.message : String(error)}`);
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', minHeight: '100vh', backgroundColor: '#f0f2f5' }}>
      <div style={{ padding: '40px', borderRadius: '8px', boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)', backgroundColor: '#ffffff', width: '400px', textAlign: 'center' }}>
        <h2 style={{ marginBottom: '30px', color: '#333' }}>Loan Application</h2>
        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          <input
            type="number"
            placeholder="Loan Amount"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            required
            style={{ padding: '12px', borderRadius: '4px', border: '1px solid #ddd', fontSize: '16px' }}
          />
          <input
            type="text"
            placeholder="Purpose of Loan"
            value={purpose}
            onChange={(e) => setPurpose(e.target.value)}
            required
            style={{ padding: '12px', borderRadius: '4px', border: '1px solid #ddd', fontSize: '16px' }}
          />
          <input
            type="number"
            placeholder="Loan Term (in months)"
            value={term}
            onChange={(e) => setTerm(e.target.value)}
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
            Submit Application
          </button>
        </form>
        {message && <p style={{ marginTop: '20px', color: message.includes('failed') || message.includes('error') ? '#dc3545' : '#28a745' }}>{message}</p>}
      </div>
    </div>
  );
};

export default LoanApplicationPage;