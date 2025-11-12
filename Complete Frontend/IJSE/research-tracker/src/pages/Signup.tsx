import React, { useState, useEffect } from "react";
import axios from "../api/axios";
import { useNavigate } from "react-router-dom";
import { Container, Form, Button, Alert, Card } from "react-bootstrap";

interface Batch {
  id: number;
  name: string;
}

export const Signup = () => {
  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [accountType, setAccountType] = useState("MEMBER");
  const [batchId, setBatchId] = useState<number | null>(null);
  const [batches, setBatches] = useState<Batch[]>([]);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const navigate = useNavigate();


  useEffect(() => {
    const fetchBatches = async () => {
      try {
        const res = await axios.get("/admin/batches");
        setBatches(res.data);
      } catch (err) {
        console.error("Failed to load batches:", err);
      }
    };
    fetchBatches();
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (password !== confirmPassword) {
      setError("Passwords do not match");
      return;
    }

    try {
      const payload: any = {
        fullName,
        email,
        password,
        confirmPassword,
        accountType,
      };
      if (accountType === "MEMBER") {
        payload.batchId = batchId;
      }

      await axios.post("/auth/signup", payload);
      setSuccess("Signup successful! Check your email for OTP.");
      setTimeout(
        () =>
          navigate(
            `/verify-email?email=${encodeURIComponent(email)}`
          ),
        2000
      );
    } catch (err: any) {
      console.error("Signup failed:", err);
      setError(err.response?.data?.message || "Signup failed");
    }
  };

  return (
    <div
      style={{ minHeight: "100vh", backgroundColor: "#f5f5f5" }}
      className="d-flex align-items-center justify-content-center"
    >
      <Container className="p-0" style={{ maxWidth: "500px" }}>
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
            <h3>Create Account</h3>
          </Card.Header>

          <Card.Body style={{ backgroundColor: "#fffde7" }}>
            {error && <Alert variant="danger">{error}</Alert>}
            {success && <Alert variant="success">{success}</Alert>}

            <Form onSubmit={handleSubmit}>
              <Form.Group className="mb-3">
                <Form.Label>Full Name</Form.Label>
                <Form.Control
                  value={fullName}
                  onChange={e => setFullName(e.target.value)}
                  required
                  style={{ borderRadius: "8px" }}
                  placeholder="Enter full name"
                />
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>Email</Form.Label>
                <Form.Control
                  type="email"
                  value={email}
                  onChange={e => setEmail(e.target.value)}
                  required
                  style={{ borderRadius: "8px" }}
                  placeholder="Enter email"
                />
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>Password</Form.Label>
                <Form.Control
                  type="password"
                  value={password}
                  onChange={e => setPassword(e.target.value)}
                  required
                  style={{ borderRadius: "8px" }}
                  placeholder="Enter password"
                />
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>Confirm Password</Form.Label>
                <Form.Control
                  type="password"
                  value={confirmPassword}
                  onChange={e => setConfirmPassword(e.target.value)}
                  required
                  style={{ borderRadius: "8px" }}
                  placeholder="Confirm password"
                />
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>Account Type</Form.Label>
                <Form.Select
                  value={accountType}
                  onChange={e => setAccountType(e.target.value)}
                  style={{ borderRadius: "8px" }}
                >
                  <option value="MEMBER">Member</option>
                  <option value="PI">PI</option>
                  <option value="ADMIN">Admin</option>
                </Form.Select>
              </Form.Group>

              {accountType === "MEMBER" && (
                <Form.Group className="mb-3">
                  <Form.Label>Select Batch</Form.Label>
                  <Form.Select
                    value={batchId ?? ""}
                    onChange={e => setBatchId(Number(e.target.value))}
                    required
                    style={{ borderRadius: "8px" }}
                  >
                    <option value="">-- Select Batch --</option>
                    {batches.map(b => (
                      <option key={b.id} value={b.id}>
                        {b.name}
                      </option>
                    ))}
                  </Form.Select>
                </Form.Group>
              )}

              <Button
                type="submit"
                className="w-100"
                style={{
                  backgroundColor: "#ff9800",
                  borderColor: "#ff9800",
                  borderRadius: "8px",
                }}
              >
                Sign Up
              </Button>
            </Form>
          </Card.Body>

         
        </Card>
      </Container>
    </div>
  );
};
