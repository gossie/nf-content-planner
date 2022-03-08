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
        <div>
            {props.topic.name} <button onClick={deleteTopic}>Löschen</button>
        </div>
    )
}