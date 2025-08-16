import React, { useState, useEffect } from "react";
import { ACTIVE_APPLICATIONS_URL, DECISIONS_URL } from "../constants/api";

interface ApplicationData {
  applicationId: number;
  amount: number;
  description: string;
  status: string;
}

interface ManagerApplicationDTO {
  application: ApplicationData;
  coDecision: string | null;
  roDecision: string | null;
}

type Application = ApplicationData & {
  coDecision?: string | null;
  roDecision?: string | null;
};

const ActiveApplicationsPage: React.FC = () => {
  const [applications, setApplications] = useState<Application[]>([]);
  const [message, setMessage] = useState("");
  const [selectedApplication, setSelectedApplication] = useState<Application | null>(null);
  const [decision, setDecision] = useState("APPROVED");
  const [comments, setComments] = useState("");
  const [verdictMessage, setVerdictMessage] = useState("");
  const [userRole, setUserRole] = useState<string | null>(null);

  useEffect(() => {
    setUserRole(localStorage.getItem("role"));
    fetchApplications();
  }, []);

  const fetchApplications = async () => {
    setMessage("Loading active applications...");
    const token = localStorage.getItem("token");
    const userId = localStorage.getItem("userId");
    const role = localStorage.getItem("role");

    if (!token || !userId || !role) {
      setMessage("User data not found. Please log in.");
      return;
    }

    let roleEndpoint = "";
    if (role === "CO") roleEndpoint = "credit-officer";
    else if (role === "RO") roleEndpoint = "risk-officer";
    else if (role === "MANAGER") roleEndpoint = "manager";
    else {
      setMessage("Invalid user role for this page.");
      return;
    }

    try {
      const response = await fetch(`${ACTIVE_APPLICATIONS_URL}/${roleEndpoint}/${userId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!response.ok) {
        const errorData = await response.json();
        setMessage(`Failed to fetch applications: ${errorData.message || response.statusText}`);
        return;
      }

      const data = await response.json();

      if (role === "MANAGER") {
        setApplications(
          data.map((dto: ManagerApplicationDTO) => ({
            ...dto.application,
            coDecision: dto.coDecision,
            roDecision: dto.roDecision,
          }))
        );
      } else {
        setApplications(data);
      }

      setMessage(data.length === 0 ? "No active applications found." : "");
    } catch (error) {
      setMessage(`An error occurred: ${error instanceof Error ? error.message : String(error)}`);
    }
  };

  const handleOpenVerdictModal = (application: Application) => {
    setSelectedApplication(application);
    setDecision("APPROVED");
    setComments("");
    setVerdictMessage("");
  };

  const handleCloseVerdictModal = () => {
    setSelectedApplication(null);
  };

  const handleVerdictSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    if (!selectedApplication) return;

    setVerdictMessage("Submitting verdict...");
    const token = localStorage.getItem("token");

    let verdictUrl = "";
    if (userRole === "CO") {
      verdictUrl = `${DECISIONS_URL}/credit-officer/${selectedApplication.applicationId}`;
    } else if (userRole === "RO") {
      verdictUrl = `${DECISIONS_URL}/risk-officer/${selectedApplication.applicationId}`;
    } else if (userRole === "MANAGER") {
      verdictUrl = `${DECISIONS_URL}/manager/${selectedApplication.applicationId}`;
    }

    try {
      const response = await fetch(verdictUrl, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ decision, comments }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        setVerdictMessage(`Failed: ${errorData.message || response.statusText}`);
        return;
      }

      setVerdictMessage("Verdict submitted successfully!");
      fetchApplications();
      setTimeout(handleCloseVerdictModal, 2000);
    } catch (error) {
      setVerdictMessage(`An error occurred: ${error instanceof Error ? error.message : String(error)}`);
    }
  };

  return (
    <div className="p-5">
      <h2 className="mb-5">Active Loan Applications</h2>
      {message && <p>{message}</p>}
      <table className="w-full border-collapse">
        <thead>
          <tr>
            <th className="border border-gray-300 p-2 text-left">Application ID</th>
            <th className="border border-gray-300 p-2 text-left">Amount</th>
            <th className="border border-gray-300 p-2 text-left">Purpose</th>
            <th className="border border-gray-300 p-2 text-left">Status</th>
            {userRole === "MANAGER" && (
              <>
                <th className="border border-gray-300 p-2 text-left">CO Decision</th>
                <th className="border border-gray-300 p-2 text-left">RO Decision</th>
              </>
            )}
            <th className="border border-gray-300 p-2 text-left">Action</th>
          </tr>
        </thead>
        <tbody>
          {applications.map((app) => (
            <tr key={app.applicationId}>
              <td className="border border-gray-300 p-2">{app.applicationId}</td>
              <td className="border border-gray-300 p-2">{app.amount}</td>
              <td className="border border-gray-300 p-2">{app.description}</td>
              <td className="border border-gray-300 p-2">{app.status}</td>
              {userRole === "MANAGER" && (
                <>
                  <td className="border border-gray-300 p-2">{app.coDecision || "Pending"}</td>
                  <td className="border border-gray-300 p-2">{app.roDecision || "Pending"}</td>
                </>
              )}
              <td className="border border-gray-300 p-2">
                <button
                  className="bg-teal-600 text-white border-none py-2 px-3 rounded cursor-pointer"
                  onClick={() => handleOpenVerdictModal(app)}
                >
                  Submit Verdict
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {selectedApplication && (
        <div className="fixed top-0 left-0 w-full h-full bg-black bg-opacity-50 flex justify-center items-center">
          <div className="bg-white p-5 rounded-lg w-96">
            <h3>Submit Verdict for Application #{selectedApplication.applicationId}</h3>
            <form onSubmit={handleVerdictSubmit}>
              <div className="mb-3">
                <label htmlFor="decision">Decision:</label>
                <select
                  id="decision"
                  value={decision}
                  onChange={(e) => setDecision(e.target.value)}
                  required
                  className="w-full p-2 rounded border border-gray-300"
                >
                  <option value="APPROVED">Approve</option>
                  <option value="REJECTED">Reject</option>
                </select>
              </div>
              <div className="mb-3">
                <label htmlFor="comments">Comments:</label>
                <textarea
                  id="comments"
                  value={comments}
                  onChange={(e) => setComments(e.target.value)}
                  placeholder="Enter your comments..."
                  required
                  className="w-full min-h-24 p-2.5 rounded border border-gray-300"
                />
              </div>
              <div className="flex justify-end gap-3">
                <button
                  type="button"
                  onClick={handleCloseVerdictModal}
                  className="bg-red-500 text-white border-none py-2 px-3 rounded cursor-pointer"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="bg-teal-600 text-white border-none py-2 px-3 rounded cursor-pointer"
                >
                  Submit
                </button>
              </div>
            </form>
            {verdictMessage && <p className="mt-3">{verdictMessage}</p>}
          </div>
        </div>
      )}
    </div>
  );
};

export default ActiveApplicationsPage;
