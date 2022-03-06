import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Course } from "./model";

export default function CourseElement() {

    const params = useParams();
    const [course, setCourse] = useState({} as Course);

    useEffect(() => {
        fetch(`${process.env.REACT_APP_BASE_URL}/api/courses/${params.courseId}`)
            .then(response => response.json())
            .then((course: Course) => setCourse(course));
    }, [params.courseId]);

    return (
        <div>Name: {course.name}</div>
    )
}
