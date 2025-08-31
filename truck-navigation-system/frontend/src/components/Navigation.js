import React from 'react';
import { Navbar, Nav, NavDropdown, Container } from 'react-bootstrap';
import { LinkContainer } from 'react-router-bootstrap';
import { useAuth } from '../contexts/AuthContext';

const Navigation = () => {
  const { user, logout } = useAuth();

  const handleLogout = () => {
    logout();
  };

  return (
    <Navbar bg="primary" variant="dark" expand="lg" className="mb-3">
      <Container fluid>
        <LinkContainer to="/dashboard">
          <Navbar.Brand>
            🚛 Truck Navigation India
          </Navbar.Brand>
        </LinkContainer>
        
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <LinkContainer to="/dashboard">
              <Nav.Link>Dashboard</Nav.Link>
            </LinkContainer>
            <LinkContainer to="/route-calculation">
              <Nav.Link>Plan Route</Nav.Link>
            </LinkContainer>
            <LinkContainer to="/truck-profiles">
              <Nav.Link>Truck Profiles</Nav.Link>
            </LinkContainer>
            <LinkContainer to="/saved-routes">
              <Nav.Link>Saved Routes</Nav.Link>
            </LinkContainer>
          </Nav>
          
          <Nav>
            <NavDropdown 
              title={`👤 ${user?.fullName || user?.username}`} 
              id="user-dropdown"
              align="end"
            >
              <NavDropdown.Item href="#profile">
                View Profile
              </NavDropdown.Item>
              <NavDropdown.Item href="#settings">
                Settings
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item onClick={handleLogout}>
                Logout
              </NavDropdown.Item>
            </NavDropdown>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Navigation;
