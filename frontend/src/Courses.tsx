import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import { Course } from "./model";

export default function Courses() {

    const [courseName, setCourseName] = useState('');
    const [courses, setCourses] = useState([] as Array<Course>)

    const { t } = useTranslation();

    const fetchAll = () => {
        fetch(`${process.env.REACT_APP_BASE_URL}/api/courses`)
            .then(response => response.json())
            .then((courses: Array<Course>) => setCourses(courses));
    }

    useEffect(() => {
        fetchAll();
    }, []);

    const createCourse = () => {
        fetch(`${process.env.REACT_APP_BASE_URL}/api/courses`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: courseName,
                topics: []
            })
        })
        .then(() => {
            setCourseName('');
            fetchAll();
        });
    }

    return (
        <div>
            <input type="text" placeholder={t('placeholderCourseName')} value={courseName} onKeyUp={ev => { if (ev.key === 'Enter') { createCourse() } }} onChange={ev => setCourseName(ev.target.value)} /><button onClick={createCourse}>{t('buttonCreateCourse')}</button>
            { courses.map(c =>
                <div key={c.id}>
                    <Link to={c.id}>{c.name}</Link>
                </div>)
            }
        </div>
    )
}