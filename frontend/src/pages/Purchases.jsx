import { useEffect, useState } from "react";
import API_URL from "../api";

function Purchases() {
  const [showForm, setShowForm] = useState(false);
  const [purchases, setPurchases] = useState([]);

  const [selectedDate, setSelectedDate] = useState("");
  const [selectedEquipment, setSelectedEquipment] = useState("");

  const [formData, setFormData] = useState({
    date: "",
    base: "",
    equipmentType: "",
    quantity: "",
    supplier: "",
  });

  useEffect(() => {
    fetch(`${API_URL}/purchases`, {
      credentials: "include",
    })
      .then((response) => response.json())
      .then((data) => {
        setPurchases(data);
      })
      .catch((error) => {
        console.error("Error loading purchases:", error);
      });
  }, []);

  const handleChange = (event) => {
    setFormData({
      ...formData,
      [event.target.name]: event.target.value,
    });
  };

  const handleSave = () => {
    fetch(`${API_URL}/purchases`, {
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
      .then((response) => response.json())
      .then((data) => {
        setPurchases([...purchases, data]);

        setFormData({
          date: "",
          base: "",
          equipmentType: "",
          quantity: "",
          supplier: "",
        });

        setShowForm(false);
      })
      .catch((error) => {
        console.error("Error saving purchase:", error);
      });
  };

  const filteredPurchases = purchases.filter((purchase) => {
    const dateMatch =
      selectedDate === "" || purchase.date === selectedDate;

    const equipmentMatch =
      selectedEquipment === "" ||
      purchase.equipmentType === selectedEquipment;

    return dateMatch && equipmentMatch;
  });

  const equipmentTypes = [
    ...new Set(purchases.map((purchase) => purchase.equipmentType)),
  ];

  return (
    <div>
      <h2>Purchases</h2>
      <p>Manage military equipment purchases.</p>

      <div className="dashboard-filters">
        <div>
          <label>Date</label>
          <input
            type="date"
            value={selectedDate}
            onChange={(event) => setSelectedDate(event.target.value)}
          />
        </div>

        <div>
          <label>Equipment Type</label>
          <select
            value={selectedEquipment}
            onChange={(event) =>
              setSelectedEquipment(event.target.value)
            }
          >
            <option value="">All Equipment</option>

            {equipmentTypes.map((equipment) => (
              <option key={equipment} value={equipment}>
                {equipment}
              </option>
            ))}
          </select>
        </div>
      </div>

      <button
        className="primary-button"
        onClick={() => setShowForm(true)}
      >
        + Add Purchase
      </button>

      {showForm && (
        <div className="form-box">
          <h3>Add Purchase</h3>

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

          <label>Quantity</label>
          <input
            type="number"
            name="quantity"
            value={formData.quantity}
            placeholder="Enter quantity"
            onChange={handleChange}
          />

          <label>Supplier</label>
          <input
            type="text"
            name="supplier"
            value={formData.supplier}
            placeholder="Enter supplier"
            onChange={handleChange}
          />

          <button className="primary-button" onClick={handleSave}>
            Save Purchase
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
            <th>Quantity</th>
            <th>Supplier</th>
          </tr>
        </thead>

        <tbody>
          {filteredPurchases.length === 0 ? (
            <tr>
              <td colSpan="5">No purchases found</td>
            </tr>
          ) : (
            filteredPurchases.map((purchase) => (
              <tr key={purchase.id}>
                <td>{purchase.date}</td>
                <td>{purchase.base}</td>
                <td>{purchase.equipmentType}</td>
                <td>{purchase.quantity}</td>
                <td>{purchase.supplier}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

export default Purchases;