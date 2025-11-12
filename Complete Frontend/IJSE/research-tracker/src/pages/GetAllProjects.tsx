import { useState, useEffect } from "react";
import api from "../api/axios";
import { Link } from "react-router-dom";

interface Project {
  id: number;
  name: string;
  status: string;
}

export const GetAllProjects = () => {
  const [projects, setProjects] = useState<Project[]>([]);

  useEffect(() => {
    const fetchProjects = async () => {
      try {
        const res = await api.get<Project[]>("/admin/projects"); 
        setProjects(res.data);
      } catch (err) {
        console.error(err);
      }
    };
    fetchProjects();
  }, []);

  return (
    <div className="container mt-4">
      <h2 className="mb-4" style={{ color: "#ff9800" }}>All Projects</h2>

      <div className="row">
        {projects.length === 0 && (
          <div className="col-12">
            <div className="alert alert-warning">No projects found.</div>
          </div>
        )}

        {projects.map((p) => (
          <div key={p.id} className="col-md-4 mb-3">
            <div
              className="card shadow"
              style={{ backgroundColor: "#fff9c4", borderRadius: "0.5rem" }}
            >
              <div className="card-body">
                <h5 className="card-title" style={{ color: "#ff9800" }}>
                  {p.name}
                </h5>
                <p className="card-text text-dark">
                  Status: <strong>{p.status}</strong>
                </p>
                <Link
                  to={`/project-details/${p.id}`}
                  className="btn btn-warning"
                  style={{ color: "#fff" }}
                >
                  View Project
                </Link>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
