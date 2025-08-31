import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Form, Button, Alert, Table, Badge } from 'react-bootstrap';
import axios from 'axios';
import { toast } from 'react-toastify';

const RouteCalculation = () => {
  const [profiles, setProfiles] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [routeResults, setRouteResults] = useState(null);

  const [formData, setFormData] = useState({
    startLatitude: '',
    startLongitude: '',
    startAddress: '',
    endLatitude: '',
    endLongitude: '',
    endAddress: '',
    truckProfileId: '',
    optimizationType: 'FASTEST',
    routeName: ''
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
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setRouteResults(null);

    try {
      const response = await axios.post('/api/v1/routes/calculate', formData);
      setRouteResults(response.data);
      toast.success('Route calculated successfully!');
    } catch (error) {
      setError(error.response?.data?.error || 'An error occurred while calculating route');
    } finally {
      setLoading(false);
    }
  };

  const formatDuration = (minutes) => {
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return `${hours}h ${mins}m`;
  };

  const formatCurrency = (amount) => {
    return `₹${parseFloat(amount).toFixed(2)}`;
  };

  const getOptimizationBadge = (type) => {
    const badges = {
      FASTEST: { variant: 'primary', text: 'Fastest' },
      SHORTEST: { variant: 'success', text: 'Shortest' },
      FUEL_EFFICIENT: { variant: 'warning', text: 'Fuel Efficient' },
      AVOID_TOLLS: { variant: 'info', text: 'Toll-Free' }
    };
    return badges[type] || { variant: 'secondary', text: type };
  };

  const getTrafficBadge = (level) => {
    const badges = {
      LOW: { variant: 'success', text: 'Low Traffic' },
      MEDIUM: { variant: 'warning', text: 'Medium Traffic' },
      HIGH: { variant: 'danger', text: 'High Traffic' }
    };
    return badges[level] || { variant: 'secondary', text: level };
  };

  return (
    <Container className="mt-4">
      <Row>
        <Col>
          <h2>🗺️ Route Calculation</h2>
          <p className="text-muted">Calculate optimized routes for your truck considering restrictions and traffic.</p>
        </Col>
      </Row>

      <Row>
        <Col lg={6}>
          <Card>
            <Card.Header>
              <h5>Route Details</h5>
            </Card.Header>
            <Card.Body>
              {error && <Alert variant="danger">{error}</Alert>}
              
              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3">
                  <Form.Label>Route Name (Optional)</Form.Label>
                  <Form.Control
                    type="text"
                    name="routeName"
                    value={formData.routeName}
                    onChange={handleInputChange}
                    placeholder="Enter route name"
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Truck Profile *</Form.Label>
                  <Form.Select
                    name="truckProfileId"
                    value={formData.truckProfileId}
                    onChange={handleInputChange}
                    required
                  >
                    <option value="">Select a truck profile</option>
                    {profiles.map((profile) => (
                      <option key={profile.id} value={profile.id}>
                        {profile.name} ({profile.truckType})
                      </option>
                    ))}
                  </Form.Select>
                </Form.Group>

                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Start Latitude *</Form.Label>
                      <Form.Control
                        type="number"
                        step="any"
                        name="startLatitude"
                        value={formData.startLatitude}
                        onChange={handleInputChange}
                        required
                        placeholder="e.g., 28.6139"
                      />
                    </Form.Group>
                  </Col>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Start Longitude *</Form.Label>
                      <Form.Control
                        type="number"
                        step="any"
                        name="startLongitude"
                        value={formData.startLongitude}
                        onChange={handleInputChange}
                        required
                        placeholder="e.g., 77.2090"
                      />
                    </Form.Group>
                  </Col>
                </Row>

                <Form.Group className="mb-3">
                  <Form.Label>Start Address</Form.Label>
                  <Form.Control
                    type="text"
                    name="startAddress"
                    value={formData.startAddress}
                    onChange={handleInputChange}
                    placeholder="e.g., New Delhi, India"
                  />
                </Form.Group>

                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>End Latitude *</Form.Label>
                      <Form.Control
                        type="number"
                        step="any"
                        name="endLatitude"
                        value={formData.endLatitude}
                        onChange={handleInputChange}
                        required
                        placeholder="e.g., 19.0760"
                      />
                    </Form.Group>
                  </Col>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>End Longitude *</Form.Label>
                      <Form.Control
                        type="number"
                        step="any"
                        name="endLongitude"
                        value={formData.endLongitude}
                        onChange={handleInputChange}
                        required
                        placeholder="e.g., 72.8777"
                      />
                    </Form.Group>
                  </Col>
                </Row>

                <Form.Group className="mb-3">
                  <Form.Label>End Address</Form.Label>
                  <Form.Control
                    type="text"
                    name="endAddress"
                    value={formData.endAddress}
                    onChange={handleInputChange}
                    placeholder="e.g., Mumbai, India"
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Optimization Type</Form.Label>
                  <Form.Select
                    name="optimizationType"
                    value={formData.optimizationType}
                    onChange={handleInputChange}
                  >
                    <option value="FASTEST">Fastest Route</option>
                    <option value="SHORTEST">Shortest Route</option>
                    <option value="FUEL_EFFICIENT">Fuel Efficient</option>
                    <option value="AVOID_TOLLS">Avoid Tolls</option>
                  </Form.Select>
                </Form.Group>

                <Button 
                  variant="primary" 
                  type="submit" 
                  className="w-100"
                  disabled={loading || profiles.length === 0}
                >
                  {loading ? 'Calculating Route...' : 'Calculate Route'}
                </Button>

                {profiles.length === 0 && (
                  <Alert variant="warning" className="mt-3">
                    Please create a truck profile first to calculate routes.
                  </Alert>
                )}
              </Form>
            </Card.Body>
          </Card>
        </Col>

        <Col lg={6}>
          {routeResults && (
            <Card>
              <Card.Header>
                <h5>Route Options</h5>
                <small className="text-muted">
                  {routeResults.restrictionsFound} restrictions found
                </small>
              </Card.Header>
              <Card.Body>
                <Table responsive>
                  <thead>
                    <tr>
                      <th>Route</th>
                      <th>Distance</th>
                      <th>Duration</th>
                      <th>Fuel Cost</th>
                      <th>Toll Cost</th>
                    </tr>
                  </thead>
                  <tbody>
                    {routeResults.routeOptions.map((option, index) => (
                      <tr key={index}>
                        <td>
                          <div>
                            <strong>{option.name}</strong>
                            <div className="mt-1">
                              <Badge 
                                bg={getOptimizationBadge(option.optimizationType).variant}
                                className="me-1"
                              >
                                {getOptimizationBadge(option.optimizationType).text}
                              </Badge>
                              <Badge bg={getTrafficBadge(option.trafficLevel).variant}>
                                {getTrafficBadge(option.trafficLevel).text}
                              </Badge>
                            </div>
                          </div>
                        </td>
                        <td>{parseFloat(option.totalDistance).toFixed(1)} km</td>
                        <td>{formatDuration(option.estimatedDuration)}</td>
                        <td>{formatCurrency(option.estimatedFuelCost)}</td>
                        <td>{formatCurrency(option.estimatedTollCost)}</td>
                      </tr>
                    ))}
                  </tbody>
                </Table>

                <div className="mt-3">
                  <h6>Route Summary</h6>
                  <ul className="list-unstyled">
                    <li><strong>Calculated at:</strong> {new Date(routeResults.calculatedAt).toLocaleString()}</li>
                    <li><strong>Restrictions found:</strong> {routeResults.restrictionsFound}</li>
                    <li><strong>Truck profile used:</strong> {routeResults.truckProfileUsed}</li>
                  </ul>
                </div>
              </Card.Body>
            </Card>
          )}

          {!routeResults && (
            <Card>
              <Card.Body className="text-center text-muted">
                <h5>Route Results</h5>
                <p>Enter route details and click "Calculate Route" to see optimized route options.</p>
              </Card.Body>
            </Card>
          )}
        </Col>
      </Row>

      <Row className="mt-4">
        <Col>
          <Card>
            <Card.Header>
              <h6>📍 Sample Coordinates for Testing</h6>
            </Card.Header>
            <Card.Body>
              <Row>
                <Col md={6}>
                  <strong>New Delhi:</strong> 28.6139, 77.2090<br/>
                  <strong>Mumbai:</strong> 19.0760, 72.8777<br/>
                  <strong>Bangalore:</strong> 12.9716, 77.5946
                </Col>
                <Col md={6}>
                  <strong>Chennai:</strong> 13.0827, 80.2707<br/>
                  <strong>Kolkata:</strong> 22.5726, 88.3639<br/>
                  <strong>Hyderabad:</strong> 17.3850, 78.4867
                </Col>
              </Row>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default RouteCalculation;