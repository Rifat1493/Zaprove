import React, { useState, useEffect } from 'react';
import { ACTIVE_APPLICATIONS_URL, DECISIONS_URL } from '../constants/api';

interface Application {
  id: number;
  amount: number;
  purpose: string;
  status: string;
}

const ActiveApplicationsPage: React.FC = () => {
  const [applications, setApplications] = useState<Application[]>([]);
  const [message, setMessage] = useState('');
  const [selectedApplication, setSelectedApplication] = useState<Application | null>(null);
  const [decision, setDecision] = useState('APPROVED');
  const [comments, setComments] = useState('');
  const [verdictMessage, setVerdictMessage] = useState('');

  const fetchApplications = async () => {
    setMessage('Loading active applications...');
    const token = localStorage.getItem('token');
    const userId = localStorage.getItem('userId');
    const userRole = localStorage.getItem('role');

    if (!token || !userId || !userRole) {
      setMessage('User data not found. Please log in.');
      return;
    }

    let roleEndpoint = '';
    if (userRole === 'CO') {
      roleEndpoint = 'credit-officer';
    } else if (userRole === 'RO') {
      roleEndpoint = 'risk-officer';
    } else if (userRole === 'MANAGER') {
      roleEndpoint = 'manager';
    } else {
      setMessage('Invalid user role for this page.');
      return;
    }

    try {
      const response = await fetch(`${ACTIVE_APPLICATIONS_URL}/${roleEndpoint}/${userId}`, {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });

      if (response.ok) {
        const data = await response.json();
        setApplications(data);
        setMessage(data.length === 0 ? 'No active applications found.' : '');
      } else {
        const errorData = await response.json();
        setMessage(`Failed to fetch applications: ${errorData.message || response.statusText}`);
      }
    } catch (error) {
      setMessage(`An error occurred: ${error instanceof Error ? error.message : String(error)}`);
    }
  };

  useEffect(() => {
    fetchApplications();
  }, []);

  const handleOpenVerdictModal = (application: Application) => {
    setSelectedApplication(application);
    setDecision('APPROVED');
    setComments('');
    setVerdictMessage('');
  };

  const handleCloseVerdictModal = () => {
    setSelectedApplication(null);
  };

  const handleVerdictSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    if (!selectedApplication) return;

    setVerdictMessage('Submitting verdict...');
    const token = localStorage.getItem('token');
    const userRole = localStorage.getItem('role');

    let roleEndpoint = '';
    if (userRole === 'CREDIT_OFFICER') {
      roleEndpoint = 'credit-officer';
    } else if (userRole === 'RISK_OFFICER') {
      roleEndpoint = 'risk-officer';
    } else if (userRole === 'MANAGER') {
      roleEndpoint = 'manager';
    }

    try {
      const response = await fetch(`${DECISIONS_URL}/${roleEndpoint}/${selectedApplication.id}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({
          decision,
          comments,
        }),
      });

      if (response.ok) {
        setVerdictMessage('Verdict submitted successfully!');
        fetchApplications(); // Refresh the list
        setTimeout(handleCloseVerdictModal, 2000);
      } else {
        const errorData = await response.json();
        setVerdictMessage(`Failed to submit verdict: ${errorData.message || response.statusText}`);
      }
    } catch (error) {
      setVerdictMessage(`An error occurred: ${error instanceof Error ? error.message : String(error)}`);
    }
  };

  return (
    <div style={{ padding: '20px' }}>
      <h2 style={{ marginBottom: '20px' }}>Active Loan Applications</h2>
      {message && <p>{message}</p>}
      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>Application ID</th>
            <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>Amount</th>
            <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>Purpose</th>
            <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>Status</th>
            <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>Action</th>
          </tr>
        </thead>
        <tbody>
          {applications.map((app) => (
            <tr key={app.id}>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{app.id}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{app.amount}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{app.purpose}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{app.status}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>
                <button onClick={() => handleOpenVerdictModal(app)}>Submit Verdict</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {selectedApplication && (
        <div style={{ position: 'fixed', top: 0, left: 0, width: '100%', height: '100%', backgroundColor: 'rgba(0,0,0,0.5)', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <div style={{ backgroundColor: 'white', padding: '20px', borderRadius: '8px', width: '400px' }}>
            <h3>Submit Verdict for Application #{selectedApplication.id}</h3>
            <form onSubmit={handleVerdictSubmit}>
              <div style={{ marginBottom: '10px' }}>
                <label htmlFor="decision">Decision:</label>
                <select
                  id="decision"
                  value={decision}
                  onChange={(e) => setDecision(e.target.value)}
                  required
                  style={{ width: '100%', padding: '8px', borderRadius: '4px', border: '1px solid #ddd' }}
                >
                  <option value="APPROVED">Approve</option>
                  <option value="REJECTED">Reject</option>
                </select>
              </div>
              <div style={{ marginBottom: '10px' }}>
                <label htmlFor="comments">Comments:</label>
                <textarea
                  id="comments"
                  value={comments}
                  onChange={(e) => setComments(e.target.value)}
                  placeholder="Enter your comments..."
                  required
                  style={{ width: '100%', minHeight: '100px', padding: '10px', borderRadius: '4px', border: '1px solid #ddd' }}
                />
              </div>
              <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '10px' }}>
                <button type="button" onClick={handleCloseVerdictModal}>Cancel</button>
                <button type="submit">Submit</button>
              </div>
            </form>
            {verdictMessage && <p style={{ marginTop: '10px' }}>{verdictMessage}</p>}
          </div>
        </div>
      )}
    </div>
  );
};

export default ActiveApplicationsPage;