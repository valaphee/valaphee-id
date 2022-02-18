/*
 * Copyright (c) 2021-2022, GrieferGames, Valaphee.
 * All rights reserved.
 */

import React, {lazy, Suspense} from 'react'
import ReactDOM from 'react-dom';
import './index.scss';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import { Helmet } from 'react-helmet';
import config from './config';

const routes = [
  {
    path: '/oauth/authorize',
    Component: lazy(() => import('./pages/Approval')),
    exact: true,
  },
  {
    path: '/sign-in',
    Component: lazy(() => import('./pages/SignIn')),
    exact: true,
  },
  {
    path: '/sign-up',
    Component: lazy(() => import('./pages/SignUp')),
    exact: true,
  },
  {
    path: '/change-password',
    Component: lazy(() => import('./pages/ChangePassword')),
    exact: true,
  },
  {
    path: '/forgot-password',
    Component: lazy(() => import('./pages/ForgotPassword')),
    exact: true,
  },
]

ReactDOM.render(
  <>
    <Helmet titleTemplate={`${config.brandName} | %s`} />
    <div style={{ background: '#eceff4', padding: '50px 20px', color: '#514d6a', borderRadius: '5px' }}>
      <div style={{ maxWidth: '400px', margin: '0px auto', fontSize: '14px' }}>
        <table cellPadding="0" cellSpacing="0" style={{ width: '100%', marginBottom: '20px', border: '0px' }}>
          <tbody>
            <tr>
              <td style={{ verticalAlign: 'top' }}>
                <h4>{config.brandLogo ? <img src={config.brandLogo} alt={config.brandName} style={{ height: '40px', marginRight: '10px' }}/> : <strong>{config.brandName}</strong>}</h4>
              </td>
            </tr>
          </tbody>
        </table>
        <div style={{ padding: '40px 40px 20px 40px', background: '#fff' }}>
          <table cellPadding="0" cellSpacing="0" style={{ width: '100%', border: '0px' }}>
            <tbody>
              <tr>
                <td>
                  <BrowserRouter basename={config.baseUrl}>
                    <Switch>
                      {routes.map(({path, Component, exact}) => (
                        <Route path={path} key={path} exact={exact} render={() =>
                          <Suspense fallback={null}>
                            <Component/>
                          </Suspense>
                        } />
                      ))}
                    </Switch>
                  </BrowserRouter>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div style={{ textAlign: 'center', fontSize: '12px', color: '#a09bb9', marginTop: '20px' }}>
          <p>Copyright (c) 2021, Valaphee.</p>
        </div>
      </div>
    </div>
  </>,
  document.getElementById('root'),
);
