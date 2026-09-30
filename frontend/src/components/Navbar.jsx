function Navbar() {
  const fullName = localStorage.getItem("fullName");
  const role = localStorage.getItem("role");

  return (
    <div className="navbar">
      <h3>Military Asset Management System</h3>

      <div className="navbar-user">
        <span>{fullName || role}</span>
      </div>
    </div>
  );
}

export default Navbar;