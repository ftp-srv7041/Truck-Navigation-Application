import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import axios from 'axios';

const Dashboard = () => {
  const { user } = useAuth();
  const [stats, setStats] = useState({
    activeProfileCount: 0,
    maxProfiles: 10,
    canCreateMore: true,
    remainingSlots: 10
  });

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      const response = await axios.get('/api/v1/truck-profiles/stats');
      setStats(response.data);
    } catch (error) {
      console.error('Error fetching stats:', error);
    }
  };

  return (
    <Container className="mt-4">
      <Row>
        <Col>
          <h2>Welcome to Truck Navigation System</h2>
          <p className="text-muted">Hello {user?.fullName}, manage your truck profiles and calculate optimized routes.</p>
        </Col>
      </Row>

      <Row className="mt-4">
        <Col md={4}>
          <Card className="h-100">
            <Card.Body className="text-center">
              <Card.Title>🚛 Truck Profiles</Card.Title>
              <Card.Text>
                Active Profiles: {stats.activeProfileCount}/{stats.maxProfiles}
              </Card.Text>
              <Card.Text>
                Remaining Slots: {stats.remainingSlots}
              </Card.Text>
              <Link to="/truck-profiles">
                <Button variant="primary">Manage Profiles</Button>
              </Link>
            </Card.Body>
          </Card>
        </Col>

        <Col md={4}>
          <Card className="h-100">
            <Card.Body className="text-center">
              <Card.Title>🗺️ Route Calculation</Card.Title>
              <Card.Text>
                Calculate optimized routes considering truck restrictions and traffic conditions.
              </Card.Text>
              <Link to="/route-calculation">
                <Button variant="success">Calculate Route</Button>
              </Link>
            </Card.Body>
          </Card>
        </Col>

        <Col md={4}>
          <Card className="h-100">
            <Card.Body className="text-center">
              <Card.Title>📊 Quick Stats</Card.Title>
              <Card.Text>
                <strong>Role:</strong> {user?.role}<br/>
                <strong>Email:</strong> {user?.email}
              </Card.Text>
              <Button variant="info" disabled>
                View Reports
              </Button>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row className="mt-4">
        <Col>
          <Card>
            <Card.Header>
              <h5>🚀 Getting Started</h5>
            </Card.Header>
            <Card.Body>
              <Row>
                <Col md={6}>
                  <h6>1. Create Truck Profile</h6>
                  <p>Add your truck specifications including dimensions, weight, and permits.</p>
                </Col>
                <Col md={6}>
                  <h6>2. Calculate Routes</h6>
                  <p>Get optimized routes that consider truck restrictions and road conditions.</p>
                </Col>
              </Row>
              <Row>
                <Col md={6}>
                  <h6>3. Save Favorite Routes</h6>
                  <p>Save frequently used routes for quick access.</p>
                </Col>
                <Col md={6}>
                  <h6>4. Monitor Restrictions</h6>
                  <p>Stay updated with road restrictions and traffic conditions.</p>
                </Col>
              </Row>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Dashboard;