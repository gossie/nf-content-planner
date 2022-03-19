import { useState } from "react";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import { useAuth } from "./auth/AuthProvider";
import Button from "./common-elements/Button";
import ErrorMessage from "./common-elements/ErrorMessage";
import GitHubLogin from "./common-elements/GitHubLogin";
import Input from "./common-elements/Input";

export default function Login() {

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const { t } = useTranslation();
    const { login } = useAuth();

    const performLogin = () => {
        login(email, password)
            .catch(e => setErrorMessage(e.message));
    }

    return (
        <div className="flex justify-around w-full">
            <div>
                <h2 className="text-xl pb-5 font-bold">{t('headlineLogin')}</h2>
                <div>
                    <Input placeholder={t('email')} value={email} onChange={setEmail} onKeyUp={ev => {if(ev.key === 'Enter') {performLogin()}}} additionalCss="mr-4" />
                    <Input placeholder={t('password')} value={password} onChange={setPassword} onKeyUp={ev => {if(ev.key === 'Enter') {performLogin()}}} type="password" additionalCss="mr-4" />
                    <Button label={t('buttonLogin')} onClick={performLogin} />
                </div>
                { errorMessage && <ErrorMessage message={errorMessage} /> }
                <GitHubLogin />
                <div className="mt-4">
                    <span>{t('goToRegister')}</span> <Link to="/registration" className="underline">{t('registerLinkText')}</Link>
                </div>
            </div>
        </div>
    )
}