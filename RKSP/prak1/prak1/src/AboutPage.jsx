// AboutPage.js
import React from 'react';
import Header from './components/Header';
import Navigation from './components/Navigation';
import './components/styles.css';
import Body from "./components/Body";
import Dialog from './components/Dialog';

const AboutPage = () => {
    const dialogs = [
        { id: 1, name: 'Dialog 1' },
        { id: 2, name: 'Dialog 2' },
        { id: 3, name: 'Dialog 3' },
    ];
    return (
        <div>
            <Header title="About" />
            <Navigation />
            <Body
                title="Welcome to about page!"
                text="Learn more about our company and our mission."
            >
            </Body>

            <div>
                {dialogs.map(dialog => (
                    <Dialog key={dialog.id} id={dialog.id} name={dialog.name} />
                ))}
            </div>
        </div>
    );
};

export default AboutPage;
