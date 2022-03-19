import { useCallback, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "./auth/AuthProvider";
import Input from "./common-elements/Input";
import { fetchAllCourses, createCourse } from "./http-client";
import { Course } from "./model";

export default function Courses() {

    const [courseName, setCourseName] = useState('');
    const [courses, setCourses] = useState([] as Array<Course>)

    const { t } = useTranslation();
    const { token } = useAuth();
    const navigate = useNavigate();

    const fetchAll = useCallback(() => {
        fetchAllCourses(token, navigate)
            .then((courses: Array<Course>) => setCourses(courses));
    }, [token, navigate]);

    useEffect(() => {
        fetchAll();
    }, [fetchAll]);

    const createNewCourse = () => {
        createCourse(courseName, token, navigate)
            .then(() => {
                setCourseName('');
                fetchAll();
            });
    }
/*
    const logout = () => {
        localStorage.removeItem('jwt');
        navigate('/login');
    }
*/
    return (
        <div className="flex justify-around w-full">
            <div>
                <Input placeholder={t('placeholderCourseName')} value={courseName} onChange={setCourseName} /><button onClick={createNewCourse}>{t('buttonCreateCourse')}</button>
                <div>
                    { courses.map(c =>
                        <div key={c.id}>
                            <Link to={c.id}>{c.name}</Link>
                        </div>)
                    }
                </div>
                {/*<div className="cursor-pointer" onClick={logout}>Logout</div>*/}
            </div>
        </div>
    )
}