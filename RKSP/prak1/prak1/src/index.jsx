import React, { StrictMode } from "react";
import * as ReactDOMClient from "react-dom/client";
import Header from "./components/Header";
import Body from "./components/Body";
import App from './App';

const app = ReactDOMClient.createRoot(document.getElementById("app"));

app.render(
  <StrictMode>
      <App></App>
    {/*<Header header="Автосалон Black Lightning" />
    <Body
      title="В продаже новые автомобили"
      text="Новые немецкие автомобили поступили в продажу в нашем автосалоне. Гарантия 20 лет."
    />*/}
  </StrictMode>
);
