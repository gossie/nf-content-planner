import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import Button from "./common-elements/Button";
import Input from "./common-elements/Input";

export default function Login() {

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const { t } = useTranslation();
    const navigate = useNavigate();

    const login = () => {
        fetch(`${process.env.REACT_APP_BASE_URL}/api/users/login`, {
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
            localStorage.setItem('jwt', token);
            navigate('/courses');
        });
    };

    return (
        <div>
            <h2 className="text-xl pb-5 font-bold">{t('headlineLogin')}</h2>
            <div>
                <Input placeholder={t('email')} value={email} onChange={setEmail} />
                <Input placeholder={t('password')} value={password} onChange={setPassword} type="password" />
                <Button label={t('buttonLogin')} onClick={login} />
            </div>
            <div>
                <a href="/oauth2/authorization/github">GitHub</a>
            </div>
        </div>
    )
}