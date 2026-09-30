import { Routes, Route, useLocation, Navigate } from "react-router-dom";
import Sidebar from "./components/Sidebar";
import Navbar from "./components/Navbar";
import Dashboard from "./pages/Dashboard";
import Purchases from "./pages/Purchases";
import Transfers from "./pages/Transfers";
import Assignments from "./pages/Assignments";
import Expenditures from "./pages/Expenditures";
import Users from "./pages/Users";
import AuditLogs from "./pages/AuditLogs";
import Login from "./pages/Login";

function App() {
  const location = useLocation();

  const isLoginPage = location.pathname === "/login";
  const isLoggedIn = localStorage.getItem("loggedIn") === "true";
  const role = localStorage.getItem("role");

  if (!isLoginPage && !isLoggedIn) {
    return <Navigate to="/login" />;
  }

  return (
    <div>
      {!isLoginPage && <Sidebar />}

      <div className={isLoginPage ? "" : "main-content"}>
        {!isLoginPage && <Navbar />}

        <div className={isLoginPage ? "" : "page-content"}>
          <Routes>
            <Route path="/login" element={<Login />} />

            <Route path="/" element={<Dashboard />} />

            <Route path="/purchases" element={<Purchases />} />

            <Route path="/transfers" element={<Transfers />} />

            <Route
              path="/assignments"
              element={
                role === "LOGISTICS_OFFICER"
                  ? <Navigate to="/" />
                  : <Assignments />
              }
            />

            <Route
              path="/expenditures"
              element={
                role === "LOGISTICS_OFFICER"
                  ? <Navigate to="/" />
                  : <Expenditures />
              }
            />

            <Route
              path="/users"
              element={
                role === "ADMIN"
                  ? <Users />
                  : <Navigate to="/" />
              }
            />

            <Route path="/audit-logs" element={<AuditLogs />} />
          </Routes>
        </div>
      </div>
    </div>
  );
}

export default App;