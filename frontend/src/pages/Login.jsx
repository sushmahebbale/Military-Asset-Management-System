import { useState } from "react";
import { useNavigate } from "react-router-dom";
import API_URL from "../api";

function Login() {
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleLogin = async (event) => {
    event.preventDefault();
    setError("");

    try {
      const response = await fetch(`${API_URL}/auth/login`, {
      credentials: "include",
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username,
          password,
        }),
      });

      if (!response.ok) {
        const errorText = await response.text();
        console.log("Login response:", response.status, errorText);
        setError("Invalid username or password");
        return;
      }

      const user = await response.json();

      localStorage.setItem("loggedIn", "true");
      localStorage.setItem("username", user.username);
      localStorage.setItem("role", user.role);
      localStorage.setItem("fullName", user.fullName);

      navigate("/");
    } catch (error) {
      console.error("Login error:", error);
      setError("Unable to connect to server");
    }
  };

  return (
    <div className="login-container">
      <div className="login-box">
        <h2>Military Asset Management System</h2>
        <p>Login to your account</p>

        <form onSubmit={handleLogin}>
          <label>Username</label>
          <input
            type="text"
            placeholder="Enter username"
            value={username}
            onChange={(event) => setUsername(event.target.value)}
          />

          <label>Password</label>
          <input
            type="password"
            placeholder="Enter password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
          />

          {error && <p>{error}</p>}

          <button type="submit" className="primary-button">
            Login
          </button>
        </form>
      </div>
    </div>
  );
}

export default Login;