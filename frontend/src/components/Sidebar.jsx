import { NavLink, useNavigate } from "react-router-dom";
import {
  FaTachometerAlt,
  FaShoppingCart,
  FaExchangeAlt,
  FaClipboardList,
  FaFileInvoiceDollar,
  FaUsers,
  FaHistory,
  FaSignOutAlt
} from "react-icons/fa";

function Sidebar() {
  const navigate = useNavigate();
  const role = localStorage.getItem("role");

  const handleLogout = () => {
    localStorage.removeItem("loggedIn");
    localStorage.removeItem("username");
    localStorage.removeItem("role");
    localStorage.removeItem("fullName");
    navigate("/login");
  };

  return (
    <div className="sidebar">
      <h3>Military AMS</h3>

      <NavLink to="/">
        <FaTachometerAlt /> Dashboard
      </NavLink>

      <NavLink to="/purchases">
        <FaShoppingCart /> Purchases
      </NavLink>

      <NavLink to="/transfers">
        <FaExchangeAlt /> Transfers
      </NavLink>

      {role !== "LOGISTICS_OFFICER" && (
        <NavLink to="/assignments">
          <FaClipboardList /> Assignments
        </NavLink>
      )}

      {role !== "LOGISTICS_OFFICER" && (
        <NavLink to="/expenditures">
          <FaFileInvoiceDollar /> Expenditures
        </NavLink>
      )}

      {role === "ADMIN" && (
        <NavLink to="/users">
          <FaUsers /> Users
        </NavLink>
      )}

      <NavLink to="/audit-logs">
        <FaHistory /> Audit Logs
      </NavLink>

      <button onClick={handleLogout} className="logout-button">
        <FaSignOutAlt /> Logout
      </button>
    </div>
  );
}

export default Sidebar;