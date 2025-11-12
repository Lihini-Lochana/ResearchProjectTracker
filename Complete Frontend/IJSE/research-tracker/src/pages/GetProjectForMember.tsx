import { useState, useEffect } from "react";
import api from "../api/axios";
import { useNavigate } from "react-router-dom";
import { Container, Card, Row, Col } from "react-bootstrap";

interface Project {
  id: number;
  title: string;
  status: string;
  piFullName?: string;
}

export const GetProjectForMember = () => {
  const [projects, setProjects] = useState<Project[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchProjects = async () => {
      try {
        const res = await api.get<Project[]>("/admin/projects/member");
        setProjects(res.data);
      } catch (err) {
        console.error("Error fetching member projects:", err);
      }
    };
    fetchProjects();
  }, []);

  return (
    <Container className="mt-4">
      <h2 style={{ color: "#ff9800", marginBottom: "1rem" }}>Your Projects</h2>

      <Row>
        {projects.length === 0 && (
          <Col>
            <p className="text-muted">No projects found.</p>
          </Col>
        )}

        {projects.map((p) => (
          <Col md={4} className="mb-3" key={p.id}>
            <Card
              onClick={() => navigate(`/project-details/${p.id}`)}
              style={{
                cursor: "pointer",
                borderRadius: "12px",
                boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
                transition: "transform 0.2s",
                backgroundColor: "#fffde7", 
              }}
              className="hover-shadow"
            >
              <Card.Body>
                <Card.Title style={{ color: "#ff9800" }}>{p.title}</Card.Title>
                <Card.Text className="text-dark mb-2">
                  Status: <strong>{p.status}</strong>
                </Card.Text>
                <Card.Text className="text-dark">
                  PI: {p.piFullName || "-"}
                </Card.Text>
                <div className="text-center mt-2">
                  <button
                    className="btn"
                    style={{
                      backgroundColor: "#ff9800",
                      color: "#fff",
                      borderRadius: "8px",
                      padding: "5px 10px",
                    }}
                    onClick={() => navigate(`/project-details/${p.id}`)}
                  >
                    View
                  </button>
                </div>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>

      <style>
        {`
          .hover-shadow:hover {
            transform: translateY(-4px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.2);
          }
        `}
      </style>
    </Container>
  );
};
