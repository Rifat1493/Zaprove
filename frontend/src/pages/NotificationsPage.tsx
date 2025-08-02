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
    <div style={{ padding: '20px' }}>
      <h2 style={{ marginBottom: '20px' }}>Notifications</h2>
      {message && <p>{message}</p>}
      <div style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
        {notifications.map((notification) => (
          <div key={notification.id} style={{ padding: '15px', borderRadius: '8px', boxShadow: '0 2px 8px rgba(0, 0, 0, 0.1)', backgroundColor: '#ffffff' }}>
            <p style={{ margin: 0, fontWeight: 'bold' }}>{notification.message}</p>
            <p style={{ margin: '5px 0 0', fontSize: '14px', color: '#555' }}>Status: {notification.status}</p>
            <p style={{ margin: '5px 0 0', fontSize: '12px', color: '#888' }}>{new Date(notification.createdAt).toLocaleString()}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default NotificationsPage;