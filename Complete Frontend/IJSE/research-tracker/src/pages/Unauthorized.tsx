import React from "react";
import { useNavigate } from "react-router-dom";

export const Unauthorized = () => {
  const navigate = useNavigate();

  return (
    <div className="container text-center mt-5">
      <h2 style={{ color: "#ff9800" }}>Unauthorized Access</h2>
      <p>You don’t have permission to view this page.</p>
      <button
        className="btn mt-3"
        style={{
          backgroundColor: "#fff9c4", 
          color: "#ff9800", 
          fontWeight: "bold",
          border: "2px solid #ff9800",
        }}
        onClick={() => navigate("/login")}
      >
        Go to Login
      </button>
    </div>
  );
};
