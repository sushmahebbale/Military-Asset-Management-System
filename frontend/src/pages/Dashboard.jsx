import { useEffect, useState } from "react";
import API_URL from "../api";

function Dashboard() {
  const [selectedDate, setSelectedDate] = useState("");
  const [selectedBase, setSelectedBase] = useState("");
  const [selectedEquipment, setSelectedEquipment] = useState("");
  const [dashboard, setDashboard] = useState({
    openingBalance: 0,
    closingBalance: 0,
    netMovement: 0,
    assigned: 0,
    expended: 0,
  });

  useEffect(() => {
    fetch(`${API_URL}/dashboard?date=${selectedDate}&base=${selectedBase}&equipmentType=${selectedEquipment}`, {
    credentials: "include",
  })
      .then((response) => response.json())
      .then((data) => {
        setDashboard(data);
      })
      .catch((error) => {
        console.error("Error loading dashboard:", error);
      });
  }, [selectedDate, selectedBase, selectedEquipment]);

  return (
    <div>
      <h2>Dashboard</h2>
      <p>Military Asset Management Dashboard</p>

      <div className="dashboard-filters">
        <div>
          <label>Date</label>
          <input
          type="date"
          value={selectedDate}
          onChange={(e) => setSelectedDate(e.target.value)}
        />
        </div>

        <div>
          <label>Base</label>
          <select
          value={selectedBase}
          onChange={(e) => setSelectedBase(e.target.value)}
          >
            <option value="">All Bases</option>
            <option value="Base 1">Base 1</option>
            <option value="Base 2">Base 2</option>
            <option value="Base 3">Base 3</option>
            <option value="Bangalore Base">Bangalore Base</option>
          </select>
        </div>

        <div>
          <label>Equipment Type</label>
          <select
          value={selectedEquipment}
          onChange={(e) => setSelectedEquipment(e.target.value)}
          >
            <option value="">All Equipment</option>
            <option>Vehicles</option>
            <option>Weapons</option>
            <option>Communication Equipment</option>
          </select>
        </div>
      </div>

      <div className="dashboard-cards">
        <div className="card">
          <h4>Opening Balance</h4>
          <h2>{dashboard.openingBalance}</h2>
        </div>

        <div className="card">
          <h4>Closing Balance</h4>
          <h2>{dashboard.closingBalance}</h2>
        </div>

        <div className="card">
          <h4>Net Movement</h4>
          <h2>{dashboard.netMovement}</h2>
        </div>

        <div className="card">
          <h4>Assigned</h4>
          <h2>{dashboard.assigned}</h2>
        </div>

        <div className="card">
          <h4>Expended</h4>
          <h2>{dashboard.expended}</h2>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;