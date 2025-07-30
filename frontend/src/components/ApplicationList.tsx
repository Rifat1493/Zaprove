import React, { useState, useEffect } from 'react';

type UserRole = 'ADMIN' | 'CUSTOMER' | 'CO' | 'RO' | 'MANAGER';

interface Application {
  id: number;
  applicationId: string;
  customerName: string;
  applicationType: string;
  amount: number;
  status: string;
  description: string;
}

interface ApplicationListProps {
  userRole: UserRole;
}

const ApplicationList: React.FC<ApplicationListProps> = ({ userRole }) => {
  const [applications, setApplications] = useState<Application[]>([]);

  useEffect(() => {
    // TODO: Fetch applications based on userRole from backend API
    // For now, using dummy data
    const dummyApplications = [
      {
        id: 1,
        applicationId: 'APP001',
        customerName: 'John Doe',
        applicationType: 'Personal Loan',
        amount: 10000,
        status: 'PENDING_CO_REVIEW',
        description: 'Loan for home renovation.',
      },
      {
        id: 2,
        applicationId: 'APP002',
        customerName: 'Jane Smith',
        applicationType: 'Business Loan',
        amount: 50000,
        status: 'PENDING_RO_REVIEW',
        description: 'Loan for new business venture.',
      },
      {
        id: 3,
        applicationId: 'APP003',
        customerName: 'Peter Jones',
        applicationType: 'Car Loan',
        amount: 25000,
        status: 'PENDING_MANAGER_REVIEW',
        description: 'Loan for purchasing a new car.',
      },
    ];
    setApplications(dummyApplications);
  }, [userRole]);

  const handleSubmitVerdict = (applicationId: string, verdict: string) => {
    console.log(`Submitting verdict for ${applicationId}: ${verdict}`);
    // TODO: Integrate with backend API to submit verdict
    alert(`Verdict ${verdict} submitted for application ${applicationId}`);
  };

  return (
    <div className="max-w-4xl mx-auto mt-8 p-6 bg-white rounded-lg shadow-md">
      <h2 className="text-2xl font-bold mb-6 text-gray-900">Applications for {userRole}</h2>
      {applications.length === 0 ? (
        <p className="text-gray-500">No applications to display.</p>
      ) : (
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Application ID</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Customer Name</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Type</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Amount</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {applications.map((app) => (
                <tr key={app.id}>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{app.applicationId}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{app.customerName}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{app.applicationType}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${app.amount.toLocaleString()}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{app.status}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    <button
                      onClick={() => handleSubmitVerdict(app.applicationId, 'APPROVED')}
                      className="text-indigo-600 hover:text-indigo-900 mr-2"
                    >
                      Approve
                    </button>
                    <button
                      onClick={() => handleSubmitVerdict(app.applicationId, 'REJECTED')}
                      className="text-red-600 hover:text-red-900"
                    >
                      Reject
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default ApplicationList;
