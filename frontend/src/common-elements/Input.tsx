interface InputProps {
    value: string;
    placeholder: string;
    onChange: (value: string) => void;
    type?: string;
}

export default function Input(props: InputProps) {
    return (
        <input type={props.type ?? "text"}
               className="border-b-2"
               placeholder={props.placeholder}
               value={props.value}
               onChange={ev => props.onChange(ev.target.value)} />
    )
}