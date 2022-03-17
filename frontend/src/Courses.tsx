import { useCallback, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { Link, Outlet, useNavigate } from "react-router-dom";
import Input from "./common-elements/Input";
import { fetchAllCourses, createCourse } from "./http-client";
import { Course } from "./model";

export default function Courses() {

    const [courseName, setCourseName] = useState('');
    const [courses, setCourses] = useState([] as Array<Course>)

    const { t } = useTranslation();
    const navigate = useNavigate();

    useEffect(() => {
        const query = new URLSearchParams(window.location.search);
        const jwt = query.get('jwt');
        if (jwt) {
            localStorage.setItem('jwt', jwt);
            window.location.search = '';
        }
    }, [])

    const fetchAll = useCallback(() => {
        fetchAllCourses(navigate)
            .then((courses: Array<Course>) => setCourses(courses));
    }, [navigate]);

    useEffect(() => {
        fetchAll();
    }, [fetchAll]);

    const createNewCourse = () => {
        createCourse(courseName, navigate)
            .then(() => {
                setCourseName('');
                fetchAll();
            });
    }

    const logout = () => {
        localStorage.removeItem('jwt');
        navigate('/login');
    }

    return (
        <div>
            <Input placeholder={t('placeholderCourseName')} value={courseName} onChange={setCourseName} /><button onClick={createNewCourse}>{t('buttonCreateCourse')}</button>
            <div>
                { courses.map(c =>
                    <div key={c.id}>
                        <Link to={c.id}>{c.name}</Link>
                    </div>)
                }
            </div>
            <div onClick={logout}>Logout</div>
            <div>
                <Outlet />
            </div>
        </div>
    )
}