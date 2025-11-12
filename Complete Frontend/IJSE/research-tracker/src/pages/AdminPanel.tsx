import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import api from "../api/axios";

interface Project {
  id: number;
  title: string;
  status: string;
}

export const AdminPanel = () => {
  const navigate = useNavigate();
  const [projects, setProjects] = useState<Project[]>([]);


  useEffect(() => {
    const fetchProjects = async () => {
      try {
        const res = await api.get<Project[]>("/admin/projects");
        setProjects(res.data);
      } catch (err) {
        console.error("Error fetching projects:", err);
      }
    };
    fetchProjects();
  }, []);

  return (
    <div className="container mt-4">
      <h1 className="text-3xl font-bold mb-2" style={{ color: "#ff9800" }}>
        Admin Panel
      </h1>
      <h4 className="mb-4">Welcome! Here you can manage projects</h4>


      <div
        className="mb-4"
        style={{
          backgroundColor: "#fff9c4",
          height: "40px",
          display: "flex",
          alignItems: "center",
          paddingLeft: "15px",
          borderRadius: "5px",
          fontWeight: "bold",
        }}
      >
        Projects
      </div>

      <div className="row">
        {projects.length === 0 && (
          <p className="text-muted">No projects found.</p>
        )}

        {projects.map((p) => (
          <div
            key={p.id}
            className="col-md-4 mb-3"
            style={{ cursor: "pointer" }}
            onClick={() => navigate(`/admin-project-details/${p.id}`)}
          >
            <div
              className="card text-white"
              style={{ backgroundColor: "#ffc107" }}
            >
              <div className="card-body text-center">
                <h5 className="card-title">{p.title}</h5>
                <p className="card-text">{p.status}</p>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
