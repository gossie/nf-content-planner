import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next"
import { Link, useNavigate } from "react-router-dom";
import Button from "./common-elements/Button";
import ErrorMessage from "./common-elements/ErrorMessage";
import GitHubLogin from "./common-elements/GitHubLogin";
import Input from "./common-elements/Input";

export default function Registration() {

    const [email, setEmail] = useState('');
    const [firstname, setFirstname] = useState('');
    const [lastname, setLastname] = useState('');
    const [password, setPassword] = useState('');
    const [passwordAgain, setPasswordAgain] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const {t} = useTranslation();
    const navigate = useNavigate();

    useEffect(() => {
        const timeoutId = setTimeout(() => setErrorMessage(''), 15000);
        return () => clearTimeout(timeoutId);
    }, [errorMessage]);

    const register = () => {
        if (!email || !password || !passwordAgain || !lastname || !firstname) {
            setErrorMessage('mandatoryErrorMessage');
        } else if (password !== passwordAgain) {
            setErrorMessage('passwordErrorMessage');
        } else {
            fetch(`${process.env.REACT_APP_BASE_URL}/api/users`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    email: email,
                    firstname: firstname,
                    lastname: lastname,
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
        }
    };

    return (
        <div className="flex justify-around w-full">
            <div>
                <h2 className="text-xl pb-5 font-bold">{t('headlineRegistration')}</h2>
                <div>
                    <div>
                        <div>
                            <Input placeholder={t('email')} value={email} onChange={setEmail} />
                        </div>
                        <div>
                            <Input placeholder={t('firstname')} value={firstname} onChange={setFirstname} />
                        </div>
                        <div>
                            <Input placeholder={t('lastname')} value={lastname} onChange={setLastname} />
                        </div>
                        <div>
                            <Input placeholder={t('password')} value={password} onChange={setPassword} type="password" />
                        </div>
                        <div>
                            <Input placeholder={t('passwordAgain')} value={passwordAgain} onChange={setPasswordAgain} type="password" />
                        </div>
                        <div>
                            <Button label={t('buttonRegistration')} onClick={register} />
                        </div>
                    </div>
                    { errorMessage && <ErrorMessage message={t(errorMessage)} /> }
                </div>
                <GitHubLogin />
                <div className="mt-4">
                    <span>{t('goToLogin')}</span> <Link to="/login" className="underline">{t('loginLinkText')}</Link>
                </div>
            </div>
        </div>
    )
}
