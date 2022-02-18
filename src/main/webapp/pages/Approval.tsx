/*
 * Copyright (c) 2021-2022, GrieferGames, Valaphee.
 * All rights reserved.
 */

import React, { useState } from 'react';
import {
  Form, Button, Spinner, Col, Container, Row,
} from 'react-bootstrap';
import { Helmet } from 'react-helmet';

const csrfCookie = document.cookie.split('; ').find((row) => row.startsWith('XSRF-TOKEN='));
const csrf = csrfCookie ? csrfCookie.split('=')[1] : null;

export default function Approval() {
  const [loading, setLoading] = useState(false);

  return (
    <>
      <Helmet title="Approve" />
      <h5 style={{ marginBottom: '20px', color: '#24222f', fontWeight: 600 }}>App wants to access your account</h5>
      <Container>
        <Row>
          <Col>
            <Form method="POST" action="oauth/authorize" noValidate onSubmit={() => setLoading(true)}>
              <Button block type="submit" disabled={loading}>{loading ? <Spinner animation="border" as="span" size="sm" /> : 'Allow'}</Button>
              <input type="hidden" name="user_oauth_approval" value="true" />
              {csrf ? <input type="hidden" name="_csrf" value={csrf} /> : null}
            </Form>
          </Col>
          <Col>
            <Form method="POST" action="oauth/authorize" noValidate onSubmit={() => setLoading(true)}>
              <Button block type="submit" disabled={loading}>{loading ? <Spinner animation="border" as="span" size="sm" /> : 'Cancel'}</Button>
              <input type="hidden" name="user_oauth_approval" value="false" />
              {csrf ? <input type="hidden" name="_csrf" value={csrf} /> : null}
            </Form>
          </Col>
        </Row>
      </Container>
    </>
  );
}
