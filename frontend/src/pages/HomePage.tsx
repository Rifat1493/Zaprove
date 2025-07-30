import React, { useState } from 'react';
import CreateUserForm from '../components/CreateUserForm';
import CreateLoanApplicationForm from '../components/CreateLoanApplicationForm';
import ApplicationList from '../components/ApplicationList';

type UserRole = 'ADMIN' | 'CUSTOMER' | 'CO' | 'RO' | 'MANAGER';

const HomePage: React.FC = () => {
  // Simulate user role for demonstration purposes
  // In a real application, this would come from authentication context
  const [userRole, setUserRole] = useState<UserRole>('ADMIN'); // Change this to test different roles: 'ADMIN', 'CUSTOMER', 'CO', 'RO', 'MANAGER'
  const [showCreateUserForm, setShowCreateUserForm] = useState<boolean>(false);
  const [showCreateLoanForm, setShowCreateLoanForm] = useState<boolean>(false);

  const handleLogout = (e: React.MouseEvent<HTMLButtonElement>) => {
    console.log('Logging out...');
    // TODO: Implement actual logout logic (e.g., clear tokens, redirect to login)
    alert('Logged out!');
  };

  const renderContent = () => {
    switch (userRole) {
      case 'ADMIN':
        return (
          <div className="space-y-8">
            <button
              onClick={() => setShowCreateUserForm(!showCreateUserForm)}
              className="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              {showCreateUserForm ? 'Hide Create User Form' : 'Create User'}
            </button>
            {showCreateUserForm && <CreateUserForm />}
            {/* Admin might also see a list of all applications or other admin-specific dashboards */}
            <ApplicationList userRole={userRole} />
          </div>
        );
      case 'CUSTOMER':
        return (
          <div className="space-y-8">
            <button
              onClick={() => setShowCreateLoanForm(!showCreateLoanForm)}
              className="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"
            >
              {showCreateLoanForm ? 'Hide Loan Application Form' : 'Create Loan Application'}
            </button>
            {showCreateLoanForm && <CreateLoanApplicationForm />}
            {/* Customers might also see their own submitted applications */}
            <ApplicationList userRole={userRole} />
          </div>
        );
      case 'CO':
      case 'RO':
      case 'MANAGER':
        return (
          <ApplicationList userRole={userRole} />
        );
      default:
        return <p className="text-gray-500 text-lg">Welcome! Please select an action.</p>;
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <nav className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex">
              <div className="flex-shrink-0 flex items-center">
                <h1 className="text-xl font-bold text-gray-900">Zaprove - {userRole} Dashboard</h1>
              </div>
            </div>
            <div className="flex items-center">
              <button
                type="button"
                onClick={handleLogout}
                className="ml-3 inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                Logout
              </button>
            </div>
          </div>
        </div>
      </nav>
      <header className="bg-white shadow">
        <div className="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8">
          <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
        </div>
      </header>
      <main>
        <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
          <div className="px-4 py-8 sm:px-0">
            {renderContent()}
          </div>
        </div>
      </main>
    </div>
  );
};

export default HomePage;