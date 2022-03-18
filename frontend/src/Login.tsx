import { useState } from "react";
import { useTranslation } from "react-i18next";
import { Link, useNavigate } from "react-router-dom";
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
        <div className="flex justify-around w-full">
            <div>
                <h2 className="text-xl pb-5 font-bold">{t('headlineLogin')}</h2>
                <div>
                    <Input placeholder={t('email')} value={email} onChange={setEmail} onKeyUp={ev => {if(ev.key === 'Enter') {login()}}} additionalCss="mr-4" />
                    <Input placeholder={t('password')} value={password} onChange={setPassword} onKeyUp={ev => {if(ev.key === 'Enter') {login()}}} type="password" additionalCss="mr-4" />
                    <Button label={t('buttonLogin')} onClick={login} />
                </div>
                <div>
                    <a href="https://github.com/login/oauth/authorize?client_id=ac870a1600ec03b7be10">{t('thirdPartyLogin', {company: 'GitHub'})}</a>
                </div>
                <div className="mt-4">
                    <span>{t('goToRegister')}</span>
                    <Link to="/registration">{t('registerLinkText')}</Link>
                </div>
            </div>
        </div>
    )
}