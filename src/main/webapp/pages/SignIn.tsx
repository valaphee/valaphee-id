/*
 * Copyright (c) 2021-2022, GrieferGames, Valaphee.
 * All rights reserved.
 */

import React, { useState } from 'react';
import {
  Form, Button, Alert, Spinner,
} from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { Formik } from 'formik';
import * as Yup from 'yup';
import { Helmet } from 'react-helmet';

const csrfCookie = document.cookie.split('; ').find((row) => row.startsWith('XSRF-TOKEN='));
const csrf = csrfCookie ? csrfCookie.split('=')[1] : null;

const SignInFormSchema = Yup.object().shape({
  username: Yup.string().min(5).max(24).required(),
  password: Yup.string().min(8).max(64).required(),
});

export default function SignIn() {
  const [loading, setLoading] = useState(false);
  const message = location.search.includes('?error') ? <Alert variant="danger">Incorrect username or password.</Alert> : location.search.includes('ref=sign-out') ? <Alert variant="success">Successfully signed out.</Alert> : location.search.includes('ref=sign-up') ? <Alert variant="success">Successfully signed up. Link for setting the password has been sent.</Alert> : location.search.includes('ref=change-password') ? <Alert variant="success">Successfully changed password.</Alert> : location.search.includes('ref=forgot-password') ? <Alert variant="success">Link to change password has been sent.</Alert> : null;

  return (
    <>
      <Helmet title="Sign-in" />
      <h5 style={{ marginBottom: '20px', color: '#24222f', fontWeight: 600 }}>Sign-in</h5>
      <Formik
        initialValues={{
          username: '',
          password: '',
        }}
        validationSchema={SignInFormSchema}
        onSubmit={(values, formikHelpers) => formikHelpers.setSubmitting(true)}
      >
        {({
          handleChange,
          handleBlur,
          values, touched,
          isValid,
          errors,
        }) => (
          <Form method="POST" action="sign-in" noValidate onSubmit={() => setLoading(true)}>
            {message}
            <Form.Group controlId="username">
              <Form.Label>Username or E-Mail</Form.Label>
              <Form.Control autoFocus type="text" name="username" value={values.username} onBlur={handleBlur} onChange={handleChange} isValid={touched.username && !errors.username} />
            </Form.Group>
            <Form.Group controlId="password">
              <Form.Label>Password</Form.Label>
              <Form.Control type="password" name="password" value={values.password} onBlur={handleBlur} onChange={handleChange} isValid={touched.password && !errors.password} />
              <Form.Text>
                Forgot your password?
                {' '}
                <Link to="forgot-password">Change password</Link>
              </Form.Text>
            </Form.Group>
            <Button block type="submit" disabled={loading || !isValid}>{loading ? <Spinner animation="border" as="span" size="sm" /> : 'Sign-in'}</Button>
            <Form.Text>
              Don&apos;t have an account?
              {' '}
              <Link to="sign-up">Sign-up</Link>
            </Form.Text>
            {csrf ? <input type="hidden" name="_csrf" value={csrf} /> : null}
          </Form>
        )}
      </Formik>
    </>
  );
}
