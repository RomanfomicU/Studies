import React, { useState } from 'react';
import axios from 'axios';
import { generateCodeVerifier, sha256 } from './pkce';

const LoginForm = ({ onLogin }) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        const codeVerifier = generateCodeVerifier();
        const codeChallenge = await sha256(codeVerifier);

        try {
            const response = await axios.post('http://localhost:8081/login', {
                username,
                password,
                code_challenge: codeChallenge,
            });
            onLogin(response.data);
        } catch (error) {
            console.error('Login failed:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="text"
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
            />
            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <button type="submit">Login</button>
        </form>
    );
};

export default LoginForm;
