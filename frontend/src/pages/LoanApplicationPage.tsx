import React, { useState } from 'react';
import { LOAN_APPLICATION_URL } from '../constants/api';

const LoanApplicationPage: React.FC = () => {
  const [applicationType, setApplicationType] = useState('CAR_LOAN');
  const [amount, setAmount] = useState('');
  const [description, setDescription] = useState('');
  const [applicationData, setApplicationData] = useState('Salaried');
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
          applicationType,
          amount: parseFloat(amount),
          description,
          applicationData,
        }),
      });

      if (response.ok) {
        setMessage('Loan application submitted successfully!');
        setApplicationType('CAR_LOAN');
        setAmount('');
        setDescription('');
        setApplicationData('Salaried');
      } else {
        const errorData = await response.json();
        setMessage(`Loan application failed: ${errorData.message || response.statusText}`);
      }
    } catch (error) {
      setMessage(`An error occurred: ${error instanceof Error ? error.message : String(error)}`);
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <div className="p-10 rounded-lg shadow-lg bg-white w-96 text-center">
        <h2 className="mb-8 text-gray-800">Loan Application</h2>
        <form onSubmit={handleSubmit} className="flex flex-col gap-5">
          <select
            value={applicationType}
            onChange={(e) => setApplicationType(e.target.value)}
            required
            className="p-3 rounded border border-gray-300 text-base"
          >
            <option value="CAR_LOAN">Car Loan</option>
            <option value="HOME_LOAN">Home Loan</option>
            <option value="PERSONAL_LOAN">Personal Loan</option>
          </select>
          <input
            type="number"
            placeholder="Loan Amount"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            required
            className="p-3 rounded border border-gray-300 text-base"
          />
          <input
            type="text"
            placeholder="Description"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            required
            className="p-3 rounded border border-gray-300 text-base"
          />
          <select
            value={applicationData}
            onChange={(e) => setApplicationData(e.target.value)}
            required
            className="p-3 rounded border border-gray-300 text-base"
          >
            <option value="Salaried">Salaried</option>
            <option value="Business">Business</option>
          </select>
          <button
            type="submit"
            className="py-3 px-5 rounded border-none bg-teal-600 text-white text-base cursor-pointer transition-colors duration-300 hover:bg-teal-700"
          >
            Submit Application
          </button>
        </form>
        {message && <p className={`mt-5 ${message.includes('failed') || message.includes('error') ? 'text-red-500' : 'text-green-500'}`}>{message}</p>}
      </div>
    </div>
  );
};

export default LoanApplicationPage;