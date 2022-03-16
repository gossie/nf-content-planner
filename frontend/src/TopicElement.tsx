import { CheckIcon, XIcon } from "@heroicons/react/solid";
import { t } from "i18next";
import { useNavigate } from "react-router-dom";
import { deleteTopic, voteForTopic } from "./http-client";
import { Course, Topic } from "./model"

interface TopicElementProps {
    topic: Topic;
    onTopicDeletion: () => void;
    onTopicVote: (course: Course) => void;
}

export default function TopicElement(props: TopicElementProps) {

    const navigate = useNavigate();

    const deleteExistingTopic = () => {
        deleteTopic(props.topic, navigate)
            .then(() => props.onTopicDeletion());
    };

    const voteTopic = () => {
        voteForTopic(props.topic, navigate)
            .then((course: Course) => props.onTopicVote(course))
            .catch(e => console.error(e.message, e));
    };

    return (
        <div className="group block max-w-xs mx-auto rounded-lg p-6 bg-white ring-1 ring-slate-900/5 shadow-lg space-y-3 mb-3">
            <div className="flex justify-between">
                <h3 className="text-slate-900 text-sm font-semibold">{props.topic.name}</h3>
                <XIcon className="h-5 w-5 text-blue-500 text-right cursor-pointer" onClick={deleteExistingTopic} />
            </div>
            <p className="text-slate-500 text-sm">
                {props.topic.description}
                <br />
                {t('topicVotes', {votes: props.topic.votes})}
            </p>
            <CheckIcon className="h-5 w-5 text-blue-500 text-right cursor-pointer" onClick={voteTopic} />
        </div>
    )
}