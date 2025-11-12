import { useState, useEffect } from "react";
import api from "../api/axios";

interface Pi {
  id: number;
  email: string;
  fullName: string;
}

interface Batch {
  id: number;
  name: string;
}

export const CreateProject = () => {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [status, setStatus] = useState("PLANNING");
  const [piId, setPiId] = useState<number | undefined>();
  const [batchId, setBatchId] = useState<number | undefined>();
  const [pis, setPis] = useState<Pi[]>([]);
  const [batches, setBatches] = useState<Batch[]>([]);

  useEffect(() => {
    const fetchPis = async () => {
      try {
        const res = await api.get<Pi[]>("/auth/pis");
        setPis(res.data);
      } catch (err) {
        console.error(err);
      }
    };

    const fetchBatches = async () => {
      try {
        const res = await api.get<Batch[]>("/admin/batches");
        setBatches(res.data);
      } catch (err) {
        console.error(err);
      }
    };

    fetchPis();
    fetchBatches();
  }, []);

  const handleCreate = async () => {
    try {
      const payload = {
        title,
        description,
        startDate: startDate || null,
        endDate: endDate || null,
        status,
        piId,
        batchId,
      };
      await api.post("/admin/projects", payload);
      alert("Project created successfully!");
      setTitle("");
      setDescription("");
      setStartDate("");
      setEndDate("");
      setStatus("PLANNING");
      setPiId(undefined);
      setBatchId(undefined);
    } catch (err: any) {
      console.error(err);
      alert(err.response?.data?.message || "Error creating project");
    }
  };

  return (
    <div className="container mt-4">
      <h2 className="mb-4" style={{ color: "#ff9800" }}>Create Project</h2>

      <div className="card shadow p-4" style={{ backgroundColor: "#fff9c4" }}>
        <div className="mb-3">
          <label className="form-label">Title</label>
          <input
            type="text"
            className="form-control"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder="Enter project title"
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Description</label>
          <textarea
            className="form-control"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Enter project description"
          />
        </div>

        <div className="row">
          <div className="col-md-6 mb-3">
            <label className="form-label">Start Date</label>
            <input
              type="date"
              className="form-control"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
            />
          </div>
          <div className="col-md-6 mb-3">
            <label className="form-label">End Date</label>
            <input
              type="date"
              className="form-control"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
            />
          </div>
        </div>

        <div className="mb-3">
          <label className="form-label">PI</label>
          <select
            className="form-select"
            value={piId ?? ""}
            onChange={(e) => setPiId(Number(e.target.value))}
          >
            <option value="">Select PI</option>
            {pis.map((s) => (
              <option key={s.id} value={s.id}>
                {s.fullName} - {s.email}
              </option>
            ))}
          </select>
        </div>

        <div className="mb-3">
          <label className="form-label">Batch</label>
          <select
            className="form-select"
            value={batchId ?? ""}
            onChange={(e) => setBatchId(Number(e.target.value))}
          >
            <option value="">Select Batch</option>
            {batches.map((b) => (
              <option key={b.id} value={b.id}>
                {b.name}
              </option>
            ))}
          </select>
        </div>

        <div className="mb-3">
          <label className="form-label">Status</label>
          <select
            className="form-select"
            value={status}
            onChange={(e) => setStatus(e.target.value)}
          >
            <option value="PLANNING">PLANNING</option>
            <option value="ACTIVE">ONGOING</option>
            <option value="ON-HOLD">COMPLETED</option>
            <option value="COMPLETED">COMPLETED</option>
            <option value="ARCHIVED">ONGOING</option>
          </select>
        </div>

        <button
          className="btn"
          style={{ backgroundColor: "#ff9800", color: "#fff" }}
          onClick={handleCreate}
        >
          Create Project
        </button>
      </div>
    </div>
  );
};
