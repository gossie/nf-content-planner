import { createContext } from 'react'
import { User } from '../model';

export interface Context {
    token: string;
    user: User,
    login: (username: string, password: string) => Promise<void>;
    logout: () => void;
}

export default createContext({} as Context)