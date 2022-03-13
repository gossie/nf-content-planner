interface ButtonProps {
    label: string;
    onClick: () => void;
}

export default function Button(props: ButtonProps) {
    return (
        <button className="bg-sky-500 hover:bg-sky-600 active:bg-sky-700 px-5 py-2 text-sm leading-5 rounded-full font-semibold text-white"
                onClick={props.onClick}>
            {props.label}
        </button>
    )
}