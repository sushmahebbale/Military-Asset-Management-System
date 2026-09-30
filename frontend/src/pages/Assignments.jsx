import { useEffect, useState } from "react";
import API_URL from "../api";

function Assignments() {
  const [showForm, setShowForm] = useState(false);
  const [assignments, setAssignments] = useState([]);

  const [formData, setFormData] = useState({
    date: "",
    equipmentType: "",
    assignedTo: "",
    base: "",
    quantity: "",
  });

  useEffect(() => {
    fetch(`${API_URL}/assignments`, {
      credentials: "include",
    })
      .then((response) => response.json())
      .then((data) => {
        setAssignments(data);
      })
      .catch((error) => {
        console.error("Error loading assignments:", error);
      });
  }, []);

  const handleChange = (event) => {
    setFormData({
      ...formData,
      [event.target.name]: event.target.value,
    });
  };

  const handleSave = () => {
    fetch(`${API_URL}/assignments`, {
      credentials: "include",
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        ...formData,
        quantity: Number(formData.quantity),
      }),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to save assignment");
        }

        return response.json();
      })
      .then((data) => {
        setAssignments([...assignments, data]);

        setFormData({
          date: "",
          equipmentType: "",
          assignedTo: "",
          base: "",
          quantity: "",
        });

        setShowForm(false);
      })
      .catch((error) => {
        console.error("Error saving assignment:", error);
        alert("Unable to save assignment");
      });
  };

  return (
    <div>
      <h2>Assignments</h2>
      <p>Assign military assets to personnel.</p>

      <button
        className="primary-button"
        onClick={() => setShowForm(true)}
      >
        + Add Assignment
      </button>

      {showForm && (
        <div className="form-box">
          <h3>Add Assignment</h3>

          <label>Date</label>
          <input
            type="date"
            name="date"
            value={formData.date}
            onChange={handleChange}
          />

          <label>Base</label>
          <select
            name="base"
            value={formData.base}
            onChange={handleChange}
          >
            <option value="">Select Base</option>
            <option value="Base 1">Base 1</option>
            <option value="Base 2">Base 2</option>
            <option value="Base 3">Base 3</option>
            <option value="Bangalore Base">Bangalore Base</option>
          </select>

          <label>Equipment Type</label>
          <input
            type="text"
            name="equipmentType"
            value={formData.equipmentType}
            placeholder="Enter equipment type"
            onChange={handleChange}
          />

          <label>Assigned To</label>
          <input
            type="text"
            name="assignedTo"
            value={formData.assignedTo}
            placeholder="Enter personnel name"
            onChange={handleChange}
          />

          <label>Quantity</label>
          <input
            type="number"
            name="quantity"
            value={formData.quantity}
            placeholder="Enter quantity"
            onChange={handleChange}
          />

          <button
            className="primary-button"
            onClick={handleSave}
          >
            Save Assignment
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
            <th>Date</th>
            <th>Base</th>
            <th>Equipment Type</th>
            <th>Assigned To</th>
            <th>Quantity</th>
          </tr>
        </thead>

        <tbody>
          {assignments.length === 0 ? (
            <tr>
              <td colSpan="5">No assignments found</td>
            </tr>
          ) : (
            assignments.map((assignment) => (
              <tr key={assignment.id}>
                <td>{assignment.date}</td>
                <td>{assignment.base}</td>
                <td>{assignment.equipmentType}</td>
                <td>{assignment.assignedTo}</td>
                <td>{assignment.quantity}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

export default Assignments;