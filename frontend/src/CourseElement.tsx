import { useCallback, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Course } from "./model";

export default function CourseElement() {

    const params = useParams();
    const [course, setCourse] = useState({} as Course);
    const [newTopic, setNewTopic] = useState('');

    const fetchCourse = useCallback(() => {
        fetch(`${process.env.REACT_APP_BASE_URL}/api/courses/${params.courseId}`)
        .then(response => response.json())
        .then((course: Course) => setCourse(course));
    }, [params.courseId]);

    useEffect(() => fetchCourse(), [fetchCourse]);

    const addTopic = () => {
        fetch(`${process.env.REACT_APP_BASE_URL}/api/courses/${params.courseId}/topics`)
            .then(() => fetchCourse());
    };

    return (
        <div>
            <div>Name: {course.name}</div>
            <div>
                <h2>Topics</h2>
                <div>
                    <input type="text" placeholder="Topic" value={newTopic} onChange={ev => setNewTopic(ev.target.value)} />
                    <button onClick={addTopic}>Speichern</button>
                </div>
                <ul>
                    {course.topics.map(t => <li key={t.id}>{t.name}</li>)}
                </ul>
            </div>
        </div>
    )
}
