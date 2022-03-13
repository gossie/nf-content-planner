import { XIcon } from "@heroicons/react/solid";
import { Topic } from "./model"

interface TopicElementProps {
    topic: Topic;
    onTopicDeletion: () => void;
}

export default function TopicElement(props: TopicElementProps) {

    const deleteTopic = () => {
        fetch(`${process.env.REACT_APP_BASE_URL}${props.topic.links.find(l => l.rel === 'self')?.href}`, {
            method: 'DELETE'
        })
        .then(() => props.onTopicDeletion());
    };

    return (
        <div className="group block max-w-xs mx-auto rounded-lg p-6 bg-white ring-1 ring-slate-900/5 shadow-lg space-y-3 mb-3">
            <div className="flex justify-between">
                <h3 className="text-slate-900 text-sm font-semibold">{props.topic.name}</h3>
                <XIcon className="h-5 w-5 text-blue-500 text-right cursor-pointer" onClick={deleteTopic} />
            </div>
            <p className="text-slate-500 text-sm">{props.topic.description}</p>
        </div>
    )
}