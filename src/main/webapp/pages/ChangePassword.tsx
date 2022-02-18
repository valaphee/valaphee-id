/*
 * Copyright (c) 2021-2022, GrieferGames, Valaphee.
 * All rights reserved.
 */

import React, { useState } from 'react';
import {
  Form, Button, Alert, Spinner, ProgressBar,
} from 'react-bootstrap';
import axios from 'axios';
import { Formik } from 'formik';
import * as Yup from 'yup';
import zxcvbn from 'zxcvbn';
import { Redirect } from 'react-router-dom';
import { Helmet } from 'react-helmet';

const ChangePasswordFormSchema = Yup.object().shape({
  password: Yup.string().required().min(8).max(64),
  confirmPassword: Yup.string().required().oneOf([Yup.ref('password')]),
});

export default function ChangePassword() {
  const token = new URLSearchParams(location.search).get('token');

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  async function onSubmit(values: {}) {
    setLoading(true);
    await axios.post(`change-password?token=${token}`, values).then(() => setSuccess(true)).catch((reason) => setError(reason.response.data.errorMessage)).finally(() => setLoading(false));
  }

  function getPasswordStrengthBarVariant(score: Number) {
    switch (score) {
      case 2:
      case 3:
        return 'warning';
      case 4:
        return 'success';
      default:
        return 'danger';
    }
  }

  return (
    <>
      {success ? <Redirect to="sign-in?ref=change-password" /> : null}
      <Helmet title="Change password" />
      <h5 style={{ marginBottom: '20px', color: '#24222f', fontWeight: 600 }}>Change password</h5>
      <Formik
        initialValues={{
          password: '',
          confirmPassword: '',
        }}
        validationSchema={ChangePasswordFormSchema}
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
            <Form.Group controlId="password">
              <Form.Label>Password</Form.Label>
              <Form.Control type="password" name="password" value={values.password} onBlur={handleBlur} onChange={handleChange} isValid={touched.password && !errors.password} style={{ borderBottomLeftRadius: 0, borderBottomRightRadius: 0 }} />
              <ProgressBar now={zxcvbn(values.password).score} max={4} variant={getPasswordStrengthBarVariant(zxcvbn(values.password).score)} style={{ borderTopLeftRadius: 0, borderTopRightRadius: 0 }} />
            </Form.Group>
            <Form.Group controlId="confirmPassword">
              <Form.Label>Confirm Password</Form.Label>
              <Form.Control type="password" name="confirmPassword" value={values.confirmPassword} placeholder="Confirm Password" onBlur={handleBlur} onChange={handleChange} isValid={touched.confirmPassword && !errors.confirmPassword} />
            </Form.Group>
            <Button block type="submit" disabled={loading || !isValid}>{loading ? <Spinner animation="border" as="span" size="sm" /> : 'Change password'}</Button>
          </Form>
        )}
      </Formik>
    </>
  );
}
