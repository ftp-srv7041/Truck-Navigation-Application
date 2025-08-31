import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button, Table, Modal, Form, Alert } from 'react-bootstrap';
import axios from 'axios';
import { toast } from 'react-toastify';

const TruckProfiles = () => {
  const [profiles, setProfiles] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editingProfile, setEditingProfile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const [formData, setFormData] = useState({
    name: '',
    description: '',
    height: '',
    width: '',
    length: '',
    maxWeight: '',
    maxAxleLoad: '',
    numberOfAxles: '',
    truckType: 'LIGHT_TRUCK',
    cargoType: 'GENERAL',
    emissionStandard: 'BS6',
    registrationNumber: '',
    hasNationalPermit: false,
    hasOversizePermit: false,
    hasHazmatPermit: false
  });

  useEffect(() => {
    fetchProfiles();
  }, []);

  const fetchProfiles = async () => {
    try {
      const response = await axios.get('/api/v1/truck-profiles');
      setProfiles(response.data);
    } catch (error) {
      toast.error('Error fetching truck profiles');
    }
  };

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === 'checkbox' ? checked : value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      if (editingProfile) {
        await axios.put(`/api/v1/truck-profiles/${editingProfile.id}`, formData);
        toast.success('Truck profile updated successfully');
      } else {
        await axios.post('/api/v1/truck-profiles', formData);
        toast.success('Truck profile created successfully');
      }
      
      setShowModal(false);
      resetForm();
      fetchProfiles();
    } catch (error) {
      setError(error.response?.data?.error || 'An error occurred');
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (profile) => {
    setEditingProfile(profile);
    setFormData({
      name: profile.name,
      description: profile.description || '',
      height: profile.height,
      width: profile.width,
      length: profile.length,
      maxWeight: profile.maxWeight,
      maxAxleLoad: profile.maxAxleLoad,
      numberOfAxles: profile.numberOfAxles,
      truckType: profile.truckType,
      cargoType: profile.cargoType,
      emissionStandard: profile.emissionStandard,
      registrationNumber: profile.registrationNumber || '',
      hasNationalPermit: profile.hasNationalPermit,
      hasOversizePermit: profile.hasOversizePermit,
      hasHazmatPermit: profile.hasHazmatPermit
    });
    setShowModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this truck profile?')) {
      try {
        await axios.delete(`/api/v1/truck-profiles/${id}`);
        toast.success('Truck profile deleted successfully');
        fetchProfiles();
      } catch (error) {
        toast.error('Error deleting truck profile');
      }
    }
  };

  const resetForm = () => {
    setFormData({
      name: '',
      description: '',
      height: '',
      width: '',
      length: '',
      maxWeight: '',
      maxAxleLoad: '',
      numberOfAxles: '',
      truckType: 'LIGHT_TRUCK',
      cargoType: 'GENERAL',
      emissionStandard: 'BS6',
      registrationNumber: '',
      hasNationalPermit: false,
      hasOversizePermit: false,
      hasHazmatPermit: false
    });
    setEditingProfile(null);
    setError('');
  };

  const handleCloseModal = () => {
    setShowModal(false);
    resetForm();
  };

  return (
    <Container className="mt-4">
      <Row>
        <Col>
          <div className="d-flex justify-content-between align-items-center mb-4">
            <h2>🚛 Truck Profiles</h2>
            <Button 
              variant="primary" 
              onClick={() => setShowModal(true)}
            >
              Add New Profile
            </Button>
          </div>
        </Col>
      </Row>

      <Row>
        <Col>
          <Card>
            <Card.Body>
              {profiles.length === 0 ? (
                <div className="text-center py-4">
                  <p>No truck profiles found. Create your first profile to get started.</p>
                </div>
              ) : (
                <Table responsive striped hover>
                  <thead>
                    <tr>
                      <th>Name</th>
                      <th>Type</th>
                      <th>Dimensions (H×W×L)</th>
                      <th>Max Weight</th>
                      <th>Registration</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {profiles.map((profile) => (
                      <tr key={profile.id}>
                        <td>
                          <strong>{profile.name}</strong>
                          {profile.description && (
                            <div className="text-muted small">{profile.description}</div>
                          )}
                        </td>
                        <td>{profile.truckType}</td>
                        <td>
                          {profile.height}m × {profile.width}m × {profile.length}m
                        </td>
                        <td>{profile.maxWeight}t</td>
                        <td>{profile.registrationNumber || 'N/A'}</td>
                        <td>
                          <Button 
                            variant="outline-primary" 
                            size="sm" 
                            className="me-2"
                            onClick={() => handleEdit(profile)}
                          >
                            Edit
                          </Button>
                          <Button 
                            variant="outline-danger" 
                            size="sm"
                            onClick={() => handleDelete(profile.id)}
                          >
                            Delete
                          </Button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {/* Add/Edit Modal */}
      <Modal show={showModal} onHide={handleCloseModal} size="lg">
        <Modal.Header closeButton>
          <Modal.Title>
            {editingProfile ? 'Edit Truck Profile' : 'Add New Truck Profile'}
          </Modal.Title>
        </Modal.Header>
        <Form onSubmit={handleSubmit}>
          <Modal.Body>
            {error && <Alert variant="danger">{error}</Alert>}
            
            <Row>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Profile Name *</Form.Label>
                  <Form.Control
                    type="text"
                    name="name"
                    value={formData.name}
                    onChange={handleInputChange}
                    required
                  />
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Registration Number</Form.Label>
                  <Form.Control
                    type="text"
                    name="registrationNumber"
                    value={formData.registrationNumber}
                    onChange={handleInputChange}
                  />
                </Form.Group>
              </Col>
            </Row>

            <Form.Group className="mb-3">
              <Form.Label>Description</Form.Label>
              <Form.Control
                as="textarea"
                rows={2}
                name="description"
                value={formData.description}
                onChange={handleInputChange}
              />
            </Form.Group>

            <Row>
              <Col md={4}>
                <Form.Group className="mb-3">
                  <Form.Label>Height (m) *</Form.Label>
                  <Form.Control
                    type="number"
                    step="0.1"
                    name="height"
                    value={formData.height}
                    onChange={handleInputChange}
                    required
                  />
                </Form.Group>
              </Col>
              <Col md={4}>
                <Form.Group className="mb-3">
                  <Form.Label>Width (m) *</Form.Label>
                  <Form.Control
                    type="number"
                    step="0.1"
                    name="width"
                    value={formData.width}
                    onChange={handleInputChange}
                    required
                  />
                </Form.Group>
              </Col>
              <Col md={4}>
                <Form.Group className="mb-3">
                  <Form.Label>Length (m) *</Form.Label>
                  <Form.Control
                    type="number"
                    step="0.1"
                    name="length"
                    value={formData.length}
                    onChange={handleInputChange}
                    required
                  />
                </Form.Group>
              </Col>
            </Row>

            <Row>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Max Weight (tonnes) *</Form.Label>
                  <Form.Control
                    type="number"
                    step="0.1"
                    name="maxWeight"
                    value={formData.maxWeight}
                    onChange={handleInputChange}
                    required
                  />
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Max Axle Load (tonnes) *</Form.Label>
                  <Form.Control
                    type="number"
                    step="0.1"
                    name="maxAxleLoad"
                    value={formData.maxAxleLoad}
                    onChange={handleInputChange}
                    required
                  />
                </Form.Group>
              </Col>
            </Row>

            <Row>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Number of Axles *</Form.Label>
                  <Form.Control
                    type="number"
                    name="numberOfAxles"
                    value={formData.numberOfAxles}
                    onChange={handleInputChange}
                    required
                  />
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Truck Type *</Form.Label>
                  <Form.Select
                    name="truckType"
                    value={formData.truckType}
                    onChange={handleInputChange}
                    required
                  >
                    <option value="MINI_TRUCK">Mini Truck</option>
                    <option value="LIGHT_TRUCK">Light Truck</option>
                    <option value="MEDIUM_TRUCK">Medium Truck</option>
                    <option value="HEAVY_TRUCK">Heavy Truck</option>
                    <option value="MULTI_AXLE">Multi Axle</option>
                    <option value="TRAILER">Trailer</option>
                  </Form.Select>
                </Form.Group>
              </Col>
            </Row>

            <Row>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Cargo Type</Form.Label>
                  <Form.Select
                    name="cargoType"
                    value={formData.cargoType}
                    onChange={handleInputChange}
                  >
                    <option value="GENERAL">General</option>
                    <option value="HAZMAT">Hazmat</option>
                    <option value="OVERSIZE">Oversize</option>
                    <option value="REFRIGERATED">Refrigerated</option>
                    <option value="LIQUID">Liquid</option>
                  </Form.Select>
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Emission Standard</Form.Label>
                  <Form.Select
                    name="emissionStandard"
                    value={formData.emissionStandard}
                    onChange={handleInputChange}
                  >
                    <option value="BS3">BS3</option>
                    <option value="BS4">BS4</option>
                    <option value="BS6">BS6</option>
                  </Form.Select>
                </Form.Group>
              </Col>
            </Row>

            <Row>
              <Col>
                <Form.Group className="mb-3">
                  <Form.Check
                    type="checkbox"
                    name="hasNationalPermit"
                    label="Has National Permit"
                    checked={formData.hasNationalPermit}
                    onChange={handleInputChange}
                  />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Check
                    type="checkbox"
                    name="hasOversizePermit"
                    label="Has Oversize Permit"
                    checked={formData.hasOversizePermit}
                    onChange={handleInputChange}
                  />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Check
                    type="checkbox"
                    name="hasHazmatPermit"
                    label="Has Hazmat Permit"
                    checked={formData.hasHazmatPermit}
                    onChange={handleInputChange}
                  />
                </Form.Group>
              </Col>
            </Row>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleCloseModal}>
              Cancel
            </Button>
            <Button variant="primary" type="submit" disabled={loading}>
              {loading ? 'Saving...' : (editingProfile ? 'Update' : 'Create')}
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
    </Container>
  );
};

export default TruckProfiles;