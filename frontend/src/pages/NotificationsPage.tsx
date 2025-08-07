import React, { useState, useEffect } from 'react';
import { NOTIFICATIONS_URL } from '../constants/api';

interface Notification {
  id: number;
  message: string;
  status: string;
  createdAt: string;
}

const NotificationsPage: React.FC = () => {
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [message, setMessage] = useState('');

  useEffect(() => {
    const fetchNotifications = async () => {
      setMessage('Loading notifications...');
      const token = localStorage.getItem('token');
      const userId = localStorage.getItem('userId');
      const userRole = localStorage.getItem('role');

      if (!token || !userId || !userRole) {
        setMessage('User information not found. Please log in.');
        return;
      }

      try {
        const response = await fetch(`${NOTIFICATIONS_URL}/by-user/${userId}/${userRole}`, {
          headers: {
            'Authorization': `Bearer ${token}`,
          },
        });

        if (response.ok) {
          const data = await response.json();
          setNotifications(data);
          setMessage(data.length === 0 ? 'No notifications found.' : '');
        } else {
          const errorData = await response.json();
          setMessage(`Failed to fetch notifications: ${errorData.message || response.statusText}`);
        }
      } catch (error) {
        setMessage(`An error occurred: ${error instanceof Error ? error.message : String(error)}`);
      }
    };

    fetchNotifications();
  }, []);

  return (
    <div className="p-5">
      <h2 className="mb-5">Notifications</h2>
      {message && <p>{message}</p>}
      <div className="flex flex-col gap-4">
        {notifications.map((notification) => (
          <div key={notification.id} className="p-4 rounded-lg shadow-md bg-white">
            <p className="m-0 font-bold">{notification.message}</p>
            <p className="mt-1 text-sm text-gray-600">Status: {notification.status}</p>
            <p className="mt-1 text-xs text-gray-500">{new Date(notification.createdAt).toLocaleString()}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default NotificationsPage;