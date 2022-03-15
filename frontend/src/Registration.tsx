import { useState } from "react";
import { useTranslation } from "react-i18next"
import { useNavigate } from "react-router-dom";
import Button from "./common-elements/Button";
import Input from "./common-elements/Input";

export default function Registration() {

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [passwordAgain, setPasswordAgain] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const {t} = useTranslation();
    const navigate = useNavigate();

    const register = () => {
        fetch(`${process.env.REACT_APP_BASE_URL}/api/users`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email: email,
                password: password,
                passwordAgain: passwordAgain
            })
        })
        .then(response => {
            if (response.status === 201) {
                return response.json();
            }
            throw new Error(response.status === 400 ? 'errorPasswordMismatch' : response.status === 409 ? 'errorEmailExists' : '')
        })
        .then(() => navigate('/login'))
        .catch(e => setErrorMessage(e.message));
    };

    return (
        <div>
            <h2 className="text-xl pb-5 font-bold">{t('headlineRegistration')}</h2>
            <div>
                <Input placeholder={t('email')} value={email} onChange={setEmail} />
                <Input placeholder={t('password')} value={password} onChange={setPassword} type="password" />
                <Input placeholder={t('passwordAgain')} value={passwordAgain} onChange={setPasswordAgain} type="password" />
                <Button label={t('buttonRegistration')} onClick={register} />
                { errorMessage && t(errorMessage) }
            </div>
            <div>
                <a href="/oauth2/authorization/github">GitHub</a>
            </div>
        </div>
    )
}