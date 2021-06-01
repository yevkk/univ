import React from 'react';
import ReactDOM from 'react-dom';
import App from './app/App';

export let serverURL = 'http://192.168.1.26:8010'

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById('root')
);
