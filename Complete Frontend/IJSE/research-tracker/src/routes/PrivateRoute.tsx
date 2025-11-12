import { ReactNode, useContext } from "react";
import { Navigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";

interface Props {
  children: ReactNode;
  roles?: string[];
}

export const PrivateRoute = ({ children, roles }: Props) => {
  const { user } = useContext(AuthContext);

  if (!user) return <Navigate to="/login" />;

  const userRole = user.role ?? "";

  if (roles && !roles.includes(userRole)) {
    return <Navigate to="/unauthorized" />;
  }

  return <>{children}</>;
};
