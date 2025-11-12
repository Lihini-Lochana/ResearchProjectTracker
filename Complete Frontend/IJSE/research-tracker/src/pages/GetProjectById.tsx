import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../api/axios";

interface Milestone {
  id?: number;
  title: string;
  description?: string;
  dueDate?: string;
  status?: string;
}

interface Document {
  id: number;
  filename: string;
  storagePath: string;
  uploadedAt: string;
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
}

export const GetProjectById = () => {
  const { id } = useParams();
  const [project, setProject] = useState<Project | null>(null);
  const [milestones, setMilestones] = useState<Milestone[]>([]);
  const [documents, setDocuments] = useState<Document[]>([]);
  const [newMilestone, setNewMilestone] = useState<Milestone>({
    title: "",
    description: "",
    dueDate: "",
    status: "PENDING",
  });
  const [file, setFile] = useState<File | null>(null);

  useEffect(() => {
    const fetchAll = async () => {
      if (!id) return;
      try {
        const projectRes = await api.get(`/admin/projects/${id}`);
        setProject(projectRes.data);

        const milestoneRes = await api.get(`/milestones/${id}`);
        setMilestones(milestoneRes.data);

        const docRes = await api.get(`/documents/${id}`);
        setDocuments(docRes.data);
      } catch (err) {
        console.error(err);
        alert("Error fetching project details or related data");
      }
    };
    fetchAll();
  }, [id]);

  const handleFileUpload = async () => {
    if (!file) {
      alert("Please select a file");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {
      await api.post(`/documents/${id}`, formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      alert("Document uploaded successfully!");

      const updatedDocs = await api.get(`/documents/${id}`);
      setDocuments(updatedDocs.data);
      setFile(null);
    } catch (err: any) {
      console.error(err);
      alert(err.response?.data?.message || "Error uploading document");
    }
  };


  const handleAddMilestone = async () => {
    if (!newMilestone.title.trim()) {
      alert("Milestone title is required");
      return;
    }

    try {
      await api.post(`/milestones/${id}`, newMilestone);
      alert("Milestone added successfully!");
      const updatedMilestones = await api.get(`/milestones/${id}`);
      setMilestones(updatedMilestones.data);
      setNewMilestone({ title: "", description: "", dueDate: "", status: "PENDING" });
    } catch (err: any) {
      console.error(err);
      alert(err.response?.data?.message || "Error adding milestone");
    }
  };

  if (!project) return <div className="container mt-4">Loading...</div>;

  return (
    <div className="container mt-4">
      <h2 className="mb-3" style={{ color: "#ff9800" }}>Project Details</h2>


      <div className="card p-3 mt-3" style={{ backgroundColor: "#f5f5f5" }}>
        <p><strong>Title:</strong> {project.title}</p>
        <p><strong>Description:</strong> {project.description}</p>
        <p><strong>Start Date:</strong> {project.startDate || "Not Set"}</p>
        <p><strong>End Date:</strong> {project.endDate || "Not Set"}</p>
        <p><strong>Status:</strong> {project.status}</p>
        <p><strong>PI:</strong> {project.piEmail || "N/A"}</p>
        <p><strong>Batch:</strong> {project.batchName || "N/A"}</p>
      </div>


      <div className="card p-3 mt-4" style={{ backgroundColor: "#fff9c4" }}>
        <h4 style={{ color: "#ff9800" }}>Documents</h4>
        <input
          type="file"
          className="form-control my-2"
          onChange={(e) => {
            const target = e.target as HTMLInputElement; 
            setFile(target.files ? target.files[0] : null);
          }}
        />
        <button className="btn btn-warning mb-3" onClick={handleFileUpload}>
          Upload Document
        </button>

        <ul className="list-group">
          {documents.length > 0 ? (
            documents.map((doc) => (
              <li key={doc.id} className="list-group-item d-flex justify-content-between align-items-center">
                <div>
                  <strong>{doc.filename}</strong>
                  <br />
                  <small className="text-muted">
                    Uploaded on:{" "}
                    {new Date(doc.uploadedAt).toLocaleString("en-GB", {
                      day: "2-digit",
                      month: "short",
                      year: "numeric",
                      hour: "2-digit",
                      minute: "2-digit",
                    })}
                  </small>
                </div>
                <a
                  href={doc.storagePath}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="btn btn-link"
                >
                  View
                </a>
              </li>
            ))
          ) : (
            <li className="list-group-item">No documents uploaded yet.</li>
          )}
        </ul>
      </div>


      <div className="card p-3 mt-4" style={{ backgroundColor: "#f5f5f5" }}>
        <h4 style={{ color: "#ff9800" }}>Milestones</h4>

        <div>
          <label className="form-label">Milestone Title:</label>
          <input
            type="text"
            className="form-control my-2"
            value={newMilestone.title}
            onChange={(e) => setNewMilestone({ ...newMilestone, title: e.target.value })}
          />

          <label className="form-label">Description:</label>
          <textarea
            className="form-control my-2"
            value={newMilestone.description}
            onChange={(e) => setNewMilestone({ ...newMilestone, description: e.target.value })}
          />

          <label className="form-label">Due date:</label>
          <input
            type="date"
            className="form-control my-2"
            placeholder="Due date"
            value={newMilestone.dueDate || ""}
            onChange={(e) => setNewMilestone({ ...newMilestone, dueDate: e.target.value })}
          />

          <label className="form-label">Status:</label>
          <select
            className="form-control my-2"
            value={newMilestone.status}
            onChange={(e) => setNewMilestone({ ...newMilestone, status: e.target.value })}
          >
            <option value="PENDING">PENDING</option>
            <option value="DONE">DONE</option>
          </select>

          <button className="btn btn-warning mb-3" onClick={handleAddMilestone}>
            Add Milestone
          </button>
        </div>

        <ul className="list-group">
          {milestones.length > 0 ? (
            milestones.map((m) => (
              <li key={m.id} className="list-group-item">
                <strong>{m.title}</strong> - {m.status}
                <div>{m.description}</div>
                <small>Due: {m.dueDate || "Not Set"}</small>
              </li>
            ))
          ) : (
            <li className="list-group-item">No milestones yet.</li>
          )}
        </ul>
      </div>
    </div>
  );
};
