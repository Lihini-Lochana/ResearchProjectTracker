import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import { Login } from "./pages/Login";
import { PrivateRoute } from "./routes/PrivateRoute";
import { CreateProject } from "./pages/CreateProject";
import { Unauthorized } from "./pages/Unauthorized";
import { GetProjectById } from "./pages/GetProjectById";
import { AdminProjectView } from "./pages/AdminProjectView";
import { Signup } from "./pages/Signup";
import { VerifyEmail } from "./pages/VerifyEmail";
import { Navbar } from "./components/Navbar";
import { AdminPanel } from "./pages/AdminPanel";
import { GetProjectsForPi } from "./pages/GetProjectsForPi";
import { GetProjectForMember } from "./pages/GetProjectForMember";

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Navbar />
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/verify-email" element={<VerifyEmail />} />
          <Route path="/unauthorized" element={<Unauthorized />} />

          <Route
            path="/projects"
            element={
              <PrivateRoute roles={["ROLE_ADMIN"]}>
                <CreateProject />
              </PrivateRoute>
            }
          />

          <Route
            path="/pi-projects"
            element={
              <PrivateRoute roles={["ROLE_PI"]}>
                <GetProjectsForPi />
              </PrivateRoute>
            }
          />

          <Route
            path="/member-projects"
            element={
              <PrivateRoute roles={["ROLE_MEMBER"]}>
                <GetProjectForMember />
              </PrivateRoute>
            }
          />

          <Route
            path="/project-details/:id"
            element={
              <PrivateRoute roles={["ROLE_MEMBER"]}>
                <GetProjectById />
              </PrivateRoute>
            }
          />

          <Route
            path="/admin-project-details/:id"
            element={
              <PrivateRoute roles={["ROLE_ADMIN", "ROLE_PI"]}>
                <AdminProjectView />
              </PrivateRoute>
            }
          />

          <Route
              path="/admin"
              element={
                <PrivateRoute roles={["ROLE_ADMIN"]}>
                  <AdminPanel />
                </PrivateRoute>
              }
            />


          <Route path="*" element={<Navigate to="/login" />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
