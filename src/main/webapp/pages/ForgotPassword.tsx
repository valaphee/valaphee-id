/*
 * Copyright (c) 2021-2022, Valaphee.
 * All rights reserved.
 */

import React, { useState } from 'react';
import {
  Form, Button, Alert, Spinner,
} from 'react-bootstrap';
import axios from 'axios';
import { Redirect } from 'react-router-dom';
import { Formik } from 'formik';
import * as Yup from 'yup';
import { Helmet } from 'react-helmet';

const ForgotPasswordFormSchema = Yup.object().shape({
  username: Yup.string().required().min(5).max(24),
});

export default function ForgotPassword() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  async function onSubmit(values: {}) {
    setLoading(true);
    await axios.post('forgot-password', values).then(() => setSuccess(true)).catch((reason) => setError(reason.response.data.errorMessage)).finally(() => setLoading(false));
  }

  return (
    <>
      {success ? <Redirect to="sign-in?ref=forgot-password" /> : null}
      <Helmet title="Forgot password" />
      <h5 style={{ marginBottom: '20px', color: '#24222f', fontWeight: 600 }}>Forgot password</h5>
      <Formik
        initialValues={{
          username: '',
        }}
        validationSchema={ForgotPasswordFormSchema}
        onSubmit={(values) => onSubmit(values)}
      >
        {({
            handleSubmit,
            handleChange,
            handleBlur,
            values,
            touched,
            isValid,
            errors,
          }) => (
          <Form noValidate onSubmit={handleSubmit}>
            {error ? error.split(',').map((item, index) => <Alert variant="danger" key={index}>{item}</Alert>) : null}
            <Form.Group controlId="username">
              <Form.Label>Username or E-Mail</Form.Label>
              <Form.Control autoFocus type="text" name="username" value={values.username} onBlur={handleBlur} onChange={handleChange} isValid={touched.username && !errors.username} />
            </Form.Group>
            <Button block type="submit" disabled={loading || !isValid}>{loading ? <Spinner animation="border" as="span" size="sm" /> : 'Change password'}</Button>
          </Form>
        )}
      </Formik>
    </>
  );
}
