import React, { useState, useEffect } from 'react';
import { Course } from './model';

function App() {

    const [courses, setCourses] = useState([] as Array<Course>)

    useEffect(() => {
        fetch(`${process.env.REACT_APP_BASE_URL}/api/courses`)
            .then(response => response.json())
            .then((courses: Array<Course>) => setCourses(courses));
    }, []);



    return (
        <div>
            { courses.map(c => <div>{c.name}</div>) }
        </div>
    );
}

export default App;
