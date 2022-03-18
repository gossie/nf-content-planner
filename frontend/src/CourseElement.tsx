import { useCallback, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { Link, useNavigate, useParams } from "react-router-dom";
import Button from "./common-elements/Button";
import ErrorMessage from "./common-elements/ErrorMessage";
import Input from "./common-elements/Input";
import { addTopicToCourse, deleteCourse, fetchCourse, fetchVotes } from "./http-client";
import { Course } from "./model";
import TopicElement from "./TopicElement";

export default function CourseElement() {

    const params = useParams();
    const [course, setCourse] = useState({} as Course);
    const [newTopicName, setNewTopicName] = useState('');
    const [newTopicDescription, setNewTopicDescription] = useState('');
    const [leftVotes, setLeftVotes] = useState(0);
    const [errorMessage, setErrorMessage] = useState('');

    const { t } = useTranslation();
    const navigate = useNavigate();

    const fetchOneCourse = useCallback(() => {
        fetchCourse(params.courseId!, navigate)
            .then((course: Course) => setCourse(course));
    }, [params.courseId, navigate]);

    const fetchLeftVotes = useCallback(() => {
        fetchVotes(params.courseId!, navigate)
            .then((votesAsString: string | void) => setLeftVotes(parseInt(votesAsString!)));
    }, [params.courseId, navigate]);

    useEffect(() => {
        fetchOneCourse();
        fetchLeftVotes();
    }, [fetchOneCourse, fetchLeftVotes]);

    const addTopic = () => {
        addTopicToCourse(course, newTopicName, newTopicDescription, navigate)
        .then(() => {
            setNewTopicName('');
            setNewTopicDescription('');
            fetchOneCourse();
        });
    };

    const deleteExistingCourse = () => {
        deleteCourse(course, navigate)
            .then(() => {
                setNewTopicName('');
                setNewTopicDescription('');
                navigate('/courses');
            })
            .catch(e => setErrorMessage(e.message));
    };

    useEffect(() => {
        const timeoutId = setTimeout(() => setErrorMessage(''), 15000);
        return () => clearTimeout(timeoutId);
    }, [errorMessage]);

    return (
        <div className="container mx-auto">
            { course.name &&
                <div>
                    <h1 className="text-2xl text-center pb-10 font-bold">{course.name}</h1>
                    <div className="flex flex-row justify-around">
                        <div>
                            <div>
                                <Link to="/courses">{t('back')}</Link>
                            </div>
                            <div>
                                <Button label={t('buttonDeleteCourse')} onClick={deleteExistingCourse} />
                                { errorMessage && <ErrorMessage message={errorMessage} /> }
                            </div>
                            <div>
                                <span>{t('votes', {"votes": leftVotes})}</span>
                            </div>
                        </div>
                        <div className="border-r-2" />
                        <div>
                            <h2 className="text-xl pb-5 font-bold">{t('headlineTopics')}</h2>
                            <div className="mb-3">
                                <Input placeholder="Topic" value={newTopicName} onChange={setNewTopicName} />
                                <textarea className="border-b-2" placeholder="Beschreibung" value={newTopicDescription} onChange={ev => setNewTopicDescription(ev.target.value)}></textarea>
                                <Button label="Speichern" onClick={addTopic} />
                            </div>
                            <ul>
                                {course.topics.map(t => <li key={t.id}><TopicElement topic={t} onTopicDeletion={() => {fetchOneCourse(); fetchLeftVotes();}} onTopicVote={(course: Course) => {setCourse(course); fetchLeftVotes();}} /></li>)}
                            </ul>
                        </div>
                    </div>
                </div>
            }
        </div>
    )
}
