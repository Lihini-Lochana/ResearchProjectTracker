import { NavLink, useNavigate } from "react-router-dom";
import { useContext } from "react";
import { AuthContext } from "../context/AuthContext";

export const Navbar = () => {
  const { user } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    navigate("/login");
    window.location.reload(); 
  };

  return (
    <div>
      <div
        className="text-center py-2 font-bold text-white"
        style={{ backgroundColor: "#ff9800" }}
      >
        <h2>IJSE institute</h2>
      </div>


      <nav
        className="d-flex justify-content-between align-items-center px-4 py-3 shadow"
        style={{ backgroundColor: "#fff9c4" }} 
      >

        <div
          className="fw-bold fs-5 text-dark cursor-pointer"
          onClick={() => navigate("/")}
        >
          Research Projects
        </div>


        <div className="d-flex align-items-center gap-3">
          {user && (
            <>
              {user.role === "ROLE_MEMBER" && (
                <NavLink
                  to="/member-projects"
                  className={({ isActive }) =>
                    isActive
                      ? "text-dark text-decoration-underline"
                      : "text-dark text-decoration-none"
                  }
                >
                  <h6>My Projects</h6>
                </NavLink>
              )}

              

              {user.role === "ROLE_ADMIN" && (
                <NavLink
                  to="/projects"
                  className={({ isActive }) =>
                    isActive
                      ? "text-dark text-decoration-underline"
                      : "text-dark text-decoration-none"
                  }
                >
                  Create Project
                </NavLink>
              )}

              {(user.role === "ROLE_ADMIN") && (
                <NavLink
                  to="/admin"
                  className={({ isActive }) =>
                    isActive
                      ? "text-dark text-decoration-underline"
                      : "text-dark text-decoration-none"
                  }
                >
                  Admin Panel
                </NavLink>
              )}

                {(user.role === "ROLE_PI") && (
                <NavLink
                  to="/pi-projects"
                  className={({ isActive }) =>
                    isActive
                      ? "text-dark text-decoration-underline"
                      : "text-dark text-decoration-none"
                  }
                >
                  PI Projects
                </NavLink>
              )}
            </>
          )}

          {!user ? (
            <>
              <NavLink to="/login" className="text-dark text-decoration-none">
                <h6>Login</h6>
              </NavLink>
              <NavLink to="/signup" className="text-dark text-decoration-none">
                <h6>Sign Up</h6>
              </NavLink>
            </>
          ) : (
            <button
              onClick={handleLogout}
              className="btn btn-sm btn-danger"
            >
              Logout
            </button>
          )}
        </div>
      </nav>
    </div>
  );
};
