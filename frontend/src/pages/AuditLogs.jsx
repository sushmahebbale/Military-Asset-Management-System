import { useEffect, useState } from "react";
import API_URL from "../api";

function AuditLogs() {
  const [logs, setLogs] = useState([]);

  useEffect(() => {
    fetch(`${API_URL}/audit-logs`, {
      credentials: "include",
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to load audit logs");
        }

        return response.json();
      })
      .then((data) => {
        setLogs(data);
      })
      .catch((error) => {
        console.error("Error loading audit logs:", error);
      });
  }, []);

  return (
    <div>
      <h2>Audit Logs</h2>
      <p>Track important actions performed in the system.</p>

      <table className="data-table">
        <thead>
          <tr>
            <th>Date & Time</th>
            <th>User</th>
            <th>Action</th>
            <th>Details</th>
          </tr>
        </thead>

        <tbody>
          {logs.length === 0 ? (
            <tr>
              <td colSpan="4">No audit logs found</td>
            </tr>
          ) : (
            logs.map((log) => (
              <tr key={log.id}>
                <td>{log.date}</td>
                <td>{log.username}</td>
                <td>{log.action}</td>
                <td>{log.details}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

export default AuditLogs;