import React, { StrictMode } from "react";
import * as ReactDOMClient from "react-dom/client";
import Header from "./components/Header";
import Body from "./components/Body";
import App from './App';

const app = ReactDOMClient.createRoot(document.getElementById("app"));

app.render(
  <StrictMode>
      <App></App>
  </StrictMode>
);
