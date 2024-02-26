// HomePage.js
import React from 'react';
import Header from './components/Header';
import Navigation from './components/Navigation';
import Body from './components/Body';
import './components/styles.css';

const HomePage = () => {
    return (
        <div>
            <Header title="Home page" />
            <Navigation />
            <Body
            title="Welcome to home page!"
            text="This is the homepage of our awesome website."
            >
            </Body>
        </div>
    );
};

export default HomePage;
