import { useContext, useEffect, useState } from "react"
import { useNavigate } from "react-router-dom";
import { User } from "../model";
import AuthContext from "./AuthContext";

interface Param {
    children?: any;
}

export default function AuthProvider({ children }: Param) {

    const [token, setToken] = useState('');
    const [user, setUser] = useState({} as User);

    const navigate = useNavigate();

    useEffect(() => {
        const query = new URLSearchParams(window.location.search);
        const jwt = query.get('jwt');
        if (jwt) {
            setToken(jwt);
            fetch(`${process.env.REACT_APP_BASE_URL}/api/users/me`, {
                headers: {
                    'Authorization': jwt
                }
            })
            .then(response => response.json())
            .then((user: User) => {
                setUser(user);
                setTimeout(() => navigate('/courses'));
            })
        }
    }, [navigate])

    const login = (email: string, password: string) => {
        return fetch(`${process.env.REACT_APP_BASE_URL}/api/users/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email: email,
                password: password
            })
        })
        .then(response => {
            if (response.status === 200) {
                return response.text();
            }
            throw new Error('error')
        })
        .then((token: string) => {
            setToken(token);
            fetch(`${process.env.REACT_APP_BASE_URL}/api/users/me`, {
                headers: {
                    'Authorization': token
                }
            })
            .then(response => response.json())
            .then((user: User) => {
                setUser(user);
                setTimeout(() => navigate('/courses'));
            })
        });
    }

    const logout = () => {

    };

    return (
        <AuthContext.Provider
            value={{
                token,
                user,
                login,
                logout
            }}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => useContext(AuthContext)