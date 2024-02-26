// ContactPage.js
import React from 'react';
import Header from './components/Header';
import Navigation from './components/Navigation';
import './components/styles.css';
import Body from "./components/Body";

const ContactPage = () => {
    return (
        <div>
            <Header title="Contact" />
            <Navigation />
            <Body
                title="Welcome to contact page!"
                text="Get in touch with us for any inquiries or feedback."
            >
            </Body>
        </div>
    );
};

export default ContactPage;
