import React, { createContext, useState, useEffect, ReactNode } from "react";
import { jwtDecode } from "jwt-decode";

interface DecodedToken {
  sub: string;
  roles?: string[];
  exp?: number;
  iat?: number;
}

interface User {
  email: string;
  role: string;
  roles?: string[];
}

interface AuthContextType {
  user: User | null;
  login: (token: string, refreshToken: string) => void;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextType>({
  user: null,
  login: () => {},
  logout: () => {},
});

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      try {
        const decoded = jwtDecode<DecodedToken>(token);
        setUser({
          email: decoded.sub,
          role: decoded.roles?.[0] || "",
          roles: decoded.roles,
        });
      } catch (err) {
        console.error("Invalid token:", err);
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
      }
    }
  }, []);

  const login = (token: string, refreshToken: string) => {
    localStorage.setItem("accessToken", token);
    localStorage.setItem("refreshToken", refreshToken);

    const decoded = jwtDecode<DecodedToken>(token);
    setUser({
      email: decoded.sub,
      role: decoded.roles?.[0] || "",
      roles: decoded.roles,
    });
  };

  const logout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};