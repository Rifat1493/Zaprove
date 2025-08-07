import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import CreateUserPage from './pages/CreateUserPage';
import LoanApplicationPage from './pages/LoanApplicationPage';
import NotificationsPage from './pages/NotificationsPage';
import ActiveApplicationsPage from './pages/ActiveApplicationsPage';
import './App.css';
import { useEffect, useState } from 'react';
import { LOGOUT_URL } from './constants/api';

const App: React.FC = () => {
  const [userRole, setUserRole] = useState<string | null>(null);

  useEffect(() => {
    const role = localStorage.getItem('role');
    setUserRole(role);

    const handleStorageChange = () => {
      setUserRole(localStorage.getItem('role'));
    };

    window.addEventListener('storage', handleStorageChange);
    return () => window.removeEventListener('storage', handleStorageChange);
  }, []);

  const handleLogout = async () => {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        await fetch(LOGOUT_URL, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${token}`,
          },
        });
      } catch (error) {
        console.error('Logout failed:', error);
      }
    }
    
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('userId');
    setUserRole(null);
    window.location.href = '/login';
  };

  return (
    <Router>
      <div className="App">
        <nav className="p-4 bg-teal-600 text-white flex justify-between items-center">
          <Link to="/" className="text-white no-underline text-2xl">Zaprove</Link>
          <div>
            <button onClick={async () => {
              try {
                const response = await fetch('http://localhost:8083/api/v1/test-dis');
                const data = await response.text();
                alert('Test API Response: ' + data);
              } catch (error) {
                console.error('Test API call failed:', error);
                alert('Test API call failed. Check console for details.');
              }
            }} className="bg-teal-600 border-none text-white cursor-pointer text-base mr-4">Test</button>
            {userRole === 'ADMIN' && (
              <Link to="/create-user" className="text-white no-underline mr-4">Create User</Link>
            )}
            {userRole === 'CUSTOMER' && (
              <>
                <Link to="/loan-application" className="text-white no-underline mr-4">Apply for Loan</Link>
                <Link to="/notifications" className="text-white no-underline mr-4">Notifications</Link>
              </>
            )}
            {(userRole === 'CREDIT_OFFICER' || userRole === 'RISK_OFFICER' || userRole === 'MANAGER') && (
              <Link to="/active-applications" className="text-white no-underline mr-4">Active Applications</Link>
            )}
            {localStorage.getItem('token') && (
              <button onClick={handleLogout} className="bg-teal-600 border-none text-white cursor-pointer text-base">Logout</button>
            )}
          </div>
        </nav>
        <main className="p-4">
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/create-user" element={userRole === 'ADMIN' ? <CreateUserPage /> : <p>You are not authorized to view this page.</p>} />
            <Route path="/loan-application" element={userRole === 'CUSTOMER' ? <LoanApplicationPage /> : <p>You are not authorized to view this page.</p>} />
            <Route path="/notifications" element={userRole === 'CUSTOMER' ? <NotificationsPage /> : <p>You are not authorized to view this page.</p>} />
            <Route path="/active-applications" element={(userRole === 'CO' || userRole === 'RO' || userRole === 'MANAGER') ? <ActiveApplicationsPage /> : <p>You are not authorized to view this page.</p>} />
            <Route path="/" element={
              <div>
                <h1>Welcome to Zaprove!</h1>
                {localStorage.getItem('token') ? (
                  <p>You are logged in as {userRole}.</p>
                ) : (
                  <p>Navigate to <Link to="/login">/login</Link> to access the login page.</p>
                )}
              </div>
            } />
          </Routes>
        </main>
      </div>
    </Router>
  );
};

export default App;
