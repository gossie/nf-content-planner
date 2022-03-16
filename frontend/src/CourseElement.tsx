import { useCallback, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate, useParams } from "react-router-dom";
import Button from "./common-elements/Button";
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

    const { t } = useTranslation();
    const navigate = useNavigate();

    const fetchOneCourse = useCallback(() => {
        fetchCourse(params.courseId!, navigate)
            .then((course: Course) => setCourse(course));
    }, [params.courseId]);

    const fetchLeftVotes = useCallback(() => {
        fetchVotes(params.courseId!, navigate)
            .then((votesAsString: string | void) => setLeftVotes(parseInt(votesAsString!)));
    }, [params.courseId]);

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

    // const deleteCourse = () => {
    //     fetch(`${process.env.REACT_APP_BASE_URL}${course.links.find(l => l.rel === 'self')?.href}`, {
    //         method: 'DELETE'
    //     })
    //     .then(() => {
    //         setNewTopicName('');
    //         setNewTopicDescription('');
    //         navigate('/courses');
    //     })
    // };

    return (
        <div className="container mx-auto">
            { course.name &&
                <div>
                    <h1 className="text-2xl text-center pb-10 font-bold">{course.name}</h1>
                    <div className="flex flex-row justify-around">
                        <div>
                            { /*
                            <Link to="/courses">{t('back')}</Link>
                            <Button label={t('buttonDeleteCourse')} onClick={deleteCourse} />
                            */ }
                            <span>{t('votes', {"votes": leftVotes})}</span>
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
