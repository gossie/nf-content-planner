import { useCallback, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate, useParams } from "react-router-dom";
import { useAuth } from "./auth/AuthProvider";
import Button from "./common-elements/Button";
import ErrorMessage from "./common-elements/ErrorMessage";
import Input from "./common-elements/Input";
import { addTopicToCourse, fetchCourse, fetchVotes } from "./http-client";
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
    const { token, user } = useAuth();
    const navigate = useNavigate();

    const fetchOneCourse = useCallback(() => {
        fetchCourse(params.courseId!, token, navigate)
            .then((course: Course) => setCourse(course));
    }, [params.courseId, token, navigate]);

    const fetchLeftVotes = useCallback(() => {
        fetchVotes(params.courseId!, token, navigate)
            .then((votesAsString: string | void) => setLeftVotes(parseInt(votesAsString!)));
    }, [params.courseId, token, navigate]);

    useEffect(() => {
        fetchOneCourse();
        fetchLeftVotes();
    }, [fetchOneCourse, fetchLeftVotes]);

    const addTopic = () => {
        if (!newTopicName) {
            setErrorMessage('errorTopicNameMandatory');
        } else {
            addTopicToCourse(course, newTopicName, newTopicDescription, token, navigate)
                .then(() => {
                    setNewTopicName('');
                    setNewTopicDescription('');
                    fetchOneCourse();
                })
                .catch(e => setErrorMessage(e.message));
        }
    };
/*
    const deleteExistingCourse = () => {
        deleteCourse(course, token, navigate)
            .then(() => {
                setNewTopicName('');
                setNewTopicDescription('');
                navigate('/courses');
            })
            .catch(e => setErrorMessage(e.message));
    };
*/
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
                            {/*
                            <div>
                                <Link to="/courses">{t('back')}</Link>
                            </div>
                            <div>
                                <Button label={t('buttonDeleteCourse')} onClick={deleteExistingCourse} />
                                { errorMessage && <ErrorMessage message={errorMessage} /> }
                            </div>
                            */}
                            <div>
                                <span>{t('votes', {"votes": leftVotes, "name": user.firstname})}</span>
                                <br />
                                <span>{t('allVotes', {"votes": course.topics.map(t => t.allVotes).reduce((prev, current) => prev + current, 0)})}</span>
                            </div>
                        </div>
                        <div className="border-r-2 mx-4" />
                        <div>
                            <h2 className="text-xl pb-5 font-bold">{t('headlineTopics')}</h2>
                            <div className="mb-6">
                                <div className="flex flex-col">
                                    <Input placeholder="Topic" value={newTopicName} onChange={setNewTopicName} />
                                    <textarea className="border-b-2" placeholder="Beschreibung" value={newTopicDescription} onChange={ev => setNewTopicDescription(ev.target.value)}></textarea>
                                    <Button label="Speichern" onClick={addTopic} />
                                </div>
                                { errorMessage && <ErrorMessage message={errorMessage} /> }
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
