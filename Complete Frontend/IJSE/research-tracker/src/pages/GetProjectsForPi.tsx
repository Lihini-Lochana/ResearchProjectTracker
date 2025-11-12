import { useState, useEffect, useContext } from "react"; 
import api from "../api/axios";
import { AuthContext } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { Container, Card, Row, Col } from "react-bootstrap";

interface Project {
  id: number;
  title: string; 
  status: string;
  batchName?: string;
}

export const GetProjectsForPi = () => {
  const { user } = useContext(AuthContext);
  const [projects, setProjects] = useState<Project[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchProjects = async () => {
      try {
        const res = await api.get<Project[]>("/admin/projects/pi");
        setProjects(res.data);
      } catch (err) {
        console.error(err);
      }
    };
    fetchProjects();
  }, []);

  return (
    <Container className="mt-4">
      <h2 style={{ color: "#ff9800", marginBottom: "1rem" }}>PI Projects</h2>

      <Row>
        {projects.length === 0 && (
          <Col>
            <p className="text-muted">No projects assigned yet.</p>
          </Col>
        )}

        {projects.map((p) => (
          <Col md={4} className="mb-3" key={p.id}>
            <Card
              onClick={() => navigate(`/admin-project-details/${p.id}`)}
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
                  Batch: {p.batchName || "-"}
                </Card.Text>
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
