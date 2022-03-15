import { useCallback, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useParams } from "react-router-dom";
import Button from "./common-elements/Button";
import Input from "./common-elements/Input";
import { Course } from "./model";
import TopicElement from "./TopicElement";

export default function CourseElement() {

    const params = useParams();
    const [course, setCourse] = useState({} as Course);
    const [newTopicName, setNewTopicName] = useState('');
    const [newTopicDescription, setNewTopicDescription] = useState('');

    const { t } = useTranslation();

    const fetchCourse = useCallback(() => {
        fetch(`${process.env.REACT_APP_BASE_URL}/api/courses/${params.courseId}`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('jwt')}`
            }
        })
        .then(response => response.json())
        .then((course: Course) => setCourse(course));
    }, [params.courseId]);

    useEffect(() => fetchCourse(), [fetchCourse]);

    const addTopic = () => {
        fetch(`${process.env.REACT_APP_BASE_URL}${course.links.find(l => l.rel === 'create-topic')?.href}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('jwt')}`
            },
            body: JSON.stringify({
                name: newTopicName,
                description: newTopicDescription
            })
        })
        .then(() => {
            setNewTopicName('');
            setNewTopicDescription('');
            fetchCourse();
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
                            <span>{t('votes', {"votes": 1})}</span>
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
                                {course.topics.map(t => <li key={t.id}><TopicElement topic={t} onTopicDeletion={fetchCourse} /></li>)}
                            </ul>
                        </div>
                    </div>
                </div>
            }
        </div>
    )
}
