import { useEffect, useState } from "react";
import API_URL from "../api";

function Expenditures() {
  const [showForm, setShowForm] = useState(false);
  const [expenditures, setExpenditures] = useState([]);

  const [formData, setFormData] = useState({
    date: "",
    equipmentType: "",
    base: "",
    reason: "",
    quantity: "",
  });

  useEffect(() => {
    fetch(`${API_URL}/expenditures`, {
      credentials: "include",
    })
      .then((response) => response.json())
      .then((data) => {
        setExpenditures(data);
      })
      .catch((error) => {
        console.error("Error loading expenditures:", error);
      });
  }, []);

  const handleChange = (event) => {
    setFormData({
      ...formData,
      [event.target.name]: event.target.value,
    });
  };

  const handleSave = () => {
    fetch(`${API_URL}/expenditures`, {
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
          throw new Error("Failed to save expenditure");
        }

        return response.json();
      })
      .then((data) => {
        setExpenditures([...expenditures, data]);

        setFormData({
          date: "",
          equipmentType: "",
          base: "",
          reason: "",
          quantity: "",
        });

        setShowForm(false);
      })
      .catch((error) => {
        console.error("Error saving expenditure:", error);
        alert("Unable to save expenditure");
      });
  };

  return (
    <div>
      <h2>Expenditures</h2>
      <p>Track military assets that have been expended.</p>

      <button
        className="primary-button"
        onClick={() => setShowForm(true)}
      >
        + Add Expenditure
      </button>

      {showForm && (
        <div className="form-box">
          <h3>Add Expenditure</h3>

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

          <label>Reason</label>
          <input
            type="text"
            name="reason"
            value={formData.reason}
            placeholder="Enter reason"
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
            Save Expenditure
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
            <th>Reason</th>
            <th>Quantity</th>
          </tr>
        </thead>

        <tbody>
          {expenditures.length === 0 ? (
            <tr>
              <td colSpan="5">No expenditures found</td>
            </tr>
          ) : (
            expenditures.map((expenditure) => (
              <tr key={expenditure.id}>
                <td>{expenditure.date}</td>
                <td>{expenditure.base}</td>
                <td>{expenditure.equipmentType}</td>
                <td>{expenditure.reason}</td>
                <td>{expenditure.quantity}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

export default Expenditures;