import React, { useState, useContext } from "react"; 
import axios from "../api/axios";
import { AuthContext } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { Form, Button, Container, Alert, Card } from "react-bootstrap";

export const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const res = await axios.post("/auth/login", { email, password });
      const { accessToken, refreshToken } = res.data;

      login(accessToken, refreshToken);

      const tokenParts = accessToken.split(".");
      if (tokenParts.length !== 3) throw new Error("Invalid JWT token");
      const payload = JSON.parse(atob(tokenParts[1]));
      const role = payload.roles?.[0] || "";

      if (role === "ROLE_ADMIN") navigate("/projects");
      else if (role === "ROLE_PI") navigate("/pi-projects");
      else if (role === "ROLE_MEMBER") navigate("/member-projects");
      else navigate("/unauthorized");
    } catch (err: any) {
      setError(err.response?.data?.message || "Login failed");
    }
  };

  return (
    <div
      style={{ minHeight: "100vh", backgroundColor: "#f5f5f5" }}
      className="d-flex align-items-center justify-content-center"
    >
      <Container style={{ maxWidth: "400px" }}>
        <Card style={{ borderRadius: "12px", boxShadow: "0 0 15px rgba(0,0,0,0.1)" }}>
          <Card.Header
            className="text-center"
            style={{
              backgroundColor: "#ff9800",
              color: "white",
              borderTopLeftRadius: "12px",
              borderTopRightRadius: "12px",
            }}
          >
            <h3>Login</h3>
          </Card.Header>

          <Card.Body style={{ backgroundColor: "#fffde7" }}>
            {error && <Alert variant="danger">{error}</Alert>}
            <Form onSubmit={handleSubmit}>
              <Form.Group className="mb-3">
                <Form.Label>Email</Form.Label>
                <Form.Control
                  type="email"
                  placeholder="Enter your email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                  style={{ borderRadius: "8px" }}
                />
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>Password</Form.Label>
                <Form.Control
                  type="password"
                  placeholder="Enter your password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                  style={{ borderRadius: "8px" }}
                />
              </Form.Group>

              <Button
                type="submit"
                className="w-100"
                style={{
                  backgroundColor: "#ff9800",
                  borderColor: "#ff9800",
                  borderRadius: "8px",
                }}
              >
                Login
              </Button>
            </Form>
          </Card.Body>

          <Card.Footer
            className="text-center"
            style={{ backgroundColor: "#eeeeee", borderBottomLeftRadius: "12px", borderBottomRightRadius: "12px" }}
          >
          </Card.Footer>
        </Card>
      </Container>
    </div>
  );
};
