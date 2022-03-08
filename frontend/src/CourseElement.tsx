import { useCallback, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Course } from "./model";

export default function CourseElement() {

    const params = useParams();
    const [course, setCourse] = useState({} as Course);
    const [newTopicName, setNewTopicName] = useState('');
    const [newTopicDescription, setNewTopicDescription] = useState('');

    const fetchCourse = useCallback(() => {
        fetch(`${process.env.REACT_APP_BASE_URL}/api/courses/${params.courseId}`)
            .then(response => response.json())
            .then((course: Course) => setCourse(course));
    }, [params.courseId]);

    useEffect(() => fetchCourse(), [fetchCourse]);

    const addTopic = () => {
        fetch(`${process.env.REACT_APP_BASE_URL}/api/courses/${params.courseId}/topics`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: newTopicName,
                description: newTopicDescription
            })
        })
        .then(() => fetchCourse());
    };

    return (
        <div>
            { course.name &&
                <div>
                    <div>Name: {course.name}</div>
                    <div>
                        <h2>Topics</h2>
                        <div>
                            <input type="text" placeholder="Topic" value={newTopicName} onChange={ev => setNewTopicName(ev.target.value)} />
                            <textarea placeholder="Beschreibung" value={newTopicDescription} onChange={ev => setNewTopicDescription(ev.target.value)}></textarea>
                            <button onClick={addTopic}>Speichern</button>
                        </div>
                        <ul>
                            {course.topics.map(t => <li key={t.id}>{t.name}</li>)}
                        </ul>
                    </div>
                </div>
            }
        </div>
    )
}
