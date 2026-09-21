import { useState } from 'react';
import type { ChangeEvent, FormEvent } from 'react';

function App() {
  const [file, setFile] = useState<File | null>(null);
  const [tenantId, setTenantId] = useState<string>('acme-corp');
  const [department, setDepartment] = useState<string>('finance');
  const [confidential, setConfidential] = useState<boolean>(true);
  const [status, setStatus] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      setFile(e.target.files[0]);
    }
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    if (!file) {
      setStatus('Please select a PDF file first.');
      return;
    }

    setLoading(true);
    setStatus('Uploading and embedding document...');

    const formData = new FormData();
    formData.append('file', file);

    const params = new URLSearchParams({
      tenantId: tenantId,
      department: department,
      confidential: confidential.toString(),
    });

    try {
      const response = await fetch(`http://localhost:8080/api/v1/rag/upload-pdf?${params.toString()}`, {
        method: 'POST',
        body: formData,
      });

      if (!response.ok) {
        throw new Error(`Upload failed with status: ${response.status}`);
      }

      const result = await response.json();
      setStatus(`Success! ${result.message}`);
    } catch (error: any) {
      console.error('Error uploading PDF:', error);
      setStatus(`Upload failed: ${error.message}`);
    } finally {
      setLoading(false);
    }
  };

  return (
      <div style={{ maxWidth: '500px', margin: '2rem auto', padding: '1.5rem', border: '1px solid #ccc', borderRadius: '8px', fontFamily: 'sans-serif' }}>
        <h2>RAG Document Ingestion</h2>
        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: '1rem' }}>
            <label style={{ display: 'block', marginBottom: '0.5rem' }}>Select PDF File:</label>
            <input type="file" accept="application/pdf" onChange={handleFileChange} required />
          </div>

          <div style={{ marginBottom: '1rem' }}>
            <label style={{ display: 'block', marginBottom: '0.25rem' }}>Tenant ID:</label>
            <input type="text" value={tenantId} onChange={(e: ChangeEvent<HTMLInputElement>) => setTenantId(e.target.value)} required style={{ width: '100%', padding: '0.5rem' }} />
          </div>

          <div style={{ marginBottom: '1rem' }}>
            <label style={{ display: 'block', marginBottom: '0.25rem' }}>Department:</label>
            <input type="text" value={department} onChange={(e: ChangeEvent<HTMLInputElement>) => setDepartment(e.target.value)} required style={{ width: '100%', padding: '0.5rem' }} />
          </div>

          <div style={{ marginBottom: '1rem' }}>
            <label>
              <input type="checkbox" checked={confidential} onChange={(e: ChangeEvent<HTMLInputElement>) => setConfidential(e.target.checked)} />
              {' '}Mark as Confidential
            </label>
          </div>

          <button type="submit" disabled={loading} style={{ padding: '0.75rem 1.5rem', backgroundColor: '#007bff', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>
            {loading ? 'Processing...' : 'Upload & Ingest'}
          </button>
        </form>

        {status && <p style={{ marginTop: '1rem', fontWeight: 'bold' }}>{status}</p>}
      </div>
  );
}

export default App;