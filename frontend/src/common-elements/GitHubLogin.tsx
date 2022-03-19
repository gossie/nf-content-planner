import { useTranslation } from "react-i18next";

export default function GitHubLogin() {

    const { t } = useTranslation();

    return (
        <div>
            <a href="https://github.com/login/oauth/authorize?client_id=ac870a1600ec03b7be10" className="text-sky-500 hover:text-sky-600 active:text-sky-700 underline">{t('thirdPartyLogin', {company: 'GitHub'})}</a>
        </div>
    );
}