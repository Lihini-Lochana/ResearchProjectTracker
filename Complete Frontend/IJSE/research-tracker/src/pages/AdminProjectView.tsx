import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../api/axios";

interface Milestone {
  id: number;
  title: string;
  description?: string;
  dueDate?: string;
  status?: string;
}

interface Document {
  id: number;
  filename: string;
  storagePathFull: string;
  uploadedAt: string;
  uploadedById?: number;
  uploadedByName?: string;
}

interface Project {
  id: number;
  title: string;
  description: string;
  startDate: string;
  endDate: string;
  status: string;
  piEmail?: string;
  batchName?: string;
  teamMembers?: { id: number; name: string; email: string }[];
}

export const AdminProjectView = () => {
  const { id } = useParams();
  const [project, setProject] = useState<Project | null>(null);
  const [milestones, setMilestones] = useState<Milestone[]>([]);
  const [documents, setDocuments] = useState<Document[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProjectData = async () => {
      if (!id) return;
      try {
        const projectRes = await api.get(`/admin/projects/${id}`);
        setProject(projectRes.data);

        const milestoneRes = await api.get(`/milestones/${id}`);
        setMilestones(milestoneRes.data);

        const docRes = await api.get(`/documents/${id}`);
        setDocuments(docRes.data);
      } catch (err) {
        console.error("Error fetching project data:", err);
        alert("Failed to fetch project details");
      } finally {
        setLoading(false);
      }
    };
    fetchProjectData();
  }, [id]);

  if (loading) return <div className="container mt-4">Loading project data...</div>;
  if (!project) return <div className="container mt-4">Project not found.</div>;

  return (
    <div className="container mt-4">
      <h2 className="mb-4" style={{ color: "#ff9800" }}>Project Overview</h2>


      <div className="card p-4 mb-4 shadow-sm" style={{ backgroundColor: "#fff9c4" }}>
        <h4 style={{ color: "#ff9800" }}>Project Details</h4>
        <hr style={{ borderColor: "#ff9800" }} />
        <p><strong>Title:</strong> {project.title}</p>
        <p><strong>Description:</strong> {project.description}</p>
        <p><strong>Status:</strong> {project.status}</p>
        <p><strong>Start Date:</strong> {project.startDate || "Not Set"}</p>
        <p><strong>End Date:</strong> {project.endDate || "Not Set"}</p>
        <p><strong>PI:</strong> {project.piEmail || "N/A"}</p>
        <p><strong>Batch:</strong> {project.batchName || "N/A"}</p>

        {project.teamMembers && project.teamMembers.length > 0 && (
          <>
            <h5 className="mt-3" style={{ color: "#ff9800" }}>Team Members</h5>
            <ul className="list-group">
              {project.teamMembers.map(member => (
                <li key={member.id} className="list-group-item" style={{ backgroundColor: "#f5f5f5" }}>
                  {member.name} ({member.email})
                </li>
              ))}
            </ul>
          </>
        )}
      </div>


      <div className="card p-4 mb-4 shadow-sm" style={{ backgroundColor: "#fff9c4" }}>
        <h4 style={{ color: "#ff9800" }}>Uploaded Documents</h4>
        <hr style={{ borderColor: "#ff9800" }} />
        {documents.length > 0 ? (
          <ul className="list-group">
            {documents.map(doc => (
              <li key={doc.id} className="list-group-item d-flex justify-content-between align-items-center" style={{ backgroundColor: "#f5f5f5" }}>
                <div>
                  <strong>{doc.filename}</strong>
                  <br />
                  <small className="text-muted">
                    Uploaded on: {new Date(doc.uploadedAt).toLocaleString("en-GB", {
                      day: "2-digit",
                      month: "short",
                      year: "numeric",
                      hour: "2-digit",
                      minute: "2-digit",
                    })}
                    {doc.uploadedByName && ` - Uploaded by: ${doc.uploadedByName}`}
                  </small>
                </div>
                <a
                  href={doc.storagePathFull}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="btn btn-sm"
                  style={{ backgroundColor: "#ff9800", color: "#fff" }}
                >
                  View
                </a>
              </li>
            ))}
          </ul>
        ) : (
          <p>No documents uploaded yet.</p>
        )}
      </div>


      <div className="card p-4 mb-5 shadow-sm" style={{ backgroundColor: "#fff9c4" }}>
        <h4 style={{ color: "#ff9800" }}>Project Milestones</h4>
        <hr style={{ borderColor: "#ff9800" }} />
        {milestones.length > 0 ? (
          <ul className="list-group">
            {milestones.map(m => (
              <li key={m.id} className="list-group-item" style={{ backgroundColor: "#f5f5f5" }}>
                <div className="d-flex justify-content-between align-items-center">
                  <div>
                    <strong>{m.title}</strong> – <span>{m.status}</span>
                    <div>{m.description}</div>
                    <small>Due: {m.dueDate || "Not Set"}</small>
                  </div>
                </div>
              </li>
            ))}
          </ul>
        ) : (
          <p>No milestones found.</p>
        )}
      </div>
    </div>
  );
};
