import React from 'react';

const Dialog = ({ id, name }) => {
    return (
        <div>
            <h3>Dialog: {name}</h3>
            <p>ID: {id}</p>
        </div>
    );
};

export default Dialog;
