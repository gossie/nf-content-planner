import { ReactNode, useContext, useState } from "react"
import { useNavigate } from "react-router-dom";
import { User } from "../model";
import AuthContext from "./AuthContext";

export default function AuthProvider({ children }) {

    const [token, setToken] = useState('');
    const [user, setUser] = useState({} as User);

    const navigate = useNavigate();

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
            fetch(`${process.env.REACT_APP_BASE_URL}/api/users/me`, {
                headers: {
                    'Authorization': token
                }
            })
            .then(response => response.json())
            .then((user: User) => {
                setToken(token);
                setUser(user);
                navigate('/courses');
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