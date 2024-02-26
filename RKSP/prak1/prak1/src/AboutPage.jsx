// AboutPage.js
import React from 'react';
import Header from './components/Header';
import Navigation from './components/Navigation';
import './components/styles.css';
import Body from "./components/Body";

const AboutPage = () => {
    return (
        <div>
            <Header title="About" />
            <Navigation />
            <Body
                title="Welcome to about page!"
                text="Learn more about our company and our mission."
            >
            </Body>
        </div>
    );
};

export default AboutPage;
