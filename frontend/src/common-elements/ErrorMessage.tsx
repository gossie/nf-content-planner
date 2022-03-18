import { useTranslation } from "react-i18next"

interface ErrorMessageProps {
    message: string
}

export default function ErrorMessage(props: ErrorMessageProps) {

    const { t } = useTranslation();

    return (
        <div className="border-2 border-red-500 rounded-md text-red-500 my-2 p-2">
            {t(props.message)}
        </div>
    )
}