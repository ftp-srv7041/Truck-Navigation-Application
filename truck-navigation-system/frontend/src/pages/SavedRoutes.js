import React from 'react';
import { Container, Row, Col, Card } from 'react-bootstrap';

const SavedRoutes = () => {
  return (
    <Container fluid>
      <Row className="mb-4">
        <Col>
          <h1 className="h2 mb-0">Saved Routes</h1>
          <p className="text-muted">Access your frequently used routes</p>
        </Col>
      </Row>

      <Row>
        <Col>
          <Card>
            <Card.Body className="text-center py-5">
              <h4>📁 Saved Routes</h4>
              <p className="text-muted">
                Your saved routes will appear here once you start planning routes.
              </p>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default SavedRoutes;
