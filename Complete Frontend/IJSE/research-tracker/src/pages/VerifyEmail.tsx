import React, { useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import axios from "../api/axios";
import { Container, Form, Button, Alert } from "react-bootstrap";

export const VerifyEmail = () => {
  const [searchParams] = useSearchParams();
  const emailFromQuery = searchParams.get("email") || "";
  const [email, setEmail] = useState(emailFromQuery);
  const [otp, setOtp] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleVerify = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setMessage("");

    try {
      await axios.post("/auth/verify-email", { email, otp });
      setMessage("Email verified successfully! You can now log in.");
      setTimeout(() => navigate("/auth/login"), 2000);
    } catch (err: any) {
      console.error("Verification failed:", err);
      setError(err.response?.data?.message || "Verification failed");
    }
  };

  return (
    <Container className="mt-5">
      <h2>Email Verification</h2>
      {error && <Alert variant="danger">{error}</Alert>}
      {message && <Alert variant="success">{message}</Alert>}

      <Form onSubmit={handleVerify}>
        <Form.Group className="mb-3">
          <Form.Label>Email</Form.Label>
          <Form.Control
            type="email"
            value={email}
            onChange={e => setEmail(e.target.value)}
            required
            readOnly={!!emailFromQuery}
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Enter OTP</Form.Label>
          <Form.Control
            type="text"
            maxLength={6}
            value={otp}
            onChange={e => setOtp(e.target.value)}
            required
          />
        </Form.Group>

        <Button type="submit">Verify Email</Button>
      </Form>
    </Container>
  );
};
