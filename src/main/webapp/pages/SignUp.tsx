/*
 * Copyright (c) 2021-2022, Valaphee.
 * All rights reserved.
 */

import React, { useState } from 'react';
import {
  Form, Button, Alert, Spinner,
} from 'react-bootstrap';
import axios from 'axios';
import { Link, Redirect } from 'react-router-dom';
import { Formik } from 'formik';
import * as Yup from 'yup';
import { Helmet } from 'react-helmet';
import { GoogleReCaptchaProvider } from 'react-google-recaptcha-v3';

const SignUpFormSchema = Yup.object().shape({
  username: Yup.string().required().min(5).max(24),
  email: Yup.string().required().email(),
  terms: Yup.bool().required().oneOf([true]),
});

export default function SignUp() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  async function onSubmit(values: {}) {
    setLoading(true);
    await axios.post('sign-up', values).then(() => setSuccess(true)).catch((reason) => setError(reason.response.data.errorMessage)).finally(() => setLoading(false));
  }

  return (
    <>
      {success ? <Redirect to="sign-in?ref=sign-up" /> : null}
      <Helmet title="Sign-up" />
      <h5 style={{ marginBottom: '20px', color: '#24222f', fontWeight: 600 }}>Sign-up</h5>
      <Formik
        initialValues={{
          username: '',
          email: '',
          terms: false,
        }}
        validationSchema={SignUpFormSchema}
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
          <GoogleReCaptchaProvider
            reCaptchaKey="6LdDrS4dAAAAACS_RLFLN-j1uXbqiybdg5iHJGim"
          >
            <Form noValidate onSubmit={handleSubmit}>
              {error ? error.split(',').map((item, index) => <Alert variant="danger" key={index}>{item}</Alert>) : null}
              <Form.Group controlId="username">
                <Form.Label>Username</Form.Label>
                <Form.Control autoFocus type="text" name="username" value={values.username} onBlur={handleBlur} onChange={handleChange} isValid={touched.username && !errors.username} />
              </Form.Group>
              <Form.Group controlId="email">
                <Form.Label>E-Mail</Form.Label>
                <Form.Control type="email" name="email" value={values.email} onBlur={handleBlur} onChange={handleChange} isValid={touched.email && !errors.email} />
              </Form.Group>
              <Form.Group><Form.Check name="terms" label="Agree to terms and conditions" onBlur={handleBlur} onChange={handleChange} /></Form.Group>

              <Button block type="submit" disabled={loading || !isValid}>{loading ? <Spinner animation="border" as="span" size="sm" /> : 'Sign-up'}</Button>
              <Form.Text>
                Already have an account?
                {' '}
                <Link to="sign-in">Sign-in</Link>
              </Form.Text>
            </Form>
          </GoogleReCaptchaProvider>
        )}
      </Formik>
    </>
  );
}
