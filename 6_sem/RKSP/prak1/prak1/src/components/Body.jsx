import React from "react";
import "./styles.css";

function Body(props) {
  return (
    <div >
      <h2>{props.title}</h2>
      <p>{props.text}</p>
    </div>
  );
}
export default Body;
