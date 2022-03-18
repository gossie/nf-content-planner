import { KeyboardEvent } from "react";

interface InputProps {
    value: string;
    placeholder: string;
    onChange: (value: string) => void;
    onKeyUp?: (ev: KeyboardEvent) => void;
    type?: string;
    additionalCss?: string;
}

export default function Input(props: InputProps) {
    return (
        <input type={props.type ?? "text"}
               className={`border-b-2 my-2 ${props.additionalCss}`}
               placeholder={props.placeholder}
               value={props.value}
               onChange={ev => props.onChange(ev.target.value)}
               onKeyUp={props.onKeyUp} />
    )
}