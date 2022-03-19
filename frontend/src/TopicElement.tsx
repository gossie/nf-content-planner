import { CheckIcon, UserCircleIcon, XIcon } from "@heroicons/react/solid";
import { t } from "i18next";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "./auth/AuthProvider";
import ErrorMessage from "./common-elements/ErrorMessage";
import { deleteTopic, removeVoteForTopic, voteForTopic } from "./http-client";
import { Course, Topic } from "./model"

interface TopicElementProps {
    topic: Topic;
    onTopicDeletion: () => void;
    onTopicVote: (course: Course) => void;
}

export default function TopicElement(props: TopicElementProps) {

    const [errorMessage, setErrorMessage] = useState('');

    const { token } = useAuth();
    const navigate = useNavigate();

    const deleteExistingTopic = () => {
        deleteTopic(props.topic, token, navigate)
            .then(() => props.onTopicDeletion())
            .catch(e => setErrorMessage(e.message));
    };

    const voteTopic = () => {
        voteForTopic(props.topic, token, navigate)
            .then((course: Course) => props.onTopicVote(course));
    };

    const removeVote = () => {
        removeVoteForTopic(props.topic, token, navigate)
            .then((course: Course) => props.onTopicVote(course));
    }

    useEffect(() => {
        console.log('im moment ist die errorMessage ' + errorMessage + '. Ich warte jetzt 15 Sekunden.');
        const timeoutId = setTimeout(() => setErrorMessage(''), 15000);
        return clearTimeout(timeoutId);
    }, [errorMessage]);

    return (
        <div className="group block max-w-xs mx-auto rounded-lg p-6 bg-white ring-1 ring-slate-900/5 shadow-lg space-y-3 mb-3">
            <div className="flex justify-between">
                <h3 className="text-slate-900 text-sm font-semibold">{props.topic.name}</h3>
                <XIcon className="h-5 w-5 text-blue-500 text-right cursor-pointer" onClick={deleteExistingTopic} />
            </div>
            { errorMessage && <ErrorMessage message={errorMessage} /> }
            <p className="text-slate-500 text-sm">
                {props.topic.description.split('\n').map((text, i) => <div key={i}>{text}</div>)}
                <br /><br />
                {t('topicVotes', {votes: props.topic.allVotes})}
            </p>
            <div className="flex justify-between">
                <span>
                    <CheckIcon className="h-5 w-5 text-blue-500 text-right cursor-pointer" onClick={voteTopic} />
                </span>
                <span className="flex">
                    { [...Array(props.topic.userVotes)].map((i: number) => <UserCircleIcon key={i} className="h-5 w-5 text-blue-500 text-right cursor-pointer" onClick={removeVote} />)  }
                </span>
            </div>
        </div>
    )
}