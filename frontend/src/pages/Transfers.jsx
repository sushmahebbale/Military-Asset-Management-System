import { useEffect, useState } from "react";
import API_URL from "../api";

function Transfers() {
  const [showForm, setShowForm] = useState(false);
  const [transfers, setTransfers] = useState([]);

  const [formData, setFormData] = useState({
    date: "",
    fromBase: "",
    toBase: "",
    equipmentType: "",
    quantity: "",
  });

  useEffect(() => {
    fetch(`${API_URL}/transfers`, {
      credentials: "include",
    })
      .then((response) => response.json())
      .then((data) => {
        setTransfers(data);
      })
      .catch((error) => {
        console.error("Error loading transfers:", error);
      });
  }, []);

  const handleChange = (event) => {
    setFormData({
      ...formData,
      [event.target.name]: event.target.value,
    });
  };

  const handleSave = () => {
    fetch(`${API_URL}/transfers`, {
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
          throw new Error("Failed to save transfer");
        }

        return response.json();
      })
      .then((data) => {
        setTransfers([...transfers, data]);

        setFormData({
          date: "",
          fromBase: "",
          toBase: "",
          equipmentType: "",
          quantity: "",
        });

        setShowForm(false);
      })
      .catch((error) => {
        console.error("Error saving transfer:", error);
        alert("Unable to save transfer");
      });
  };

  return (
    <div>
      <h2>Transfers</h2>
      <p>Transfer equipment between military bases.</p>

      <button
        className="primary-button"
        onClick={() => setShowForm(true)}
      >
        + Add Transfer
      </button>

      {showForm && (
        <div className="form-box">
          <h3>Add Transfer</h3>

          <label>Date</label>
          <input
            type="date"
            name="date"
            value={formData.date}
            onChange={handleChange}
          />

          <label>From Base</label>
          <select
            name="fromBase"
            value={formData.fromBase}
            onChange={handleChange}
          >
            <option value="">Select From Base</option>
            <option value="Base 1">Base 1</option>
            <option value="Base 2">Base 2</option>
            <option value="Base 3">Base 3</option>
            <option value="Bangalore Base">Bangalore Base</option>
          </select>

          <label>To Base</label>
          <select
            name="toBase"
            value={formData.toBase}
            onChange={handleChange}
          >
            <option value="">Select To Base</option>
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

          <label>Quantity</label>
          <input
            type="number"
            name="quantity"
            value={formData.quantity}
            placeholder="Enter quantity"
            onChange={handleChange}
          />

          <button className="primary-button" onClick={handleSave}>
            Save Transfer
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
            <th>Timestamp</th>
            <th>Date</th>
            <th>From Base</th>
            <th>To Base</th>
            <th>Equipment Type</th>
            <th>Quantity</th>
          </tr>
        </thead>

        <tbody>
          {transfers.length === 0 ? (
            <tr>
              <td colSpan="6">No transfers found</td>
            </tr>
          ) : (
            transfers.map((transfer) => (
              <tr key={transfer.id}>
                <td>
                  {transfer.transferTimestamp
                    ? new Date(transfer.timestamp).toLocaleString()
                    : "-"}
                </td>
                <td>{transfer.date}</td>
                <td>{transfer.fromBase}</td>
                <td>{transfer.toBase}</td>
                <td>{transfer.equipmentType}</td>
                <td>{transfer.quantity}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

export default Transfers;