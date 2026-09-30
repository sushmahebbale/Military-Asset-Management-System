import { useEffect, useState } from "react";
import API_URL from "../api";

function Users() {
  const [showForm, setShowForm] = useState(false);
  const [users, setUsers] = useState([]);

  const [formData, setFormData] = useState({
    username: "",
    password: "",
    role: "",
    fullName: "",
    assignedBase: "",
  });

  useEffect(() => {
    fetch(`${API_URL}/users`, {
      credentials: "include",
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Unable to load users");
        }
        return response.json();
      })
      .then((data) => {
        setUsers(data);
      })
      .catch((error) => {
        console.error("Error loading users:", error);
      });
  }, []);

  const handleChange = (event) => {
    setFormData({
      ...formData,
      [event.target.name]: event.target.value,
    });
  };

  const handleSave = () => {
    fetch(`${API_URL}/users`, {
      credentials: "include",
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Unable to save user");
        }
        return response.json();
      })
      .then((data) => {
        setUsers([...users, data]);

        setFormData({
          username: "",
          password: "",
          role: "",
          fullName: "",
          assignedBase: "",
        });

        setShowForm(false);
      })
      .catch((error) => {
        console.error("Error saving user:", error);
      });
  };

  return (
    <div>
      <h2>Users</h2>
      <p>Manage system users and their roles.</p>

      <button
        className="primary-button"
        onClick={() => setShowForm(true)}
      >
        + Add User
      </button>

      {showForm && (
        <div className="form-box">
          <h3>Add User</h3>

          <label>Full Name</label>
          <input
            type="text"
            name="fullName"
            value={formData.fullName}
            placeholder="Enter full name"
            onChange={handleChange}
          />

          <label>Username</label>
          <input
            type="text"
            name="username"
            value={formData.username}
            placeholder="Enter username"
            onChange={handleChange}
          />

          <label>Password</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            placeholder="Enter password"
            onChange={handleChange}
          />

          <label>Role</label>
          <select
            name="role"
            value={formData.role}
            onChange={handleChange}
          >
            <option value="">Select Role</option>
            <option value="ADMIN">ADMIN</option>
            <option value="BASE_COMMANDER">BASE_COMMANDER</option>
            <option value="LOGISTICS_OFFICER">
              LOGISTICS_OFFICER
            </option>
          </select>

          <label>Assigned Base</label>
          <input
            type="text"
            name="assignedBase"
            value={formData.assignedBase}
            placeholder="Enter assigned base"
            onChange={handleChange}
          />

          <button className="primary-button" onClick={handleSave}>
            Save User
          </button>

          <button
            className="cancel-button"
            onClick={() => setShowForm(false)}
          >
            Cancel
          </button>
        </div>
      )}

      <table className="data-table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Username</th>
            <th>Role</th>
            <th>Assigned Base</th>
            <th>Status</th>
          </tr>
        </thead>

        <tbody>
          {users.length === 0 ? (
            <tr>
              <td colSpan="5">No users found</td>
            </tr>
          ) : (
            users.map((user) => (
              <tr key={user.id}>
                <td>{user.fullName}</td>
                <td>{user.username}</td>
                <td>{user.role}</td>
                <td>{user.assignedBase || "-"}</td>
                <td>Active</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

export default Users;